    package Game;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;

    public class SudokuDatabase {
        private Connection connection;

        public SudokuDatabase() throws SQLException {
            connection = JdbcConn.getConnection();
        }

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

        public void close() throws SQLException {
            connection.close();
        }

        public ResultSet getUsers() throws SQLException {
            String query = "SELECT username FROM users WHERE role = 'user'";
            PreparedStatement stmt = connection.prepareStatement(query);
            return stmt.executeQuery();
        }

        // New method to delete a user
        public void deleteUser(String username) throws SQLException {
            // First, delete the user's scores to maintain referential integrity
            String deleteScoresQuery = "DELETE FROM scores WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteScoresQuery)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            // Then, delete the user from the users table
            String deleteUserQuery = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }
        }

        // New method to update a user's username
        public void updateUserUsername(String oldUsername, String newUsername) throws SQLException {
            // Update the username in the users table
            String updateUserQuery = "UPDATE users SET username = ? WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateUserQuery)) {
                stmt.setString(1, newUsername);
                stmt.setString(2, oldUsername);
                stmt.executeUpdate();
            }

            // Update the username in the scores table to maintain consistency
            String updateScoresQuery = "UPDATE scores SET username = ? WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateScoresQuery)) {
                stmt.setString(1, newUsername);
                stmt.setString(2, oldUsername);
                stmt.executeUpdate();
            }
        }

        public ResultSet getUserHistory(String username) throws SQLException {
            // Query to fetch historical game data for a specific user
            String query = "SELECT game_date, score FROM scores WHERE username = ? ORDER BY game_date DESC";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username); // Set the username parameter in the query
            return stmt.executeQuery(); // Return the result set containing the user's history
        }
    }
