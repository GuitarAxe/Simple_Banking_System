package banking;

import java.sql.*;

public class SQLInsertApp {

    private static int id = 0;
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

    public void updateId() {
        String sql = "SELECT row FROM table WHERE id=(SELECT max(id) FROM table)";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            id = rs.getInt("id");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insert(String cardNumber, String pin, int balance) {
        String sql = "INSERT INTO card(id, number, pin, balance) VALUES(?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("id " + id);
            id++;
            pstmt.setInt(1, id);
            pstmt.setString(2, cardNumber);
            pstmt.setString(3, pin);
            pstmt.setInt(4, balance);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void insertBalance(int balance, String cardNumber) {
        String sql = "UPDATE card SET balance = ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, balance);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
