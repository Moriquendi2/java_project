/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;
import java.util.List;

/** Small cache snapshot used by the recovery module. */
public record ParkingStateSnapshot(
        Instant savedAt,
        List<ParkingSpace> spaces,
        List<ParkingSession> sessions,
        List<Incident> incidents) {
    public ParkingStateSnapshot {
        spaces = List.copyOf(spaces);
        sessions = List.copyOf(sessions);
        incidents = List.copyOf(incidents);
    }
}
