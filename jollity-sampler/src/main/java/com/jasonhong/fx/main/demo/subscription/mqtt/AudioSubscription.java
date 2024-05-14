package com.jasonhong.fx.main.demo.subscription.mqtt;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioSubscription extends Pane {

    private final AtomicInteger audioCount = new AtomicInteger(0);
    //private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean isSubscribed = false;
    private List<File> files;
    AudioConsumer ac;
    Button subscribeButton ;
    //Label audioLabel = new Label("Received Audio: 0");
    public  AudioSubscription() {

        subscribeButton=    new Button("订阅");
        subscribeButton.setOnAction(event -> {
            if (!isSubscribed) {
                try {
                    startSubscription();
                } catch (IOException e) {
                    isSubscribed = false;
                    subscribeButton.setText("订阅失败");
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    isSubscribed = false;
                    subscribeButton.setText("订阅失败");
                    throw new RuntimeException(e);
                }

                isSubscribed = true;
                subscribeButton.setText("订阅:0");
            } else {
                stopSubscription();
                isSubscribed = false;
                subscribeButton.setText("订阅");
            }
        });

//        VBox root = new VBox(10, audioLabel, subscribeButton);
//        Scene scene = new Scene(root, 300, 200);
//
//        primaryStage.setTitle("Audio Subscription Example");
//        primaryStage.setScene(scene);
//        primaryStage.show();
        this.getChildren().addAll(  subscribeButton);
    }

    private void startSubscription() throws IOException, TimeoutException {
         if(ac!=null) {
             ac.stopConsuming();
         }
         ac = new AudioConsumer(this);
        ac.startConsuming();
        this.addEventHandler(AudioMessageEvent.RECEIVED, event -> {
            if(files==null) {
               files = new ArrayList<>();
            }
            files.add(event.getFile());
            int newCount = audioCount.incrementAndGet();
            Platform.runLater(() -> {
                // Update UI
                //Label audioLabel = (Label) this.getScene().lookup("#audioLabel");
                subscribeButton.setText("订阅:"+newCount);
        });
        //executor.scheduleAtFixedRate(() -> {


                // Show notification (optional)  
                // showNotification("New Audio Received");  
            });
      //  }, 1, 1, TimeUnit.SECONDS); // Simulate receiving audio every second
    }

    private void stopSubscription() {
        //executor.shutdown();
    }

    // Optional method to show a notification  
    private void showNotification(String message) {
        // Implement your notification logic here
        // You can use JavaFX's Notifications or a custom PopupWindow
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}