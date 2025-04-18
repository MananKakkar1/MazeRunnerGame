package com.example.mazerunnergame;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.net.URL;

public class MazeView {
    private static final int TILE_SIZE = 40;
    private final GridPane gridPane;
    private final Button seeSolutionButton;
    private final Button resetButton;
    private final Button generateButton;
    private final Button changeSizeButton;
    private final VBox vboxLayout;
    private final HBox hBox;
    private Text winnerMessage;
//    System.out.println("Player sprite URL: " + playerUrl);
    public final Image playerSprite = new Image(getClass().getResource("/com/example/mazerunnergame/human_sprite.png").toExternalForm());
    public final Image botSprite = new Image(getClass().getResource("/com/example/mazerunnergame/robot_sprite.png").toExternalForm());
    public final Image goalSprite = new Image(getClass().getResource("/com/example/mazerunnergame/goal_sprite.png").toExternalForm());


    public MazeView() {
        this.gridPane = new GridPane();
        this.resetButton = new Button("Reset");
        this.seeSolutionButton = new Button("See Solution");
        this.generateButton = new Button("Generate New Maze");
        this.changeSizeButton = new Button("Change Maze Size");
        this.vboxLayout = new VBox(10);
        this.hBox = new HBox(10);
        this.winnerMessage = new Text("");

        hBox.getChildren().addAll(seeSolutionButton, resetButton, winnerMessage);
        vboxLayout.getChildren().addAll(changeSizeButton, generateButton, hBox, gridPane);
    }
    public void drawAnimatedPlayer(ImageView characterView, int[] playerPosition) {
         // however you build your grid
        GridPane.setRowIndex(characterView, playerPosition[0]);
        GridPane.setColumnIndex(characterView, playerPosition[1]);
        gridPane.getChildren().add(characterView);
    }

    public void drawMaze(int[][] grid, int[] playerPos, int[] botPos) {
        gridPane.getChildren().clear();
        int[] goalPos = {-1, -1};
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Rectangle cell = new Rectangle(TILE_SIZE, TILE_SIZE);
                cell.setStroke(Color.BLACK);
                if (grid[i][j] == 1) {
                    cell.setFill(Color.BROWN);
                } else if (grid[i][j] == 0) {
                    cell.setFill(Color.BLACK);
                } else {
                    cell.setFill(Color.BLACK);
                    goalPos[0] = j;
                    goalPos[1] = i;
                }

                if (i == playerPos[0] && j == playerPos[1]) {
                    cell.setFill(Color.BLUE);
                }
                if (i == botPos[0] && j == botPos[1]) {
                    cell.setFill(Color.RED);
                }

                gridPane.add(cell, j, i);
            }
        }
        ImageView playerView = new ImageView(playerSprite);
        playerView.setFitWidth(TILE_SIZE);
        playerView.setFitHeight(TILE_SIZE);
        gridPane.add(playerView, playerPos[1], playerPos[0]);

        ImageView botView = new ImageView(botSprite);
        botView.setFitWidth(TILE_SIZE);
        botView.setFitHeight(TILE_SIZE);
        gridPane.add(botView, botPos[1], botPos[0]);

        ImageView goalView = new ImageView(goalSprite);
        goalView.setFitWidth(TILE_SIZE);
        goalView.setFitHeight(TILE_SIZE);
        gridPane.add(goalView, goalPos[0], goalPos[1]);

    }

    public void showWinnerMessage() {
        winnerMessage.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        winnerMessage.setText("Winner!");
        winnerMessage.setFill(Color.BLACK);
    }

    public VBox getVBoxLayout() {
        return vboxLayout;
    }

    public Button getSeeSolutionButton() {
        return seeSolutionButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getGenerateButton() {
        return generateButton;
    }

    public Button getChangeSizeButton() {
        return changeSizeButton;
    }

}
