package com.jasonhong.fx.main.fxml.audio.record;

import atlantafx.base.controls.Spacer;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.util.DialogUtil;
import com.jasonhong.fx.main.util.RecentFilesManager;
import com.jasonhong.media.audio.handler.AudioRecorderHandler;
import com.jasonhong.fx.main.page.CommonPage;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.collections.FastArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.jasonhong.fx.main.fxml.ocr.ImageToText.SUPPORTED_MEDIA_TYPES;
import static com.jasonhong.fx.main.util.DialogUtil.chooseOutputFile;
import static com.jasonhong.fx.main.util.FXUtil.openFileFolder;
import static com.jasonhong.fx.main.util.MediaPlayerUtil.play;
import static java.lang.Long.MAX_VALUE;

public class AudioRecorderPage extends CommonPage {

    public static final String NAME = "录音";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3","m4a","wav");

    private AudioRecorderHandler handler;
    private File outputFile;

    private Button openFolderButton;
    private Button playButton;
    private Button stopButton;
    private Button recordButton;
    private long startTime;
    Label statusLabel;
    Label timeLabel;
    ReadOnlyObjectWrapper<Boolean> isAutoStop = new  ReadOnlyObjectWrapper<Boolean> ();
    public AudioRecorderPage() {
        handler = new AudioRecorderHandler();
        VBox vbox = new VBox(10);
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        gp = new GaphPanel();
        gp.setMaxWidth(MAX_VALUE);
        VBox.setVgrow(gp, Priority.ALWAYS);
          statusLabel = new Label("点击开始录音");
          timeLabel = new Label("00:00:00");
        HBox infoBox = new HBox(10);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().addAll(statusLabel,timeLabel);
        vbox.getChildren().addAll(new Spacer(),gp,infoBox,controlBox);
//        controlBox.getChildren().add(statusLabel);


        recordButton = new Button("录音");
        recordButton.setOnAction(e -> {
            try {
                startRecording(statusLabel);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            }
        });
        controlBox.getChildren().add(recordButton);

        stopButton = new Button("停止");
        stopButton.setDisable(true);
        stopButton.setOnAction(e -> stopRecording( ));
        controlBox.getChildren().add(stopButton);

        CheckBox checkBox= new CheckBox("自动停止");

//        checkBox.setDisable(true);
//        checkBox.selectedProperty().bind(isAutoStop);
        checkBox.setOnAction(e ->             isAutoStop.set(checkBox.isSelected()));
        controlBox.getChildren().add(checkBox);
//        TextField timeText= new TextField("30");
//        controlBox.getChildren().add(timeText);
       timeSpinner = new Spinner<>(0, 86400 , 30); // 0 minutes to 23 hours 59 minutes
        timeSpinner.setEditable(true); // Allow user input
        controlBox.getChildren().add(timeSpinner);
        Button chooseFileButton = new Button("保存文件");
        chooseFileButton.setOnAction(e -> {
            outputFile = chooseOutputFile(DialogUtil.AUDIO, SUPPORTED_MEDIA_TYPES);
            if (outputFile != null) {
                handler.setOutputFile(outputFile);
            }
        });
        controlBox.getChildren().add(chooseFileButton);

        playButton = new Button("播放");
        playButton.setDisable(true);
        playButton.setOnAction(e -> play(outputFile));
        controlBox.getChildren().add(playButton);

        openFolderButton = new Button("打开文件夹");
        openFolderButton.setDisable(true);
        openFolderButton.setOnAction(e -> openFileFolder(outputFile));
        controlBox.getChildren().add(openFolderButton);

//        BorderPane bp = new BorderPane();
//
//        StackPane
//        gp.
//        bp.setCenter(gp);
//
//        bp.setBottom(vbox);
        getChildren().addAll(vbox);
        this.setAlignment(Pos.BASELINE_CENTER);
    }

    Spinner<Integer> timeSpinner;
    GaphPanel gp;
    AnimationTimer timer;

    private void startRecording(Label statusLabel) throws LineUnavailableException, UnsupportedAudioFileException {
        if (outputFile == null) {
            statusLabel.setText("请选择保存的文件地址");
            return;
        }

        if (handler.isRecording()) {
            statusLabel.setText
                    ("已经开始录音!");
            return;
        }
          timer=   new AnimationTimer() {
            @Override
            public void handle(long now) {
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                long elapsedMillis = System.currentTimeMillis()-startTime;
                long seconds = elapsedMillis / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;

                seconds %= 60;
                minutes %= 60;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timeLabel.setText(timeString);
            }
        };
        timer.start();

        handler.setAudioStreamReadyCallback(new AudioRecorderHandler.AudioStreamReadyCallback() {
            @Override
            public void onAudioStreamReady(AudioInputStream stream) {
                try {
                    gp.setNewMixer(handler.getAudioStream());
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        handler.startRecording();
            startTime = System.currentTimeMillis();
if(isAutoStop.get()!=null && isAutoStop.get()) {
    // 创建一个ScheduledExecutorService来管理定时任务
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // 提交一个定时任务，在5秒后执行
    executor.scheduleAtFixedRate(() -> {
         if(Duration.ofMillis(System.currentTimeMillis() - startTime).getSeconds() >= timeSpinner.getValue()) {
                Platform.runLater(() -> {
                    stopRecording( );
                });

                // 在JavaFX线程中更新UI（如果需要）
                Platform.runLater(() -> {
                    statusLabel.setText("已自动停止!");
                });
                try {
                    executor.shutdown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }, 0,1, TimeUnit.SECONDS);
}
        statusLabel.setText("正在录音...");

        recordButton.setDisable(true);
        stopButton.setDisable(false);
        openFolderButton.setDisable(true);
        playButton.setDisable(true);

    }

    private void stopRecording( ) {
        handler.stopRecording();
        statusLabel.setText("停止录音");
        recordButton.setDisable(false);
        stopButton.setDisable(true);
        openFolderButton.setDisable(false);
        playButton.setDisable(false);
        new RecentFilesManager(RecentFilesManager.RecentFileType.ADUIO).addRecentFile(outputFile);
        gp.stop();
        timer.stop();
    }


    @Override
    public String getName() {
        return NAME;
    }
}
