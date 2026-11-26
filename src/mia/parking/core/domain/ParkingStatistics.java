/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/** Basic reporting values requested by the project description.
 * 中文：封装指定时段内的停车数量、占用和时长统计。
 */
public record ParkingStatistics(
        Instant from,
        Instant to,
        int completedSessions,
        double averageOccupancyPercent,
        Map<String, Integer> terminalVisits,
        Duration averageEntryToSpace,
        Duration averageSpaceToExit) {
    /**
     * 中文：复制统计映射，确保统计结果不可变。
     */
    public ParkingStatistics {
        terminalVisits = Map.copyOf(terminalVisits);
    }
}
