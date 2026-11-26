/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;

/** An anomaly or operational alert displayed to control-center staff.
 * 中文：记录需要控制中心关注的停车运营事件。
 */
public record Incident(
        String id,
        Type type,
        String message,
        String zoneId,
        String spaceId,
        String plate,
        Instant occurredAt,
        boolean resolved) {
    public enum Type {
        CAPACITY_WARNING,
        MISPARKING,
        ACCESSIBLE_SPACE_VIOLATION,
        DRIVING_TIMEOUT,
        PARKING_TIMEOUT,
        DEVICE_FAILURE,
        PATROL_REPORT
    }
}
