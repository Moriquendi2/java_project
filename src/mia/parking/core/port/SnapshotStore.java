/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.util.Optional;
import mia.parking.core.domain.ParkingStateSnapshot;

/** Boundary for storing and loading parking-state recovery snapshots.
 * 中文：定义停车状态快照的读取和保存接口。
 */
public interface SnapshotStore {
    Optional<ParkingStateSnapshot> load();

    /**
     * 中文：保存传入的停车状态快照。
     */
    void save(ParkingStateSnapshot snapshot);
}
