package com.jasonhong.fx.main.component;//package com.jasonhong.audioplayer.component;
//
//import com.jasonhong.core.stt.OfflineSpeechRecognition;
//import com.jasonhong.core.tts.MaryTTSUtil;
//import edu.cmu.sphinx.api.Configuration;
//import edu.cmu.sphinx.api.StreamSpeechRecognizer;
//import edu.cmu.sphinx.api.SpeechResult;
//import edu.cmu.sphinx.result.Result;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.concurrent.Service;
//import javafx.concurrent.Task;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.control.TextArea;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//
//public class OfflineSpeechRecognitionView extends Application {
//    private Timeline timer;
//    private Timeline timeline;
//    private TextArea textArea;
//    private Button voiceButton;
//    StreamSpeechRecognizer recognizer;
//
//    private ScheduledExecutorService scheduler;
//    private ProgressIndicator progressIndicator;
//    private Service<Void> audioProcessingService;
//    private Service<Void> audioService;
//    int time = 0;
//    boolean isGetResult = false;
//
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        OfflineSpeechRecognition.main(null);      // 创建配置对象
//        Configuration configuration = new Configuration();
//
//        // 设置模型路径和资源
//        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/zh-cn/zh_cn.cd_cont_5000");
//        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/zh-cn/zh_cn.dic");
//        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/zh-cn/zh_cn.lm.bin");
//
//
//        // 设置识别器
//        configuration.setUseGrammar(false);
//        recognizer = new StreamSpeechRecognizer(configuration);
//
//        progressIndicator = new ProgressIndicator();
//        progressIndicator.setVisible(true); // 显示加载指示器
//        progressIndicator.setProgress(0); // 重置进度
////        progressIndicator.set
//        // 创建并配置音频处理服务
//        audioProcessingService = new Service<Void>() {
//            @Override
//            protected Task<Void> createTask() {
//                return new Task<Void>() {
//                    @Override
//                    protected Void call() throws Exception {
//                        // 这里是模拟的长时间运行任务，你需要替换为实际的音频处理逻辑
//                        processAudioData();
//                        return null;
//                    }
//
//
//                };
//            }
//        };
//
//        // 设置服务完成时的处理逻辑
//        audioProcessingService.setOnSucceeded(event -> {
//            voiceButton.setText("开始录音");
//            voiceButton.setDisable(false);
//            progressIndicator.setVisible(false); // 隐藏加载指示器
//        });
//
//        // 启动服务
//        audioProcessingService.start();
//        isGetResult = false;
//        textArea = new TextArea();
//        textArea.setText("Hello, world!");
////        String textToSpeak = "Hello, world!";
//        voiceButton = new Button("语音按钮");
//        voiceButton.setOnMousePressed(event -> startRecording());
//        voiceButton.setOnMouseReleased(event -> stopRecording());
//
//        voiceButton.setDisable(true); // 禁用按钮，防止重复点击
//        VBox root = new VBox(10, textArea, voiceButton,progressIndicator);
//        Scene scene = new Scene(root, 400, 300);
//        root.setAlignment(Pos.CENTER);
//        root.setPadding(new Insets(10));
//        primaryStage.setTitle("Text to Speech");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        startLoading();
//    }
//
//    private void stopRecording() {
//        StringBuilder dot = new StringBuilder();
//        for (int i = -1; i < time; i++) {
//            dot.append(".");
//        }
//
//
//        Platform.runLater(() -> {
//            while (!isGetResult) {
//                voiceButton.setDisable(true);
//                voiceButton.setText(dot.toString());
//                time++;
//            }
//        });
//
//        // 更改按钮文本并启动定时器等待处理结束
//        voiceButton.setText("处理中...");
//
//
//
//        audioService = new Service<Void>() {
//            @Override
//            protected Task<Void> createTask() {
//                return new Task<Void>() {
//                    @Override
//                    protected Void call() throws Exception {
////                         这里是模拟的长时间运行任务，你需要替换为实际的音频处理逻辑
////                         处理音频数据（这里只是示例，你可以添加自己的处理逻辑）
//                        SpeechResult result;
//                        if ((result = recognizer.getResult()) != null) {
////                            Platform.runLater(() -> {
//                                textArea.setText(result.getHypothesis());
//                                isGetResult = true;
//                                time = 0;
//                                // 处理结束后重置按钮状态
//                                voiceButton.setText("开始录音");
//                                voiceButton.setDisable(false);
////                            });
//                        }
//
//                       // recognizer.stopMic();
//
//                        return null;
//                    }
//
//
//                };
//            }
//   };
//
//        // 设置服务完成时的处理逻辑
//        audioService.setOnSucceeded(event -> {
//            voiceButton.setText("开始录音");
//            voiceButton.setDisable(false);
//            progressIndicator.setVisible(false); // 隐藏加载指示器
//        });
//
//        // 启动服务
//        audioService.start();
//    }
//
//
//    private void resetLoading() {
//        // 使用ScheduledExecutorService来安排重置操作
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.schedule(() -> {
//            progressIndicator.setProgress(0);
//            timeline.play();
//        }, 5, TimeUnit.SECONDS);
//    }
//
//    private void startRecording() {
//
//        //recognizer.startMic();
//        System.out.println("start recording");
//        isGetResult=false;
//    }
//
//    private void processAudioData() { // 模拟处理完成，更新进度
//        recognizer.startRecognition(InputStream.nullInputStream());
////        recognizer.stopRecognition();
//        updateProgress(1, 1);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    private void updateProgress(double workDone, double totalWork) {
//        if (progressIndicator != null) {
//            progressIndicator.setProgress(totalWork);
//            //progressIndicator.getValue(). (workDone, totalWork);
//        }
//    }
//
//    private void startLoading() {
//        progressIndicator.setVisible(true);
//
//        // 创建Timeline动画来控制进度
//        timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
//            double progress = progressIndicator.getProgress();
//            if (progress < 1 && (progress+0.1<1)) {
//                progressIndicator.setProgress(progress + 0.1);
//            } else {
//                timeline.stop();
//                resetLoading();
//            }
//        }));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.setAutoReverse(false);
//        timeline.play();
//    }
//
//
//
//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        if (timeline != null) {
//            timeline.stop();
//        }
//        if (scheduler != null) {
//            scheduler.shutdown();
//        }
//
//        recognizer.stopRecognition();
//    }
//}