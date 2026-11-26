/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;
import java.util.List;

/** Read-only data needed by the control-center dashboard.
 * 中文：封装仪表盘展示所需的车位、分区、事件和会话概览。
 */
public record DashboardSnapshot(
        Instant capturedAt,
        List<ZoneSnapshot> zones,
        List<ParkingSpace> spaces,
        List<Incident> recentIncidents,
        int activeSessions) {
    /**
     * 中文：复制集合，确保仪表盘快照不可变。
     */
    public DashboardSnapshot {
        zones = List.copyOf(zones);
        spaces = List.copyOf(spaces);
        recentIncidents = List.copyOf(recentIncidents);
    }

    /**
     * 中文：返回快照中的车位总数。
     */
    public int totalSpaces() {
        return spaces.size();
    }

    /**
     * 中文：统计快照中空闲且已启用的车位数量。
     */
    public int availableSpaces() {
        return (int) spaces.stream()
                .filter(space -> space.enabled() && space.status() == ParkingSpaceStatus.FREE)
                .count();
    }
}
