
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FraudServlet extends HttpServlet {

    private Connection connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        String path = System.getProperty("user.home")
                + File.separator + "fraud_detection.db";
        return DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    private void createTable() throws Exception {
        try (Connection con = connect();
             Statement st = con.createStatement()) {
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, hour INTEGER, failed_attempts INTEGER, " +
                "new_device TEXT, new_location TEXT, distance REAL, " +
                "international TEXT, risk_score INTEGER, risk_level TEXT, " +
                "action TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            double amount = Double.parseDouble(request.getParameter("amount"));
            int hour = Integer.parseInt(request.getParameter("hour"));
            int attempts = Integer.parseInt(request.getParameter("attempts"));
            String device = request.getParameter("device");
            String location = request.getParameter("location");
            double distance = Double.parseDouble(request.getParameter("distance"));
            String international = request.getParameter("international");

            if (amount < 0 || hour < 0 || hour > 23 ||
                attempts < 0 || distance < 0) {
                throw new IllegalArgumentException("Invalid input values.");
            }

            int score = 0;
            StringBuilder reasons = new StringBuilder();

            if (amount > 50000) {
                score += 20;
                reasons.append("Large transaction amount; ");
            }
            if (hour < 6) {
                score += 15;
                reasons.append("Unusual transaction time; ");
            }
            if (attempts >= 3) {
                score += 15;
                reasons.append("Multiple failed attempts; ");
            }
            if ("yes".equals(device)) {
                score += 10;
                reasons.append("New device; ");
            }
            if ("yes".equals(location)) {
                score += 10;
                reasons.append("New location; ");
            }
            if (distance > 500) {
                score += 10;
                reasons.append("Large travel distance; ");
            }
            if ("yes".equals(international)) {
                score += 10;
                reasons.append("International transaction; ");
            }

            String level = score >= 60 ? "HIGH"
                    : score >= 30 ? "MEDIUM" : "LOW";
            String action = score >= 60 ? "BLOCK TRANSACTION"
                    : score >= 30 ? "VERIFY TRANSACTION"
                    : "ALLOW TRANSACTION";

            if (reasons.length() == 0) {
                reasons.append("No risk indicators detected.");
            }

            createTable();

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO transactions " +
                    "(amount, hour, failed_attempts, new_device, " +
                    "new_location, distance, international, risk_score, " +
                    "risk_level, action) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                ps.setDouble(1, amount);
                ps.setInt(2, hour);
                ps.setInt(3, attempts);
                ps.setString(4, device);
                ps.setString(5, location);
                ps.setDouble(6, distance);
                ps.setString(7, international);
                ps.setInt(8, score);
                ps.setString(9, level);
                ps.setString(10, action);
                ps.executeUpdate();
            }

            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><head><title>Fraud Result</title>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<style>body{font-family:Arial;background:#101827;color:white;padding:30px}");
            out.println(".card{max-width:750px;margin:auto;background:#1e293b;padding:25px;border-radius:15px}");
            out.println("h1{color:#60a5fa} .score{font-size:36px;font-weight:bold}");
            out.println("a{color:#93c5fd}table{width:100%;border-collapse:collapse;margin-top:20px}");
            out.println("td,th{padding:9px;border-bottom:1px solid #475569;text-align:left}");
            out.println("</style></head><body><div class='card'>");
            out.println("<h1>Fraud Detection Result</h1>");
            out.println("<p class='score'>Risk Score: " + score + "/90</p>");
            out.println("<h2>Risk Level: " + level + "</h2>");
            out.println("<p><b>Recommended action:</b> " + action + "</p>");
            out.println("<p><b>Risk indicators:</b> " + reasons + "</p>");
            out.println("<p>Transaction saved successfully to the SQLite database.</p>");
            out.println("<h2>Recent Transaction History</h2>");
            out.println("<table><tr><th>ID</th><th>Amount</th><th>Score</th><th>Risk</th><th>Time</th></tr>");

            try (Connection con = connect();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                    "SELECT id, amount, risk_score, risk_level, created_at " +
                    "FROM transactions ORDER BY id DESC LIMIT 10")) {
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>"
                        + rs.getDouble("amount") + "</td><td>"
                        + rs.getInt("risk_score") + "</td><td>"
                        + rs.getString("risk_level") + "</td><td>"
                        + rs.getString("created_at") + "</td></tr>");
                }
            }

            out.println("</table><p><a href='index.html'>Check another transaction</a></p>");
            out.println("</div></body></html>");

        } catch (IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid input. Please check the values and try again.");
        } catch (Exception ex) {
            throw new ServletException("Fraud detection or database operation failed.", ex);
        }
    }
}
