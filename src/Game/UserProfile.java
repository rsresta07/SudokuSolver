package Game;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserProfile extends JPanel {
    public UserProfile(App app, String username) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use vertical BoxLayout for stacking

        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Wrap titleLabel in a JPanel with BoxLayout
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(titleLabel); // Add the title label to the panel

        // Add the title panel to the main UserProfile panel
        add(titlePanel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 4 rows, 1 column, with padding
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel usernameL = new JLabel("Username: ");
        JLabel usernameLabel = new JLabel("" + username);
        usernameL.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        infoPanel.add(usernameL);
        infoPanel.add(usernameLabel);

        int gamesPlayed = getGamesPlayed(username);
        int highestScore = getHighestScore(username);
        int shortestTime = getShortestTime(username);

        JLabel gamesPlayedL = new JLabel("Games Played: ");
        JLabel gamesPlayedLabel = new JLabel(""+ gamesPlayed);
        gamesPlayedL.setFont(new Font("Arial", Font.BOLD, 18));
        gamesPlayedLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        infoPanel.add(gamesPlayedL);
        infoPanel.add(gamesPlayedLabel);

        JLabel highestScoreL = new JLabel("Highest Score: ");
        JLabel highestScoreLabel = new JLabel("" + highestScore);
        highestScoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        highestScoreL.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(highestScoreL);
        infoPanel.add(highestScoreLabel);

        JLabel shortestTimeL = new JLabel("Shortest Time: ");
        JLabel shortestTimeLabel = new JLabel("" + shortestTime + " seconds");
        shortestTimeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        shortestTimeL.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(shortestTimeL);
        infoPanel.add(shortestTimeLabel);

        add(infoPanel);

        // Personal leaderboard panel
        JLabel leaderboardTitle = new JLabel("Personal Leaderboard");
        leaderboardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboardTitle.setAlignmentX(Component.LEFT_ALIGNMENT); // Align title label to the left

        // Set up JTable for leaderboard
        String[] columnNames = { "Score", "Time (s)", "Difficulty" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the JTable model with data
        List<String[]> leaderboardData = getLeaderboard(username);
        for (String[] entry : leaderboardData) {
            tableModel.addRow(entry);
        }

        JTable leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardTable.setRowHeight(30);

        // Center-align cell contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Create JScrollPane and set preferred size
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT); // Align scroll pane to the left

        // ** Add the following line to set the preferred size of the scroll pane **
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Adjust width and height as needed

        // ** Optionally, set a preferred size for the JTable **
        leaderboardTable.setPreferredScrollableViewportSize(new Dimension(400, 200)); // Adjust width and height as
                                                                                      // needed

        // Leaderboard and Button Container Panel
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new GridBagLayout());
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill the cell horizontally
        gbc.insets = new Insets(10, 0, 10, 0); // Vertical gap of 10

        JButton backButton = new JButton("Back to Main Menu");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align button to center within the panel
        backButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "mainMenu");
        });

        leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between table and button

        gbc.gridx = 0; // Column
        gbc.gridy = 0; // Row
        leaderboardPanel.add(leaderboardTitle, gbc);
        gbc.gridy = 1; // Move to next row
        leaderboardPanel.add(scrollPane, gbc);
        gbc.gridy = 2; // Move to next row
        leaderboardPanel.add(backButton, gbc);

        add(leaderboardPanel); // Add leaderboard panel containing table and button
    }

    private int getGamesPlayed(String username) {
        int gamesPlayed = 0;
        String query = "SELECT COUNT(*) FROM scores WHERE username = ?";

        try (Connection conn = JdbcConn.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    gamesPlayed = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching games played: " + e.getMessage());
            e.printStackTrace();
        }
        return gamesPlayed;
    }

    private int getHighestScore(String username) {
        int highestScore = 0;
        String query = "SELECT MAX(score) FROM scores WHERE username = ?";

        try (Connection conn = JdbcConn.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    highestScore = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching highest score: " + e.getMessage());
            e.printStackTrace();
        }
        return highestScore;
    }

    private int getShortestTime(String username) {
        int shortestTime = Integer.MAX_VALUE;
        String query = "SELECT MIN(time_taken) FROM scores WHERE username = ?";

        try (Connection conn = JdbcConn.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    shortestTime = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching shortest time: " + e.getMessage());
            e.printStackTrace();
        }
        return shortestTime == Integer.MAX_VALUE ? 0 : shortestTime;
    }

    private List<String[]> getLeaderboard(String username) {
        List<String[]> leaderboardData = new ArrayList<>();
        String query = "SELECT score, time_taken, difficulty FROM scores WHERE username = ? ORDER BY score DESC LIMIT 5";

        try (Connection conn = JdbcConn.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int score = rs.getInt("score");
                    int timeTaken = rs.getInt("time_taken");
                    String difficulty = rs.getString("difficulty");
                    leaderboardData.add(new String[] { String.valueOf(score), String.valueOf(timeTaken), difficulty });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard data: " + e.getMessage());
            e.printStackTrace();
        }
        return leaderboardData;
    }
}
