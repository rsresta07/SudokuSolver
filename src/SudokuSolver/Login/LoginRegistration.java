package SudokuSolver.Login;

import javax.swing.*;
import SudokuSolver.JdbcConn;
import SudokuSolver.MainMenu;
import SudokuSolver.RoundedButtonUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * {@code LoginRegistration} class provides a GUI for user login and registration within the Sudoku Solver application.
 * <p>
 * This class extends {@link JFrame} and displays a window where users can enter their username and password to log in,
 * or click a button to open the registration window. It handles user authentication by checking credentials against
 * stored data in a MySQL database.
 * </p>
 */
public class LoginRegistration extends JFrame {

    /**
     * Constructs a {@code LoginRegistration} frame with login and registration functionality.
     * <p>
     * Sets up the window with the title "Sudoku Solver - Login and Registration". Initializes and configures the 
     * components such as labels, text fields, and buttons for login and registration. Adds action listeners to handle
     * login and registration events. Positions the window at the center of the screen and sets its size.
     * </p>
     */

    public LoginRegistration() {
        setTitle("Sudoku Solver - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Create and configure the panel using GridBagLayout
        JPanel loginRegistrationPanel = new JPanel(new GridBagLayout());
        loginRegistrationPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Allow components to expand horizontally
        gbc.weightx = 1.0;

        // Create and add username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginRegistrationPanel.add(usernameLabel, gbc);

        JTextField usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
//        gbc.weightx = 1.0; // Allow the text field to take up more space
        gbc.gridwidth = 2; 
        loginRegistrationPanel.add(usernameTextField, gbc);

        // Create and add password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginRegistrationPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;  // Span 2 columns for a bigger size
//        gbc.weightx = 1.0;
        loginRegistrationPanel.add(passwordField, gbc);

        // Create and add login and register buttons
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
//        gbc.weightx = 0.5;
        loginRegistrationPanel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
//        gbc.weightx = 0.5;
        loginRegistrationPanel.add(registerButton, gbc);

        // Add action listeners for login and register buttons
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    dispose();
                    new MainMenu(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Registration();
            }
        });

        // Add the panel to the frame
        getContentPane().add(loginRegistrationPanel, BorderLayout.CENTER);

        // Set the size of the window
        setSize(400, 300);

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);

        // Make the window visible
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        Color buttonColor = new Color(51, 153, 255); // Steel Blue color
        Color borderColor = new Color(51, 153, 255);
        int borderRadius = 30;

        button.setUI(new RoundedButtonUI(buttonColor, borderColor, borderRadius));
    }

    private boolean validateLogin(String username, String password) {
        try (Connection conn = JdbcConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return BCrypt.checkpw(password, storedHash);
                }
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("SQL Exception occurred: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new LoginRegistration();
    }
}