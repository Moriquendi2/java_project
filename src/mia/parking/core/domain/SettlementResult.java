/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.math.BigDecimal;

/** Charge and gate decision returned for an exit request.
 * 中文：封装离场结算金额和闸机放行结果。
 */
public record SettlementResult(
        String sessionId,
        BigDecimal parkingFee,
        BigDecimal fine,
        BigDecimal total,
        boolean paid,
        boolean gateAllowed) {
}
