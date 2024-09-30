package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private App app;

    public MainMenu(App app, String username) {
        this.app = app;
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu userMenu = new JMenu("User: " + username);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(userMenu);

        JMenu logoutMenu = new JMenu("Logout");
        menuBar.add(logoutMenu);

        JMenuItem logoutMenuItem = new JMenuItem("Confirm Logout");
        logoutMenu.add(logoutMenuItem);

        logoutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                app.setUsername(null);

                app.showOtherPanel("login");

                JOptionPane.showMessageDialog(null, "Logged out successfully", "Logout",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        add(menuBar, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Hi, " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        menuPanel.add(titleLabel);

        JButton playButton = new JButton("Play Sudoku");
        styleButton(playButton);

        playButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "playSudoku");
        });
        menuPanel.add(playButton);

        JButton solverButton = new JButton("Sudoku Solver");
        styleButton(solverButton);

        solverButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "sudokuSolver");
            app.showSudokuFrame();
        });
        menuPanel.add(solverButton);

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
