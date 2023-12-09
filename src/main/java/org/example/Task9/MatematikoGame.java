package org.example.Task9;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MatematikoGame extends Application {
    private static final int GRID_SIZE = 5;
    private static final int CARD_COUNT = 52;
    private List<Integer> cards;
    private int[][] grid;
    private GridPane gridPane;
    private boolean playerTurn = true;
    private int playerScore = 0;
    private int computerScore = 0;
    private Button startButton;
    private VBox root;
    private BorderPane rootBorderPane;

    public static void main(String[] args) {
        launch(args);
    }
    public static void launchMatematikoGame() {
        Platform.runLater(() -> {
            new MatematikoGame().start(new Stage());
        });
    }

    @Override
    public void start(Stage primaryStage) {
        initializeGame();
        root = createStartScreen();
        rootBorderPane = new BorderPane();
        rootBorderPane.setCenter(root);

        Scene scene = new Scene(rootBorderPane, 400, 400);
        primaryStage.setTitle("Математико");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGame() {
        cards = generateCards();
        grid = new int[GRID_SIZE][GRID_SIZE];
        Collections.shuffle(cards);
    }

    private List<Integer> generateCards() {
        List<Integer> cardList = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            cardList.add(i);
            cardList.add(i);
            cardList.add(i);
            cardList.add(i);
        }
        Collections.shuffle(cardList);
        return cardList.subList(0, CARD_COUNT);
    }

    private VBox createStartScreen() {
        VBox startScreen = new VBox(20);
        startButton = new Button("Начать");
        startButton.setStyle("-fx-font-size: 20;");
        startButton.setMinSize(100, 60);
        startButton.setOnAction(e -> startGame());
        startScreen.getChildren().add(startButton);
        startScreen.setAlignment(Pos.CENTER);
        return startScreen;
    }

    private void startGame() {
        startButton.setDisable(true);
        root.getChildren().remove(startButton);

        gridPane = createGridPane();
        root.getChildren().add(gridPane);

        playComputerTurn();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Button button = new Button();
                button.setMinSize(50, 50);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> handleButtonClick(finalI, finalJ));
                gridPane.add(button, j, i);
            }
        }
        return gridPane;
    }

    private void handleButtonClick(int row, int col) {
        if (grid[row][col] == 0 && playerTurn) {
            int card = cards.remove(0);
            grid[row][col] = card;
            updateButton(row, col, card);
            calculateScore(card);
            if (cards.size() <= 29) {
                endGame();
            } else {
                playerTurn = false;
                playComputerTurn();
            }
        }
    }

    private void playComputerTurn() {
        if (!cards.isEmpty()) {
            int randomIndex = new Random().nextInt(cards.size());
            int card = cards.remove(randomIndex);

            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (grid[i][j] == 0) {
                        grid[i][j] = card;
                        updateButton(i, j, card);
                        calculateScore(card);
                        playerTurn = true;
                        return;
                    }
                }
            }
            playerTurn = true;
        }
    }

    private void updateButton(int row, int col, int card) {
        Button button = (Button) gridPane.getChildren().get(row * GRID_SIZE + col);
        button.setText(String.valueOf(card));
    }

    private void calculateScore(int card) {
        int rowPoints = calculateRowPoints();
        int columnPoints = calculateColumnPoints();
        int diagonalPoints = calculateDiagonalPoints();

        if (playerTurn) {
            playerScore += rowPoints + columnPoints + diagonalPoints;
        } else {
            computerScore += rowPoints + columnPoints + diagonalPoints;
        }
    }

    private int calculateRowPoints() {
        int rowPoints = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            int count = 1;
            for (int j = 1; j < GRID_SIZE; j++) {
                if (grid[i][j] == grid[i][j - 1] && grid[i][j] != 0) {
                    count++;
                } else {
                    rowPoints += getRowPoints(count);
                    count = 1;
                }
            }
            rowPoints += getRowPoints(count);
        }
        return rowPoints;
    }

    private int calculateColumnPoints() {
        int columnPoints = 0;
        for (int j = 0; j < GRID_SIZE; j++) {
            int count = 1;
            for (int i = 1; i < GRID_SIZE; i++) {
                if (grid[i][j] == grid[i - 1][j] && grid[i][j] != 0) {
                    count++;
                } else {
                    columnPoints += getRowPoints(count);
                    count = 1;
                }
            }
            columnPoints += getRowPoints(count);
        }
        return columnPoints;
    }

    private int calculateDiagonalPoints() {
        int diagonalPoints = 0;

        int count = 1;
        for (int i = 1; i < GRID_SIZE; i++) {
            if (grid[i][i] == grid[i - 1][i - 1] && grid[i][i] != 0) {
                count++;
            } else {
                diagonalPoints += getDiagonalPoints(count);
                count = 1;
            }
        }
        diagonalPoints += getDiagonalPoints(count);

        count = 1;
        for (int i = 1; i < GRID_SIZE; i++) {
            if (grid[i][GRID_SIZE - i - 1] == grid[i - 1][GRID_SIZE - i] && grid[i][GRID_SIZE - i - 1] != 0) {
                count++;
            } else {
                diagonalPoints += getDiagonalPoints(count);
                count = 1;
            }
        }
        diagonalPoints += getDiagonalPoints(count);

        return diagonalPoints;
    }

    private int getRowPoints(int count) {
        switch (count) {
            case 2:
                return 10;
            case 3:
                return 40;
            case 4:
                return 80;
            case 5:
                return 50;
            default:
                return 0;
        }
    }

    private int getDiagonalPoints(int count) {
        switch (count) {
            case 2:
                return 20;
            case 3:
                return 50;
            case 4:
                return 90;
            case 5:
                return 60;
            default:
                return 0;
        }
    }

    private void showScores() {
        HBox scoresBox = new HBox(20);
        scoresBox.setAlignment(Pos.CENTER);
        Label playerScoreLabel = new Label("Очки игрока: " + playerScore);
        Label computerScoreLabel = new Label("Очки компьютера: " + computerScore);
        scoresBox.getChildren().addAll(playerScoreLabel, computerScoreLabel);

        rootBorderPane.setBottom(scoresBox);
    }

    private void endGame() {
        VBox results = new VBox(20);
        results.setAlignment(Pos.CENTER);
        results.getChildren().addAll(
                new Label("Игра завершена!")
        );

        rootBorderPane.getChildren().clear();
        rootBorderPane.setCenter(results);
        showScores();
    }
}