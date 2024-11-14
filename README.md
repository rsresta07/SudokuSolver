
# Java GUI Sudoku Solver

This is a Java-based Graphical User Interface (GUI) Sudoku Solver. It provides an interactive interface for solving Sudoku puzzles. It is written in Java and uses a backtracking algorithm to solve the puzzle.

## Documentation

<<<<<<< HEAD
-   A user-friendly GUI that allows input of the Sudoku puzzle.
-   The ability to solve any valid Sudoku puzzle.
-   Provides a clear message if the puzzle is unsolvable.
-   The ability to save and load puzzles from a file.
-   Support for different levels of difficulty.

## Admin Password

**Username** : admin1
**Password** : admin123

## Algorithm

### SudokuSolver Class

=======
###  SudokuSolver Class
>>>>>>> acbd1dc3025ed61b0c93d0cb2c18929ee49990ee
The Backtracking Algorithm is implemented in the `SudokuSolver.java`. It's implemented in the private `solve` method.

The algorithm starts at line 29:

```java
private static boolean solve(int[][] puzzle, int row, int col) {
```

First, it checks if we've reached the end of the puzzle:

```java
if (row == 9) {
    // All rows have been filled, and the puzzle is solved
    return true;
}
```

If we've reached the end of a row, it moves to the next row:

```java
if (col == 9) {
    // Move to the next row
    return solve(puzzle, row + 1, 0);
}
```

If the current cell is already filled, it moves to the next cell:

```java
if (puzzle[row][col] != 0) {
    // Cell is already filled, move to next cell
    return solve(puzzle, row, col + 1);
}
```

The core of the algorithm is in the following loop:

```java
for (int value = 1; value <= 9; value++) {
    if (isValid(puzzle, row, col, value)) {
        puzzle[row][col] = value;
        if (solve(puzzle, row, col + 1)) {
            return true;
        }
        puzzle[row][col] = 0; // Backtrack
    }
}
```

This loop tries each value from 1 to 9 in the current cell. If a value is valid (doesn't violate Sudoku rules), it:

-   Places the value in the cell
-   Recursively tries to solve the rest of the puzzle
-   If the recursive call returns true, it means a solution is found
-   If not, it backtracks by setting the cell back to 0 and tries the next value

If no value works for this cell, it returns false, which triggers backtracking in the previous recursive call:

```java
return false;
```

The `isValid` method (lines 64-82) checks if placing a value in a specific cell violates any Sudoku rules by checking the row, column, and 3x3 sub-grid.
<br>
<br>

### SudokuGenerator Class

Another part is `SudokuGenerator.java` which contains an algorithm for generating Sudoku puzzles. The core of the generation algorithm is contained in the `solve` method, which uses a recursive backtracking approach to generate a complete Sudoku solution.

The algorithm starts at line 76:

```java
private boolean solve(int row, int col) {
    if (col == 9) {
        col = 0;
        row++;
        if (row == 9) {
            return true;
        }
    }
```

This part handles moving to the next row when we reach the end of a column. If we've filled the entire grid (row == 9), we return true as we've found a solution.

```java
if (solution[row][col] != 0) {
        return solve(row, col + 1);
    }
```

If the current cell is already filled (not 0), we move to the next cell.

```java
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
```

This is the core of the backtracking algorithm:

-   We generate a list of numbers 1-9 in random order.
-   For each number, we check if it's safe to place in the current cell.
-   If it's safe, we place the number and recursively try to solve the rest of the grid.
-   If the recursive call returns true, we've found a solution.
-   If not, we remove the number (backtrack) and try the next number.

```java
return false;
```

If we've tried all numbers and none work, we return false to trigger backtracking.

The `isSafe` method (lines 157-159) checks if a number can be placed in a cell without violating Sudoku rules:

```java
private boolean isSafe(int row, int col, int num) {
    return !usedInRow(row, num) && !usedInColumn(col, num) && !usedInBox(row - row % 3, col - col % 3, num);
}
```

After generating a complete solution, the algorithm removes cells to create the puzzle:

```java
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
```

This method:

-   Randomly selects cells to remove.
-   Removes a cell's value temporarily.
-   Check if the puzzle still has a unique solution.
-   If not, it restores the value; if yes, it keeps the cell empty.
-   Continues until the desired number of cells are removed.

## Features

- A user-friendly GUI that allows input of the Sudoku puzzle.
- The ability to solve any valid Sudoku puzzle.
- Provides a clear message if the puzzle is unsolvable.
- The ability to save and load puzzles from a file.
- Support for different levels of difficulty.

## Installation

<<<<<<< HEAD
-   Clone or download the project from GitHub.
-   Open the project in your preferred Java Integrated Development Environment (IDE).
-   Add jar files to the classpath if needed.
-   Create the database using the provided SQL file.
-   Build and run the project from your IDE.

## Usage

-   Run the project.
-   Login or Register as a user.
-   Enter the values in the empty cells of the Sudoku puzzle in the GUI.
-   Click on the "Solve" button to solve the puzzle.
-   If the puzzle is solvable, the solved puzzle will be displayed in the GUI.
-   A clear message will be displayed in the GUI if the puzzle is unsolvable or has multiple solutions.
=======
- Clone or download the project from GitHub.
- Open the project in your preferred Java Integrated Development Environment (IDE).
- Add jar files to the classpath if needed.
- Create the database using the provided SQL file.
- Build and run the project from your IDE.
    
## Run Locally

- Run the project.
- Login or Register as a user.
- Enter the values in the empty cells of the Sudoku puzzle in the GUI.
- Click on the "Solve" button to solve the puzzle.
- If the puzzle is solvable, the solved puzzle will be displayed in the GUI.
- A clear message will be displayed in the GUI if the puzzle is unsolvable or has multiple solutions.
>>>>>>> acbd1dc3025ed61b0c93d0cb2c18929ee49990ee


## Future Enhancements
<<<<<<< HEAD

-   Improve the algorithm to make it more efficient and faster
-   Add a feature to allow users to highlight cells that contain the same value
=======
- Improve the algorithm to make it more efficient and faster
- Add a feature to allow users to highlight cells that contain the same value
## License

[MIT](https://choosealicense.com/licenses/mit/)

>>>>>>> acbd1dc3025ed61b0c93d0cb2c18929ee49990ee
