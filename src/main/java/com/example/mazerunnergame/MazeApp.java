package com.example.mazerunnergame;
import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class MazeApp extends Application {
    private int width, height;
    @Override
    public void start(Stage primaryStage) {
        getMazeSize();
        MazeModel model = new MazeModel(height, width);
        MazeView view = new MazeView();
        MazeController controller = new MazeController(model, view);
        controller.startGame(primaryStage);
    }

    private void getMazeSize() {
        while (true) {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Maze Size");
            dialog.setHeaderText("Enter the width of the maze");
            dialog.setContentText("Width:");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) {
                System.out.println("Please enter a valid width");
                continue;
            }
            TextInputDialog dialog2 = new TextInputDialog("1");
            dialog2.setTitle("Maze Size");
            dialog2.setHeaderText("Enter the height of the maze");
            dialog2.setContentText("Height:");
            Optional<String> result2 = dialog2.showAndWait();
            if (result2.isEmpty()) {
                System.out.println("Please enter a valid height");
                continue;
            }
            this.width =   Integer.parseInt(result.get());
            this.height = Integer.parseInt(result2.get());
            if (width < 10 || height < 10) {
                System.out.println("Maze size must be at least 10x10");
                continue;
            }
            break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
