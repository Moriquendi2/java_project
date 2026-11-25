/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;

/** Finding reported by a mobile patrol member. */
public record PatrolReport(
        String spaceId,
        String plate,
        Incident.Type type,
        String note,
        Instant reportedAt) {
}
