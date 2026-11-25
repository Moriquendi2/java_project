/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/** Basic reporting values requested by the project description. */
public record ParkingStatistics(
        Instant from,
        Instant to,
        int completedSessions,
        double averageOccupancyPercent,
        Map<String, Integer> terminalVisits,
        Duration averageEntryToSpace,
        Duration averageSpaceToExit) {
    public ParkingStatistics {
        terminalVisits = Map.copyOf(terminalVisits);
    }
}
