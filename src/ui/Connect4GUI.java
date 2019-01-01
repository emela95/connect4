package ui;

import javafx.application.Application;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.shape.Shape;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.effect.Lighting;
import javafx.scene.Group;
import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/***
 * This is a Connect4 GUI class that creates a grid where the game is being played
 * Prompts the user for option to player with another user or play against computer
 * Displays the result of the game either a win or tie
 * Prompts the user to play again or quit the game
 */

public class Connect4GUI extends Application {
    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static boolean yellowDisc;
    private Stage mainWindow;
    private Cell [][] grid = new Cell[COLUMNS][ROWS];
    private Pane rootDisc = new Pane();
    private List<Cell> yellowList = new ArrayList<>();
    private Button newGame;
    private Button twoPlayerMode;
    private Button computerPlayerMode;
    private Button exitGame;
    private Stage newStage;
    private Stage gameStage;
    private Stage drawStage;
    private boolean computer = false;



    /**
     *
     * @param stage is the stage object that gets displayed as the main window of the application
     */
    @Override
    public void start(Stage stage)  {
        mainWindow = stage;
        gameType(); //User game play choice

        //Two player mode
        twoPlayerMode.setOnMouseClicked(twoP->{
            gamePlay(new Stage(),computer);
            gameStage.close();
        });


//Computer mode
        computerPlayerMode.setOnMouseClicked(comp -> {
            gamePlay(new Stage(),computer);
            computer = true;
            gameStage.close();
        });

    }

