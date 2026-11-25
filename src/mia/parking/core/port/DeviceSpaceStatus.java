/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

/** Current state reported by a simulated or physical space device. */
public record DeviceSpaceStatus(String spaceId, boolean enabled, boolean occupied, String plate) {
}
