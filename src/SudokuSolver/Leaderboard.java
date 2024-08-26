package SudokuSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * The {@code Leaderboard} class is a Java Swing application designed to display a leaderboard
 * for a Sudoku game. It allows users to select a difficulty level and view the rankings of
 * players based on their performance.
 * <p>
 * The leaderboard includes columns for rank, username, number of puzzles solved, total score,
 * and the shortest time taken. Users can select the difficulty level from a combo box, and
 * click the "Show" button to refresh the leaderboard based on the selected difficulty.
 * </p>
 */
public class Leaderboard extends JFrame {
    private JComboBox<String> difficultyComboBox;
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;

    /**
     * Constructs a new {@code Leaderboard} instance and initializes the UI components.
     * Sets up the leaderboard panel, difficulty selection panel, and the leaderboard table.
     */
    public Leaderboard() {
        setTitle("Leaderboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create leaderboard panel
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Create difficulty selection panel
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create and add the label
        JLabel difficultyLabel = new JLabel("Difficulty:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        selectionPanel.add(difficultyLabel, gbc);
        
        // Create and add the dropdown
        difficultyComboBox = new JComboBox<>();
        difficultyComboBox.addItem("All");
        difficultyComboBox.addItem("Easy");
        difficultyComboBox.addItem("Medium");
        difficultyComboBox.addItem("Hard");
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        selectionPanel.add(difficultyComboBox, gbc);

        // Create and add the Show button
        JButton showButton = new JButton("Show");
        styleButton(showButton);
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 10, 5, 5); // Add more spacing on the right of the dropdown
        selectionPanel.add(showButton, gbc);

        // Add action listener to the Show button
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve and display the leaderboard based on the selected difficulty
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                showLeaderboard(selectedDifficulty);
            }
        });

        // Ensure components are properly sized and spaced
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Make sure the button is aligned to the end

        // Create leaderboard table
        tableModel = new DefaultTableModel();
        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setRowHeight(25);
        leaderboardTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        leaderboardTable.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);

        leaderboardPanel.add(selectionPanel, BorderLayout.NORTH);
        leaderboardPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(leaderboardPanel);
        pack();
        setPreferredSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Retrieves and displays the leaderboard data based on the selected difficulty level.
     * <p>
     * This method queries the database for leaderboard data and updates the table model
     * with the results. The leaderboard data includes rank, username, number of puzzles solved,
     * total score, and the shortest time taken.
     * </p>
     * 
     * @param difficulty the selected difficulty level for filtering the leaderboard data
     */
    private void showLeaderboard(String difficulty) {
        try {
            // Retrieve leaderboard data from the database based on the selected difficulty
            SudokuDatabase database = new SudokuDatabase();
            ResultSet resultSet = database.getLeaderboard(difficulty);

            // Create column headers
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Rank");
            columnNames.add("Username");
            columnNames.add("Puzzles Solved");
            columnNames.add("Score");
            columnNames.add("Shortest Time (s)");

            // Populate data rows
            Vector<Vector<Object>> data = new Vector<>();
            int rank = 1;
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rank);
                row.add(resultSet.getString("username"));
                row.add(resultSet.getInt("puzzles_solved"));
                row.add(resultSet.getInt("total_score"));
                row.add(resultSet.getInt("shortest_time"));
                data.add(row);
                rank++;
            }

            // Update table model
            tableModel.setDataVector(data, columnNames);

            database.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Styles the given button with a rounded appearance and specific color.
     * 
     * @param button the button to style
     */
    private void styleButton(JButton button) {
        // Set button properties
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        // Define colors and border radius
        Color buttonColor = new Color(51, 153, 255); // Steel Blue color
        Color borderColor = new Color(51, 153, 255); // Same color for a seamless look
        int borderRadius = 30;

        // Apply custom UI
        button.setUI(new RoundedButtonUI(buttonColor, borderColor, borderRadius));
    }

    /**
     * The entry point of the {@code Leaderboard} application. Initializes and displays the
     * {@code Leaderboard} frame.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Leaderboard();
            }
        });
    }
}
