/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mia.parking.core.domain.Incident;
import mia.parking.core.domain.ParkingSession;
import mia.parking.core.domain.ParkingSpace;
import mia.parking.core.domain.ParkingSpaceCategory;
import mia.parking.core.domain.ParkingSpaceStatus;
import mia.parking.core.domain.ParkingStateSnapshot;
import mia.parking.core.domain.ParkingStatistics;
import mia.parking.core.domain.SettlementResult;
import mia.parking.core.port.ParkingRepository;

/** Sample-data repository used for development without a MySQL server. */
public final class InMemoryParkingRepository implements ParkingRepository {
    private final Map<String, ParkingSpace> spaces = new LinkedHashMap<>();
    private final Map<String, ParkingSession> sessions = new LinkedHashMap<>();
    private final List<Incident> incidents = new ArrayList<>();
    private final List<SettlementResult> settlements = new ArrayList<>();

    public InMemoryParkingRepository() {
        addZone("A", 4);
        addZone("B", 4);
        addZone("C", 4);
    }

    private void addZone(String zoneId, int count) {
        for (int index = 1; index <= count; index++) {
            String id = zoneId + String.format("%02d", index);
            spaces.put(id, new ParkingSpace(id, zoneId, id, ParkingSpaceCategory.GENERAL,
                    false, index == 1, true, ParkingSpaceStatus.FREE, null));
        }
    }

    @Override
    public synchronized List<ParkingSpace> loadSpaces() {
        return List.copyOf(spaces.values());
    }

    @Override
    public synchronized List<ParkingSession> loadActiveSessions() {
        return sessions.values().stream().filter(session -> session.exitedAt() == null).toList();
    }

    @Override
    public synchronized Optional<ParkingSession> findSessionById(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public synchronized Optional<ParkingSession> findActiveSessionByPlate(String plate) {
        return sessions.values().stream()
                .filter(session -> session.exitedAt() == null && session.entry().plate().equalsIgnoreCase(plate))
                .findFirst();
    }

    @Override
    public synchronized void saveSession(ParkingSession session) {
        sessions.put(session.id(), session);
    }

    @Override
    public synchronized void saveSpace(ParkingSpace space) {
        spaces.put(space.id(), space);
    }

    @Override
    public synchronized void saveIncident(Incident incident) {
        incidents.add(incident);
    }

    @Override
    public synchronized void saveSettlement(SettlementResult settlement) {
        settlements.add(settlement);
    }

    @Override
    public synchronized void restoreState(ParkingStateSnapshot snapshot) {
        spaces.clear();
        snapshot.spaces().forEach(space -> spaces.put(space.id(), space));
        sessions.clear();
        snapshot.sessions().forEach(session -> sessions.put(session.id(), session));
        incidents.clear();
        incidents.addAll(snapshot.incidents());
    }

    @Override
    public synchronized List<Incident> loadRecentIncidents(int limit) {
        return incidents.stream()
                .sorted(Comparator.comparing(Incident::occurredAt).reversed())
                .limit(Math.max(limit, 0))
                .toList();
    }

    @Override
    public synchronized ParkingStatistics loadStatistics(Instant from, Instant to) {
        int completed = (int) sessions.values().stream()
                .filter(session -> session.exitedAt() != null)
                .count();
        double occupancy = spaces.isEmpty() ? 0 : spaces.values().stream()
                .filter(space -> space.status() != ParkingSpaceStatus.FREE
                        && space.status() != ParkingSpaceStatus.DISABLED)
                .count() * 100.0 / spaces.size();
        return new ParkingStatistics(from, to, completed, occupancy, Map.of(),
                Duration.ZERO, Duration.ZERO);
    }

}
