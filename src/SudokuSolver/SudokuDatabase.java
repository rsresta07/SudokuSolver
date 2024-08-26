package SudokuSolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages interactions with the Sudoku database.
 * 
 * This class provides methods for connecting to a MySQL database, saving scores, retrieving leaderboard data, and closing
 * the database connection. The database connection is established using JDBC, and the class assumes a table named 'scores' 
 * exists in the 'sudoku' database.
 */
public class SudokuDatabase {
    /** The JDBC connection to the database. */
    private Connection connection;
    
    /** The URL for the database connection. */
    private String url = "jdbc:mysql://localhost:3306/sudoku";
    
    /** The username for the database connection. */
    private String username = "root";
    
    /** The password for the database connection. */
    private String password = "";

    /**
     * Constructs a {@code SudokuDatabase} instance and establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs or the url is {@code null}
     */
    public SudokuDatabase() throws SQLException {
        // Connect to the database
        connection = DriverManager.getConnection(url, username, password);
    }
    
    /**
     * Saves the player's score to the database.
     *
     * @param username the username of the player
     * @param score the score achieved by the player
     * @param time the time taken to solve the puzzle (in seconds)
     * @param difficulty the difficulty level of the puzzle
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public void saveScore(String username, int score, int time, String difficulty) throws SQLException {
        String query = "INSERT INTO scores (username, score, time_taken, difficulty) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.setInt(3, time);
            stmt.setString(4, difficulty);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Retrieves the leaderboard from the database.
     *
     * @param difficulty the difficulty level of the leaderboard. If "All", retrieves the leaderboard for all difficulties.
     * @return a {@link ResultSet} containing the leaderboard data
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public ResultSet getLeaderboard(String difficulty) throws SQLException {
        String query;
        if (difficulty.equals("All")) {
            query = "SELECT username, COUNT(id) AS puzzles_solved, MIN(time_taken) AS shortest_time, SUM(score) AS total_score FROM scores GROUP BY username ORDER BY puzzles_solved DESC, shortest_time ASC";
        } else {
            query = "SELECT username, COUNT(id) AS puzzles_solved, MIN(time_taken) AS shortest_time, SUM(score) AS total_score FROM scores WHERE difficulty = ? GROUP BY username ORDER BY puzzles_solved DESC, shortest_time ASC";
        }
        PreparedStatement stmt = connection.prepareStatement(query);
        if (!difficulty.equals("All")) {
            stmt.setString(1, difficulty);
        }
        return stmt.executeQuery();
    }

    /**
     * Closes the database connection.
     * 
     * This method should be called to release database resources when they are no longer needed.
     *
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public void close() throws SQLException {
        // Close the database connection
        connection.close();
    }
}
