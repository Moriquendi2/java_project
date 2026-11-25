/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Result returned after an entry request is processed; identifiers and route are null when rejected. */
public record EntryResult(
        boolean accepted,
        String sessionId,
        String assignedSpaceId,
        String route,
        String message) {
}
