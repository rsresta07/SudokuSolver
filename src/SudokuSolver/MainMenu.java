package SudokuSolver;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import SudokuSolver.Login.LoginRegistration;

/**
 * The {@code MainMenu} class represents the main menu interface for the SudokuSolver application.
 * <p>
 * This class provides options for users to play Sudoku, access the Sudoku solver, view the leaderboard,
 * and log out. The main menu is displayed after successful login, and it includes buttons to navigate
 * to different parts of the application.
 * </p>
 * <p>
 * The menu bar shows the currently logged-in user's name and includes a logout option. Clicking "Logout"
 * will close the main menu and open the login and registration interface. The menu panel includes buttons
 * for playing Sudoku, using the Sudoku solver, and viewing the leaderboard.
 * </p>
 */
public class MainMenu extends JFrame {

    /**
     * Constructs a new {@code MainMenu} instance with the given username.
     * Initializes the user interface, including the menu bar, user menu, and menu panel with
     * buttons to access various features of the application.
     * 
     * @param username the username of the currently logged-in user
     */
    public MainMenu(String username) {
        setTitle("SUDOKU");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        // Create user menu
        JMenu userMenu = new JMenu("User: " + username);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(userMenu);

        // Create logout menu item
//      // Create logout menu
        JMenu logoutMenu = new JMenu("Logout");
        menuBar.add(logoutMenu);

        // Create a menu item for the logout action
        JMenuItem logoutMenuItem = new JMenuItem("Confirm Logout");
        logoutMenu.add(logoutMenuItem);

        // Add action listener to the menu item
        logoutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close main menu
                new LoginRegistration(); // Open login and registration UI
            }
        });

        // Optional: Add a mouse listener to the logout menu to make it clickable
        logoutMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMenu.doClick(); // Simulate a click to open the menu
            }
        });

        // Create menu panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create title label
        JLabel titleLabel = new JLabel("Sudoku Solver", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        menuPanel.add(titleLabel);

        // Create play button
        JButton playButton = new JButton("Play Sudoku");
        styleButton(playButton);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close main menu
                new PlaySudoku(username); // Open PlaySudoku UI
            }
        });
        menuPanel.add(playButton);

        // Create solver button
        JButton solverButton = new JButton("Sudoku Solver");
        styleButton(solverButton);
        solverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close main menu
                new SudokuFrame(username); // Open Sudoku Solver UI
            }
        });
        menuPanel.add(solverButton);

        // Create leaderboard button
        JButton leaderboardButton = new JButton("Leaderboard");
        styleButton(leaderboardButton);
        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Leaderboard(); // Open leaderboard UI
            }
        });
        menuPanel.add(leaderboardButton);

        getContentPane().add(menuPanel, BorderLayout.CENTER);

        // Set the size of the window
        setSize(400, 500);

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);

        // Make the window visible
        setVisible(true);
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
}
