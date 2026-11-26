/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Result returned after an entry request is processed; identifiers and route are null when rejected.
 * 中文：封装入场处理结果及分配车位、路线等信息。
 */
public record EntryResult(
        boolean accepted,
        String sessionId,
        String assignedSpaceId,
        String route,
        String message) {
}
