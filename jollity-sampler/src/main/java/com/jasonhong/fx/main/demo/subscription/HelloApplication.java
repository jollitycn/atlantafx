package com.jasonhong.fx.main.demo.subscription;

import com.jasonhong.fx.main.component.FileMenu;
import com.jasonhong.fx.main.component.FileSelectedEvent;
import com.jasonhong.fx.main.demo.subscription.mqtt.AudioSubscription;
import com.jasonhong.media.audio.codec.AudioRecorder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {
    private Slider volumeSlider;
    private MediaPlayer mediaPlayer;
    private Slider progressSlider;
    private Button playPauseButton;
    static String fileDir = "E:/studio/project/audioPlayer/src/assets/audios/"; // 替换为你的音乐文件路径

    String fileStr = fileDir + "music.mp3"; // 替换为你的音乐文件路径
    String musicFile = "file:/" + fileStr; // 替换为你的音乐文件路径
    VolumeControlPopup volumeControlPopup;
    AudioRecorderControlPopup audioRecorderControlPopup;
    MediaView mediaView;
    ImageView imageView;
    private final Object lock = new Object();
    private int sharedVariable;
    ChangeListener<Number> progressValueListener = null;
    ChangeListener<Duration> currentTimePropertyListener = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 创建媒体播放器

        updatePermission();
        initAudio(primaryStage);

    }

    private void initAudio(Stage primaryStage) {
        Platform.runLater(() -> {
            Media media = new Media(musicFile);
            mediaPlayer = new MediaPlayer(media);
            imageView = new ImageView();
//        imageView.setFitWidth(300);
//        imageView.setFitHeight(300);

            mediaView = new MediaView(mediaPlayer);
            //当前时间和总时间的显示
            Label currentTimeLabel = new Label("00:00");
            Label totalTimeLabel = new Label(formatTime(mediaPlayer.getMedia().getDuration().toSeconds()));

            //mediaPlayer.getTotalDuration();
            // 创建进度条
            progressSlider = new Slider(0, media.getDuration().toSeconds(), 0);

            progressSlider.setBlockIncrement(1);
            progressSlider.setMajorTickUnit(1.0);
            progressSlider.setMinorTickCount(1);
            progressSlider.setSnapToTicks(true);
            progressSlider.setShowTickMarks(true);
            progressSlider.setShowTickLabels(true);

            // 添加监听器到onReady属性
            // 设置当媒体准备好时的动作
//        mediaPlayer.setOnReady(new Runnable() {
//            @Override
//            public void run() {
//                // 在这里执行当媒体准备好时要进行的操作
//                System.out.println("Media is ready to play.");
//                if (media.getDuration().toMillis() > 0) {
//                    System.out.println("Media duration: " + media.getDuration().toSeconds() + " seconds");
//                    progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds()); }
//               // mediaPlayer.play(); // 开始播放媒体
//            }
//        });
            // ChangeListener<Number> finalProgressValueListener = progressValueListener;
            currentTimePropertyListener = new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                    // progressSlider.valueProperty().removeListener(progressValueListener);
                    Platform.runLater(() -> {
                        //       synchronized(mediaPlayer) {
                        currentTimeLabel.setText(String.valueOf(newValue.toSeconds()));
                        System.out.println("currentTimePropertyListener" + newValue.toSeconds());
                        progressSlider.setValue(newValue.toSeconds());
                        System.out.println("progressSlider" + progressSlider.getValue());
                        //   progressSlider.valueProperty().addListener(progressValueListener);
                        //  }
                    });
                }
            };
//     //   ChangeListener<Duration> finalCurrentTimePropertyListener = currentTimePropertyListener;
//        progressValueListener = new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//               // mediaPlayer.currentTimeProperty().removeListener(currentTimePropertyListener);

//   Platform.runLater(() -> {
//       synchronized(mediaPlayer) {
//           System.out.println("progressValueListener" + newValue.doubleValue());
//           if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
//               mediaPlayer.pause();
//           }// 暂停以设置新的时间
//           mediaPlayer.setStartTime(Duration.seconds(newValue.doubleValue()));
//           if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
//               mediaPlayer.play();
//           }
//       }
//                  // mediaPlayer.currentTimeProperty().addListener(currentTimePropertyListener);
//                });
//            }
//        } ;
            //mediaPlayer

            mediaPlayer.currentTimeProperty().addListener(currentTimePropertyListener);
//        EventHandler<? super MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//              //  Platform.runLater(() -> {
//                System.out.println(event);
//                //   System.out.println("progressValueListener" + newValue.doubleValue());
//                //   System.out.println("progressSlider"+progressSlider);
//                // 在这里获取Slider的最大x坐标值
//                //  Bounds boundsInParent = progressSlider.localToParent(progressSlider.getBoundsInLocal());
//                /// double maxX = boundsInParent.getMaxX();
//
//                //   System.out.println("Slider的最大x坐标值: " + maxX);
//                //  double rate= (event.getX() *1.0/ progressSlider.getWidth());
//
//                // 计算滑块在轨道上的相对位置（进度）
//                double progress = progressSlider.getValue() / (progressSlider.getMax() - progressSlider.getMin());
//                System.out.println("滑块在轨道上的相对位置（进度）: " + progress);
//                //  System.out.println("rate"+ rate);
//                double v = progress * mediaPlayer.getMedia().getDuration().toSeconds();
//                System.out.println("v" + v);
////                if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
////                    mediaPlayer.pause();
////                }
//                    MediaPlayer.Status orginStatus=  mediaPlayer.getStatus();
//                while (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
//                    mediaPlayer.pause();
//
//                }
//                Duration newTime = Duration.seconds(v);
//                System.out.println("newTime" + newTime);
//                // 暂停以设置新的时间
//                mediaPlayer.setStartTime(newTime);
//                if (orginStatus.equals(MediaPlayer.Status.PLAYING)) {
//                    mediaPlayer.play();
//                }
//               //});
//           }
//        } ;

            progressSlider.setOnMouseClicked(event -> {
                MediaPlayer.Status orginStatus = mediaPlayer.getStatus();
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Play");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 计算滑块在轨道上的相对位置（进度）
                double progress = progressSlider.getValue() / (progressSlider.getMax() - progressSlider.getMin());
                System.out.println("滑块在轨道上的相对位置（进度）: " + progress);
                //  System.out.println("rate"+ rate);
                double v = progress * mediaPlayer.getMedia().getDuration().toSeconds();
                System.out.println("v" + v);
                Duration newTime = Duration.seconds(v);
                System.out.println("newTime" + newTime);
                // 暂停以设置新的时间
                mediaPlayer.setStartTime(newTime);
                if (orginStatus == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.play();
                    playPauseButton.setText("Pause");
                }
            });

            //progressSlider.setOnMouseClicked(eventHandler);
            // 添加监听器以更新MediaPlayer的currentTime当滑块值改变时
            //   progressSlider.valueProperty().addListener(progressValueListener);

//        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
//            Platform.runLater(() -> {
//                progressSlider.setValue(newValue.toMinutes());
//            }); });

// 更新进度条和时间的显示

//        mediaPlayer.onReadyProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (newValue) {
//                    // 媒体已经准备好
//                    if (media.getDuration().toMillis() > 0) {
//                        System.out.println("Media duration: " + media.getDuration().toSeconds() + " seconds");
//                    } else {
//                        System.out.println("Media duration is not available, but media is ready.");
//                    }
//                    // 在这里可以执行其他操作，比如开始播放媒体等
//                    mediaPlayer.play();
//                }
//            }
//        });
//        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            mediaPlayer.setStopTime(new Duration((long) newValue));
//        });
            //progressSlider.valueProperty().bindBidirectional(mediaPlayer.currentTimeProperty());
// 创建一个自定义绑定来同步进度条和MediaPlayer的currentTime
//        DoubleBinding currentTimeBinding = Bindings.createDoubleBinding(() -> {
//            if (mediaPlayer.getCurrentTime().toSeconds() >= 0) {
//                return mediaPlayer.getCurrentTime().toSeconds();
//            } else {
//                return 0.0;
//            }
//        }, mediaPlayer.currentTimeProperty());
// 创建音量滑块并设置其范围
            volumeSlider = new Slider();
            volumeSlider.setMin(0);
            volumeSlider.setMax(1);
            volumeSlider.setValue(0.5); // 初始设置为50%音量
            volumeSlider.setMajorTickUnit(0.1);
            volumeSlider.setMinorTickCount(9);
            volumeSlider.setSnapToTicks(true);
            volumeSlider.setBlockIncrement(0.1);
            // 绑定音量滑块的值到MediaPlayer的volume属性
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                mediaPlayer.setVolume(newValue.doubleValue());
            });
            // progressSlider.valueProperty().bindBidirectional((Property<Number>) currentTimeBinding);


            // 创建播放/暂停按钮
            playPauseButton = new Button("Play");
            playPauseButton.setOnAction(event -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Play");
                } else {
                    mediaPlayer.play();
                    playPauseButton.setText("Pause");
                }
            });
            // 创建音量控制弹出窗口
            volumeControlPopup = new VolumeControlPopup(mediaPlayer);

            // 创建显示音量控制按钮的按钮
            Button showVolumeControlButton = new Button("显示音量控制");
            showVolumeControlButton.setOnAction(event -> {
                if (volumeControlPopup.isShowing()) {
                    volumeControlPopup.hide();
                    showVolumeControlButton.setText("显示音量控制");
                } else {
                    volumeControlPopup.show(primaryStage);
                    showVolumeControlButton.setText("隐藏音量控制");
                }
            });

            audioRecorderControlPopup = new AudioRecorderControlPopup(mediaPlayer);
            // 创建显示音量控制按钮的按钮
            Button showAudioRecorderControlButton = new Button("录制音频");
            showAudioRecorderControlButton.setOnAction(event -> {
                if (audioRecorderControlPopup.isShowing()) {
                    audioRecorderControlPopup.hide();
                    showAudioRecorderControlButton.setText("显示录制音频");
                } else {
                    audioRecorderControlPopup.show(primaryStage);
                    showAudioRecorderControlButton.setText("隐藏录制音频");
                }
            });

            Button fullScreenButton = new Button("Full Screen");
            fullScreenButton.setOnAction(event -> {
                if (primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                    fullScreenButton.setText("Full Screen");
                } else {
                    primaryStage.setFullScreen(true);
                    fullScreenButton.setText("Exit Full Screen");
                }
            });
            // 创建一个菜单栏
            MenuBar menuBar = new MenuBar();


            FileMenu fileMenu = new FileMenu(primaryStage, fileDir);
