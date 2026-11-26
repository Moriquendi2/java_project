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

/** Application boundary used by the control center and simulated devices.
 * 中文：定义控制中心和模拟设备调用的停车业务操作。
 */
public interface ParkingControlService {
    /** Creates a session and reserves a space, or returns an unaccepted result.
     * 中文：处理入场请求、分配符合条件的车位并创建停车会话。
     */
    EntryResult handleEntry(EntryRequest request);

    /** Applies a sensor/camera arrival, departure, or failure message.
     * 中文：根据设备消息更新车位和停车会话状态。
     */
    void handleSpaceEvent(SpaceEvent event);

    /** Closes the active plate session and returns the payment and gate decision.
     * 中文：结算停车费用、结束会话并返回出口放行决定。
     */
    SettlementResult handleExit(ExitRequest request);

    /** Returns a read-only dashboard view of the current parking state.
     * 中文：汇总车位、分区和近期事件供仪表盘显示。
     */
    DashboardSnapshot getDashboardSnapshot();

    /** A null zone id means all spaces. */
    List<ParkingSpace> getSpaces(String zoneId);

    Optional<ParkingSpace> getSpaceDetails(String spaceId);

    /** Updates the space and sends the matching enable/disable device command.
     * 中文：更新车位启停状态并同步下发设备命令。
     */
    void setSpaceEnabled(String spaceId, boolean enabled);

    /**
     * 中文：将巡逻人员提交的情况记录为停车事件。
     */
    void reportPatrolFinding(PatrolReport report);

    /**
     * 中文：读取指定时间范围内的停车统计。
     */
    ParkingStatistics getStatistics(Instant from, Instant to);

    /** Saves current in-memory state through the configured snapshot adapter.
     * 中文：保存当前停车状态以供后续恢复。
     */
    void saveSnapshot();

    /** Restores state from the configured snapshot when one is available.
     * 中文：从快照存储恢复停车状态。
     */
    void restoreSnapshot();
}
