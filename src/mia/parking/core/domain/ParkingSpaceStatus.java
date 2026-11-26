/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Current operational state shown by the control center.
 * 中文：列出车位可能处于的运行状态。
 */
public enum ParkingSpaceStatus {
    FREE,
    RESERVED,
    OCCUPIED,
    MISPARKED,
    OVERDUE,
    DISABLED
}
