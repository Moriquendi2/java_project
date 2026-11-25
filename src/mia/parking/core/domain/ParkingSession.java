/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Duration;
import java.time.Instant;

/** One vehicle visit, including assigned and actually occupied spaces. */
public record ParkingSession(
        String id,
        EntryRequest entry,
        String assignedSpaceId,
        String actualSpaceId,
        Instant enteredAt,
        Instant parkedAt,
        Instant leftSpaceAt,
        Instant exitedAt,
        Duration actualParkingDuration) {
}
