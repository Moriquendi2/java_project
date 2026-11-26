/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.domain;

import java.time.Instant;
import java.util.List;

/** Small cache snapshot used by the recovery module.
 * 中文：封装停车运行状态的恢复快照。
 */
public record ParkingStateSnapshot(
        Instant savedAt,
        List<ParkingSpace> spaces,
        List<ParkingSession> sessions,
        List<Incident> incidents) {
    /**
     * 中文：复制集合，确保恢复快照不可变。
     */
    public ParkingStateSnapshot {
        spaces = List.copyOf(spaces);
        sessions = List.copyOf(sessions);
        incidents = List.copyOf(incidents);
    }
}
