/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import mia.parking.core.domain.Incident;
import mia.parking.core.domain.ParkingSession;
import mia.parking.core.domain.ParkingSpace;
import mia.parking.core.domain.ParkingStatistics;
import mia.parking.core.domain.ParkingStateSnapshot;
import mia.parking.core.domain.SettlementResult;

/** Data access boundary implemented with JDBC by the persistence owner. */
public interface ParkingRepository {
    List<ParkingSpace> loadSpaces();

    List<ParkingSession> loadActiveSessions();

    Optional<ParkingSession> findSessionById(String sessionId);

    Optional<ParkingSession> findActiveSessionByPlate(String plate);

    void saveSession(ParkingSession session);

    void saveSpace(ParkingSpace space);

    void saveIncident(Incident incident);

    void saveSettlement(SettlementResult settlement);

    void restoreState(ParkingStateSnapshot snapshot);

    List<Incident> loadRecentIncidents(int limit);

    ParkingStatistics loadStatistics(Instant from, Instant to);
}
