/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.core.port;

import java.time.Instant;

/** Shared time source for business rules, devices, and scenario simulation. */
public interface Clock {
    Instant now();
}
