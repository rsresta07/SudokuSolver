package SudokuSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The {@code MainMenu} class represents the main menu interface for the SudokuSolver application.
 */
public class MainMenu extends JPanel {
    private App app; // Store reference to the App instance

    public MainMenu(App app, String username) {
        this.app = app; // Initialize the app reference
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create user menu
        JMenu userMenu = new JMenu("User: " + username);
        menuBar.add(Box.createHorizontalGlue()); // Align user menu to the right
        menuBar.add(userMenu);

        // Create logout menu
        JMenu logoutMenu = new JMenu("Logout");
        menuBar.add(logoutMenu);

        // Create a menu item for the logout action
        JMenuItem logoutMenuItem = new JMenuItem("Confirm Logout");
        logoutMenu.add(logoutMenuItem);

     // Add action listener to the menu item
        logoutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Logout menu item clicked");
                
                app.setUsername(null); // Clear the username in the app
                
                // Show the login panel
                app.showOtherPanel("login");
                
                // Optional: Show a confirmation dialog
                JOptionPane.showMessageDialog(null, "Logged out successfully", "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        // Add the menu bar to the MainMenu panel
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        add(menuBar, BorderLayout.NORTH);

        // Create menu panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Hi, " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        menuPanel.add(titleLabel);

        // Create play button
        JButton playButton = new JButton("Play Sudoku");
        styleButton(playButton);
        playButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "playSudoku");
        });
        menuPanel.add(playButton);

        // Create solver button
        JButton solverButton = new JButton("Sudoku Solver");
        styleButton(solverButton);
        solverButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "sudokuSolver");
            app.showSudokuFrame(); // Ensure menu bar is set for SudokuFrame
        });
        menuPanel.add(solverButton);

        // Create leaderboard button
        JButton leaderboardButton = new JButton("Leaderboard");
        styleButton(leaderboardButton);
        leaderboardButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "leaderboard");
        });
        menuPanel.add(leaderboardButton);

        add(menuPanel, BorderLayout.CENTER);
    }
    
    private void styleButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        Color buttonColor = new Color(51, 153, 255); // Steel Blue color
        Color borderColor = new Color(51, 153, 255); // Same color for a seamless look
        int borderRadius = 30;
        button.setUI(new RoundedButtonUI(buttonColor, borderColor, borderRadius));
    }
}
