## Java GUI Sudoku Solver

This is a Java-based Graphical User Interface (GUI) Sudoku Solver. It provides an interactive interface for solving Sudoku puzzles. It is written in Java and uses a backtracking algorithm to solve the puzzle.

## Features

- A user-friendly GUI that allows input of the Sudoku puzzle.
- The ability to solve any valid Sudoku puzzle.
- Provides a clear message if the puzzle is unsolvable.
- The ability to save and load puzzles from a file.
- Support for different levels of difficulty.

## Algorithm
- The Backtracking Algorithm is implemented in the `SudokuSolver.java` file.
```java
Line 16: public static boolean solve(int[][] puzzle) {…
    - An entry point for the solver method.

Line 17: return solve(puzzle, 0, 0);
    - The recursive solve method is called, starting from the top-left corner of the puzzle (row 0, column 0).

Line 32: private static boolean solve(int[][] puzzle, int row, int col) {…
    - The private recursive method handles the backtracking logic.

Line 39: return solve(puzzle, row + 1, 0);
    - If the end of a row is reached, the algorithm proceeds to the next row.

Line 43: return solve(puzzle, row, col + 1);
    - If the current cell is already filled, the algorithm moves to the next column in the same row.

Line 49: if (solve(puzzle, row, col + 1)) {…
    - Recursively attempts to solve the puzzle by trying the next cell after placing a number in the current cell.
```

## Installation

- Clone or download the project from GitHub.
- Open the project in your preferred Java Integrated Development Environment (IDE).
- Add jar files to the classpath if needed.
- Create the database using the provided SQL file.
- Build and run the project from your IDE.

## Usage
- Run the project.
- Login or Register as a user.
- Enter the values in the empty cells of the Sudoku puzzle in the GUI.
- Click on the "Solve" button to solve the puzzle.
- If the puzzle is solvable, the solved puzzle will be displayed in the GUI.
- A clear message will be displayed in the GUI if the puzzle is unsolvable or has multiple solutions.

## Future Enhancements
- Improve the algorithm to make it more efficient and faster
- Add a feature to allow users to highlight cells that contain the same value
