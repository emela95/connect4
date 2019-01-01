package core;
import java.util.Random;


/**
 *
 * This computer adds a computer player functionality to the game
 *
 */


/**
 *
 * inherited constructor from connect4
 *
 * */
public class Connect4ComputerPlayer extends Connect4 {

    public Connect4ComputerPlayer() {
        super();
    }

    /**
     * loop method overriden
     * @return gamePlay
     */

    @Override
    public boolean loop () {
        while (gamePlay) {
            checkForPlayerWinner(playerX);
            if (!gamePlay) {
                return gamePlay;
            }

            checkForPlayerWinner(computerPlayer);

        }

        return gamePlay;
    }


    /**
     *
     * This method will recursively check column validity if invalid column is selected, and then a new column is
     * tried. In this case a new column number is randomly generated until a valid one is found by passing it
     * recursively
     *
     * @param columnNumber is the column number which player/computer plays
     * @param player is the identity of player or computer
     *
     */
    public void placeGamePieceComputer(int columnNumber, String player) {
        for (int i = 5; i >= 0; i--) {
            if (!displayGrid[0][columnNumber].equals("| ")) {
                Random random = new Random();
                computer = random.nextInt(7);
                placeGamePieceComputer(computer, player);
                return;
            }


            else {
                if(displayGrid[i][computer].equals("| ")){
                    displayGrid[i][computer] = "|" + player;
                    playerCCoinCount--;
                    return;

                }
            }


        }
    }


    /**
     * method overriden from connect4 checks if player or computer is playing and determines who has the turn to play
     *
     * @param player is the respective player who has the current turn.
     */

    @Override
    public void checkForPlayerWinner(String player) {
        if (checkForDraw()) {
            System.out.println("No winner in this game. It is a tie");
            gamePlay = false;
            return;
        }

        if (!player.equals(computerPlayer)) {
            super.checkForPlayerWinner(player);
        }

        else {
            if (player.equals(computerPlayer)) {
                placeGamePieceComputer(computer, player);

                Random placement = new Random();
                computer = placement.nextInt(7);
                System.out.println("Computer's Turn:" + "\n");
                System.out.println(boardOutput ());

                if (verticalWinCheck (player)) {
                    System.out.println("Computer won the game!");
                    System.out.println(boardOutput ());
                    gamePlay = false;
                    return;
                }


                if (horizontalWinCheck (player)) {
                    System.out.println("Computer won the game!");
                    System.out.println(boardOutput ());
                    gamePlay = false;
                    return;
                }

                if (diagonalDescendingWinCheck (player)) {
                    System.out.println("Computer won the game!");
                    System.out.println(boardOutput ());
                    gamePlay = false;
                    return;
                }

                if (diagonalAscendingWinCheck (player)) {
                    System.out.println("Computer won the game!");
                    System.out.println(boardOutput ());
                    gamePlay = false;
                    return;
                }


                return;
            }

        }
    }


}



