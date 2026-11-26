/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.simulation.device;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mia.parking.core.port.DeviceGateway;
import mia.parking.core.port.DeviceSpaceStatus;

/** Small in-memory device adapter used while the physical devices are simulated.
 * 中文：在内存中模拟闸机和车位设备的状态及控制命令。
 */
public final class SimulatedDeviceGateway implements DeviceGateway {
    private final Map<String, DeviceSpaceStatus> spaces = new LinkedHashMap<>();
    private final Map<String, Boolean> gates = new LinkedHashMap<>();

    /**
     * 中文：设置指定闸机的模拟开关状态。
     */
    @Override
    public synchronized void setGateOpen(String gateId, boolean open) {
        gates.put(gateId, open);
    }

    /**
     * 中文：更新车位启停状态并同步下发设备命令。
     */
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

    /** Updates the simulated sensor state for a scenario.
     * 中文：记录模拟设备上报的车位状态。
     */
    public synchronized void reportSpaceStatus(DeviceSpaceStatus status) {
        spaces.put(status.spaceId(), status);
    }

    /** Returns whether a simulated gate is open.
     * 中文：查询指定模拟闸机是否开启。
     */
    public synchronized boolean isGateOpen(String gateId) {
        return gates.getOrDefault(gateId, false);
    }
}
