/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;
import java.util.List;

/** Read-only data needed by the control-center dashboard. */
public record DashboardSnapshot(
        Instant capturedAt,
        List<ZoneSnapshot> zones,
        List<ParkingSpace> spaces,
        List<Incident> recentIncidents,
        int activeSessions) {
    public DashboardSnapshot {
        zones = List.copyOf(zones);
        spaces = List.copyOf(spaces);
        recentIncidents = List.copyOf(recentIncidents);
    }

    public int totalSpaces() {
        return spaces.size();
    }

    public int availableSpaces() {
        return (int) spaces.stream()
                .filter(space -> space.enabled() && space.status() == ParkingSpaceStatus.FREE)
                .count();
    }
}
