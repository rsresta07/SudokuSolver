/**
 * The {@code App} class serves as the entry point for the SudokuSolver application.
 * It initializes the {@link SudokuSolver.Login.LoginRegistration} class and 
 * invokes its {@code main} method to start the login and registration process.
 */
package SudokuSolver;

import SudokuSolver.Login.LoginRegistration;

public class App {
    public static void main(String[] args) {
        LoginRegistration lr = new LoginRegistration();
        lr.main(args);
    }
}
