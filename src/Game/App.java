package Game;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Login.LoginRegistration;

public class App {
    private String username;
    private JFrame mainFrame;
    private SudokuFrame sudokuFramePanel;

    // Static instance for Singleton
    private static App instance;

    // Private constructor to prevent instantiation
    private App() {
        mainFrame = new JFrame("Sudoku Generator and Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600);

        // CardLayout to manage different panels
        CardLayout cardLayout = new CardLayout();
        mainFrame.setLayout(cardLayout);

        // Initial login panel
        LoginRegistration loginPanel = new LoginRegistration(mainFrame, this);
        mainFrame.add(loginPanel, "login");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - mainFrame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - mainFrame.getHeight()) / 2);
        mainFrame.setLocation(centerX, centerY);

        mainFrame.setVisible(true);
    }

    // Public method to access the instance of App (Singleton)
    public static App getInstance() {
        if (instance == null) {
            instance = new App(); // Create instance if it doesn't exist
        }
        return instance;
    }

    // Define the main method as required for the program entry point
    public static void main(String[] args) {
        // Start the application
        SwingUtilities.invokeLater(() -> {
            App app = App.getInstance(); // Get the Singleton instance
            // Show login screen instead of main menu on startup
            app.showLoginScreen();
        });
    }

    public void showLoginScreen() {
        CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();
        mainFrame.remove(mainFrame.getContentPane().getComponent(0)); // Remove any existing panel

        // Show the login panel
        LoginRegistration loginPanel = new LoginRegistration(mainFrame, this);
        mainFrame.add(loginPanel, "login");

        cardLayout.show(mainFrame.getContentPane(), "login");
    }

    public void showMainMenu(String username) {
        setUsername(username);

        CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();
        mainFrame.remove(mainFrame.getContentPane().getComponent(0));

        JPanel mainMenuPanel = new MainMenu(this, getUsername());
        JPanel playSudokuPanel = new PlaySudoku(getUsername(), mainFrame);
        JPanel leaderboardPanel = new Leaderboard(mainFrame);

        sudokuFramePanel = new SudokuFrame(this, getUsername());

        // Add UserProfile panel for future reference
        JPanel userProfilePanel = new UserProfile(this, getUsername());

        mainFrame.add(mainMenuPanel, "mainMenu");
        mainFrame.add(playSudokuPanel, "playSudoku");
        mainFrame.add(sudokuFramePanel, "sudokuSolver");
        mainFrame.add(leaderboardPanel, "leaderboard");
        mainFrame.add(userProfilePanel, "userProfile"); // Add user profile to the CardLayout

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
        mainFrame.setJMenuBar(sudokuFramePanel.getMenuBar());
    }

    public void showOtherPanel(String panelName) {
        CardLayout layout = (CardLayout) mainFrame.getContentPane().getLayout();

        if (panelName.equals("login")) {
            JPanel loginPanel = new LoginRegistration(mainFrame, this);
            mainFrame.remove(mainFrame.getContentPane().getComponent(0));
            mainFrame.add(loginPanel, "login");
        }

        layout.show(mainFrame.getContentPane(), panelName);
        mainFrame.setJMenuBar(null);
    }

    public void showAdminDashboard() {
        try {
            SudokuDatabase db = new SudokuDatabase();
            JPanel adminDashboardPanel = new AdminDashboardPanel(db, this);
            mainFrame.add(adminDashboardPanel, "adminDashboard");

            CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();
            cardLayout.show(mainFrame.getContentPane(), "adminDashboard");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Failed to load admin dashboard.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}