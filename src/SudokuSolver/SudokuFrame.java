package SudokuSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Represents the main user interface for solving Sudoku puzzles.
 * 
 * This class extends {@link JPanel} to provide a GUI for interacting with Sudoku puzzles, including functionalities to 
 * load and save puzzles, solve the puzzle, and reset the board. It features a grid-based board for entering Sudoku numbers,
 * buttons for solving and resetting the puzzle, and menu options for loading and saving puzzles.
 */
public class SudokuFrame extends JPanel {
    /** The 2D array of {@link JTextField} representing the Sudoku board. */
    public JTextField[][] Board;
    private JMenuBar menuBar; // Menu bar for SudokuFrame
    private App app; // Reference to the App instance

    /**
     * Constructs a {@code SudokuFrame} instance with the given username.
     * Initializes the GUI components, sets up event listeners, and creates the board for the Sudoku puzzle.
     *
     * @param app the App instance
     * @param username the username of the current player
     */
    SudokuFrame(App app, String username) {
        this.app = app; // Store the App instance
        setLayout(new BorderLayout());
        menuBar = new JMenuBar(); // Initialize the menu bar

        // File Addition
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load Puzzle");
        JMenuItem saveItem = new JMenuItem("Save Puzzle");
        JMenuItem aboutItem = new JMenuItem("About");
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        // About Dialog
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(app.getMainFrame(),
                        "SudokuSolver v1.0\n\nThis program allows you to solve Sudoku puzzles.\n\nCreated by Rameshwor Shrestha and Salin Manandhar",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

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

        // Buttons for solving, resetting, and returning to the main menu
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        JButton resetButton = new JButton("Reset");
        styleButton(resetButton);
        JButton solveButton = new JButton("Solve");
        styleButton(solveButton);
        
        // Action for Back button: return to Main Menu
        backButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "mainMenu");
            app.showOtherPanel("mainMenu"); // Ensure menu bar is removed when going back to main menu
        });
        
        // Action for Solve button: solve Sudoku
        solveButton.addActionListener(e -> {
            if (solveSudoku()) {
                JOptionPane.showMessageDialog(app.getMainFrame(), "Puzzle solved!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(app.getMainFrame(), "Puzzle is unsolvable.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Action for Reset button: clear board
        resetButton.addActionListener(e -> {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Board[i][j].setText("");
                }
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);
        
        // Add panels to the layout
        add(BoardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // File loading
        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(app.getMainFrame());
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
                    JOptionPane.showMessageDialog(app.getMainFrame(), "Error loading file: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // File saving
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(app.getMainFrame());
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
                    JOptionPane.showMessageDialog(app.getMainFrame(), "Puzzle saved successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(app.getMainFrame(), "Error saving file: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Returns the menu bar of this SudokuFrame.
     *
     * @return the menu bar of this SudokuFrame
     */
    public JMenuBar getMenuBar() {
        return menuBar;
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
