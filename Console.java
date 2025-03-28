package Sprint3;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.Component;
import javax.swing.SwingUtilities;

public class Console {
    private Board board;
    private JPanel panel;
    private JTextField textField;
    private boolean isSimpleGame = true;
    private int blueSOSCount = 0;
    private int redSOSCount = 0;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private JRadioButton blueS;
    private JRadioButton blueO;
    private JRadioButton redS;
    private JRadioButton redO;
    
    private SOSGameLogic sosGameLogic;
    
    private char currentPlayer = 'S';
    private char bluePlayerLetter = 'S';
    private char redPlayerLetter = 'O';
    private boolean isBluePlayerTurn = true;
    private boolean sosJustCreated = false;
    
    public Console(Board board, JPanel panel, JTextField textField, 
                   JLabel turnLabel, JLabel scoreLabel,
                   JRadioButton blueS, JRadioButton blueO,
                   JRadioButton redS, JRadioButton redO) {
        this.board = board;
        this.panel = panel;
        this.textField = textField;
        this.turnLabel = turnLabel;
        this.scoreLabel = scoreLabel;
        this.blueS = blueS;
        this.blueO = blueO;
        this.redS = redS;
        this.redO = redO;
        
        this.sosGameLogic = new SOSGameLogic(this, board, panel, turnLabel, scoreLabel);
        
        addPlayerLetterListeners();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addButtonListeners();
                updateTurnLabel();
            }
        });
    }
    public MouseAdapter createNewGameAction() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int gridSize = Integer.parseInt(textField.getText());
                    if (gridSize > 16) {
                        JOptionPane.showMessageDialog(panel,
                            "Please enter a grid size between 3 and 16.\nThe grid size has been set to 16.",
                            "Invalid Grid Size", JOptionPane.WARNING_MESSAGE);
                        gridSize = 16;
                    }
                    else if (gridSize < 3) {
                        JOptionPane.showMessageDialog(panel,
                            "Please enter a grid size between 3 and 16.\nThe grid size has been set to 3.",
                            "Invalid Grid Size", JOptionPane.WARNING_MESSAGE);
                        gridSize = 3;
                    }
                    
                    board = new Board(panel, gridSize, Console.this, turnLabel, scoreLabel);
                    
                    sosGameLogic = new SOSGameLogic(Console.this, board, panel, turnLabel, scoreLabel);
                    
                    resetGame();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid integer.");
                }
            }
        };
    }
    public int getBlueSOSCount() {
        return blueSOSCount;
    }

    public int getRedSOSCount() {
        return redSOSCount;
    }

    public boolean isSimpleGame() {
        return isSimpleGame;
    }

    public void setGameMode(boolean isSimpleGame) {
        this.isSimpleGame = isSimpleGame;
        sosGameLogic.resetGame();
    }

    public void makeMove(JButton button) {
        if (button.getText().isEmpty()) {
            // Determine the selected letter based on current player's radio buttons
            char selectedLetter = isBluePlayerTurn ? 
                (blueS.isSelected() ? 'S' : 'O') : 
                (redS.isSelected() ? 'S' : 'O');
            
            button.setText(String.valueOf(selectedLetter));

            boolean sosCreated = sosGameLogic.checkForSOS(button);

            if (isSimpleGame) {
                if (sosCreated) {
                    String winner = isBluePlayerTurn ? "Blue" : "Red";
                    JOptionPane.showMessageDialog(panel, winner + " player wins by creating an SOS!");
                    resetGame();
                    return;
                }
                // In simple game, alternate turns normally
                isBluePlayerTurn = !isBluePlayerTurn;
            } else {
                // In general game mode
                if (sosCreated) {
                    // Increment SOS count for the current player
                    if (isBluePlayerTurn) {
                        blueSOSCount++;
                    } else {
                        redSOSCount++;
                    }
                    updateScoreLabel();
                    
                    // Player gets another turn if SOS is created
                    sosJustCreated = true;
                } else {
                    // If SOS was just created in the previous turn, switch turns
                    if (sosJustCreated) {
                        isBluePlayerTurn = !isBluePlayerTurn;
                        sosJustCreated = false;
                    }
                    // If no SOS was created and it wasn't a just-after-SOS turn, continue current turn
                    else {
                        isBluePlayerTurn = !isBluePlayerTurn;
                    }
                }
            }

            updateTurnLabel();
            sosGameLogic.endGame();
        }
    }

    public void resetGame() {
        board.clearBoard();
        
        blueSOSCount = 0;
        redSOSCount = 0;
        
        isBluePlayerTurn = true;
        sosJustCreated = false;
        
        // Reset radio buttons to initial state
        blueS.setSelected(true);
        blueO.setSelected(false);
        redS.setSelected(false);
        redO.setSelected(true);
        
        bluePlayerLetter = 'S';
        redPlayerLetter = 'O';
        
        updateTurnLabel();
        updateScoreLabel();
        
        addButtonListeners();
    }

    private void addPlayerLetterListeners() {
        // Blue player letter selection
        blueS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bluePlayerLetter = 'S';
                if (isBluePlayerTurn) {
                    currentPlayer = 'S';
                    updateTurnLabel();
                }
            }
        });
        
        blueO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bluePlayerLetter = 'O';
                if (isBluePlayerTurn) {
                    currentPlayer = 'O';
                    updateTurnLabel();
                }
            }
        });
        
        redS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redPlayerLetter = 'S';
                if (!isBluePlayerTurn) {
                    currentPlayer = 'S';
                    updateTurnLabel();
                }
            }
        });
        
        redO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redPlayerLetter = 'O';
                if (!isBluePlayerTurn) {
                    currentPlayer = 'O';
                    updateTurnLabel();
                }
            }
        });
    }

    private void updateTurnLabel() {
        String playerColor = isBluePlayerTurn ? "Blue" : "Red";
        char playerLetter = isBluePlayerTurn ? 
            (blueS.isSelected() ? 'S' : 'O') : 
            (redS.isSelected() ? 'S' : 'O');
        
        currentPlayer = playerLetter;
        turnLabel.setText("Current Turn: " + playerColor + " (" + playerLetter + ")");
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Blue SOS: " + blueSOSCount + " | Red SOS: " + redSOSCount);
    }


    public MouseAdapter clearBoardAction() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (board != null) {
                    resetGame();
                }
            }
        };
    }

    private void addButtonListeners() {
        addListenersRecursively(panel);
    }

    private void addListenersRecursively(Container container) {
        Component[] components = container.getComponents();
        
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                for (ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeMove((JButton) e.getSource());
                    }
                });
            } 
            else if (component instanceof Container) {
                addListenersRecursively((Container) component);
            }
        }
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void initializeExistingBoard() {
        addButtonListeners();
    }
}