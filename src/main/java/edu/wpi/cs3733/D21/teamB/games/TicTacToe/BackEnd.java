package edu.wpi.cs3733.D21.teamB.games.TicTacToe;

import java.util.Random;

/**
 * Backend for tic tac toe.  Represents board as char[][], places new marks,
 * evaluates winning conditions, and powers computer mark placement
 * @author Colin Flueck
 */
class BackEnd {


    private int numRows;
    private int numColumns;
    private char[][] board;
    private char currentPlayerMark = 'x';

    public BackEnd(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.board = new char[numRows][numColumns];
        initializeBoard(numRows, numColumns);
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentPlayerMark() {
        return currentPlayerMark;
    }

    public void setPlayer(char mark) {
        currentPlayerMark = mark;
    }

    public void changePlayer() {
        if (currentPlayerMark == 'x') {
            currentPlayerMark = 'o';
        } else {
            currentPlayerMark = 'x';
        }
    }

    // sets every square on the board to -
    public void initializeBoard(int numRows, int numColumns) {
        for (int i = 0; i <numRows; i++) {
            for (int j=0; j<numColumns ;j++) {
                board[i][j] = '-';
            }
        }
    }
//
//    public void printBoard() {
//        System.out.println("-------------");
//        for (int i=0;i<3;i++) {
//            System.out.print("| ");
//            for (int j=0;j<3;j++) {
//                System.out.print(board[i][j] + " | ");
//            }
//            System.out.println();
//            System.out.println("-------------");
//        }
//    }

    //Winning identification methods
    public boolean checkForWin() {
        return checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin();
    }

    private boolean checkRowsForWin() {
        for (int i=0;i<3;i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumnsForWin() {
        for (int i=0;i<3;i++) {
            if (checkRowCol(board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalsForWin() {
        return(checkRowCol(board[0][0], board[1][1], board[2][2]) || checkRowCol(board[0][2], board[1][1], board[2][0]));
    }

    private boolean checkRowCol(char c1, char c2, char c3) {
        return(c1 != '-' && c1 == c2 && c2 == c3);
    }

    public boolean placeMark(int row, int col) {
        if ((row < 3) && (row > -1) && (col > -1) && (col < 3)) {
            if (board[row][col] == '-') {
                board[row][col] = currentPlayerMark;
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    // possible methods for changing grid size
    public void setGridSize(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }


    // ----- Computer Methods below ----- //

    public void computerPlaceMark() {
        Random rand = new Random();

        // "smart offensive play" if it has 2/3 in a row, column, or diagonal
        if (computerRowPlace('o')) {
           // System.out.println("\nOffensive Row Play");
            computerRowPlace('o');
        } else if (computerColPlace('o')) {
          //  System.out.println("\nOffensive Column Play");
            computerColPlace('o');
        } else if (computerDiagonalPlace('o')) {
         //   System.out.println("\nOffensive Diagonal Play");
            computerDiagonalPlace('o');

            //Checks for rows, columns, and diagonals to be blocked if 2/3
        } else if (computerRowPlace('x')) {
            computerRowPlace('x');
          //  System.out.println("\nPlayer O blocks your '3 in a row'");
        } else if (computerColPlace('x')) {
            computerColPlace('x');
          //  System.out.println("\nPlayer O blocks your '3 in a column'");
        } else if (computerDiagonalPlace('x')) {
            computerDiagonalPlace('x');
         //   System.out.println("\nPlayer O blocks your '3 in a diagonal'");

            //Steps for turn 1 and maybe 2
        } else if (board[1][1] == '-') {
            placeMark(1,1);
        } else if (board[0][0] == '-') {
            placeMark(0,0);

            //for future placements, corners are prefered
        } else if (board[0][2] == '-') {
            placeMark(0,2);
        } else if (board[2][0] == '-') {
            placeMark(2,0);
        } else if (board[2][2] == '-') {
            placeMark(2,2);

            //random placement if it doesn't any other option
        } else if (!isBoardFull() && !checkForWin()) {

            int comRow = rand.nextInt(3);
            int comCol = rand.nextInt(3);

            while (!placeMark(comRow, comCol))
            {
              //  System.out.println("Random number: (" + comRow + "," + comCol + ") didn't work :(");
                comRow = rand.nextInt(3);
                comCol = rand.nextInt(3);
                if (placeMark(comRow, comCol) || isBoardFull()) {
                    break;
                }
            }
            placeMark(comRow, comCol);
           // System.out.println("\nPlayer " + Character.toUpperCase(currentPlayerMark) + " rolls the dice with (" + comRow + "," + comCol + ")!");
        }
    }

    //Compares two characters together, used in all computer checks
    private boolean computerCheckChar(char c1, char c2, char mark) {
        return(c1 == mark && c1 == c2);
    }

    //Checks to see if player X has two in a row
    public boolean computerRowPlace(char mark) {
        for (int i=0;i<3;i++) {
            if ((computerCheckChar(board[i][0], board[i][1], mark)) && (board[i][2] == '-')) {
                placeMark(i,2);
                return true;
            }
            else if ((computerCheckChar(board[i][0], board[i][2], mark)) && (board[i][1] == '-')) {
                placeMark(i,1);
                return true;
            }
            else if ((computerCheckChar(board[i][1], board[i][2], mark)) && (board[i][0] == '-')) {
                placeMark(i,0);
                return true;
            }
        }
        return false; //how to not place a point?
    }

    //Checks to see if player X has two in a row
    public boolean computerColPlace(char mark) {
        for (int i=0;i<3;i++) {
            if ((computerCheckChar(board[0][i], board[1][i], mark)) && (board[2][i] == '-')) {
                placeMark(2,i);
                return true;
            }
            else if ((computerCheckChar(board[0][i], board[2][i], mark)) && (board[1][i] == '-')) {
                placeMark(1,i);
                return true;
            }
            else if ((computerCheckChar(board[1][i], board[2][i], mark)) && (board[0][i] == '-')) {
                placeMark(0,i);
                return true;
            }
        }
        return false;
    }

    //Checks to see if player X has two in a diagonal
    public boolean computerDiagonalPlace(char mark) {
        //middle spot
        if (((computerCheckChar(board[0][0], board[2][2], mark)) | (computerCheckChar(board[2][0], board[0][2], mark))) && (board[1][1] == '-')) {
            placeMark(1,1);
            return true;
        }
        //top left
        else if ((computerCheckChar(board[1][1], board[2][2], mark)) && (board[0][0] == '-')) {
            placeMark(0,0);
            return true;
        }
        //bottom right
        else if ((computerCheckChar(board[1][1], board[0][0], mark)) && (board[2][2] == '-')) {
            placeMark(2,2);
            return true;
        }
        //top right
        else if ((computerCheckChar(board[1][1], board[2][0], mark)) && (board[0][2] == '-')) {
            placeMark(0,2);
            return true;
        }
        //bottom left
        else if ((computerCheckChar(board[1][1], board[0][2], mark)) && (board[2][0] == '-')) {
            placeMark(2,0);
            return true;
        }
        return false;
    }

}