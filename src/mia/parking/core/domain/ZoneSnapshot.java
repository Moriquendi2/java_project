/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Occupancy summary for one parking zone. */
public record ZoneSnapshot(String zoneId, int totalSpaces, int occupiedSpaces) {
    public int occupancyPercent() {
        return totalSpaces == 0 ? 0 : occupiedSpaces * 100 / totalSpaces;
    }
}
