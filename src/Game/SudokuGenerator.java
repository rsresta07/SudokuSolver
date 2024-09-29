package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    
    public static final int EASY = 40;
    
    public static final int MEDIUM = 30;
    
    public static final int HARD = 20;

    public static final int NIGHTMARE = 20;

    
    private int[][] puzzle;
    
    private int[][] solution;

    public void generate(int difficulty) {
        puzzle = new int[9][9];
        solution = new int[9][9];

        solve(0, 0);

        copySolutionToPuzzle();

        removeCells(difficulty);
    }

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

    private List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    private boolean isSafe(int row, int col, int num) {
        return !usedInRow(row, num) && !usedInColumn(col, num) && !usedInBox(row - row % 3, col - col % 3, num);
    }

    private boolean usedInRow(int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (solution[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInColumn(int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (solution[row][col] == num) {
                return true;
            }
        }
        return false;
    }

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

    private void copySolutionToPuzzle() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle[row][col] = solution[row][col];
            }
        }
    }

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

    private boolean hasUniqueSolution() {
        int[][] copy = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                copy[row][col] = puzzle[row][col];
            }
        }

        return solve(0, 0) && !hasMultipleSolutions(copy);
    }

    private boolean hasMultipleSolutions(int[][] puzzle) {
        int[][] copy = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                copy[row][col] = puzzle[row][col];
            }
        }

        return !solve(0, 0);
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int[][] getSolution() {
        return solution;
    }
}
