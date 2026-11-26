/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;

/** Finding reported by a mobile patrol member.
 * 中文：封装巡逻人员上报的停车场现场情况。
 */
public record PatrolReport(
        String spaceId,
        String plate,
        Incident.Type type,
        String note,
        Instant reportedAt) {
}
