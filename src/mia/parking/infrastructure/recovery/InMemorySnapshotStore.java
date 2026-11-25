/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.recovery;

import java.util.Optional;
import mia.parking.core.domain.ParkingStateSnapshot;
import mia.parking.core.port.SnapshotStore;

/** Development snapshot adapter; replace with a file-backed store for delivery. */
public final class InMemorySnapshotStore implements SnapshotStore {
    private ParkingStateSnapshot snapshot;

    @Override
    public synchronized Optional<ParkingStateSnapshot> load() {
        return Optional.ofNullable(snapshot);
    }

    @Override
    public synchronized void save(ParkingStateSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
