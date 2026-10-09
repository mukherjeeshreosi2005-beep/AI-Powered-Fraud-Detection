import java.sql.*;

public class CheckTransactions {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:sqlite:" + System.getProperty("user.home")
                + "/fraud_detection.db";

        try (Connection c = DriverManager.getConnection(url);
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(
                 "SELECT id, amount, risk_score, risk_level, created_at " +
                 "FROM transactions ORDER BY id DESC LIMIT 10")) {

            while (r.next()) {
                System.out.println(
                    "ID=" + r.getInt("id") +
                    ", Amount=" + r.getDouble("amount") +
                    ", Score=" + r.getInt("risk_score") +
                    ", Risk=" + r.getString("risk_level") +
                    ", Time=" + r.getString("created_at"));
            }
        }
    }
}