    /**
     *
     * @return returns the shape created for the game board. This is done by constructing a rectangular grid for the game
     * Also the circle is subtracted from the total rectangle grid for the overall appearance of game
     */
    private Shape constructGrid () {

        Shape shape = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);
        //Create overall rectangle shape to punch holes in.
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Shape circle = new Circle(TILE_SIZE / 2); //Create circles to punch out of rectangle
                circle.setLayoutX(TILE_SIZE / 2);
                circle.setLayoutY(TILE_SIZE / 2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);

                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant(); //Effects for game board visuals
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.rgb (6, 38, 86));




        return shape;

    }

    /**
     * This method creates the window asking what type of game the user would like to play: 2 player
     * or against a computer.
     */
    private void gameType(){
        gameStage = new Stage();

        //HBox
        HBox player = new HBox();
        player.setPadding(new Insets(12,60,60,60));
        player.setSpacing(10);

        //Labels for text
        Label gamePlay = new Label ("Chose game play!");
        gamePlay.setFont(Font.font("SansSerif, Courier", FontWeight.BOLD,24));
        player.getChildren().add(gamePlay);

        //Gui Buttons
        twoPlayerMode = new Button("Play Two Player Mode");
        computerPlayerMode = new Button("Play Computer Mode");
        player.getChildren().add(twoPlayerMode);
        player.getChildren().add(computerPlayerMode);

        Scene stageScene = new Scene(player);
        gameStage.setScene(stageScene);
        gameStage.show();
    }

    /**
     * This method creates the window if a tieGame occurs. The user has the option to exit the application
     * or play another game.
     */
    private void tieGame (){
        drawStage = new Stage();

        //HBox
        HBox draw = new HBox();
        draw.setPadding(new Insets(12,60,60,60));
        draw.setSpacing(10);

        //Label text
        Label displayDraw = new Label ("Its a Tie! No winner.");
        displayDraw.setFont(Font.font("SansSerif, Courier", FontWeight.BOLD,35));
        draw.getChildren().add(displayDraw);


        //Gui Buttons
        draw.getChildren().add(newGame);
        draw.getChildren().add(exitGame);


        Scene stageScene = new Scene(draw);
        drawStage.setScene(stageScene);
        drawStage.show();


        //Section of a new game
        newGame.setOnMouseClicked(game -> {
            drawStage.close();
            mainWindow.close();

            Platform.runLater(() -> new Connect4GUI().start(new Stage()));

        });

        //close the game
        exitGame.setOnMouseClicked(exit -> {
            drawStage.close();
            mainWindow.close();

        });
    }

    /**
     * This is the window that is displayed when a winner is detected. At game end the user has the
     * option to either exit the application or play another game.
     */

    private void gameOver(){
        newStage = new Stage();

        //VBox
        HBox winner = new HBox();
        winner.setPadding(new Insets(12,60,60,60));
        winner.setSpacing(12);

        //Label
        Label displayWinner = new Label ((yellowDisc ? "Yellow!" : "Red!") + " Wins");
        displayWinner.setFont(Font.font("SansSerif, Courier", FontWeight.BOLD,24));
        winner.getChildren().add(displayWinner);

        //Buttons
        winner.getChildren().add(newGame);
        winner.getChildren().add(exitGame);

        //Display
        Scene stageScene = new Scene(winner);
        newStage.setScene(stageScene);
        newStage.show();
    }


    /**
     *
     * @param stage The stage object that the game is displayed upon.
     * @param computer Boolean that toggles computer mode off and on. This is dependent of what style of
     *                 game play the user selects.
     */

    private void gamePlay(Stage stage, boolean computer){
        this.computer = computer;

        //Create GUI and implement functionality
        Group root = new Group();
        Scene scene = new Scene(root, (COLUMNS+1) * TILE_SIZE, (ROWS+1)*TILE_SIZE);
        root.getChildren().add(rootDisc);
        Shape gridShape = constructGrid ();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());
        newGame = new Button("Play Again?");
        exitGame = new Button("Quit?");


        //prompt to open new game
        newGame.setOnMouseClicked(game -> {
            newStage.close();
            stage.close();

            Platform.runLater(() -> new Connect4GUI().start(new Stage()));

        });

        //close the application

        exitGame.setOnMouseClicked(exit -> {
            newStage.close();
            stage.close();
        });

        //Display
        stage.setTitle("Connect4 Game");
        stage.setScene(scene);
        stage.show();
    }



    /**
     *
     * Extends circle class, creates circles on the display grid
     *
     *
     * */
    private static class Cell extends Circle {
        private boolean yellow = false;

        /**
         *
         * @param yellow boolean value that toggles back and forth between yellow disc placement and
         *              red disc placement. This helps simulate player turn.
         */
        public Cell (boolean yellow) {

            super(TILE_SIZE/2, yellow ? Color.YELLOW : Color.RED); //Creation of discs using the
            //super ctr of the Circle class. Color is determined by value of boolean yellow.
            this.yellow = yellow;
            setCenterX(TILE_SIZE / 2);
            setCenterY(TILE_SIZE / 2);

        }


    }



    /**
     *
     * @return the list of type Rectangle t
     * creates hover feature for the game appearance
     */
    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rectangle = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rectangle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rectangle.setFill(Color.TRANSPARENT);

            //creates hover effect on selection of a particular column
            rectangle.setOnMouseEntered(e -> rectangle.setFill(Color.rgb (200, 200, 50, 0.3)));
            rectangle.setOnMouseExited(e -> rectangle.setFill(Color.TRANSPARENT));


            final int column = x;

            rectangle.setOnMouseClicked(e-> {  //If the column is clicked and computer mode is engaged, computer
                if(computer){
                    placeComputerDisc(new Cell (computer));

                }

                placeDisc( new Cell (yellowDisc), column);
            });

            list.add(rectangle);

        }

        return list;
    }

    /**
     *
     * @param cell is the disc object that is going to be placed onto the game board by the computer.
     */
    private void placeComputerDisc(Cell cell) {
        int row = ROWS - 1;
        Random random = new Random (); //Random column choice generation
        int column = random.nextInt (7);

        do {
            if (!isDisc (column, row).isPresent ()) { //If there is not a disc present the game piece can be placed.
                break;
            }

            row--; //Continuously decrement row so that the disc goes to the bottom most slot.
        } while (row >= 0);
        if (row < 0) {
            return;
        }

        grid[column][row] = cell;
        yellowList.add (cell);
        rootDisc.getChildren ().add (cell);
        cell.setTranslateX (column * (TILE_SIZE + 5) + TILE_SIZE / 4);
        cell.setTranslateY (row * (TILE_SIZE + 5) + TILE_SIZE / 4);

        final int currentRow = row;


        TranslateTransition animation = new TranslateTransition (Duration.seconds (0.5), cell);
        animation.setToY (row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        animation.setOnFinished (e -> {

            if (findWin (column, currentRow)) { //Checks for win possibilities after each piece is placed.
                gameOver (); //Display game over window
            }


            yellowDisc = !yellowDisc; //Change the disc color (changing turns);


        });
        animation.play ();
    }


    /**
     *
     * @param cell is the disc being placed by the user's selection.
     * @param column is the target column
     */
    private void placeDisc(Cell cell, int column) {
        int row = ROWS - 1;
        do {
            if (!isDisc (column, row).isPresent ()) { //If there is not a disc present, a disc can be placed.
                break;
            }
//decrements the row so that the disc appears at the bottom
            row--;


        } while (row >= 0);
        if (row < 0) {
            return;
        }

        grid[column][row] = cell;
        yellowList.add (cell);
        rootDisc.getChildren ().add (cell);
        cell.setTranslateX (column * (TILE_SIZE + 5) + TILE_SIZE / 4);
        cell.setTranslateY (row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        final int currentRow = row;


        if (findWin (column, currentRow)) {
            gameOver ();
        }


        yellowDisc = !yellowDisc;


    }

    /**
     *
     * validates if the disc is empty or not
     * @param column column of disc
     * @param row row of the disc
     * @return returns optional
     */

    public Optional<Cell> isDisc(int column, int row) {
        if (column < 0 || column >= COLUMNS || row < 0 || row >= ROWS)
            return Optional.empty();


        return Optional.ofNullable(grid[column][row]); //Returns an Optional object with value of element in the grid.
    }


    /**
     *
     *
     *
     * @param points is a list of 2D points that are passed through to check the x and y coordinates
     *               of pieces placed to see if there is a possible combination of four of a player's game piece. The
     *               pieces can be aligned either be vertical,horizontal, upper diagonal, lower diagonal.
     * @return This method returns a true value if the count climbs to 4 (a win has been detected), and false otherwise
     * (there is no win detected).
     */
    private boolean check(List<Point2D> points){
        int count = 0;
        for(Point2D p : points){
            int column = (int)p.getX();
            int rows = (int)p.getY();

            Cell cell = isDisc(column,rows).orElse(new Cell (!yellowDisc));
            if(cell.yellow == yellowDisc){
                count++;
                if(count == 4){
                    return true;
                }
            }else{
                count = 0;
            }
        }

        return false;
    }

    /**
     *
     * Checks for a tie, or for a win in any direction.
     *
     * @param column Current column where the piece has been placed used.
     * @param row Current row column where the piece has been placed.
     * @return Returns true
     */
    private boolean findWin(int column, int row) {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());
//Creates a list of the x and y
        List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());
//Creates a list of the x and y coordinates of the placed
        Point2D topLeft = new Point2D(column-3,row-3);
        List<Point2D> diagonalTop = IntStream.rangeClosed(0,6)
                .mapToObj(i ->topLeft.add(i,i))
                .collect(Collectors.toList());
//Creates a list of the x and y coordinates of the placed
        Point2D bottomLeft = new Point2D(column-3,row+3);
        List<Point2D> diagonalBottom = IntStream.rangeClosed(0,6)
                .mapToObj(i ->bottomLeft.add(i,-i))
                .collect(Collectors.toList());

//Passes each list of coordinates into the check method to locate win.
        return check(vertical) || check(horizontal)
                || check(diagonalBottom) || check(diagonalTop);

    }





}
