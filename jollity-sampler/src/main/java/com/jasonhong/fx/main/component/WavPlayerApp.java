package com.jasonhong.fx.main.component;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WavPlayerApp extends Application {

    private String basePath =
            "E:\\studio\\project\\audioPlayer\\data\\books\\eae3219072633303eae86e3\\当你又忙又累，必须人间清醒\\page\\wav";

    private ListView<String> listView;
    private Label statusLabel;
    private int currentIndex = 0;
    private List<Path> wavFiles = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        statusLabel = new Label("Loading...");
        listView = new ListView<>();

        VBox root = new VBox(10, listView, statusLabel);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("WAV Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadWavsInDirectory(getBasePath()); // 替换为你的目录路径
        ObservableList<String> fileNames = FXCollections.observableArrayList(
                wavFiles.stream()
                        .map(Path::getFileName)
                        .map(Path::toString) // 或者使用 getName() 方法来获取不带路径的文件名
                        .collect(Collectors.toList())
        );
        listView.setItems(FXCollections.observableArrayList(fileNames));

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> playCurrentAudio());
        root.getChildren().add(playButton);

        playCurrentAudio(); // 也可以从列表中选择后播放
    }





    private Clip currentClip;

    private void stopCurrentClip() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
        currentClip = null; // 清除引用
    }

    private void playWav(Path wavFile) {
        Platform.runLater(() -> {
        stopCurrentClip(); // 停止当前播放的音频

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile.toFile());
            currentClip = AudioSystem.getClip();
            currentClip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        Platform.runLater(() -> statusLabel.setText("Playing finished: " + wavFile.getFileName()));
                        // 如果需要自动播放下一个文件，可以在这里调用
                        playCurrentAudio();
                    }
                }
            });
            currentClip.open(audioStream);

            // 播放音频时，选中ListView中的对应项
                        listView.getSelectionModel().select(wavFiles.indexOf(wavFile));

            // 可选：使用FadeTransition来高亮显示当前项（这里只是一个示例）
            Node selectedCell = listView.lookup(".cell:selected");
            if (selectedCell != null) {
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), selectedCell);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(1.5); // 或者其他你想要的高亮值
                fadeTransition.setAutoReverse(true);
                fadeTransition.setCycleCount(2); // 高亮两次，然后恢复
                fadeTransition.play();
            }

            currentClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
     // 滚动到选中的列表项
    scrollToSelectedItem(listView);
        });
}

private void scrollToSelectedItem(ListView<?> listView) {
    int index = listView.getSelectionModel().getSelectedIndex();
    if (index != -1) {
        listView.scrollTo(index);

        // 如果ListView的内容太多，无法一次性滚动到顶部，可以通过设置滚动条位置来实现
        ScrollBar verticalScrollBar = (ScrollBar) listView.lookup(".scroll-bar:vertical");
        if (verticalScrollBar != null) {
            double max = verticalScrollBar.getMax();
            double min = verticalScrollBar.getMin();
            double vvalue = (max - min) * index / listView.getItems().size() + min;
            verticalScrollBar.setValue(vvalue);
        }
    }
}


private void loadWavsInDirectory(String directoryPath) {
            Path directory = Paths.get(directoryPath);
            try {
                Files.walk(directory)
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".wav"))
                        .forEach(wavFiles::add);

                statusLabel.setText("Loading finished. Files loaded.");
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Error loading files");
            }
        }

        private void playCurrentAudio() {
            if (currentIndex < wavFiles.size()) {
                Path wavFile = wavFiles.get(currentIndex);
                playWav(wavFile);
                currentIndex++;
            } else {
                statusLabel.setText("Playback finished.");
                currentIndex = 0; // 如果需要循环播放，可以重置索引
            }
        }


        public static void main(String[] args) {
            launch(args);
        }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = String.valueOf(Path.of(basePath,"page","wav"));

    }
}