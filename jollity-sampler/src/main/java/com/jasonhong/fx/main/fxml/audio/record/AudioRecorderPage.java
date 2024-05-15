package com.jasonhong.fx.main.fxml.audio.record;

import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.util.DialogUtil;
import com.jasonhong.fx.main.util.RecentFilesManager;
import com.jasonhong.media.audio.handler.AudioRecorderHandler;
import com.jasonhong.fx.main.page.CommonPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.collections.FastArrayList;

import java.io.File;
import java.util.List;
import java.util.Set;

import static com.jasonhong.fx.main.fxml.ocr.ImageToText.SUPPORTED_MEDIA_TYPES;
import static com.jasonhong.fx.main.util.DialogUtil.chooseOutputFile;
import static com.jasonhong.fx.main.util.FXUtil.openFileFolder;
import static com.jasonhong.fx.main.util.MediaPlayerUtil.play;

public class AudioRecorderPage extends CommonPage {

    public static final String NAME = "录音";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3","m4a","wav");

    private AudioRecorderHandler handler;
    private File outputFile;

    private Button openFolderButton;
    private Button playButton;
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

        playButton = new Button("播放");
        playButton.setDisable(true);
        playButton.setOnAction(e -> play(outputFile));
        vbox.getChildren().add(playButton);

        openFolderButton = new Button("打开文件夹");
        openFolderButton.setDisable(true);
        openFolderButton.setOnAction(e -> openFileFolder(outputFile));
        vbox.getChildren().add(openFolderButton);
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
        openFolderButton.setDisable(true);
        playButton.setDisable(true);
    }

    private void stopRecording(Label statusLabel) {
        handler.stopRecording();
        statusLabel.setText("停止录音");
        recordButton.setDisable(false);
        stopButton.setDisable(true);
        openFolderButton.setDisable(false);
        playButton.setDisable(false);
        new RecentFilesManager(RecentFilesManager.RecentFileType.ADUIO).addRecentFile(outputFile);
    }


    @Override
    public String getName() {
        return NAME;
    }
}
