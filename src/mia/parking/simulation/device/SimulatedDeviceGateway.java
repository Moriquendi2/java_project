/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.simulation.device;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mia.parking.core.port.DeviceGateway;
import mia.parking.core.port.DeviceSpaceStatus;

/** Small in-memory device adapter used while the physical devices are simulated. */
public final class SimulatedDeviceGateway implements DeviceGateway {
    private final Map<String, DeviceSpaceStatus> spaces = new LinkedHashMap<>();
    private final Map<String, Boolean> gates = new LinkedHashMap<>();

    @Override
    public synchronized void setGateOpen(String gateId, boolean open) {
        gates.put(gateId, open);
    }

    @Override
    public synchronized void setSpaceEnabled(String spaceId, boolean enabled) {
        DeviceSpaceStatus current = spaces.get(spaceId);
        spaces.put(spaceId, new DeviceSpaceStatus(spaceId, enabled,
                current != null && current.occupied(), current == null ? null : current.plate()));
    }

    @Override
    public synchronized List<DeviceSpaceStatus> querySpaceStatus() {
        return new ArrayList<>(spaces.values());
    }

    /** Updates the simulated sensor state for a scenario. */
    public synchronized void reportSpaceStatus(DeviceSpaceStatus status) {
        spaces.put(status.spaceId(), status);
    }

    /** Returns whether a simulated gate is open. */
    public synchronized boolean isGateOpen(String gateId) {
        return gates.getOrDefault(gateId, false);
    }
}
