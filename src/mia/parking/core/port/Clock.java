/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.time.Instant;

/** Shared time source for business rules, devices, and scenario simulation.
 * 中文：定义业务服务和模拟流程共用的时间来源接口。
 */
public interface Clock {
    /**
     * 中文：返回当前统一时钟时间。
     */
    Instant now();
}
