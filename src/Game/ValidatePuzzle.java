package Game;

import javax.swing.JOptionPane;

public class ValidatePuzzle {
    
    public static boolean validate(int[][] puzzle) {
        
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

