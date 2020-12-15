package banking;

import java.sql.*;

public class SQLSelectApp {

    private final String URL = "jdbc:sqlite:card.s3db";

    private Connection connect() {
        // SQLite connection string
        String url = URL;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Account selectAccount(String cardNumber) {
        String sql = "SELECT * FROM card WHERE number = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                String pin = rs.getString("pin");
                int balance = rs.getInt("balance");

                Card card = new Card(number, pin);
                return new Account(card, balance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int selectBalance(String cardNumber) {
        String sql = "SELECT balance FROM card WHERE number = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();

            return rs.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean selectNumber(String cardNumber) {
        String sql = "SELECT number FROM card WHERE number = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();

            String number = rs.getString("number");
            if (number != null && !number.isEmpty()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
