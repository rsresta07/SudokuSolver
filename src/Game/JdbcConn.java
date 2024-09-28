package Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConn {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sudoku";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}
