package main.java.samwilkins333.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// https://docs.oracle.com/javafx/2/fxml_get_started/why_use_fxml.htm#CHDCHIBE
// https://scss.tcd.ie/publications/theses/diss/2015/TCD-SCSS-DISSERTATION-2015-069.pdf

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Scrabble.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}