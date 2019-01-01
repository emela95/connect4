package core;
/**
 * Description: this class is for the game logic
 * This class allows two player mode i.e user vs computer to play the game
 *
 * @author Chiemela Nwoke
 * @version 10/30/2018
 *
 *
 * */
import java.util.Arrays;
import java.util.Scanner;

//import static ui.Connect4TextConsole.displayGrid;
//import static ui.Connect4TextConsole.isWinner;


//package core;
import ui.Connect4TextConsole;
import java.util.InputMismatchException;
import java.util.Random;


public class Connect4 extends Connect4TextConsole {

    protected String[][] displayGrid;
    protected String playerX;
    protected String playerO;
    protected int playerXCount;
    protected int playerOCount;
    protected int playerCCoinCount;
    protected int invalidMove;
    protected String computerPlayer;
    protected int computer;
    protected boolean gamePlay;
    protected final int maxRow = 6;
    protected final int maxColumn = 7;


    /**
     * Constructor for console
     *
     * */
    public Connect4() {
        Random random = new Random();
        playerX = "X";
        playerO = "O";
        computerPlayer = "O";
        playerXCount = 21;
        playerOCount = 21;
        invalidMove = 0;
        computer = random.nextInt(7);
        gamePlay = true;
        displayGrid = new String[maxRow][maxColumn];
        for (int row = 0; row < displayGrid.length; row++) {
            for (int col = 0; col < displayGrid[row].length; col++) {
                displayGrid[row][col] = "| ";
            }
        }
    }

    /**
     *
     * This method checks the validity of column if invalid column is selected, and then a new column is
     * tried.
     *
     * @param colNum is the column number the player is attempting to place a piece at.
     * @param player determines the player as being either a playerX, playerO, or computer
     * @return Returns true if the player has selected a column in the bounds of the board, returns false
     * if the player has selected a column outside of the board bounds.
     *
     */
    public boolean moveValidation (int colNum, String player){
        Scanner scan = new Scanner(System.in);
        if(colNum > 7 || colNum < 0) {
            if(!player.equals(computerPlayer)) {
                System.out.println("Invalid move. Please select valid column 1-7");
                invalidMove = scan.nextInt();
                moveValidation (invalidMove, player);
                return false;
            }


        }

        return true;
    }



    /**
     *
     * This method places respective game pieces on the board. It will also check whether or not the attempted
     * column is full or not. If it is full this method will recursively call to check validity of new user input.
     *
     * @param columNumber is the column that the user's piece is being placed.
     * @param user is user who has the turn.
     *
     */
    public void gamePlacement (int columNumber, String user)  {
        Scanner scan = new Scanner(System.in);
        for(int i = 5; i >= 0; i--){
            if(!displayGrid[0][columNumber].equals("| ")){
                System.out.println("Space is already filled, please enter a new choice.");
                int newChoice = scan.nextInt();
                gamePlacement (newChoice-1,user);
                return;
            }

            else{
                if(displayGrid[i][columNumber].equals("| ")) {
                    displayGrid[i][columNumber] = "|"+ user;
                    if(user.equals(playerO))
                        playerOCount--;
                    if(user.equals(playerX))
                        playerXCount--;
                    return;
                }

            }

        }
    }

    /**
     * This method checks the player winner
     * @param player whose turn it it to play the game
     * @throws InputMismatchException if inputs are mismatched
     *
     *
     * */

    public void checkForPlayerWinner(String player){
        if (checkForDraw()) {
            System.out.println("PlayerX and " + player +  " have come to a draw. No winner this game.");
            gamePlay = false;
            return;
        }

        try {
            System.out.println("Player " + player + " - your turn. Choose a column number from 1-7");
            Scanner scan = new Scanner(System.in);
            int turn = scan.nextInt();

            while (!moveValidation (turn, player)) {
                turn = invalidMove;


            }

            gamePlacement (turn - 1, player);
            System.out.println("Player " + player + "'s Move: " + "\n");
            System.out.println(boardOutput ());
            if (verticalWinCheck (player)) {
                System.out.println("Player " + player + " won the game");
                System.out.println(boardOutput ());
                gamePlay = false;
                return;
            }

            if (horizontalWinCheck (player)) {
                System.out.println("Player " + player + " won the game");
                System.out.println(boardOutput ());
                gamePlay = false;
                return;
            }

            if (diagonalDescendingWinCheck (player)) {
                System.out.println("Player " + player + " won the game");
                System.out.println(boardOutput ());
                gamePlay = false;
                return;
            }

            if (diagonalAscendingWinCheck (player)) {
                System.out.println("Player " + player + " won the game");
                System.out.println(boardOutput ());
                gamePlay = false;
                return;
            }
        }

        catch(InputMismatchException miss){
            System.out.println("Invalid entry. Accepts only numbers. No letters or special characters allowed. " +
                    "Please try again. ");
            checkForPlayerWinner(player);

        }


        gamePlay = true;
        return;
    }




