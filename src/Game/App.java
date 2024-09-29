package Game;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Login.LoginRegistration;

public class App {
    private String username; 
    private JFrame mainFrame;
    private SudokuFrame sudokuFramePanel; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        mainFrame = new JFrame("Sudoku Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600);

        CardLayout cardLayout = new CardLayout();
        mainFrame.setLayout(cardLayout);

        LoginRegistration loginPanel = new LoginRegistration(mainFrame, this);

        mainFrame.add(loginPanel, "login");

        cardLayout.show(mainFrame.getContentPane(), "login");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - mainFrame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - mainFrame.getHeight()) / 2);
        mainFrame.setLocation(centerX, centerY);

        mainFrame.setVisible(true);
    }

    public void showMainMenu(String username) {
        setUsername(username);

        CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();
        mainFrame.remove(mainFrame.getContentPane().getComponent(0));

        JPanel mainMenuPanel = new MainMenu(this, getUsername());
        JPanel playSudokuPanel = new PlaySudoku(getUsername(), mainFrame); 
        JPanel leaderboardPanel = new Leaderboard(mainFrame);

        sudokuFramePanel = new SudokuFrame(this, getUsername());

        mainFrame.add(mainMenuPanel, "mainMenu");
        mainFrame.add(playSudokuPanel, "playSudoku");
        mainFrame.add(sudokuFramePanel, "sudokuSolver");
        mainFrame.add(leaderboardPanel, "leaderboard");

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
}
