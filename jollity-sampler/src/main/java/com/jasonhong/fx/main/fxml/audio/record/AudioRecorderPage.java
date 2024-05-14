package com.jasonhong.fx.main.fxml.audio.record;

import com.jasonhong.fx.main.util.DialogUtil;
import com.jasonhong.media.audio.handler.AudioRecorderHandler;
import com.jasonhong.fx.main.page.CommonPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Set;

import static com.jasonhong.fx.main.fxml.ocr.ImageToText.SUPPORTED_MEDIA_TYPES;
import static com.jasonhong.fx.main.util.DialogUtil.chooseOutputFile;

public class AudioRecorderPage extends CommonPage {

    public static final String NAME = "录音";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3","m4a","wav");

    private AudioRecorderHandler handler;
    private File outputFile;

    private Button stopButton;
    private Button recordButton;
    public AudioRecorderPage() {
        handler = new AudioRecorderHandler();
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Label statusLabel = new Label("点击开始录音");
        vbox.getChildren().add(statusLabel);

        recordButton = new Button("录音");
        recordButton.setOnAction(e -> startRecording(statusLabel));
        vbox.getChildren().add(recordButton);

        stopButton = new Button("停止");
        stopButton.setDisable(true);
        stopButton.setOnAction(e -> stopRecording(statusLabel));
        vbox.getChildren().add(stopButton);

        Button chooseFileButton = new Button("保存文件");
        chooseFileButton.setOnAction(e -> {
            outputFile = chooseOutputFile(DialogUtil.AUDIO, SUPPORTED_MEDIA_TYPES);
            if (outputFile != null) {
                handler.setOutputFile(outputFile);
            }
        });
        vbox.getChildren().add(chooseFileButton);
        getChildren().add(vbox);
    }


    private void startRecording(Label statusLabel) {
        if (outputFile == null) {
            statusLabel.setText("请选择保存的文件地址");
            return;
        }

        if (handler.isRecording()) {
            statusLabel.setText
                    ("已经开始录音!");
            return;
        }
        handler.startRecording();
        statusLabel.setText("正在录音...");

        recordButton.setDisable(true);
        stopButton.setDisable(false);
    }

    private void stopRecording(Label statusLabel) {
        handler.stopRecording();
        statusLabel.setText("停止录音");
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }


    @Override
    public String getName() {
        return NAME;
    }
}
