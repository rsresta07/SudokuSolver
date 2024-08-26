package SudokuSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Represents the main user interface for solving Sudoku puzzles.
 * 
 * This class extends {@link JFrame} to provide a GUI for interacting with Sudoku puzzles, including functionalities to 
 * load and save puzzles, solve the puzzle, and reset the board. It features a grid-based board for entering Sudoku numbers,
 * buttons for solving and resetting the puzzle, and menu options for loading and saving puzzles.
 */
public class SudokuFrame extends JFrame {
    /** The 2D array of {@link JTextField} representing the Sudoku board. */
    public JTextField[][] Board;

    /**
     * Constructs a {@code SudokuFrame} instance with the given username.
     * Initializes the GUI components, sets up event listeners, and creates the board for the Sudoku puzzle.
     *
     * @param username the username of the current player
     */
    SudokuFrame(String username) {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // File Addition
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load Puzzle");
        JMenuItem saveItem = new JMenuItem("Save Puzzle");
        JMenuItem aboutItem = new JMenuItem("About");
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SudokuFrame.this,
                        "SudokuSolver v1.0\n\nThis program allows you to solve Sudoku puzzles.\n\nCreated by Rameshwor Shrestha and Salin Manandhar",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        fileMenu.add(aboutItem);

        JPanel BoardPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        Board = new JTextField[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(40, 40));
                textField.setHorizontalAlignment(JTextField.CENTER);
                Board[i][j] = textField;
                BoardPanel.add(textField);

                // Add borders around each 3x3 grid
                if (i % 3 == 2 && j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK));
                } else if (i % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 0, Color.BLACK));
                } else if (j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 2, Color.BLACK));
                } else {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
                }
            }
        }

        // Buttons Solve, Reset, and Back
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        JButton resetButton = new JButton("Reset");
        styleButton(resetButton);
        JButton solveButton = new JButton("Solve");
        styleButton(solveButton);
        JPanel ButtonPanel = new JPanel();
        backButton.setToolTipText("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close SudokuSolver UI
                new MainMenu(username); // Open Main Menu
            }
        });
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (solveSudoku()) {
                    JOptionPane.showMessageDialog(SudokuFrame.this, "Puzzle solved!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SudokuFrame.this, "Puzzle is unsolvable.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        Board[i][j].setText("");
                    }
                }
            }
        });
        ButtonPanel.add(solveButton, BorderLayout.CENTER);
        ButtonPanel.add(resetButton, BorderLayout.EAST);
        ButtonPanel.add(backButton, BorderLayout.WEST);
        getContentPane().add(BoardPanel, BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);
        setVisible(true);

        // Loading puzzle file in txt format only
        loadItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(SudokuFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                        String line;
                        int row = 0;
                        while ((line = reader.readLine()) != null && row < 9) {
                            String[] values = line.trim().split("\\s+");
                            for (int col = 0; col < 9 && col < values.length; col++) {
                                String value = values[col];
                                if (!value.equals(".")) {
                                    Board[row][col].setText(value);
                                }
                            }
                            row++;
                        }
                        reader.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(SudokuFrame.this, "Error loading file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Saving solved puzzle as a file
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(SudokuFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                String value = Board[i][j].getText().trim();
                                if (value.isEmpty()) {
                                    writer.write(".");
                                } else {
                                    writer.write(value);
                                }
                                if (j < 8) {
                                    writer.write(" ");
                                }
                            }
                            writer.newLine();
                        }
                        writer.close();
                        JOptionPane.showMessageDialog(SudokuFrame.this, "Puzzle saved successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(SudokuFrame.this, "Error saving file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Solves the Sudoku puzzle based on the current state of the board.
     * 
     * This method copies the current values from the text fields into a 2D integer array, validates the puzzle, and attempts
     * to solve it using the {@link SudokuSolver}. If the puzzle is successfully solved, the solution is updated back into the
     * text fields with a delay to visualize the solving process.
     * 
     * @return {@code true} if the puzzle is successfully solved, {@code false} otherwise
     */
    private boolean solveSudoku() {
        int[][] puzzle = new int[9][9];
        // Copy values from text fields to puzzle array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = Board[i][j].getText().trim();
                if (!value.isEmpty()) {
                    puzzle[i][j] = Integer.parseInt(value);
                }
            }
        }
        // Validate puzzle
        if (!ValidatePuzzle.validate(puzzle)) {
            return false;
        }
        // Check if puzzle is empty
        boolean isEmpty = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] != 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        if (isEmpty) {
            JOptionPane.showMessageDialog(SudokuFrame.this, "Puzzle is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (SudokuSolver.solve(puzzle)) {
            // Copy values from puzzle array back to text fields
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Board[i][j].setText(Integer.toString(puzzle[i][j]));
                    // Delay to visualize the solving process
                    try {
                        Thread.sleep(30); // Adjust the delay time as needed
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return true;
        } else {
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
}
