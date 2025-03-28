package Sprint3;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board {
    private JPanel gridPanel;
    private int gridSize;
    
    public Board(JPanel gridPanel, int gridSize, Console console, JLabel turnLabel, JLabel scoreLabel) {
        this.gridPanel = gridPanel;
        this.gridSize = gridSize;
        
        createGrid();
        
        if (console != null) {
            new SOSGameLogic(console, this, gridPanel, turnLabel, scoreLabel);
        }
    }

    public void createGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize));
        for (int i = 0; i < gridSize * gridSize; i++) {
            JButton button = new JButton("");
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.setFocusable(false);
            gridPanel.add(button);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }

    public void clearBoard() {
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setText("");
            }
        }
    }
}
    