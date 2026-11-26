/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.time;

import java.time.Instant;
import mia.parking.core.port.Clock;

/** Production clock adapter backed by the JDK system clock.
 * 中文：通过 JDK 系统时间实现统一时钟接口。
 */
public final class SystemClock implements Clock {
    /**
     * 中文：返回当前统一时钟时间。
     */
    @Override
    public Instant now() {
        return Instant.now();
    }
}
