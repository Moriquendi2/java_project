/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Occupancy summary for one parking zone.
 * 中文：汇总单个分区的车位数量和占用情况。
 */
public record ZoneSnapshot(String zoneId, int totalSpaces, int occupiedSpaces) {
    /**
     * 中文：计算分区车位占用百分比；分区为空时返回零。
     */
    public int occupancyPercent() {
        return totalSpaces == 0 ? 0 : occupiedSpaces * 100 / totalSpaces;
    }
}
