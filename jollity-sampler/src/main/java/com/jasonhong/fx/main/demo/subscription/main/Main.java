package com.jasonhong.fx.main.demo.subscription.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {  
  
    @Override  
    public void start(Stage primaryStage) throws IOException {
        // 加载FXML文件
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = loader.load();

        // 设置控制器（如果FXML文件中没有指定）
        MainController controller = loader.getController();
        primaryStage.setTitle("Hello World!");  
        Label label = new Label("Welcome to JavaFX!");  
//        StackPane root = new StackPane();
//        root.getChildren().add(label);
        primaryStage.setScene(new Scene(root, 300, 250));  
        primaryStage.show();  
    }  
  
    public static void main(String[] args) {  
        launch(args);  
    }  
}