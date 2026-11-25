/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import mia.parking.core.domain.DashboardSnapshot;
import mia.parking.core.domain.EntryRequest;
import mia.parking.core.domain.EntryResult;
import mia.parking.core.domain.ExitRequest;
import mia.parking.core.domain.ParkingSpace;
import mia.parking.core.domain.ParkingStatistics;
import mia.parking.core.domain.PatrolReport;
import mia.parking.core.domain.SettlementResult;
import mia.parking.core.domain.SpaceEvent;

/** Application boundary used by the control center and simulated devices. */
public interface ParkingControlService {
    /** Creates a session and reserves a space, or returns an unaccepted result. */
    EntryResult handleEntry(EntryRequest request);

    /** Applies a sensor/camera arrival, departure, or failure message. */
    void handleSpaceEvent(SpaceEvent event);

    /** Closes the active plate session and returns the payment and gate decision. */
    SettlementResult handleExit(ExitRequest request);

    /** Returns a read-only dashboard view of the current parking state. */
    DashboardSnapshot getDashboardSnapshot();

    /** A null zone id means all spaces. */
    List<ParkingSpace> getSpaces(String zoneId);

    Optional<ParkingSpace> getSpaceDetails(String spaceId);

    /** Updates the space and sends the matching enable/disable device command. */
    void setSpaceEnabled(String spaceId, boolean enabled);

    void reportPatrolFinding(PatrolReport report);

    ParkingStatistics getStatistics(Instant from, Instant to);

    /** Saves current in-memory state through the configured snapshot adapter. */
    void saveSnapshot();

    /** Restores state from the configured snapshot when one is available. */
    void restoreSnapshot();
}
