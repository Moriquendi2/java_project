/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.infrastructure.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Opens MySQL connections using the local files/database.properties settings. */
public final class DatabaseConnection {
    private static final Path CONFIG_PATH = Path.of("files", "database.properties");

    private DatabaseConnection() {
    }

    public static Connection open() throws IOException, SQLException {
        if (!Files.isRegularFile(CONFIG_PATH)) {
            throw new IOException("Missing " + CONFIG_PATH
                    + ". Copy files/database.properties.example and set your local MySQL credentials.");
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
            properties.load(input);
        }

        String url = required(properties, "jdbc.url");
        String username = required(properties, "jdbc.username");
        String password = properties.getProperty("jdbc.password", "");
        return DriverManager.getConnection(url, username, password);
    }

    private static String required(Properties properties, String key) throws IOException {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IOException("Missing " + key + " in " + CONFIG_PATH + ".");
        }
        return value.trim();
    }
}
