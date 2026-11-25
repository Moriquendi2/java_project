/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.time;

import java.time.Instant;
import mia.parking.core.port.Clock;

/** Production clock adapter backed by the JDK system clock. */
public final class SystemClock implements Clock {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
