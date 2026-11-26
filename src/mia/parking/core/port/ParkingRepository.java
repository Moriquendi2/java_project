/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import mia.parking.core.domain.Incident;
import mia.parking.core.domain.ParkingSession;
import mia.parking.core.domain.ParkingSpace;
import mia.parking.core.domain.ParkingStatistics;
import mia.parking.core.domain.ParkingStateSnapshot;
import mia.parking.core.domain.SettlementResult;

/** Data access boundary implemented with JDBC by the persistence owner.
 * 中文：定义停车业务的数据读取、保存和状态恢复接口。
 */
public interface ParkingRepository {
    List<ParkingSpace> loadSpaces();

    List<ParkingSession> loadActiveSessions();

    Optional<ParkingSession> findSessionById(String sessionId);

    Optional<ParkingSession> findActiveSessionByPlate(String plate);

    /**
     * 中文：按编号新增或更新停车会话。
     */
    void saveSession(ParkingSession session);

    /**
     * 中文：按编号新增或更新车位记录。
     */
    void saveSpace(ParkingSpace space);

    /**
     * 中文：创建并保存运营事件。
     */
    void saveIncident(Incident incident);

    /**
     * 中文：保存一条停车结算记录。
     */
    void saveSettlement(SettlementResult settlement);

    /**
     * 中文：使用快照恢复车位、会话和事件数据。
     */
    void restoreState(ParkingStateSnapshot snapshot);

    List<Incident> loadRecentIncidents(int limit);

    /**
     * 中文：统计指定时段内的停车数据。
     */
    ParkingStatistics loadStatistics(Instant from, Instant to);
}
