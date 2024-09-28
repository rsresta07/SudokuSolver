package SudokuSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

import SudokuSolver.RoundedButtonUI;

/**
 * {@code Registration} class provides a GUI for user registration in the Sudoku Solver application.
 * <p>
 * This class extends {@link JFrame} and displays a window where users can enter a username, password, and confirm their
 * password to register. It handles validation of input fields, checks for existing usernames, and stores the new user
 * credentials in a MySQL database with a hashed password.
 * </p>
 */
public class Registration extends JFrame {

    /**
     * Constructs a {@code Registration} frame with registration functionality.
     * <p>
     * Sets up the window with the title "Registration". Initializes and configures the components such as labels, text fields,
     * and a register button. Adds an action listener to the register button to handle the registration process, including
     * validation and storing user data. Positions the window at the center of the screen and sets its size.
     * </p>
     */
    public Registration() {
        setTitle("Registration");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create and configure registration panel
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Create and add username label and text field to the panel
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registrationPanel.add(usernameLabel, gbc);
        
        JTextField usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        registrationPanel.add(usernameTextField, gbc);

        // Create and add password label and password field to the panel
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registrationPanel.add(passwordLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        registrationPanel.add(passwordField, gbc);


        // Create and add confirm password label and password field to the panel
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registrationPanel.add(confirmPasswordLabel, gbc);
        
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        registrationPanel.add(confirmPasswordField, gbc);

        // Create and add register button to the panel
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.CENTER;
        registrationPanel.add(registerButton, gbc);

        // Add action listener for the register button
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate registration form
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (validateUsername(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Hash the password before storing it
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    if (addUser(username, hashedPassword)) {
                        JOptionPane.showMessageDialog(null, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Close registration GUI
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Add the registration panel to the frame
        getContentPane().add(registrationPanel, BorderLayout.CENTER);
        pack();

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);

        setVisible(true);
    }

    /**
     * Checks if the provided username already exists in the database.
     * <p>
     * Connects to the MySQL database and queries for the username. Returns {@code true} if the username is found, indicating
     * that it already exists; otherwise, returns {@code false}.
     * </p>
     *
     * @param username The username to check for existence.
     * @return {@code true} if the username exists; {@code false} otherwise.
     */
    private boolean validateUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Username already exists
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
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
     * Adds a new user to the database with the provided username and hashed password.
     * <p>
     * Connects to the MySQL database and inserts the username and hashed password into the {@code users} table. Returns
     * {@code true} if the user was added successfully; otherwise, returns {@code false}.
     * </p>
     *
     * @param username The username of the new user.
     * @param hashedPassword The hashed password of the new user.
     * @return {@code true} if the user was added successfully; {@code false} otherwise.
     */
    private boolean addUser(String username, String hashedPassword) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            int rows = stmt.executeUpdate();
            return rows > 0; // User added successfully

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
