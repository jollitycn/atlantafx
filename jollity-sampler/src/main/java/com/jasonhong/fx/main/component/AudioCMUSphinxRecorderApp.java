package com.jasonhong.fx.main.component;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.scene.control.Label;  
import javafx.scene.layout.VBox;  
import javafx.stage.FileChooser;  
import javafx.stage.Stage;  
  
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;  
import java.io.IOException;  
  
public class AudioCMUSphinxRecorderApp extends Application {

    private TargetDataLine line;
    private AudioFormat format;
    private boolean isRecording = false;
    private ByteArrayOutputStream audioData;
    Button recordButton;
    Button stopButton;Button saveButton;
    Label statusLabel;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Audio Recorder");

        // 创建音频格式  
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);

        // 设置用户界面  
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

          recordButton = new Button("Start Recording");
        recordButton.setOnAction(event -> startRecording());

          stopButton = new Button("Stop Recording");
        stopButton.setOnAction(event -> stopRecording());
        stopButton.setDisable(true);

          saveButton = new Button("Save Audio");
        saveButton.setOnAction(event -> saveAudio());
        saveButton.setDisable(true);
        statusLabel = new Label("Not recording");

        root.getChildren().addAll(recordButton, stopButton, saveButton, statusLabel);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startRecording() {
        if (isRecording) {
            return;
        }

        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                return;
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            audioData = new ByteArrayOutputStream();
            Thread recordingThread = new Thread(() -> {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = line.read(buffer, 0, buffer.length)) != -1) {
                    audioData.write(buffer, 0, bytesRead);
                }
            });
            recordingThread.start();
            Platform.runLater(() -> {
                recordButton.setDisable(true);
                stopButton.setDisable(false);
                statusLabel.setText("Recording");
            });
            isRecording = true;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (!isRecording) {
            return;
        }

        line.stop();
        line.close();
        isRecording = false;
        Platform.runLater(() -> {
            recordButton.setDisable(true);
            stopButton.setDisable(true);
           saveButton.setDisable(false);
            statusLabel.setText("Stop Recording");
        });
    }

    private void saveAudio() {
        if (audioData == null || audioData.size() == 0) {
            System.out.println("No audio data to save");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Audio File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(audioData.toByteArray()), format, AudioSystem.NOT_SPECIFIED), AudioFileFormat.Type.WAVE, file);
                System.out.println("Audio saved to " + file.getAbsolutePath());
                audioData = null; // Clear audio data after saving  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Platform.runLater(() -> {
            statusLabel.setText("saveAudio successfully");
            recordButton.setDisable(false);
            stopButton.setDisable(true);
            saveButton.setDisable(true);
            statusLabel = new Label("Not recording");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}