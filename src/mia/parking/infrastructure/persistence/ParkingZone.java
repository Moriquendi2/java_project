/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

/** Simple row model for the standalone PARKING_ZONE JDBC example. */
public record ParkingZone(String id, String name, String description, int capacity) {
}
