/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.util.List;

/** Boundary for gate and parking-space device commands and status queries. */
public interface DeviceGateway {
    void setGateOpen(String gateId, boolean open);

    void setSpaceEnabled(String spaceId, boolean enabled);

    List<DeviceSpaceStatus> querySpaceStatus();
}
