package com.example.spacerunner;

import View.ConfirmExit;
import View.ViewManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            ViewManager manager = new ViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.setTitle("SpaceRunner the Game");
            primaryStage.setResizable(false);
            //primaryStage.setFullScreen(true);
            primaryStage.setOnCloseRequest(x ->{
                x.consume();
                if(ConfirmExit.askConfirmation()) {
                    Platform.exit();
                }
            });
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch();
    }
}
