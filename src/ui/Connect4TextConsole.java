package ui;
import core.Connect4;
import core.Connect4ComputerPlayer;
import java.util.Scanner;
import javafx.application.Application;
/**
 * Description: this class is for the text-based UI
 * this class gives the option for players to either play with user or computer
 *
 *
 * @author Chiemela Nwoke
 * @version 10/30/2018
 *
 *
 * */


public class Connect4TextConsole extends Connect4GUI {
    public static void main(String [] args)  throws Exception  {
        String question;
        Scanner scan = new Scanner(System.in);


        System.out.println("Please select a " +
                "type of game play. Enter 'console' to play in console view. Enter 'GUI' to play in GUI view");

        question = scan.nextLine();
        if (question.equalsIgnoreCase("GUI")) { //Launch GUI play
            Application.launch(Connect4GUI.class, args);

        }

        if (question.equalsIgnoreCase("console")) {

            Connect4ComputerPlayer computerGame = new Connect4ComputerPlayer();
            Connect4 gameBoard = new Connect4();
            System.out.println("Enter 'P' if you want to play against another player; enter 'C' to " +
                    "" + "play against computer.");

            question = scan.nextLine();
            if (question.equals("C")) {
                System.out.println("Begin Game.");
                computerGame.boardOutput ();
                computerGame.loop ();

            } else {
                if (question.equals("P")) {
                    System.out.println("Begin Game.");
                    gameBoard.boardOutput ();
                    gameBoard.loop ();

                } else
                    throw new Exception("Please try again!, Not a valid entry. Please enter only 'P' or 'C' to start a game. " +
                            "Game now exiting. ");

            }


        }
    }

}