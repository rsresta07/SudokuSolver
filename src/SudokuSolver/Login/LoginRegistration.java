package SudokuSolver.Login;

import javax.swing.*;
import SudokuSolver.App;
import SudokuSolver.JdbcConn;
import SudokuSolver.RoundedButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginRegistration extends JPanel {

    private final JFrame mainFrame;
    private final App app; // Reference to the App class

    public LoginRegistration(JFrame mainFrame, App app) {
        this.mainFrame = mainFrame;
        this.app = app; // Initialize reference to the App class
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel welcomeLabel = new JLabel("Welcome to Sudoku", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Increase the font size to make it bigger
        add(welcomeLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);

        JTextField usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameTextField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx=0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(loginButton, gbc);
        
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(registerButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    app.setUsername(username); // Pass username to App class

                    // Create and show main menu with username
                    app.showMainMenu(username);

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Registration(); // Show registration panel
            }
        });

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
}
