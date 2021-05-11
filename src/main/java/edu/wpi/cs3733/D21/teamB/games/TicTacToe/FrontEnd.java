package edu.wpi.cs3733.D21.teamB.games.TicTacToe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

/**
 * GUI for tic tac toe.  Includes button grid for game, score label, and menu bar.
 * @author Colin Flueck
 */
public class FrontEnd {
    private JFrame frame;
    private JButton[][] buttons;
    private JLabel scoreLabel;
    private int gridSize = 3;
    private Match match;



    /**
     * GUI Constructor.  Creates grid for tic tac toe buttons, score section, and menubar.
     * Also instantiates and adds various action and mouse listeners.
     */
    public FrontEnd(Match match, BackEnd gameBoard) {
        this.match = match;
        frame = new JFrame("Tic Tac Toe");

        // activated when grid buttons clicked
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] index = getButtonIndex(e);
                match.setPlayerSelection(index[0], index[1]);
            }
        };

        // activated when mouse goes over and leaves grid buttons
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                int[] index = getButtonIndex(e);
                buttons[index[0]][index[1]].setForeground(Color.blue);

                if (buttons[index[0]][index[1]].getText().equals("")) {
                    String mark = Character.toString(gameBoard.getCurrentPlayerMark());
                    buttons[index[0]][index[1]].setText(mark);
                    buttons[index[0]][index[1]].setForeground(Color.gray);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                int[] index = getButtonIndex(e);
                buttons[index[0]][index[1]].setForeground(Color.black);

                char[][] board = gameBoard.getBoard();

                String mark = Character.toString(board[index[0]][index[1]]);
                if (mark.equals("-")) {
                    mark = "";
                }
                buttons[index[0]][index[1]].setText(mark);
            }
        };


        JPanel mainPanel = new JPanel();
        JPanel grid = new JPanel();

        // these numbers can change for a 4x4 or 5x5 grid.  Will also need to update backend though
        buttons = new JButton[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 100));

                // adds listeners for hovering and clicks
                buttons[i][j].addActionListener(actionListener);
                buttons[i][j].addMouseListener(mouseAdapter);
                grid.add(buttons[i][j]);
            }
        }

        grid.setLayout(new GridLayout(gridSize, gridSize));

        JLabel title = new JLabel("Tic Tac Toe!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.PLAIN, 60));

        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem newMatch = new JMenuItem("New Match");
        JMenuItem stopGame = new JMenuItem("Stop Playing");

        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuBar.add(settings);
//        menuBar.add(newMatch);
        menuBar.add(stopGame);

// ---------------------- Menu Bar Action Listeners ---------------------- //

        stopGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(frame, "Would you like to quit?", "Quit Game", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    // not sure if there is a better way to close the game
                    // first command closes JFrame, but backend code still runs
                    frame.dispose();
                    System.exit(0);
                }
            }
        });

        newMatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                match.resetAll();
//                match.startNewMatch();
            }
        });

        // opens a window if the user clicks on the settings menu item
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSlider num = new JSlider(3,8,3);

                Hashtable <Integer, JLabel> numbers = new Hashtable<Integer, JLabel>();
                numbers.put(3, new JLabel("3x3"));
                numbers.put(4, new JLabel("4x4"));
                numbers.put(5, new JLabel("5x5"));
                numbers.put(6, new JLabel("6x6"));
                numbers.put(7, new JLabel("7x7"));
                numbers.put(8, new JLabel("8x8"));


                num.setMinorTickSpacing(1);
                num.setLabelTable(numbers);
                num.setPaintTicks(true);
                num.setPaintLabels(true);

                JPanel panel = new JPanel();
                JLabel gridLabel = new JLabel("Select the grid size");
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//                panel.add(gridLabel);
//                panel.add(num);

                panel.add(new JLabel("Grid size changes, single player vs two player, and more settings coming soon."));
                panel.add(Box.createRigidArea(new Dimension(0,10)));
                panel.add(new JLabel("Click Yes to save your changes"));

                // the user must save or cancel their changes to settings
                int n = JOptionPane.showConfirmDialog(frame, panel, "Settings", JOptionPane.YES_OPTION, JOptionPane.CANCEL_OPTION);
                if  (n == JOptionPane.YES_OPTION) {
                    gridSize = num.getValue();
                    System.out.println(gridSize);
                } else {
                    System.out.println("\nCancelled\n");
                }
            }
        });


        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(grid, BorderLayout.CENTER);
        mainPanel.add(title, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);


        JLabel scoreHeader = new JLabel("Total Match Scores");
        scoreHeader.setFont(new Font("Arial", Font.PLAIN, 30));
        scoreHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel = new JLabel("Player X: 0          Player O: 0          Tie: 0");
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 25));


        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.add(Box.createRigidArea(new Dimension(0,10)));
        scorePanel.add(scoreHeader);
        scorePanel.add(Box.createRigidArea(new Dimension(0,10)));
        scorePanel.add(scoreLabel);
        frame.add(scorePanel, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);

    }

    // used in action listeners to determine which button is pressed
    private int[] getButtonIndex(AWTEvent e) {
        JButton button = (JButton) e.getSource();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (button.equals(buttons[i][j])) {
                    return new int[] {i,j};
                }
            }
        }
        return null;
    }

    // GUI update, sets grid buttons to correct character
    public void resetBoard(char[][] board) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] == '-') {
                    buttons[i][j].setText("");
                } else {
                    buttons[i][j].setText(Character.toString(board[i][j]));
                }
            }
        }
    }


    public void updateScore(int[] scores) {
        scoreLabel.setText("Player X: " + scores[0] + "          Player O: " + scores[1] + "          Tie: " + scores[2]);
    }

    // Pop up message when a game ends with result and option to play again
    public void playAgainMessage(String message) {
        JOptionPane popup = new JOptionPane();
        message = "<html>" + message + " <br/> <br/> Would you like to play again? </html>";
        int n = popup.showConfirmDialog(frame, message, "Game Over", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.NO_OPTION) {
            match.setPlayGame(false);
        }
    }

    // creates a popup message with title and message
    public void popUpMessage(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
