/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import mia.parking.core.domain.DashboardSnapshot;
import mia.parking.core.domain.EntryRequest;
import mia.parking.core.domain.EntryResult;
import mia.parking.core.domain.ExitRequest;
import mia.parking.core.domain.Incident;
import mia.parking.core.domain.ParkingSession;
import mia.parking.core.domain.ParkingSpace;
import mia.parking.core.domain.ParkingSpaceStatus;
import mia.parking.core.domain.ParkingStateSnapshot;
import mia.parking.core.domain.ParkingStatistics;
import mia.parking.core.domain.PatrolReport;
import mia.parking.core.domain.SettlementResult;
import mia.parking.core.domain.SpaceEvent;
import mia.parking.core.domain.ZoneSnapshot;
import mia.parking.core.port.Clock;
import mia.parking.core.port.DeviceGateway;
import mia.parking.core.port.ParkingRepository;
import mia.parking.core.port.SnapshotStore;

/**
 * Small in-memory implementation for wiring the modules together during development.
 * Replace its demonstration allocation and fee rules with the completed core rules.
 */
public final class DemoParkingControlService implements ParkingControlService {
    private final ParkingRepository repository;
    private final SnapshotStore snapshotStore;
    private final Clock clock;
    private final DeviceGateway devices;

    public DemoParkingControlService(ParkingRepository repository,
                                     SnapshotStore snapshotStore,
                                     Clock clock,
                                     DeviceGateway devices) {
        this.repository = repository;
        this.snapshotStore = snapshotStore;
        this.clock = clock;
        this.devices = devices;
    }

    @Override
    public synchronized EntryResult handleEntry(EntryRequest request) {
        Optional<ParkingSpace> available = repository.loadSpaces().stream()
                .filter(space -> space.enabled() && space.status() == ParkingSpaceStatus.FREE)
                .filter(space -> !request.accessibleSpaceRequired() || space.accessible())
                .filter(space -> !request.chargingSpaceRequired() || space.charging())
                .findFirst();
        if (available.isEmpty()) {
            return new EntryResult(false, null, null, null, "No matching space is available.");
        }

        Instant now = clock.now();
        ParkingSpace space = available.get();
        String sessionId = UUID.randomUUID().toString();
        repository.saveSpace(withSpaceState(space, ParkingSpaceStatus.RESERVED, request.plate(), true));
        repository.saveSession(new ParkingSession(sessionId, request, space.id(), null,
                now, null, null, null, null));
        devices.setGateOpen(request.entryGateId(), true);
        return new EntryResult(true, sessionId, space.id(), "Follow signs to " + space.label(), "Assigned.");
    }

    @Override
    public synchronized void handleSpaceEvent(SpaceEvent event) {
        Optional<ParkingSpace> matchingSpace = repository.loadSpaces().stream()
                .filter(space -> space.id().equals(event.spaceId())).findFirst();
        if (matchingSpace.isEmpty()) {
            return;
        }
        ParkingSpace space = matchingSpace.get();

        if (event.type() == SpaceEvent.Type.DEVICE_FAILURE) {
            repository.saveSpace(withSpaceState(space, ParkingSpaceStatus.DISABLED, space.assignedPlate(), false));
            devices.setSpaceEnabled(space.id(), false);
            saveIncident(Incident.Type.DEVICE_FAILURE, "Space device reported a failure.", space, event.plate());
            return;
        }

        Optional<ParkingSession> active = repository.findActiveSessionByPlate(event.plate());
        if (active.isEmpty()) {
            return;
        }
        ParkingSession session = active.get();
        Instant occurredAt = event.occurredAt() == null ? clock.now() : event.occurredAt();
        if (event.type() == SpaceEvent.Type.ARRIVAL) {
            boolean assigned = session.assignedSpaceId().equals(space.id());
            repository.saveSpace(withSpaceState(space,
                    assigned ? ParkingSpaceStatus.OCCUPIED : ParkingSpaceStatus.MISPARKED,
                    event.plate(), space.enabled()));
            if (!assigned) {
                repository.loadSpaces().stream()
                        .filter(candidate -> candidate.id().equals(session.assignedSpaceId()))
                        .findFirst()
                        .ifPresent(candidate -> repository.saveSpace(
                                withSpaceState(candidate, ParkingSpaceStatus.FREE, null, candidate.enabled())));
                saveIncident(Incident.Type.MISPARKING, "Vehicle parked in an unassigned space.", space, event.plate());
            }
            repository.saveSession(new ParkingSession(session.id(), session.entry(), session.assignedSpaceId(),
                    space.id(), session.enteredAt(), occurredAt, null, null, null));
        } else {
            repository.saveSpace(withSpaceState(space, ParkingSpaceStatus.FREE, null, true));
            repository.saveSession(new ParkingSession(session.id(), session.entry(), session.assignedSpaceId(),
                    session.actualSpaceId(), session.enteredAt(), session.parkedAt(), occurredAt, null, null));
        }
    }

