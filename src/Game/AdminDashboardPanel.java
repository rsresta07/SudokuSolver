package Game;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class AdminDashboardPanel extends JPanel {
    private SudokuDatabase db;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private App app;

    public AdminDashboardPanel(SudokuDatabase db, App app) {
        this.db = db;
        this.app = app; // Add reference to the main app instance
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Admin Dashboard - User List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Table to display users
        String[] columnNames = { "Username", "Actions" };
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);

        // Retrieve users and populate the table initially
        loadUserData();

        // Custom renderer and editor for buttons in the Actions column
        TableColumn actionColumn = userTable.getColumnModel().getColumn(1);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JTextField()));

        JScrollPane tableScrollPane = new JScrollPane(userTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Logout button for admin
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            app.showLoginScreen(); // Switch to the login screen
            JOptionPane.showMessageDialog(this, "Logged out successfully", "Logout", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());
        actionPanel.add(logoutButton);

        // Adding action panel to the AdminDashboard
        add(mainPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    // Method to reload user data from the database
    private void loadUserData() {
        try {
            ResultSet rs = db.getUsers();
            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                String username = rs.getString("username");
                Object[] rowData = { username, "Actions" }; // Placeholder for buttons
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving user data", "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ButtonRenderer - Renders buttons in the action column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText("Actions");
            return this;
        }
    }

    // ButtonEditor - Handles button actions
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;
        private JTable table;

        public ButtonEditor(JTextField textField) {
            super(textField);
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.table = table;
            this.label = "Actions";
            isPushed = true;

            JButton button = new JButton(label);
            button.addActionListener(e -> {
                if (isPushed) {
                    String username = (String) table.getValueAt(row, 0);
                    showUserOptions(username); // Call method to show options (Edit, Delete, etc.)
                    fireEditingStopped(); // Commit the editing immediately
                }
                isPushed = false;
            });
            return button;
        }

        private void showUserOptions(String username) {
            String[] options = { "Edit", "Delete" }; // Removed "Show History"
            int choice = JOptionPane.showOptionDialog(AdminDashboardPanel.this,
                    "Choose an action for user " + username, "User Actions",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            switch (choice) {
                case 0: // Edit
                    editUser(username);
                    break;
                case 1: // Delete
                    deleteUser(username);
                    break;
            }
        }

    }

    // Show leaderboard (History)
    // private void showLeaderboard(String username) {
    // try {
    // ResultSet history = db.getUserHistory(username);

    // if (!history.isBeforeFirst()) { // Check if ResultSet is empty
    // JOptionPane.showMessageDialog(this, "No history found for user: " + username,
    // "User History", JOptionPane.INFORMATION_MESSAGE);
    // return;
    // }

    // StringBuilder historyText = new StringBuilder("History for user: " + username
    // + "\n\n");

    // // Loop through the ResultSet and append each entry to historyText
    // while (history.next()) {
    // String gameDate = history.getString("game_date"); // Make sure the date
    // format is compatible
    // int score = history.getInt("score");
    // historyText.append("Date: ").append(gameDate).append(", Score:
    // ").append(score).append("\n");
    // }

    // JOptionPane.showMessageDialog(this, historyText.toString(), "User History",
    // JOptionPane.INFORMATION_MESSAGE);

    // } catch (SQLException e) {
    // JOptionPane.showMessageDialog(this, "Error retrieving user history",
    // "Database Error",
    // JOptionPane.ERROR_MESSAGE);
    // e.printStackTrace();
    // }
    // }

    // Delete user and reload the data
    private void deleteUser(String username) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user " + username + "?",
                "Delete User", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                db.deleteUser(username);
                JOptionPane.showMessageDialog(this, "User " + username + " deleted successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String rowUsername = (String) tableModel.getValueAt(i, 0);
                    if (rowUsername.equals(username)) {
                        tableModel.removeRow(i);
                        break;
                    }
                }

                // Refresh the table model to ensure editors/renderers are updated
                userTable.setModel(new DefaultTableModel(tableModel.getDataVector(),
                        new Vector<>(Arrays.asList("Username", "Actions"))));
                tableModel.fireTableDataChanged(); // Notify table data change

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user", "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Edit user and reload the data
    private void editUser(String username) {
        String newUsername = JOptionPane.showInputDialog(this, "Enter new username for " + username, username);
        if (newUsername != null && !newUsername.isEmpty()) {
            try {
                db.updateUserUsername(username, newUsername);
                JOptionPane.showMessageDialog(this, "User updated successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadUserData(); // Reload the user data
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating user", "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
