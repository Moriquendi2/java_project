/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Basic JDBC reads and inserts for the standalone parking-zone example.
 * 中文：通过 JDBC 查询和写入停车分区记录。
 */
public final class ParkingZoneDao {
    public List<ParkingZone> findAll() throws IOException, SQLException {
        String sql = "SELECT zone_id, zone_name, description, capacity "
                + "FROM PARKING_ZONE ORDER BY zone_id";
        List<ParkingZone> zones = new ArrayList<>();
        try (Connection connection = DatabaseConnection.open();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                zones.add(new ParkingZone(
                        results.getString("zone_id"),
                        results.getString("zone_name"),
                        results.getString("description"),
                        results.getInt("capacity")));
            }
        }
        return List.copyOf(zones);
    }

    /**
     * 中文：向数据库写入一条停车分区记录。
     */
    public void insert(ParkingZone zone) throws IOException, SQLException {
        String sql = "INSERT INTO PARKING_ZONE (zone_id, zone_name, description, capacity) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.open();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, zone.id());
            statement.setString(2, zone.name());
            statement.setString(3, zone.description());
            statement.setInt(4, zone.capacity());
            statement.executeUpdate();
        }
    }
}