    @Override
    public synchronized SettlementResult handleExit(ExitRequest request) {
        Optional<ParkingSession> active = repository.findActiveSessionByPlate(request.plate());
        if (active.isEmpty()) {
            return new SettlementResult(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, false, false);
        }

        ParkingSession session = active.get();
        Instant now = clock.now();
        Duration duration = Duration.between(session.enteredAt(), now);
        long billableHours = Math.max(1, (duration.toMinutes() + 59) / 60);
        // Demonstration rate only; the parking owner implements the rates from description.md.
        BigDecimal fee = BigDecimal.valueOf(billableHours).multiply(BigDecimal.TEN);
        SettlementResult result = new SettlementResult(session.id(), fee, BigDecimal.ZERO, fee, true, true);
        repository.saveSettlement(result);
        repository.saveSession(new ParkingSession(session.id(), session.entry(), session.assignedSpaceId(),
                session.actualSpaceId(), session.enteredAt(), session.parkedAt(), session.leftSpaceAt(), now, duration));
        repository.loadSpaces().stream()
                .filter(space -> space.id().equals(session.assignedSpaceId())
                        || space.id().equals(session.actualSpaceId()))
                .forEach(space -> repository.saveSpace(withSpaceState(space,
                        space.enabled() ? ParkingSpaceStatus.FREE : ParkingSpaceStatus.DISABLED,
                        null, space.enabled())));
        devices.setGateOpen(request.exitGateId(), true);
        return result;
    }

    @Override
    public DashboardSnapshot getDashboardSnapshot() {
        List<ParkingSpace> spaces = repository.loadSpaces();
        List<ZoneSnapshot> zones = spaces.stream().collect(Collectors.groupingBy(
                        ParkingSpace::zoneId, java.util.TreeMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> new ZoneSnapshot(entry.getKey(), entry.getValue().size(),
                        (int) entry.getValue().stream()
                                .filter(space -> space.status() != ParkingSpaceStatus.FREE
                                        && space.status() != ParkingSpaceStatus.DISABLED)
                                .count()))
                .toList();
        return new DashboardSnapshot(clock.now(), zones, spaces,
                repository.loadRecentIncidents(10), repository.loadActiveSessions().size());
    }

    @Override
    public List<ParkingSpace> getSpaces(String zoneId) {
        return repository.loadSpaces().stream()
                .filter(space -> zoneId == null || zoneId.equals(space.zoneId()))
                .toList();
    }

    @Override
    public Optional<ParkingSpace> getSpaceDetails(String spaceId) {
        return repository.loadSpaces().stream().filter(space -> space.id().equals(spaceId)).findFirst();
    }

    @Override
    public synchronized void setSpaceEnabled(String spaceId, boolean enabled) {
        repository.loadSpaces().stream().filter(space -> space.id().equals(spaceId)).findFirst().ifPresent(space -> {
            ParkingSpaceStatus status = enabled
                    ? (space.status() == ParkingSpaceStatus.DISABLED ? ParkingSpaceStatus.FREE : space.status())
                    : ParkingSpaceStatus.DISABLED;
            repository.saveSpace(withSpaceState(space, status, space.assignedPlate(), enabled));
            devices.setSpaceEnabled(spaceId, enabled);
        });
    }

    @Override
    public void reportPatrolFinding(PatrolReport report) {
        ParkingSpace space = getSpaceDetails(report.spaceId()).orElse(null);
        saveIncident(report.type(), report.note(), space, report.plate());
    }

    @Override
    public ParkingStatistics getStatistics(Instant from, Instant to) {
        return repository.loadStatistics(from, to);
    }

    @Override
    public void saveSnapshot() {
        snapshotStore.save(new ParkingStateSnapshot(clock.now(), repository.loadSpaces(),
                repository.loadActiveSessions(), repository.loadRecentIncidents(100)));
    }

    @Override
    public void restoreSnapshot() {
        snapshotStore.load().ifPresent(repository::restoreState);
    }

    private void saveIncident(Incident.Type type, String message, ParkingSpace space, String plate) {
        repository.saveIncident(new Incident(UUID.randomUUID().toString(), type, message,
                space == null ? null : space.zoneId(), space == null ? null : space.id(), plate,
                clock.now(), false));
    }

    private ParkingSpace withSpaceState(ParkingSpace space, ParkingSpaceStatus status,
                                        String plate, boolean enabled) {
        return new ParkingSpace(space.id(), space.zoneId(), space.label(), space.category(),
                space.accessible(), space.charging(), enabled, status, plate);
    }
}
