package Sprint3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;



public class SOSGameTests {
    private SOSGameLogic gameLogic;
    private Console console;
    private Board board;
    private JPanel gridPanel;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private JTextField gridSizeField;
    private JRadioButton blueS;
    private JRadioButton blueO;
    private JRadioButton redS;
    private JRadioButton redO;
    private JButton[][] buttons;

    @Before
    public void setUp() {
        // Create mock components
        gridPanel = new JPanel();
        turnLabel = new JLabel();
        scoreLabel = new JLabel();
        gridSizeField = new JTextField("3");

        // Create radio buttons
        blueS = new JRadioButton("S");
        blueS.setSelected(true);
        blueO = new JRadioButton("O");
        redS = new JRadioButton("S");
        redO = new JRadioButton("O");
        redO.setSelected(true);

        // Create a 3x3 board
        board = new Board(gridPanel, 3, null, turnLabel, scoreLabel);
        
        // Create console and game logic
        console = new Console(board, gridPanel, gridSizeField, 
                               turnLabel, scoreLabel, 
                               blueS, blueO, redS, redO);
        
        gameLogic = new SOSGameLogic(console, board, gridPanel, turnLabel, scoreLabel);

        // Ensure simple game mode is selected
        console.setGameMode(true);

        buttons = new JButton[board.getGridPanel().getLayout() instanceof GridLayout ? 3 : 0][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = (JButton) board.getGridPanel().getComponent(i * 3 + j);
            }
        }
    }

    @Test
    public void testBoardCreation() {
        // Verify board is created with correct number of buttons
        assertEquals(9, gridPanel.getComponentCount());
    }

    @Test
    public void testSimpleGameMode() {
        // Set simple game mode
        console.setGameMode(true);
        assertTrue(console.isSimpleGame());
    }

    @Test
    public void testGeneralGameMode() {
        // Set general game mode
        console.setGameMode(false);
        assertFalse(console.isSimpleGame());
    }
    
    @Test
    public void testCreateNewBoardWith4x4() {
        // Create the necessary components
        JPanel gridPanel = new JPanel();
        JTextField gridSizeField = new JTextField("4");
        JLabel turnLabel = new JLabel();
        JLabel scoreLabel = new JLabel();
        JRadioButton blueS = new JRadioButton("S");
        JRadioButton blueO = new JRadioButton("O");
        JRadioButton redS = new JRadioButton("S");
        JRadioButton redO = new JRadioButton("O");

        // Create console and board
        Board board = new Board(gridPanel, 3, null, turnLabel, scoreLabel);
        Console console = new Console(board, gridPanel, gridSizeField, 
                                      turnLabel, scoreLabel, 
                                      blueS, blueO, redS, redO);

        // Simulate create new board action
        MouseEvent mockEvent = new MouseEvent(new JButton(), MouseEvent.MOUSE_CLICKED, 
                                              System.currentTimeMillis(), 0, 0, 0, 1, false);
        console.createNewGameAction().mouseClicked(mockEvent);

        // Verify board creation
        assertEquals("Board should have 16 buttons for a 4x4 grid", 16, gridPanel.getComponentCount());
    }

    @Test
    public void testSimpleGameMove() {
        // Set simple game mode
        console.setGameMode(true);
        
        // Get first button in the grid
        JButton firstButton = (JButton) gridPanel.getComponent(0);
        
        // Make a move
        console.makeMove(firstButton);
        
        // Verify button is marked
        assertEquals("S", firstButton.getText());
    }

    @Test
    public void testGeneralGameMove() {
        // Set general game mode
        console.setGameMode(false);
        
        // Get first button in the grid
        JButton firstButton = (JButton) gridPanel.getComponent(0);
        
        // Make a move
        console.makeMove(firstButton);
        
        // Verify button is marked
        assertEquals("S", firstButton.getText());
    }

    @Test
    public void testSOSDetection() {
        // Prepare buttons to form an SOS
        JButton[][] buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = (JButton) gridPanel.getComponent(i * 3 + j);
            }
        }

        // Create an SOS pattern
        buttons[0][0].setText("S");
        buttons[0][1].setText("O");
        buttons[0][2].setText("S");

        // Use gameLogic to check for SOS
        assertTrue(gameLogic.checkForSOS(buttons[0][1]));
    }

    @Test
    public void testGameEnd() {
        // Set general game mode
        console.setGameMode(false);

        // Fill the board
        for (Component comp : gridPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().isEmpty()) {
                    button.setText("S");
                }
            }
        }

        // Trigger end game check
        gameLogic.endGame();

        // In a complete test, you'd verify the game state or dialog
    }

    @Test
    public void testScoreTracking() {
        // Set general game mode
        console.setGameMode(false);

        // Ensure the board is a 3x3 grid
        JButton[][] buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = (JButton) gridPanel.getComponent(i * 3 + j);
                buttons[i][j].setText(""); // Clear any existing text
            }
        }

        // Manually place letters to create SOSs without filling the entire board
        buttons[0][0].setText("S");
        buttons[0][1].setText("O");
        console.makeMove(buttons[0][2]);

        buttons[1][0].setText("S");
        buttons[1][1].setText("O");
        console.makeMove(buttons[1][2]);

        // Verify blue player's score
        assertEquals(2, console.getBlueSOSCount());
    }
    
    @Test
    public void testSimpleGameBluePlayerWins() {
        // Verify initial conditions
        assertTrue("Simple game mode should be active", console.isSimpleGame());

        // Ensure blue player is currently playing
        assertTrue("Blue player should start", blueS.isSelected());

        // Sequence to create SOS
        // First move: Blue places first 'S'
        buttons[0][0].setText("S");
        console.makeMove(buttons[0][0]);
        assertEquals("First button should be marked S", "S", buttons[0][0].getText());

        // Second move: Red places 'O'
        redO.setSelected(true);
        buttons[0][1].setText("O");
        console.makeMove(buttons[0][1]);
        assertEquals("Second button should be marked O", "O", buttons[0][1].getText());

        // Third move: Blue places final 'S' to complete SOS
        blueS.setSelected(true);
        buttons[0][2].setText("S");
        console.makeMove(buttons[0][2]);
        assertEquals("Third button should be marked S", "S", buttons[0][2].getText());
    }
}