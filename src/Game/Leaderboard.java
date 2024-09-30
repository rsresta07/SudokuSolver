package Game;

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

public class Leaderboard extends JPanel {
    private JComboBox<String> difficultyComboBox;
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;

    public Leaderboard(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel selectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel difficultyLabel = new JLabel("Difficulty:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        selectionPanel.add(difficultyLabel, gbc);

        difficultyComboBox = new JComboBox<>();
        difficultyComboBox.addItem("All");
        difficultyComboBox.addItem("Easy");
        difficultyComboBox.addItem("Medium");
        difficultyComboBox.addItem("Hard");
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        selectionPanel.add(difficultyComboBox, gbc);

        JButton showButton = new JButton("Show");
        styleButton(showButton);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 10, 5, 5);
        selectionPanel.add(showButton, gbc);

        JButton backButton = new JButton("Back");
        styleButton(backButton);

        gbc.gridx = 3;
        gbc.insets = new Insets(5, 10, 5, 5);
        selectionPanel.add(backButton, gbc);

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                showLeaderboard(selectedDifficulty);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) mainFrame.getContentPane().getLayout();
                layout.show(mainFrame.getContentPane(), "mainMenu");
            }
        });

        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        tableModel = new DefaultTableModel();
        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setRowHeight(25);
        leaderboardTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        leaderboardTable.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);

        add(selectionPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void showLeaderboard(String difficulty) {
        try {
            SudokuDatabase database = new SudokuDatabase();
            ResultSet resultSet = database.getLeaderboard(difficulty);

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Rank");
            columnNames.add("Username");
            columnNames.add("Puzzles Solved");
            columnNames.add("Score");
            columnNames.add("Shortest Time (s)");

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

            tableModel.setDataVector(data, columnNames);

            database.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void styleButton(JButton button) {

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        Color buttonColor = new Color(51, 153, 255);
        Color borderColor = new Color(51, 153, 255);
        int borderRadius = 30;

        button.setUI(new RoundedButtonUI(buttonColor, borderColor, borderRadius));
    }
}