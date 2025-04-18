package com.example.mazerunnergame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;


public class MazeController {
    private final MazeModel model;
    private final MazeView view;
    private int width, height;
    private Stage stage;

    private Timeline gameLoop;

    public MazeController(MazeModel model, MazeView view) {
        this.model = model;
        this.view = view;
    }

    public void startGame(Stage stage) {
        this.stage = stage;
        view.drawMaze(model.getGrid(), model.getPlayerPosition(), model.getBotPosition());

        Scene scene = new Scene(view.getVBoxLayout());

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> model.movePlayer(-1, 0);
                case S -> model.movePlayer(1, 0);
                case A -> model.movePlayer(0, -1);
                case D -> model.movePlayer(0, 1);
            }
            view.drawMaze(model.getGrid(), model.getPlayerPosition(), model.getBotPosition());

            if (model.isGameWon()) {
                view.showWinnerMessage();
            }
        });

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            if (model.moveBot()) {
                view.drawMaze(model.getGrid(), model.getPlayerPosition(), model.getBotPosition());
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        view.getSeeSolutionButton().setOnAction(this::handleSeeSolutionButton);
        view.getResetButton().setOnAction(this::handleResetButton);
        view.getGenerateButton().setOnAction(this::handleGeneration);
        view.getChangeSizeButton().setOnAction(this::handleChangeSize);

        stage.setScene(scene);
        stage.setTitle("Maze Game");
        stage.show();
    }

    private void handleSeeSolutionButton(ActionEvent event) {
        if (!gameLoop.getStatus().equals(Timeline.Status.RUNNING) && event.getSource() != view.getResetButton()) {
            gameLoop.play();
        }
    }

    private void handleResetButton(ActionEvent event) {
        if (event.getSource() == view.getResetButton()) {
            model.resetPositions();
            view.drawMaze(model.getGrid(), model.getPlayerPosition(), model.getBotPosition());
            gameLoop.stop();
        }
    }

    private void handleGeneration(ActionEvent event) {
        if (event.getSource() == view.getGenerateButton()) {
            model.generateMaze();
            model.ensureValidGoalPosition();
            model.resetPositions();
            view.drawMaze(model.getGrid(), model.getPlayerPosition(), model.getBotPosition());
            gameLoop.stop();
        }
    }

    private void handleChangeSize(ActionEvent event) {
        if (event.getSource() == view.getChangeSizeButton()) {
            getMazeSize();
            model.updateGrid(this.height, this.width);
            view.drawMaze(model.getGrid(), model.getPlayerPosition(), model.getBotPosition());
            gameLoop.stop();
            stage.setWidth(model.getCols()*41);
            stage.setHeight(model.getRows()*48);
        }
    }

    private void getMazeSize() {
        while (true) {
            TextInputDialog dialog = new TextInputDialog("10");
            dialog.setTitle("Maze Size");
            dialog.setHeaderText("Enter the width of the maze");
            dialog.setContentText("Width:");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) {
                System.out.println("Please enter a valid width");
                continue;
            }
            TextInputDialog dialog2 = new TextInputDialog("10");
            dialog2.setTitle("Maze Size");
            dialog2.setHeaderText("Enter the height of the maze");
            dialog2.setContentText("Height:");
            Optional<String> result2 = dialog2.showAndWait();
            if (result2.isEmpty()) {
                System.out.println("Please enter a valid height");
                continue;
            }
            this.width = Integer.parseInt(result.get());
            this.height = Integer.parseInt(result2.get());
            if (width < 10 || height < 10) {
                System.out.println("Maze size must be at least 10x10");
                continue;
            }
            break;
        }
    }
}
