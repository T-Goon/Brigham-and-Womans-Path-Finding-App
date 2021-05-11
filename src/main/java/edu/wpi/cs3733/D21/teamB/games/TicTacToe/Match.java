package edu.wpi.cs3733.D21.teamB.games.TicTacToe;


/**
 * Manages gameplay for matches and games.
 * @author Colin Flueck
 */
public class Match {

    private BackEnd gameBoard;
    private FrontEnd display;

    private int playerRowSelection = -1;
    private int playerColumnSelection = -1;
    private boolean playGame = true;

    private int[] scores;
    private int numRows;
    private int numColumns;

    /**
     * Match class handles backend, frontend, and gameplay flow.
     * Matches are composed of games which involve numerous turns.
     */
    public Match() {
        numRows = 3;
        numColumns = 3;
        gameBoard = new BackEnd(numRows, numColumns);
        gameBoard.initializeBoard(numRows, numColumns);

        display = new FrontEnd(this, gameBoard);
        scores = new int[3];

            newGame();
    }


    // add newMatch for when settings change (size, etc.)

    public void newGame() {

        while (playGame) {

            // reset backend
            gameBoard.setPlayer('x');
            gameBoard.initializeBoard(numRows,numColumns);

            //update gui
            display.resetBoard(gameBoard.getBoard());

            // ensure no marks are accidentally placed
            playerRowSelection = -1;
            playerColumnSelection = -1;

            // play one game
            scores = playGame();

            // update match scores
            String scoreMessage = "Player X: " + scores[0] + "\t\t\tPlayer O: " + scores[1] + "\t\t\tTies: " + scores[2];
            display.updateScore(scores);

            System.out.println(scoreMessage);

        }
        System.out.println("Thanks for playing!");
    }



    public int[] playGame() {

        // runs game until someone wins or there's a tie
        while (!gameBoard.checkForWin() && !gameBoard.isBoardFull()) {

            //gameBoard.printBoard();

            int row = playerRowSelection;
            int col = playerColumnSelection;

            //ensures the mark can be placed, and asks again if not
            while (!gameBoard.placeMark(row, col)) {

                row = playerRowSelection;
                col = playerColumnSelection;
                System.out.print(">");


                try {
                    Thread.sleep(100);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                if (gameBoard.placeMark(row, col) || gameBoard.isBoardFull()) {
                    break;
                }
            }

            gameBoard.placeMark(row, col);
            display.resetBoard(gameBoard.getBoard());

            if (!gameBoard.checkForWin() && !gameBoard.isBoardFull()) {
                gameBoard.changePlayer();
                gameBoard.computerPlaceMark();
                display.resetBoard(gameBoard.getBoard());
            }

            if (!gameBoard.checkForWin() && !gameBoard.isBoardFull()) {
                gameBoard.changePlayer();
            }
        }

        if (!gameBoard.checkForWin() && gameBoard.isBoardFull()) {
            display.playAgainMessage("The game was a tie!");
           // gameBoard.printBoard();
            scores[2]++;
        }
        else {
           // gameBoard.printBoard();
            String winMessage = "Player " + Character.toUpperCase(gameBoard.getCurrentPlayerMark()) + " Wins!";
            display.playAgainMessage(winMessage);

            if (gameBoard.getCurrentPlayerMark() == 'x') {
                scores[0] +=1;
            } else {
                scores[1] +=1;
            }
        }
        return scores;
    }

    public void setPlayerSelection(int row, int column) {
        playerRowSelection = row;
        playerColumnSelection = column;

    }

    public void setPlayGame(boolean playGame) {
        this.playGame = playGame;
    }

}
