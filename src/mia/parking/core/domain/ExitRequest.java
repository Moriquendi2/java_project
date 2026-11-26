/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Information captured at an exit barrier.
 * 中文：封装车辆离场时的车牌、出口和支付方式。
 */
public record ExitRequest(String plate, String exitGateId, String paymentMethod) {
}
