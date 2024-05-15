package com.jasonhong.fx.main.fxml.audio.record;

import com.jasonhong.fx.main.page.CommonPage;
import com.jasonhong.fx.main.util.DialogUtil;
import com.jasonhong.fx.main.util.RecentFilesManager;
import com.jasonhong.media.audio.handler.AudioRecorderHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.util.Set;

import static com.jasonhong.fx.main.util.DialogUtil.chooseOutputFile;
import static com.jasonhong.fx.main.util.FXUtil.openFileFolder;
import static com.jasonhong.fx.main.util.MediaPlayerUtil.play;

public class AudioClonePage extends CommonPage {

    public static final String NAME = "声音克隆";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3","m4a","wav");

    private AudioRecorderHandler handler;
    private File outputFile;

    private Button openFolderButton;
    private Button playButton;
    private Button stopButton;
    private Button recordButton;

    public AudioClonePage() {
        TextArea ta = new TextArea();
        ta.setText("该目录为腾讯视频的缓存目录。\n" +
                "保留该目录下的文件可以减少您观看节目时等待缓冲的时间，也可以供腾讯视频客户端在未联网状态下观看您最近观看过的节目。\n" +
                "系统将定期自动将超过指定文件夹大小（默认5GB，可进入软件设置中进行调整）的文件清理。离线下载的视频节目不受此限制，不会被删除。\n" +
                "如果磁盘空间不足，可以在关闭腾讯视频客户端的情况下删除本目录下的文件。");
        handler = new AudioRecorderHandler();
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        HBox audioBox = new HBox();

        Label statusLabel = new Label("点击开始录音");
        vbox.getChildren().add(statusLabel);

        recordButton = new Button("试听");
        recordButton.setOnAction(e -> {
            try {
                startRecording(statusLabel);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        });
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



    private void startRecording(Label statusLabel) throws LineUnavailableException {
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
