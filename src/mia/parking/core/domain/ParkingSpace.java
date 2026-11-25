/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

/** Shared parking-space record used by the core, UI, and persistence modules. */
public record ParkingSpace(
        String id,
        String zoneId,
        String label,
        ParkingSpaceCategory category,
        boolean accessible,
        boolean charging,
        boolean enabled,
        ParkingSpaceStatus status,
        String assignedPlate) {
}
