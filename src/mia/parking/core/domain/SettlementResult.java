/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.math.BigDecimal;

/** Charge and gate decision returned for an exit request. */
public record SettlementResult(
        String sessionId,
        BigDecimal parkingFee,
        BigDecimal fine,
        BigDecimal total,
        boolean paid,
        boolean gateAllowed) {
}
