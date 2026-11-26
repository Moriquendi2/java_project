/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

import java.io.IOException;
import java.sql.SQLException;

/** Reads and prints the sample parking zones to demonstrate a MySQL connection.
 * 中文：演示连接 MySQL 并读取停车分区样例。
 */
public final class DatabaseDemo {
    /**
     * 中文：阻止创建独立数据库演示类的实例。
     */
    private DatabaseDemo() {
    }

    /**
     * 中文：创建应用服务和模拟适配器，并显示主窗口。
     */
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
