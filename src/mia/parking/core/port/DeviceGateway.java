/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.util.List;

/** Boundary for gate and parking-space device commands and status queries.
 * 中文：定义闸机与车位设备的控制和状态查询接口。
 */
public interface DeviceGateway {
    /**
     * 中文：设置指定闸机的模拟开关状态。
     */
    void setGateOpen(String gateId, boolean open);

    /**
     * 中文：更新车位启停状态并同步下发设备命令。
     */
    void setSpaceEnabled(String spaceId, boolean enabled);

    List<DeviceSpaceStatus> querySpaceStatus();
}
