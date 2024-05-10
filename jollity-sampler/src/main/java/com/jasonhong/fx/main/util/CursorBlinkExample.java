package com.jasonhong.fx.main.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;  
import javafx.application.Application;  
import javafx.scene.Scene;  
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;  
import javafx.stage.Stage;  
import javafx.util.Duration;  
  
public class CursorBlinkExample extends Application {  
  
    private Timeline cursorBlinkTimeline;  
  
    @Override  
    public void start(Stage primaryStage) {  
        TextArea textArea = new TextArea("这是一个模拟光标闪动的TextArea");  
  
        // 创建Timeline来模拟光标闪动  
        cursorBlinkTimeline = new Timeline(  
                new KeyFrame(Duration.seconds(0.5), event -> {  
                    // 这里只是简单地切换背景色作为示例，实际上你可能需要更复杂的逻辑  
                    textArea.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));  
                }),  
                new KeyFrame(Duration.seconds(0.5), event -> {  
                    // 假设这里有一个“光标”颜色，但实际上是背景色的短暂变化  
                    textArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                }),  
                new KeyFrame(Duration.seconds(1.0), event -> {  
                    // 重复动画  
                    cursorBlinkTimeline.playFromStart();  
                })  
        );  
        cursorBlinkTimeline.setCycleCount(Timeline.INDEFINITE);  
        cursorBlinkTimeline.play();  
  
        StackPane root = new StackPane(textArea);  
        Scene scene = new Scene(root, 300, 250);  
  
        primaryStage.setTitle("Cursor Blink Example");  
        primaryStage.setScene(scene);  
        primaryStage.show();  
    }  
  
    public static void main(String[] args) {  
        launch(args);  
    }  
}