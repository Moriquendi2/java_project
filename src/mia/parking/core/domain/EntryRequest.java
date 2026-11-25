/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Duration;

/** Information captured at an entrance barrier. Optional text fields and expectedDuration may be null. */
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
