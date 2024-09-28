package SudokuSolver;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App {
    private String username; // Store the logged-in username
    private JFrame mainFrame; // Keep a reference to the main frame
    private SudokuFrame sudokuFramePanel; // Store reference to SudokuFrame

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App(); // Create instance of App
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        // Create the main JFrame
        mainFrame = new JFrame("Sudoku Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600); // Set initial size

        // Set layout
        CardLayout cardLayout = new CardLayout();
        mainFrame.setLayout(cardLayout);

        // Create the login panel
        LoginRegistration loginPanel = new LoginRegistration(mainFrame, this);

        // Add the login panel to the frame
        mainFrame.add(loginPanel, "login");

        // Show the login panel first
        cardLayout.show(mainFrame.getContentPane(), "login");

        // Pack the frame to fit components
//        mainFrame.pack();

        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - mainFrame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - mainFrame.getHeight()) / 2);
        mainFrame.setLocation(centerX, centerY);

        // Make the frame visible
        mainFrame.setVisible(true);
    }

    // Call this method after successful login to switch to the main menu
    public void showMainMenu(String username) {
        setUsername(username);

        // Remove login panel and add main menu panel
        CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();
        mainFrame.remove(mainFrame.getContentPane().getComponent(0)); // Remove login panel

        // Create and add the panels (e.g., main menu, Sudoku game)
        JPanel mainMenuPanel = new MainMenu(this, getUsername());
        JPanel playSudokuPanel = new PlaySudoku(getUsername(), mainFrame); // Use updated PlaySudoku
        JPanel leaderboardPanel = new Leaderboard(mainFrame);

        // Create the SudokuFrame panel
        sudokuFramePanel = new SudokuFrame(this, getUsername());

        // Add the panels to the frame using CardLayout
        mainFrame.add(mainMenuPanel, "mainMenu");
        mainFrame.add(playSudokuPanel, "playSudoku");
        mainFrame.add(sudokuFramePanel, "sudokuSolver");
        mainFrame.add(leaderboardPanel, "leaderboard");

        // Show the main menu panel
        cardLayout.show(mainFrame.getContentPane(), "mainMenu");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }
    
    public void showSudokuFrame() {
        CardLayout layout = (CardLayout) mainFrame.getContentPane().getLayout();
        layout.show(mainFrame.getContentPane(), "sudokuSolver");
        mainFrame.setJMenuBar(sudokuFramePanel.getMenuBar()); // Set the menu bar to SudokuFrame's menu bar
    }

    public void showOtherPanel(String panelName) {
        CardLayout layout = (CardLayout) mainFrame.getContentPane().getLayout();

        if (panelName.equals("login")) {
            // Create a new LoginRegistration instance
            JPanel loginPanel = new LoginRegistration(mainFrame, this);
            
            // Replace the existing login panel
            mainFrame.remove(mainFrame.getContentPane().getComponent(0)); // Remove the old login panel
            mainFrame.add(loginPanel, "login"); // Add the new login panel
        }

        layout.show(mainFrame.getContentPane(), panelName);
        mainFrame.setJMenuBar(null); // Remove menu bar for other panels
    }
}
