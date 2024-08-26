/**
 * The {@code SudokuSolver} class provides methods to solve a Sudoku puzzle using a backtracking algorithm.
 */
package SudokuSolver;

public class SudokuSolver {
    /**
     * Solves a Sudoku puzzle using a backtracking algorithm.
     * 
     * This method serves as an entry point to solve the puzzle by calling the private recursive solve method.
     * 
     * @param puzzle A 2D array representing the Sudoku puzzle. Each element in the array represents a cell
     *               in the puzzle, with 0 indicating an empty cell and values 1-9 indicating the filled cells.
     * @return {@code true} if the puzzle is solved successfully, {@code false} otherwise.
     */
    public static boolean solve(int[][] puzzle) {
        return solve(puzzle, 0, 0);
    }

    /**
     * Recursively solves the Sudoku puzzle using backtracking.
     * 
     * This method attempts to fill the puzzle by placing numbers in empty cells, and backtracks if a number
     * placement violates Sudoku rules. It continues until the puzzle is completely and correctly filled.
     * 
     * @param puzzle A 2D array representing the Sudoku puzzle.
     * @param row The current row index in the puzzle grid. Represents the row that is currently being
     *            filled or checked.
     * @param col The column index of the current cell in the puzzle grid.
     * @return {@code true} if the puzzle is solved successfully, {@code false} otherwise.
     */
    private static boolean solve(int[][] puzzle, int row, int col) {
        if (row == 9) {
            // All rows have been filled, puzzle is solved
            return true;
        }
        if (col == 9) {
            // Move to next row
            return solve(puzzle, row + 1, 0);
        }
        if (puzzle[row][col] != 0) {
            // Cell is already filled, move to next cell
            return solve(puzzle, row, col + 1);
        }
        // Try all possible values for the current cell
        for (int value = 1; value <= 9; value++) {
            if (isValid(puzzle, row, col, value)) {
                puzzle[row][col] = value;
                if (solve(puzzle, row, col + 1)) {
                    return true;
                }
                puzzle[row][col] = 0; // Backtrack
            }
        }
        return false;
    }

    /**
     * Checks if placing a value in a specific cell is valid according to Sudoku rules.
     * 
     * This method ensures that a value can be placed in the cell without violating Sudoku constraints:
     * - The value must not already be present in the same row.
     * - The value must not already be present in the same column.
     * - The value must not already be present in the same 3x3 sub-grid.
     * 
     * @param puzzle A 2D array representing the Sudoku puzzle.
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @param value The value to be placed in the cell.
     * @return {@code true} if the value can be placed in the cell without violating Sudoku rules, {@code false} otherwise.
     */
    private static boolean isValid(int[][] puzzle, int row, int col, int value) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (puzzle[row][i] == value || puzzle[i][col] == value) {
                return false;
            }
        }
        // Check 3x3 grid
        int gridRow = row / 3 * 3;
        int gridCol = col / 3 * 3;
        for (int i = gridRow; i < gridRow + 3; i++) {
            for (int j = gridCol; j < gridCol + 3; j++) {
                if (puzzle[i][j] == value) {
                    return false;
                }
            }
        }
        return true;
    }
}