//        fileMenu.addEventHandler(F);
            // 将文件菜单添加到菜单栏中
            fileMenu.addEventHandler(FileSelectedEvent.FILE_SELECTED, event -> {
                File selectedFile = event.getSelectedFile();
                musicFile = selectedFile.toURI().toString();
                // 处理文件...
                initAudio(primaryStage);
                System.out.println("File selected: " + selectedFile.getPath());
            });

            menuBar.getMenus().add(fileMenu);
//        root.getChildren().add(menuBar);
            BorderPane top = new BorderPane();
            top.setTop(menuBar); // 将MenuBar设置为BorderPane的顶部内容
            AudioSubscription as  = new AudioSubscription();
            FlowPane fp = new FlowPane();
            fp.getChildren().addAll(playPauseButton, showVolumeControlButton, showAudioRecorderControlButton, fullScreenButton,as);

            // 设置界面布局
            VBox main = new VBox(  imageView, mediaView, progressSlider,currentTimeLabel,fp );
            // 将标签添加到布局中
            main.setPadding(new Insets(10));
            main.setAlignment(Pos.CENTER);
//            main.getChildren().addAll(currentTimeLabel);
             VBox root = new VBox(10, top,main  );
            // 将标签添加到布局中
            root.getChildren().addAll(currentTimeLabel);

            Scene scene = new Scene(root, 400, 300);
