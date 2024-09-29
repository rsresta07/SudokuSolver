package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SudokuFrame extends JPanel {
    
    public JTextField[][] Board;
    private JMenuBar menuBar; 
    private App app; 

    SudokuFrame(App app, String username) {
        this.app = app;
        setLayout(new BorderLayout());
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load Puzzle");
        JMenuItem saveItem = new JMenuItem("Save Puzzle");
        JMenu aboutMenu = new JMenu("About");
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);

        aboutMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

                if (i % 3 == 2 && j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK));
                } else if (i % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 0, Color.BLACK));
                } else if (j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 2, Color.BLACK));
                } else {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
                }

                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        String input = textField.getText();
                        if (input.length() > 1) {
                            textField.setText(input.substring(0, 1));
                        }

                        if (e.getKeyChar() >= '1' && e.getKeyChar() <= '9') {
                            
                        } else{
                            textField.setText("");
                        }
                    }
                });
            }
        }

        JButton backButton = new JButton("Back");
        styleButton(backButton);

        JButton resetButton = new JButton("Reset");
        styleButton(resetButton);
        
        JButton solveButton = new JButton("Solve");
        styleButton(solveButton);
               
        backButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) app.getMainFrame().getContentPane().getLayout();
            layout.show(app.getMainFrame().getContentPane(), "mainMenu");
            app.showOtherPanel("mainMenu"); 
        });
        
        solveButton.addActionListener(e -> {
            if (solveSudoku()) {
                JOptionPane.showMessageDialog(app.getMainFrame(), "Puzzle solved!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(app.getMainFrame(), "Puzzle is unsolvable.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
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
        
        add(BoardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private boolean solveSudoku() {
        int[][] puzzle = new int[9][9];
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = Board[i][j].getText().trim();
                if (!value.isEmpty()) {
                    puzzle[i][j] = Integer.parseInt(value);
                }
            }
        }
        
        if (!ValidatePuzzle.validate(puzzle)) {
            return false;
        }
        
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
            
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Board[i][j].setText(Integer.toString(puzzle[i][j]));
                    
                    try {
                        Thread.sleep(30); 
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
        
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        Color buttonColor = new Color(51, 153, 255); // Steel Blue color
        Color borderColor = new Color(51, 153, 255); // Same color for a seamless look
        int borderRadius = 30;

        button.setUI(new RoundedButtonUI(buttonColor, borderColor, borderRadius));
    }
    
}
