/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

/** Current state reported by a simulated or physical space device.
 * 中文：封装设备上报的车位启用、占用和车辆状态。
 */
public record DeviceSpaceStatus(String spaceId, boolean enabled, boolean occupied, String plate) {
}
