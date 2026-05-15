package aquafit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
   private static final String URL =
        "jdbc:mysql://164.92.253.36:3306/gym_management_system_database"
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

private static final String USER = "12323499_project_db";
private static final String PASSWORD = "PUT_YOUR_PASSWORD";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