//            URL url= getClass().getResource("/css/style.css");
//            scene.getStylesheets().add(url.toExternalForm());


            mediaPlayer.setOnReady(() -> {
                progressSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
                Image image = (Image) mediaPlayer.getMedia().getMetadata().get("image");

                imageView.setImage(image);
                if (image != null) {
                    // 计算图片的最小边，以此确定正方形的尺寸
                    double minSide = Math.min(image.getWidth(), image.getHeight());
                    // 设置viewport来裁剪图片为正方形
                    imageView.setViewport(new Rectangle2D(
                            (image.getWidth() - minSide) / 2, // x坐标，使正方形居中
                            (image.getHeight() - minSide) / 2, // y坐标，使正方形居中
                            minSide, // 宽度
                            minSide // 高度
                    ));
                }
                // 绑定ImageView的fitWidth和fitHeight到场景的宽高，保持图片的纵横比

            });
            imageView.fitWidthProperty().bind(scene.widthProperty().multiply(0.33)); // 假设图片占据场景宽度的80%
            imageView.fitHeightProperty().bind(scene.heightProperty().multiply(0.33)); // 假设图片占据场景高度的80%
            //设置保持图片纵横比
            imageView.setPreserveRatio(true);
            primaryStage.setTitle("Simple Music Player");
            primaryStage.setScene(scene);
            // 设置Stage为无边框样式
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//
//        // 创建一个简单的关闭按钮
//        Button closeButton = new Button("Close");
//        closeButton.setOnAction(event -> primaryStage.close());
//
//        // 创建一个StackPane作为根节点，并设置其布局和填充
//        StackPane closeButtonPa = new StackPane();
//        closeButtonPa.setAlignment(Pos.TOP_RIGHT);
//        closeButtonPa.setPadding(new Insets(10));
//        closeButtonPa.getChildren().add(closeButton);
//        root.getChildren().add(closeButtonPa);


            primaryStage.show();


