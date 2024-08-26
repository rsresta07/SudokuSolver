package SudokuSolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The {@code JdbcConn} class provides a method to establish a connection to the
 * MySQL database for the SudokuSolver application.
 * <p>
 * It uses the JDBC URL to connect to a MySQL database named "sudoku" with the
 * default username "root" and no password.
 * </p>
 */
public class JdbcConn {
    
    /**
     * Establishes and returns a connection to the MySQL database.
     * 
     * @return a {@link Connection} object for the MySQL database
     * @throws SQLException if a database access error occurs or the URL is incorrect
     */
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sudoku";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}
