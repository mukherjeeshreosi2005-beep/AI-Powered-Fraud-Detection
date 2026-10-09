import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class VerifyDatabase {
    public static void main(String[] args) {
        Path dbPath = Path.of(
            System.getProperty("user.home"),
            "fraud_detection.db"
        );

        String url = "jdbc:sqlite:" + dbPath.toAbsolutePath();

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            System.out.println("Database: " + dbPath);
            System.out.println("Connection successful.");

            try (ResultSet tables = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table'")) {
                System.out.println("Database tables:");

                while (tables.next()) {
                    System.out.println("- " + tables.getString("name"));
                }
            }

            try (ResultSet rows = statement.executeQuery(
                    "SELECT COUNT(*) FROM transactions")) {
                if (rows.next()) {
                    System.out.println(
                        "Saved transactions: " + rows.getInt(1)
                    );
                }
            }

        } catch (Exception e) {
            System.err.println("Verification failed: " + e.getMessage());
            System.exit(1);
        }
    }
}