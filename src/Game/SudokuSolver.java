package Game;

public class SudokuSolver {

    public static boolean solve(int[][] puzzle) {
        return solve(puzzle, 0, 0);
    }

    private static boolean solve(int[][] puzzle, int row, int col) {
        if (row == 9) {

            return true;
        }
        if (col == 9) {

            return solve(puzzle, row + 1, 0);
        }
        if (puzzle[row][col] != 0) {

            return solve(puzzle, row, col + 1);
        }

        for (int value = 1; value <= 9; value++) {
            if (isValid(puzzle, row, col, value)) {
                puzzle[row][col] = value;
                if (solve(puzzle, row, col + 1)) {
                    return true;
                }
                puzzle[row][col] = 0;
            }
        }
        return false;
    }

    private static boolean isValid(int[][] puzzle, int row, int col, int value) {

        for (int i = 0; i < 9; i++) {
            if (puzzle[row][i] == value || puzzle[i][col] == value) {
                return false;
            }
        }

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
