package com.jasonhong.fx.main.component.smart.smartHome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClockApp extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClockApp.fxml"));
        VBox root = loader.load();
        ClockController controller = loader.getController();
        controller.init(); // 如果有初始化代码，可以放在这里

        primaryStage.setTitle("JavaFX Clock");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}