    /**
     * Checks the contents of the display and validates it
     * @return contents
     *
     *
     *
     * */

    public String boardOutput() {
        String contents = "";
        StringBuffer sb = new StringBuffer(contents);
        for (int row = 0; row < displayGrid.length; row++) {
            for (int col = 0; col < displayGrid[row].length; col++) {
                if(displayGrid[row][col] != null)
                    sb.append(displayGrid[row][col]);

            }

            sb.append("\n");

        }

        System.out.println(sb);
        return contents;
    }

    /**
     *
     * This method checks for game placement in the horizontal direction.
     *
     * @param player is the player who has the turn.
     * @return This method will return true if the current player has played four game pieces consecutively in
     * the horizontal direction, and false otherwise (no current win).
     *
     */

    public boolean horizontalWinCheck (String player) {
        int checkX = 0;

        for (int row = 0; row < displayGrid.length; row++) {
            for (int col = 0; col < displayGrid[row].length; col++) {
                if (displayGrid[row][col].equals("|" + player)) {
                    checkX++;
                    if(checkX == 4) return true;

                } else
                    checkX = 0;
            }
        }

        return false;
    }

    /**
     *
     * This method checks for consecutive game piece placement in the vertical direction.
     *
     * @param player is the respective player who has the current turn.
     * @return This method will return true if the current player has placed four consecutive game pieces in
     * the vertical direction, and false otherwise (no current win).
     *
     */
    public boolean verticalWinCheck (String player) {
        int checkX = 0;

        for (int col = 0; col < displayGrid[0].length; col++) {
            for (int row = 0; row < displayGrid.length; row++) {
                if (displayGrid[row][col].equals("|" + player)) {
                    checkX++;

                    if (checkX == 4) return true;

                }
                else
                    checkX = 0;
            }

        }

        return false;
    }

    /**
     *
     * This method checks for game placement for ascending diagonal by comparing the current
     * board space with the next three spaces to the right and above.
     *
     * @param player is the respective player who has the current turn.
     * @return This method will return true if the current player has placed four consecutive game pieces
     * diagonally in an ascending direction, and false otherwise if no win.
     *
     */

    public boolean diagonalAscendingWinCheck (String player){
        for(int row = 0; row < maxRow -3; row++){
            for(int col = 0; col < maxColumn -3; col++){
                if(displayGrid[row][col].equals("|" + player)
                        && displayGrid[row+1][col+1].equals("|" + player)
                        && displayGrid[row+2][col+2].equals("|" + player)
                        && displayGrid[row+3][col+3].equals("|" + player)){

                    return true;
                }

            }

        }

        return false;
    }

    /**
     *
     * This method checks for consecutive game piece placement for descending diagonals by comparing the current
     * board space with the previous three spaces to the left and below.
     *
     * @param player is the respective player who has the current turn.
     * @return This method will return true if the current player has placed four consecutive game pieces
     * diagonally in a descending direction, and false otherwise no win.
     *
     */

    public boolean diagonalDescendingWinCheck (String player) {
        for (int row = 3; row < maxRow; row++) {
            for (int col = 0; col < maxColumn -3; col++) {
                if (displayGrid[row][col].equals("|" + player)
                        && displayGrid[row - 1][col + 1].equals("|" + player)
                        && displayGrid[row - 2][col + 2].equals("|" + player)
                        && displayGrid[row - 3][col + 3].equals("|" + player)) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * method checks for draw game
     * @return false is the game is a draw and true if it is a draw
     *
     * */
    public boolean checkForDraw(){
        if(playerXCount == 0 && playerOCount == 0 || playerXCount == 0 && playerCCoinCount ==0){
            return true;
        }

        return false;
    }




    /**
     * this method keeps looping the game until a winner is decided
     * @return gamePlay
     *
     * */
    public boolean loop() {

        while(gamePlay){
            checkForPlayerWinner(playerX);
            if(!gamePlay){
                return gamePlay;
            }

            checkForPlayerWinner(playerO);
            if(!gamePlay) {
                return gamePlay;
            }


        }

        return gamePlay;
    }

    /**
     *
     * @return this method will return a string representation of the connect4 board.
     *
     */

    @Override
    public String toString () {
        return "Connect4{" +
                "displayGrid=" + Arrays.toString (displayGrid) +
                ", playerX='" + playerX + '\'' +
                ", playerO='" + playerO + '\'' +
                ", computerPlayer='" + computerPlayer + '\'' +
                ", playerXCount=" + playerXCount +
                ", playerOCount=" + playerOCount +
                ", playerCCoinCount=" + playerCCoinCount +
                ", invalidMove=" + invalidMove +
                ", gamePlay=" + gamePlay +
                ", maxRow=" + maxRow +
                ", maxColumn=" + maxColumn +
                ", computer=" + computer +
                '}';
    }
}