//        // 当窗口大小变化时，重新计算viewport以保持正方形
//        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
//            double newFitWidth = imageView.getFitWidth();
//            double squareSide = newFitWidth; // 正方形的边长等于ImageView的fitWidth
//            double x = (imageView.getImage().getWidth() - squareSide) / 2;
//            double y = (imageView.getImage().getHeight() - squareSide) / 2;
//            imageView.setViewport(new javafx.geometry.Rectangle2D(x, y, squareSide, squareSide));
//        });

            primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
                // 注意：通常不需要在高度变化时调整viewport，因为宽度已经绑定了，并且保持了纵横比
                // 但如果你有特殊需求，可以在这里添加额外的逻辑
            });
        });
    }

    private static void startUpdateProcess() {
        // 使用ScheduledExecutorService来定期执行更新任务
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 在这里执行更新操作
                updateResource();
                System.out.println("资源已更新，当前时间：" + Instant.now());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, UPDATE_INTERVAL, TimeUnit.SECONDS);
    }

    private static void updateResource() {
        // 在这里编写更新资源的逻辑
        // 例如，从数据库、文件或网络获取新数据，并更新本地缓存或状态
        System.out.println("执行更新操作...");
    }

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long UPDATE_INTERVAL = 5; // 更新间隔，单位：秒

    private void updatePermission() {
        Path filePath = Paths.get(fileStr);
        try {
            // 获取当前文件的权限
            Set<PosixFilePermission> currentPermissions = Files.getPosixFilePermissions(filePath);
            System.out.println("Current permissions: " + PosixFilePermissions.toString(currentPermissions));

            // 创建一个新的权限集合，添加或删除所需的权限
            Set<PosixFilePermission> newPermissions = new HashSet<>(currentPermissions);
            newPermissions.add(PosixFilePermission.OWNER_READ); // 添加所有者读权限
            newPermissions.add(PosixFilePermission.OWNER_WRITE); // 添加所有者写权限
            newPermissions.add(PosixFilePermission.OWNER_EXECUTE); // 添加所有者执行权限
            newPermissions.remove(PosixFilePermission.GROUP_WRITE); // 移除组写权限
            newPermissions.remove(PosixFilePermission.OTHERS_WRITE); // 移除其他用户写权限

            // 应用新的权限
            Files.setPosixFilePermissions(filePath, newPermissions);

            // 验证新的权限是否已设置
            Set<PosixFilePermission> verifiedPermissions = Files.getPosixFilePermissions(filePath);
            System.out.println("New permissions: " + PosixFilePermissions.toString(verifiedPermissions));
        } catch (UnsupportedOperationException e) {
            System.err.println("Your file system does not support POSIX file permissions.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch( args);
    }

    // 自定义的音量控制弹出窗口类
    private static class AudioRecorderControlPopup {
        private final Stage stage;
        private AudioRecorder recorder;
        private boolean isRecording = false;
        private Timeline timer;
        private Label timerLabel;

        public AudioRecorderControlPopup(MediaPlayer mediaPlayer) {
            stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("录制音频");
// 创建录音器实例

            // 创建计时器标签
            timerLabel = new Label("00:00");
            timerLabel.setPadding(new javafx.geometry.Insets(10));
// 创建计时器
            timer = new Timeline(
                    new KeyFrame(Duration.millis(1000), event -> {
                        int seconds = (int) (timer.getCurrentTime().toSeconds());
                        int milliseconds = (int) ((timer.getCurrentTime().toMillis() % 1000));
                        timerLabel.setText(String.format("%02d:%03d", seconds, milliseconds));
                    })
            );
            timer.setCycleCount(Timeline.INDEFINITE);
            //timer.play();
            // 创建按钮并添加点击事件监听器
            Button recordButton = new Button("开始录音");
            Button stopButton = new Button("停止录音");
            //String outputFile =fileDir+System.currentTimeMillis() +"output.wav";
            recordButton.setOnAction(event -> {
                recorder = new AudioRecorder();
                if (!isRecording) {
                    isRecording = true;
                    recorder.startRecording();
                    Platform.runLater(() -> {
                        timer.play();
                    });
                }
            });

            stopButton.setOnAction(event -> {
                if (isRecording) {
                    // 停止录音并更新UI
                    Platform.runLater(() -> {
                        isRecording = false;
                        // 这里可以添加代码来等待录音结束，比如通过用户输入或计时器
                        //recorder.save(outputFile);
                        timer.stop();
                    });
                    recorder.stopRecording();
                    try {
                        recorder.saveAsMP3(fileDir, System.currentTimeMillis() + "output.mp3");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (EncoderException e) {
                        throw new RuntimeException(e);
                    }
                }


            });
            VBox root = new VBox(10, recordButton, stopButton);
            root.getChildren().addAll(timerLabel);
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root, 150, 100);
            stage.setScene(scene);
        }

        public void show(Stage primaryStage) {
            stage.show();
            stage.toFront();
        }

        public void hide() {
            stage.hide();
        }

        public boolean isShowing() {
            return stage.isShowing();
        }
    }

    // 自定义的音量控制弹出窗口类
    private static class VolumeControlPopup {
        private final Stage stage;
        private final Slider volumeSlider;

        public VolumeControlPopup(MediaPlayer mediaPlayer) {
            stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("音量控制");

            volumeSlider = new Slider();
            volumeSlider.setMin(0);
            volumeSlider.setMax(1);
            volumeSlider.setValue(mediaPlayer.getVolume());

            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                mediaPlayer.setVolume(newValue.doubleValue());
            });

            VBox root = new VBox(10, volumeSlider);
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root, 150, 100);
            stage.setScene(scene);
        }

        public void show(Stage primaryStage) {
            stage.show();
            stage.toFront();
        }

        public void hide() {
            stage.hide();
        }

        public boolean isShowing() {
            return stage.isShowing();
        }
    }

    // 格式化时间的辅助方法
    private static String formatTime(double seconds) {
        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}