/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.util.Optional;
import mia.parking.core.domain.ParkingStateSnapshot;

/** Boundary for storing and loading parking-state recovery snapshots. */
public interface SnapshotStore {
    Optional<ParkingStateSnapshot> load();

    void save(ParkingStateSnapshot snapshot);
}
