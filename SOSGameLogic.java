package Sprint3;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;

public class SOSGameLogic {
    private Console console;
    private Board board;
    private JPanel panel;
    private JLabel turnLabel;
    private JLabel scoreLabel;

    public SOSGameLogic(Console console, Board board, JPanel panel, 
                        JLabel turnLabel, JLabel scoreLabel) {
        this.console = console;
        this.board = board;
        this.panel = panel;
        this.turnLabel = turnLabel;
        this.scoreLabel = scoreLabel;
    }


    public boolean checkSOSPattern(JButton[][] grid, int row, int col, int dRow, int dCol, String pattern) {
        int gridSize = grid.length;
        
        if (pattern.equals("SOS")) {
            if (row + 2*dRow >= 0 && row + 2*dRow < gridSize && 
                col + 2*dCol >= 0 && col + 2*dCol < gridSize) {
                
                String firstLetter = grid[row][col].getText();
                String secondLetter = grid[row + dRow][col + dCol].getText();
                String thirdLetter = grid[row + 2*dRow][col + 2*dCol].getText();
                
                return firstLetter.equals("S") && 
                       secondLetter.equals("O") && 
                       thirdLetter.equals("S");
            }
        } else if (pattern.equals("OSO")) {
            if (row + 2*dRow >= 0 && row + 2*dRow < gridSize && 
                col + 2*dCol >= 0 && col + 2*dCol < gridSize) {
                
                String firstLetter = grid[row][col].getText();
                String secondLetter = grid[row + dRow][col + dCol].getText();
                String thirdLetter = grid[row + 2*dRow][col + 2*dCol].getText();
                
                return firstLetter.equals("O") && 
                       secondLetter.equals("S") && 
                       thirdLetter.equals("O");
            }
        }
        
        return false;
    }
    
    public void updateBoard(Board newBoard) {
        this.board = newBoard;
    }

    public boolean checkForSOS(JButton changedButton) {
        JButton[][] gridButtons = convertToGrid();
        int row = -1, col = -1;
        
        for (int i = 0; i < gridButtons.length; i++) {
            for (int j = 0; j < gridButtons[i].length; j++) {
                if (gridButtons[i][j] == changedButton) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) break;
        }
        
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            if (checkSOSInDirection(gridButtons, row, col, dir[0], dir[1])) {
                return true;
            }
        }
        
        return false;
    }

    public boolean checkSOSInDirection(JButton[][] grid, int changedRow, int changedCol, int dRow, int dCol) {
        int gridSize = grid.length;
        
        int firstRow = changedRow - 2*dRow;
        int firstCol = changedCol - 2*dCol;
        int secondRow = changedRow - dRow;
        int secondCol = changedCol - dCol;
        int thirdRow = changedRow;
        int thirdCol = changedCol;
        
        if (firstRow >= 0 && firstRow < gridSize &&
            firstCol >= 0 && firstCol < gridSize &&
            secondRow >= 0 && secondRow < gridSize &&
            secondCol >= 0 && secondCol < gridSize &&
            thirdRow >= 0 && thirdRow < gridSize &&
            thirdCol >= 0 && thirdCol < gridSize) {
            
            String firstLetter = grid[firstRow][firstCol].getText();
            String secondLetter = grid[secondRow][secondCol].getText();
            String thirdLetter = grid[thirdRow][thirdCol].getText();
            
            if (firstLetter.equals("S") && 
                secondLetter.equals("O") && 
                thirdLetter.equals("S")) {
                return true;
            }
        }
        
        int leftRow = changedRow - dRow;
        int leftCol = changedCol - dCol;
        int rightRow = changedRow + dRow;
        int rightCol = changedCol + dCol;
        
        if (leftRow >= 0 && leftRow < gridSize &&
            leftCol >= 0 && leftCol < gridSize &&
            rightRow >= 0 && rightRow < gridSize &&
            rightCol >= 0 && rightCol < gridSize) {
            
            String leftLetter = grid[leftRow][leftCol].getText();
            String middleLetter = grid[changedRow][changedCol].getText();
            String rightLetter = grid[rightRow][rightCol].getText();
            
            if (leftLetter.equals("S") && 
                middleLetter.equals("O") && 
                rightLetter.equals("S")) {
                return true;
            }
        }
        
        return false;
    }

    public void setGameMode(boolean isSimpleGame) {
        console.setGameMode(isSimpleGame);
    }

    public void resetGame() {
        console.resetGame();
    }

    public boolean isValidSOS(JButton[][] grid, int row, int col, int dRow, int dCol, String pattern) {
        int gridSize = grid.length;
        
        if (pattern.equals("SOS")) {
            if (row + 2*dRow >= 0 && row + 2*dRow < gridSize && 
                col + 2*dCol >= 0 && col + 2*dCol < gridSize) {
                
                String firstLetter = grid[row][col].getText();
                String secondLetter = grid[row + dRow][col + dCol].getText();
                String thirdLetter = grid[row + 2*dRow][col + 2*dCol].getText();
                
                return firstLetter.equals("S") && 
                       secondLetter.equals("O") && 
                       thirdLetter.equals("S");
            }
        } else if (pattern.equals("OSO")) {
            if (row + 2*dRow >= 0 && row + 2*dRow < gridSize && 
                col + 2*dCol >= 0 && col + 2*dCol < gridSize) {
                
                String firstLetter = grid[row][col].getText();
                String secondLetter = grid[row + dRow][col + dCol].getText();
                String thirdLetter = grid[row + 2*dRow][col + 2*dCol].getText();
                
                return firstLetter.equals("O") && 
                       secondLetter.equals("S") && 
                       thirdLetter.equals("O");
            }
        }
        
        return false;
    }

    public void endGame() {
        if (isBoardFull()) {
            if (!console.isSimpleGame()) {
                int blueSOSCount = console.getBlueSOSCount();
                int redSOSCount = console.getRedSOSCount();
                
                String result;
                if (blueSOSCount > redSOSCount) {
                    result = "Blue player wins with " + blueSOSCount + " SOSs!";
                } else if (redSOSCount > blueSOSCount) {
                    result = "Red player wins with " + redSOSCount + " SOSs!";
                } else {
                    result = "Game ended in a draw!";
                }
                
                JOptionPane.showMessageDialog(panel, result);
                resetGame();
            } else {
                JOptionPane.showMessageDialog(panel, "Game ended in a draw!");
                resetGame();
            }
        }
    }

    private JButton[][] convertToGrid() {
        Component[] components = panel.getComponents();
        int gridSize = (int) Math.sqrt(components.length);
        JButton[][] grid = new JButton[gridSize][gridSize];
        
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = (JButton) components[i * gridSize + j];
            }
        }
        
        return grid;
    }

    private boolean isBoardFull() {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}