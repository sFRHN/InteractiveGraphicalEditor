/*
 * NAME: Sayed Farhaan Rafi Bhat
 * NSID: bcl568
 * Student Number: 11354916
 */

package org.example.assignment3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditorApp extends Application {


    /**
     * Start the application
     * @param stage the stage
     * @throws IOException if the FXML file is not found
     */
    @Override
    public void start(Stage stage) throws IOException {
        MainUI root = new MainUI();
        Scene scene = new Scene(root);
        stage.setTitle("EditorApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
