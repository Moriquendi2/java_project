/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Duration;

/** Information captured at an entrance barrier. Optional text fields and expectedDuration may be null.
 * 中文：封装入口采集的车牌、用户类别和车位偏好等信息。
 */
public record EntryRequest(
        String plate,
        String vehicleType,
        UserCategory userCategory,
        String subscriptionId,
        String entryGateId,
        String flightNumber,
        String terminalId,
        String airline,
        Duration expectedDuration,
        boolean accessibleSpaceRequired,
        boolean chargingSpaceRequired) {
}
