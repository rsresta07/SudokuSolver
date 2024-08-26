/**
 * The {@code SudokuGenerator} class generates a Sudoku puzzle with a specified difficulty level and provides
 * methods to access the generated puzzle and its solution.
 * 
 * This class includes methods to create a complete Sudoku solution, generate a puzzle by removing cells based on
 * difficulty, and ensure the puzzle has a unique solution.
 */
package SudokuSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    /** Difficulty level constant for easy puzzles (40 cells to be removed). */
    public static final int EASY = 40;
    /** Difficulty level constant for medium puzzles (30 cells to be removed). */
    public static final int MEDIUM = 30;
    /** Difficulty level constant for hard puzzles (20 cells to be removed). */
    public static final int HARD = 20;

    /** 2D array representing the generated puzzle with some cells removed. */
    private int[][] puzzle;
    /** 2D array representing the complete solution of the Sudoku puzzle. */
    private int[][] solution;

    /**
     * Generates a Sudoku puzzle based on the specified difficulty level.
     * 
     * This method first generates a complete Sudoku solution using a backtracking algorithm, then copies the
     * solution to the puzzle grid and removes a specified number of cells based on the difficulty level to create
     * the final puzzle.
     * 
     * @param difficulty the difficulty level of the puzzle (number of cells to remove). Use constants
     *                   {@link #EASY}, {@link #MEDIUM}, or {@link #HARD}.
     */
    public void generate(int difficulty) {
        puzzle = new int[9][9];
        solution = new int[9][9];

        // Generate a complete solution
        solve(0, 0);

        // Copy the solution to the puzzle grid
        copySolutionToPuzzle();

        // Remove cells based on the specified difficulty level
        removeCells(difficulty);
    }

    /**
     * Recursively solves the Sudoku puzzle using backtracking.
     * 
     * This method attempts to place numbers in empty cells one by one and backtracks if a number placement
     * violates Sudoku rules. It continues until the puzzle is solved.
     * 
     * @param row the current row index in the Sudoku grid.
     * @param col the current column index in the Sudoku grid.
     * @return {@code true} if the puzzle is solved, {@code false} otherwise.
     */
    private boolean solve(int row, int col) {
        if (col == 9) {
            col = 0;
            row++;
            if (row == 9) {
                return true;
            }
        }

        if (solution[row][col] != 0) {
            return solve(row, col + 1);
        }

        List<Integer> numbers = generateRandomNumbers();
        for (int num : numbers) {
            if (isSafe(row, col, num)) {
                solution[row][col] = num;
                if (solve(row, col + 1)) {
                    return true;
                }
                solution[row][col] = 0;
            }
        }

        return false;
    }

    /**
     * Generates a shuffled list of numbers from 1 to 9.
     * 
     * This method is used to ensure that numbers are tried in a random order during the solving process.
     * 
     * @return a shuffled list of integers from 1 to 9.
     */
    private List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    /**
     * Checks if a number can be safely placed in a specified cell.
     * 
     * This method ensures that placing a number in the specified cell does not violate Sudoku rules by checking
     * the row, column, and 3x3 sub-grid.
     * 
     * @param row the row index of the cell.
     * @param col the column index of the cell.
     * @param num the number to be placed in the cell.
     * @return {@code true} if the number can be placed safely, {@code false} otherwise.
     */
    private boolean isSafe(int row, int col, int num) {
        return !usedInRow(row, num) && !usedInColumn(col, num) && !usedInBox(row - row % 3, col - col % 3, num);
    }

    /**
     * Checks if a number is used in the specified row.
     * 
     * @param row the row index to check.
     * @param num the number to check for.
     * @return {@code true} if the number is used in the row, {@code false} otherwise.
     */
    private boolean usedInRow(int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (solution[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a number is used in the specified column.
     * 
     * @param col the column index to check.
     * @param num the number to check for.
     * @return {@code true} if the number is used in the column, {@code false} otherwise.
     */
    private boolean usedInColumn(int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (solution[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a number is used in the 3x3 sub-grid that contains the specified cell.
     * 
     * @param boxStartRow the starting row index of the 3x3 sub-grid.
     * @param boxStartCol the starting column index of the 3x3 sub-grid.
     * @param num the number to check for.
     * @return {@code true} if the number is used in the 3x3 sub-grid, {@code false} otherwise.
     */
    private boolean usedInBox(int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (solution[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Copies the complete solution to the puzzle grid.
     * 
     * This method ensures that the puzzle grid starts with a valid complete Sudoku solution.
     */
    private void copySolutionToPuzzle() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle[row][col] = solution[row][col];
            }
        }
    }

    /**
     * Removes cells from the puzzle grid based on the specified difficulty level.
     * 
     * This method removes a certain number of cells from the puzzle grid to create the Sudoku puzzle while
     * ensuring the puzzle has a unique solution.
     * 
     * @param difficulty the difficulty level of the puzzle (number of cells to remove). Use constants
     *                   {@link #EASY}, {@link #MEDIUM}, or {@link #HARD}.
     */
    private void removeCells(int difficulty) {
        Random rand = new Random();
        int cellsToRemove = 81 - difficulty;

        while (cellsToRemove > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            if (puzzle[row][col] != 0) {
                int temp = puzzle[row][col];
                puzzle[row][col] = 0;

                if (!hasUniqueSolution()) {
                    puzzle[row][col] = temp;
                } else {
                    cellsToRemove--;
                }
            }
        }
    }

    /**
     * Checks if the current puzzle configuration has a unique solution.
     * 
     * This method temporarily modifies the puzzle to check if there is exactly one solution for the given configuration.
     * 
     * @return {@code true} if the puzzle has a unique solution, {@code false} otherwise.
     */
    private boolean hasUniqueSolution() {
        int[][] copy = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                copy[row][col] = puzzle[row][col];
            }
        }

        return solve(0, 0) && !hasMultipleSolutions(copy);
    }

    /**
     * Checks if the current puzzle configuration has multiple solutions.
     * 
     * This method verifies whether there is more than one solution for the given puzzle configuration.
     * 
     * @param puzzle the puzzle configuration to check.
     * @return {@code true} if the puzzle has multiple solutions, {@code false} otherwise.
     */
    private boolean hasMultipleSolutions(int[][] puzzle) {
        int[][] copy = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                copy[row][col] = puzzle[row][col];
            }
        }

        return !solve(0, 0);
    }

    /**
     * Gets the generated puzzle with some cells removed.
     * 
     * @return a 2D array representing the Sudoku puzzle with cells removed.
     */
    public int[][] getPuzzle() {
        return puzzle;
    }

    /**
     * Gets the complete solution of the Sudoku puzzle.
     * 
     * @return a 2D array representing the complete solution of the Sudoku puzzle.
     */
    public int[][] getSolution() {
        return solution;
    }
}
