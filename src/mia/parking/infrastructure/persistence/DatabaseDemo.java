/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

import java.io.IOException;
import java.sql.SQLException;

/** Reads and prints the sample parking zones to demonstrate a MySQL connection. */
public final class DatabaseDemo {
    private DatabaseDemo() {
    }

    public static void main(String[] args) {
        try {
            var zones = new ParkingZoneDao().findAll();
            if (zones.isEmpty()) {
                System.out.println("Connected to MySQL. PARKING_ZONE contains no rows.");
                return;
            }
            System.out.println("Connected to MySQL. Parking zones:");
            for (ParkingZone zone : zones) {
                System.out.printf("%s | %s | capacity %d | %s%n",
                        zone.id(), zone.name(), zone.capacity(), zone.description());
            }
        } catch (IOException | SQLException exception) {
            System.err.println("Database demo failed: " + exception.getMessage());
            System.exit(1);
        }
    }
}
