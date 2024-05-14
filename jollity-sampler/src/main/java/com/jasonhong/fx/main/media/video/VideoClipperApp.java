package com.jasonhong.fx.main.media.video;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VideoClipperApp extends Application {

    private static final String VIDEO_FILE_PATH = "E:/studio/project/audioPlayer/src/assets/vedios/vedio.mp4"; // 替换为你的视频文件路径
    private static final int FRAME_CAPTURE_INTERVAL = 1000; // 捕获帧的间隔，单位：毫秒
    private static final int FRAME_HZ_MAX = 144; // 捕获帧的间隔，单位：毫秒


    private static final int IMAGE_WIDTH = 640; // 图片宽度  
    private static final int IMAGE_HEIGHT = 480; // 图片高度  

    private Slider progressSlider;
    private MediaPlayer mediaPlayer;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private WritableImage writableImage;
    private ScheduledExecutorService scheduler;
    private MediaView mediaView;

    private final ObservableList<MyWritableImage> images = FXCollections.observableArrayList();

    Label listLabel = new Label("List");
private  long mediaStartTime;
    @Override
    public void start(Stage primaryStage) {
        try {
            // 加载视频文件
            Media media = new Media(new File(VIDEO_FILE_PATH).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            // mediaPlayer.setMute(true); // 如果不需要声音，可以设置为静音
            mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(IMAGE_WIDTH / 3);
            mediaView.setFitHeight(IMAGE_HEIGHT / 3);

            Label mediaViewLabel = new Label("video");
            VBox groupVideo = new VBox(mediaViewLabel,mediaView);
            // 创建可写图片
            writableImage = new WritableImage(IMAGE_WIDTH, IMAGE_HEIGHT);


            // 开始播放视频
//        ChangeListener playingPropertyListener = new ChangeListener<Duration>() {
//            @Override
//            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
//                captureFrame();
//            }
//        };
//

            // 创建进度条
            progressSlider = new Slider(0, media.getDuration().toSeconds(), 0);

            progressSlider.setBlockIncrement(1);
            progressSlider.setMajorTickUnit(1.0);
            progressSlider.setMinorTickCount(1);
            progressSlider.setSnapToTicks(true);
            progressSlider.setShowTickMarks(true);
            progressSlider.setShowTickLabels(true);


            //mediaView.on
            canvas = new Canvas(IMAGE_WIDTH / 3, IMAGE_HEIGHT / 3);

            graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


            // 创建播放控制按钮
            Button playButton = new Button("Play");
            Button pauseButton = new Button("Pause");
            Button stopButton = new Button("Stop");
            Button convertButton = new Button("convert");

            // 为按钮添加事件处理程序
            playButton.setOnAction(event -> {
                if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                    mediaPlayer.play();
                }
            });

            pauseButton.setOnAction(event -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                }
            });

            stopButton.setOnAction(event -> {
                mediaPlayer.stop();
                mediaPlayer.setStartTime(new Duration(0)); // 重置到视频开始位置
            });
            convertButton.setOnAction(event -> {
                try {
                    new ImageToVideoConverter().convert(this.images);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            VBox root = new VBox(10);
            HBox view = new HBox(10); // 10是垂直间距
            // 创建场景并添加组件
//        StackPane root = new StackPane();
            HBox buttonsNode = new HBox();
            buttonsNode.getChildren().addAll(playButton, pauseButton, stopButton,convertButton);


            ListView<MyWritableImage> listView = new ListView<>();
            listView.setItems(images);

            // 创建一个StackPane作为根节点
            VBox listViewNode = new VBox(listLabel,listView);
//            root.getChildren().add(listView); // 将ListView添加到StackPane中
//            HBox listViewNode = new HBox(10,listView); // 10是垂直间距
            // 自定义单元格的渲染方式

            Label canvasLabel = new Label("canvas");
            VBox groupCanvas = new VBox(canvasLabel,canvas);
            view.getChildren().addAll(groupVideo, groupCanvas, listViewNode);
//            listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
////                   MyWritableImage item= listView.getItems().get( listView.getEditingIndex()) ;
////                    MediaPlayer.Status status = mediaPlayer.getStatus();
////                                mediaPlayer.pause();
////                                mediaPlayer.setStartTime(item.getCurrentTime());
////                                if (status == MediaPlayer.Status.PLAYING) {
////                                    mediaPlayer.play();
////                                }
//                }
//            });
            listView.setCellFactory(lv -> new ListCell<MyWritableImage>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(MyWritableImage item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {

                        imageView.setImage(item.getSnapshot());
//                        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                            @Override
//                            public void handle(MouseEvent event) {
//                                MediaPlayer.Status status = mediaPlayer.getStatus();
//                                mediaPlayer.pause();
//                                mediaPlayer.setStartTime(item.getCurrentTime());
//                                if (status == MediaPlayer.Status.PLAYING) {
//                                    mediaPlayer.play();
//                                }
//                            }
//                        });
                        setGraphic(imageView);
                    }
                }
            });


            root.getChildren().addAll(view, buttonsNode);
            Scene scene = new Scene(root, IMAGE_WIDTH, IMAGE_HEIGHT);

            // 设置舞台并显示
            primaryStage.setTitle("Video Clipper and Frame Capturer");
            primaryStage.setScene(scene);
            primaryStage.show();
            mediaPlayer.setOnReady(() -> {   // 创建定时器来捕获视频帧
                scheduler = new ScheduledThreadPoolExecutor(1);


                scheduler.scheduleAtFixedRate(() -> captureFrame(), 0, Integer.valueOf(FRAME_CAPTURE_INTERVAL / FRAME_HZ_MAX), TimeUnit.MILLISECONDS);
                // 创建画布和图形上下文
                // 计算图片的最小边，以此确定正方形的尺寸

                double minSide = Math.min(media.getWidth(), media.getHeight());

                mediaView.getFitWidth();
                  mediaStartTime = System.currentTimeMillis();
            });

            //mediaPlayer.
            // mediaPlayer.onPlayingProperty().addListener(playingPropertyListener);
            mediaPlayer.setAutoPlay(false);
            mediaPlayer.play();
// 监听MediaPlayer的状态变化，当播放完毕时打印信息
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {

                    System.out.println("Media playback has finished.");
                    // 这里你可以执行一些播放完毕后需要的操作
                    if (!mediaPlayer.isAutoPlay()) {
                        mediaPlayer.stop();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureFrame() {
        try {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                long startTime = System.currentTimeMillis();
               // mediaPlayer.pause();
                Platform.runLater(() -> {
                    Duration  time =  mediaPlayer.getCurrentTime();
//                    System.out.println( );
//                    System.out.println(startTime );
                    // 暂停视频以捕获精确的帧
//            if(mediaView.isSmooth()){
                    // 获取当前帧的图像
                    WritableImage snapshot = mediaView.snapshot(null, null);
                    MyWritableImage image = new MyWritableImage(snapshot,time);
                    images.add(image);
                    listLabel.setText("list:"+images.size());
                    //mediaView.
                    // 绘制图像到画布上
                    graphicsContext.drawImage(snapshot, 0, 0);
                    // 将画布内容复制到可写图片中
                    canvas.snapshot(null, writableImage);
                    //}

                    System.out.println("end " + (System.currentTimeMillis() - startTime)  + "mediaTime: "+time + ",fps:"+ (images.size()/time.toSeconds()));
                });

                // 恢复视频播放
               // mediaPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // 关闭定时器和媒体播放器
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}