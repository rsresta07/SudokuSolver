/**
 * The {@code ValidatePuzzle} class provides methods to validate the correctness of a Sudoku puzzle.
 * 
 * This class ensures that the Sudoku puzzle adheres to Sudoku rules: each row, column, and 3x3 sub-grid must
 * contain unique values from 1 to 9, and values must be within the valid range.
 */
package SudokuSolver;

import javax.swing.JOptionPane;

public class ValidatePuzzle {
    
    /**
     * Validates a given Sudoku puzzle to ensure it follows Sudoku rules.
     * 
     * This method checks the puzzle for duplicate values in each row, column, and 3x3 sub-grid. It also verifies
     * that all values are within the range of 1 to 9. If any rules are violated, an error message is shown to the
     * user and the method returns {@code false}.
     * 
     * @param puzzle A 2D array representing the Sudoku puzzle. Each element in the array represents a cell in the
     *               puzzle, with 0 indicating an empty cell and values 1-9 indicating filled cells.
     * @return {@code true} if the puzzle is valid according to Sudoku rules, {@code false} otherwise.
     */
    public static boolean validate(int[][] puzzle) {
        // Check rows
        for (int i = 0; i < 9; i++) {
            boolean[] used = new boolean[9];
            for (int j = 0; j < 9; j++) {
                int value = puzzle[i][j];
                if (value < 0 || value > 9) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid puzzle: Value at row " + (i + 1) + ", column " + (j + 1)
                                    + " is not between 1 and 9.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (value != 0) {
                    if (used[value - 1]) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid puzzle: Row " + (i + 1) + " contains duplicate values.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    used[value - 1] = true;
                }
            }
        }
        
        // Check columns
        for (int j = 0; j < 9; j++) {
            boolean[] used = new boolean[9];
            for (int i = 0; i < 9; i++) {
                int value = puzzle[i][j];
                if (value < 0 || value > 9) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid puzzle: Value at row " + (i + 1) + ", column " + (j + 1)
                                    + " is not between 1 and 9.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (value != 0) {
                    if (used[value - 1]) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid puzzle: Column " + (j + 1) + " contains duplicate values.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    used[value - 1] = true;
                }
            }
        }
        
        // Check 3x3 grids
        for (int gridRow = 0; gridRow < 9; gridRow += 3) {
            for (int gridCol = 0; gridCol < 9; gridCol += 3) {
                boolean[] used = new boolean[9];
                for (int i = gridRow; i < gridRow + 3; i++) {
                    for (int j = gridCol; j < gridCol + 3; j++) {
                        int value = puzzle[i][j];
                        if (value < 0 || value > 9) {
                            JOptionPane.showMessageDialog(null,
                                    "Invalid puzzle: Value at row " + (i + 1) + ", column " + (j + 1)
                                            + " is not between 1 and 9.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if (value != 0) {
                            if (used[value - 1]) {
                                JOptionPane
                                        .showMessageDialog(null,
                                                "Invalid puzzle: Grid (" + (gridRow / 3 + 1) + "," + (gridCol / 3 + 1)
                                                        + ") contains duplicate values.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                            used[value - 1] = true;
                        }
                    }
                }
            }
        }
        return true;
    }
}
