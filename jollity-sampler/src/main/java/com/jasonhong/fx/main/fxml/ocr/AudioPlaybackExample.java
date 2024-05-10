package com.jasonhong.fx.main.fxml.ocr;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
  
public class AudioPlaybackExample extends Application {  
  
    private MediaPlayer mediaPlayer;  
  
    @Override  
    public void start(Stage primaryStage) {  
        Button playButton = new Button("Play Audio");  
        playButton.setOnAction(event -> playAudio());  
  
        StackPane root = new StackPane();  
        root.getChildren().add(playButton);  
  
        Scene scene = new Scene(root, 300, 250);  
  
        primaryStage.setTitle("Audio Playback Example");  
        primaryStage.setScene(scene);  
        primaryStage.show();  
    }  
  
    private void playAudio() {  
        // 创建Media对象，指定音频文件的URL  
        Media media = new Media(getClass().getResource("/path/to/your/audiofile.mp3").toExternalForm());  
  
        // 创建MediaPlayer对象并设置其media属性  
        if (mediaPlayer == null || !mediaPlayer.getMedia().equals(media)) {  
            mediaPlayer = new MediaPlayer(media);  
        }  
  
        // 播放音频  
        mediaPlayer.play();  
  
        // 如果需要，你可以添加监听器来检测播放何时结束  
        mediaPlayer.setOnEndOfMedia(new Runnable() {  
            @Override  
            public void run() {  
                // 播放结束后的处理逻辑  
                System.out.println("Audio playback finished.");  
            }  
        });  
    }  
  
    public static void main(String[] args) {  
        launch(args);  
    }  
}