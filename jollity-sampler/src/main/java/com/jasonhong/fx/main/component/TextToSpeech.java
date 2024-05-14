package com.jasonhong.fx.main.component;

import com.jasonhong.tts.MaryTTSUtil;
import javafx.application.Application;
import javafx.scene.Scene;  
import javafx.scene.control.TextArea;  
import javafx.scene.control.Button;  
import javafx.scene.layout.VBox;  
import javafx.stage.Stage;  
  
public class TextToSpeech extends Application {
  
    private TextArea textArea;  
    private Button generateButton;  
  
    @Override  
    public void start(Stage primaryStage) {  
        textArea = new TextArea();
        textArea.setText( "Hello, world!");
//        String textToSpeak = "Hello, world!";
        generateButton = new Button("Generate Speech");  
        generateButton.setOnAction(event -> generateSpeech());  
  
        VBox root = new VBox(10, textArea, generateButton);  
        Scene scene = new Scene(root, 400, 300);  
  
        primaryStage.setTitle("Text to Speech");  
        primaryStage.setScene(scene);  
        primaryStage.show();  
    }  
  
    private void generateSpeech() {  
        // 获取文本框中的文本  
        String text = textArea.getText();  
        // 调用MaryTTS生成录音  
        generateAudio(text);  
    }  
  
    private void generateAudio(String text) {
        MaryTTSUtil.generateAudio(text);
    }
    public static void main(String[] args) {
        launch( args);
    }
}