package Sprint3;

import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gui extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
    private JTextField gridSizeField;
    private JPanel gameGridPanel;
    
    private JLabel turnLabel;
    private JLabel scoreLabel;
    
    private JRadioButton blueS;
    private JRadioButton blueO;
    private JRadioButton redS;
    private JRadioButton redO;
    
    private JRadioButton simpleGameRadio;
    private JRadioButton generalGameRadio;
    
    private Board board;
    private Console console;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                gui frame = new gui();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public gui() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1080, 720);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        initializeComponents();
        
        setupGame();
    }

    private void initializeComponents() {
        gameGridPanel = new JPanel();
        gameGridPanel.setBounds(124, 85, 773, 516);
        contentPane.add(gameGridPanel);

        turnLabel = new JLabel("Current Turn: Blue (S)");
        turnLabel.setBounds(431, 58, 200, 16);
        contentPane.add(turnLabel);

        scoreLabel = new JLabel("Blue SOS: 0 | Red SOS: 0");
        scoreLabel.setBounds(431, 30, 200, 16);
        scoreLabel.setVisible(false);
        contentPane.add(scoreLabel);

        gridSizeField = new JTextField("3");
        gridSizeField.setBounds(477, 623, 49, 26);
        contentPane.add(gridSizeField);

        initializePlayerRadioButtons();

        initializeGameModeRadioButtons();

        initializeActionButtons();

        addTitleAndLabels();
    }

    private void initializePlayerRadioButtons() {
        ButtonGroup bluePlayerGroup = new ButtonGroup();
        ButtonGroup redPlayerGroup = new ButtonGroup();

        blueS = new JRadioButton("S");
        blueS.setSelected(true);
        blueS.setBounds(38, 474, 69, 23);
        blueO = new JRadioButton("O");
        blueO.setBounds(38, 500, 69, 23);
        bluePlayerGroup.add(blueS);
        bluePlayerGroup.add(blueO);
        contentPane.add(blueS);
        contentPane.add(blueO);

        redS = new JRadioButton("S");
        redO = new JRadioButton("O");
        redO.setSelected(true);
        redS.setBounds(903, 474, 141, 23);
        redO.setBounds(903, 500, 141, 23);
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redS);
        redGroup.add(redO);
        contentPane.add(redS);
        contentPane.add(redO);
    }

    private void initializeGameModeRadioButtons() {
        ButtonGroup gameModeGroup = new ButtonGroup();

        simpleGameRadio = new JRadioButton("Simple Game");
        simpleGameRadio.setSelected(true);
        simpleGameRadio.setBounds(62, 625, 141, 23);
        gameModeGroup.add(simpleGameRadio);
        contentPane.add(simpleGameRadio);

        generalGameRadio = new JRadioButton("General Game");
        generalGameRadio.setBounds(903, 625, 141, 23);
        gameModeGroup.add(generalGameRadio);
        contentPane.add(generalGameRadio);
    }

    private void initializeActionButtons() {
        JButton newGameBoardButton = new JButton("Create New Game Board");
        newGameBoardButton.setBounds(654, 622, 193, 29);
        contentPane.add(newGameBoardButton);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBounds(250, 622, 117, 29);
        contentPane.add(newGameButton);

    }

    private void addTitleAndLabels() {
        JLabel titleLabel = new JLabel("SOS");
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
        titleLabel.setBounds(124, 6, 90, 75);
        contentPane.add(titleLabel);

        JLabel boardSizeLabel = new JLabel("Board Size:");
        boardSizeLabel.setBounds(408, 628, 102, 16);
        contentPane.add(boardSizeLabel);

        JLabel bluePlayerLabel = new JLabel("Blue Player");
        bluePlayerLabel.setBounds(38, 451, 91, 16);
        contentPane.add(bluePlayerLabel);

        JLabel redPlayerLabel = new JLabel("Red Player");
        redPlayerLabel.setBounds(926, 451, 92, 16);
        contentPane.add(redPlayerLabel);
    }

    private void setupGame() {
        int defaultGridSize = 3;
        board = new Board(gameGridPanel, defaultGridSize, null, turnLabel, scoreLabel);
        
        console = new Console(board, gameGridPanel, gridSizeField, turnLabel, scoreLabel, 
                              blueS, blueO, redS, redO);

        attachGameControlListeners();

        attachGameModeListeners();
    }

    private void attachGameControlListeners() {
        for (Component comp : contentPane.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals("Create New Game Board")) {
                ((JButton) comp).addMouseListener(console.createNewGameAction());
            }
            if (comp instanceof JButton && ((JButton) comp).getText().equals("New Game")) {
                ((JButton) comp).addMouseListener(console.clearBoardAction());
            }
        }
    }

    private void attachGameModeListeners() {
        simpleGameRadio.addActionListener(e -> {
            console.setGameMode(true);
            scoreLabel.setVisible(false);
        });

        generalGameRadio.addActionListener(e -> {
            console.setGameMode(false);
            scoreLabel.setVisible(true);
        });
    }
}