package Game;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Random;

public class PlaySudoku extends JPanel {
    private JTextField[][] Board;

    private long startTime = 0;

    private boolean puzzleGenerated = false;

    private String username;

    private JLabel puzzleIdLabel;

    private JLabel livesIdLabel;

    private SudokuGenerator generator;

    private SudokuSolver solver;

    private int[][] puzzle;

    private int[][] solution;

    private int score = 0;

    private String selectedOption;

    private int currentDifficulty;

    private Timer gameTimer;

    private JLabel timerLabel;

    private long timeTaken;

    private int hint;

    private JButton hintBtn;

    private int lives;

    private String[][] temp;

    public PlaySudoku(String username, JFrame mainFrame) {
        this.username = username;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        JPanel BoardPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        Board = new JTextField[9][9];

        JPanel headingPanel = new JPanel(new GridLayout(1, 1));

        puzzleIdLabel = new JLabel("Difficulty Level: ");
        puzzleIdLabel.setFont(new Font("Arial", Font.BOLD, 12));

        livesIdLabel = new JLabel("Lives: ");
        livesIdLabel.setFont(new Font("Arial", Font.BOLD, 12));

        headingPanel.add(puzzleIdLabel);
        headingPanel.add(livesIdLabel);

        add(headingPanel, BorderLayout.NORTH);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(40, 40));
                textField.setHorizontalAlignment(JTextField.CENTER);
                Board[i][j] = textField;
                BoardPanel.add(textField);

