/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Information captured at an exit barrier. */
public record ExitRequest(String plate, String exitGateId, String paymentMethod) {
}
