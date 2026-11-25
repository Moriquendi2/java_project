/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Current operational state shown by the control center. */
public enum ParkingSpaceStatus {
    FREE,
    RESERVED,
    OCCUPIED,
    MISPARKED,
    OVERDUE,
    DISABLED
}
