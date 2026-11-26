/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

/** Simple row model for the standalone PARKING_ZONE JDBC example.
 * 中文：表示 JDBC 示例使用的停车分区记录。
 */
public record ParkingZone(String id, String name, String description, int capacity) {
}