                if (i % 3 == 2 && j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK));
                } else if (i % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 0, Color.BLACK));
                } else if (j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 2, Color.BLACK));
                } else {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
                }

                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        String input = textField.getText();
                        if (input.length() > 1) {
                            textField.setText(input.substring(0, 1));
                        }

                        if (e.getKeyChar() >= '1' && e.getKeyChar() <= '9') {
                            checkCurrentInput();
                        } else {
                            textField.setText("");
                        }
                    }
                });

                textField.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        if (puzzleGenerated && startTime == 0) {
                            startTime = System.currentTimeMillis();
                        }
                        clearCellHighlights();
                    }

                    public void removeUpdate(DocumentEvent e) {
                    }

                    public void changedUpdate(DocumentEvent e) {
                    }
                });
            }
        }

        JButton backButton = new JButton("Back");
        styleButton(backButton);

        JButton generate = new JButton("Generate");
        styleButton(generate);

        hintBtn = new JButton("Hint: " + hint);
        styleButton(hintBtn);

        JPanel ButtonPanel = new JPanel();

        timerLabel = new JLabel("00:00:00");
        ButtonPanel.add(timerLabel);

        backButton.setToolTipText("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) mainFrame.getContentPane().getLayout();
                cardLayout.show(mainFrame.getContentPane(), "mainMenu");
            }
        });

        ButtonPanel.add(generate);
        ButtonPanel.add(hintBtn);
        ButtonPanel.add(backButton);

        generator = new SudokuGenerator();
        solver = new SudokuSolver();

        generate.addActionListener(e -> generatePuzzle());

        hintBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (puzzleGenerated && hint > 0) {
                    clearCellHighlights();

                    boolean filledGrid = false;

                    Random rand = new Random();

                    while (!filledGrid) {
                        int row = rand.nextInt(9);
                        int col = rand.nextInt(9);

                        if (Board[row][col].getText().isEmpty()) {
                            Board[row][col].setText(String.valueOf(solution[row][col]));
                            Board[row][col].setEditable(false);
                            Board[row][col].setBackground(Color.GREEN);
                            hint--;
                            hintBtn.setText("Hint: " + hint);
                            filledGrid = true;
                            checkCurrentInput();
                        }
                    }


                    if (hint == 0) {
                        hintBtn.setEnabled(false);
                        hintBtn.setToolTipText("Out of hint");
                    }

                }
            }
        });

        add(BoardPanel, BorderLayout.CENTER);
        add(ButtonPanel, BorderLayout.SOUTH);
    }

    private void checkCurrentInput() {
        if (puzzleGenerated) {
            
            boolean correct = true;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String value = Board[i][j].getText();

                    if (!value.isEmpty()) {
                        if (!value.equals(String.valueOf(solution[i][j]))) {
                            Board[i][j].setBackground(Color.RED);
                            correct = false;

                            if (temp[i][j] == null || !temp[i][j].equals(value)) {
                                temp[i][j] = value;
                                lives--;
                                livesIdLabel.setText("Lives: " + lives);

                                if (lives == 0) {
                                    gameTimer.stop();

                                    puzzleGenerated = false;

                                    JOptionPane.showMessageDialog(PlaySudoku.this,
                                            "You've run out of lives. Try Again!!!",
                                            "Game Over",
                                            JOptionPane.INFORMATION_MESSAGE);

                                    disableGameBoard();

                                }
                            }
                        }// else {
                        //     Board[i][j].setBackground(Color.WHITE);
                        // }
                    } else {
                        correct = false;
                    }
                }
            }

            if (correct) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long seconds = (elapsedTime / 1000) % 60;
                long minutes = (elapsedTime / (1000 * 60)) % 60;
                long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
                String message = String.format("Time taken: %02d:%02d:%02d", hours, minutes, seconds);

                timeTaken = elapsedTime;

                if (selectedOption.equals("Easy")) {
                    score = 10;
                } else if (selectedOption.equals("Medium")) {
                    score = 20;
                } else if (selectedOption.equals("Hard")) {
                    score = 30;
                } else if (selectedOption.equals("Nightmare")) {
                    score = 50;
                }

                hintBtn.setEnabled(false);
                disableGameBoard();

                gameTimer.stop();

                JOptionPane.showMessageDialog(PlaySudoku.this,
                        "Congratulations!\nScore: " + score +
                                "\nTime taken: " + message +
                                "\nDifficulty Level: " + selectedOption,
                        "Congratulations!",
                        JOptionPane.INFORMATION_MESSAGE);
                try {
                    SudokuDatabase database = new SudokuDatabase();
                    database.saveScore(username, score, (int) (timeTaken / 1000), selectedOption);
                    database.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(PlaySudoku.this,
                    "Please generate a puzzle first.",
                    "No Puzzle Generated",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void disableGameBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Board[i][j].setEditable(false);
            }
        }
    }

    private void generatePuzzle() {

        if (puzzleGenerated) {
            int confirm = JOptionPane.showConfirmDialog(PlaySudoku.this,
                    "A game is already in progress. Are you sure you want to create new puzzle?",
                    "Confirm new game",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.NO_OPTION) {
                return;
            } else {
                gameTimer.stop();
                puzzleGenerated = false;
            }
        }

        String[] options = { "Easy", "Medium", "Hard", "Nightmare" };
        selectedOption = (String) JOptionPane.showInputDialog(
                PlaySudoku.this,
                "Choose difficulty level:",
                "Difficulty Level",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedOption != null) {
            currentDifficulty = getDifficultyFromOption(selectedOption);

            generator.generate(currentDifficulty);
            puzzle = generator.getPuzzle();
            solution = generator.getSolution();

            puzzleIdLabel.setText("Difficulty Level: " + selectedOption);
            populateBoardWithPuzzle();
            startTime = System.currentTimeMillis();
            temp = new String[9][9];
            puzzleGenerated = true;

            if (selectedOption.equals("Nightmare")) {
                lives = 1;
                hint = 0;
                hintBtn.setText("Hint: " + hint);
                hintBtn.setEnabled(false);
            } else {
                hint = 3;
                lives = 3;
                hintBtn.setText("Hint: " + hint);
                hintBtn.setEnabled(true);
            }
            livesIdLabel.setText("Lives: " + lives);

            gameTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (startTime != 0) {
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        long seconds = (elapsedTime / 1000) % 60;
                        long minutes = (elapsedTime / (1000 * 60)) % 60;
                        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
                        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                    }
                }
            });
            gameTimer.start();
        }
    }

    private int getDifficultyFromOption(String option) {
        switch (option) {
            case "Easy":
                return SudokuGenerator.EASY;
            case "Medium":
                return SudokuGenerator.MEDIUM;
            case "Hard":
                return SudokuGenerator.HARD;
            case "Nightmare":
                return SudokuGenerator.NIGHTMARE;
            default:
                throw new IllegalArgumentException("Unknown difficulty: " + option);
        }
    }

    private void populateBoardWithPuzzle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = puzzle[i][j];
                Board[i][j].setText(value != 0 ? String.valueOf(value) : "");
                Board[i][j].setEditable(value == 0);
            }
        }
    }

    private void clearCellHighlights() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Board[i][j].setBackground(Color.WHITE);
            }
        }
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
