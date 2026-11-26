/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;

/** Arrival, departure, or device-failure message from a space device; null occurredAt uses the shared clock.
 * 中文：封装车位设备上报的车辆到达、驶离或设备故障。
 */
public record SpaceEvent(Type type, String spaceId, String plate, Instant occurredAt) {
    public enum Type {
        ARRIVAL,
        DEPARTURE,
        DEVICE_FAILURE
    }
}
