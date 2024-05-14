package com.jasonhong.fx.main.demo.subscription.mqtt;

import javafx.application.Application;
import javafx.application.Platform;  
import javafx.scene.Scene;  
import javafx.scene.control.Label;  
import javafx.scene.layout.StackPane;  
import javafx.stage.Stage;  
  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
  
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;  
import org.eclipse.paho.client.mqttv3.MqttCallback;  
import org.eclipse.paho.client.mqttv3.MqttClient;  
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;  
import org.eclipse.paho.client.mqttv3.MqttException;  
import org.eclipse.paho.client.mqttv3.MqttMessage;  
  
public class MqttFxApp extends Application {  
  
    private ExecutorService executorService = Executors.newSingleThreadExecutor();  
    private MqttClient client;  
    private Label messageLabel = new Label("Waiting for messages...");  
  private Stage primaryStage;
    @Override  
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MQTT FX App");  
        StackPane root = new StackPane();  
        root.getChildren().add(messageLabel);  
        Scene scene = new Scene(root, 300, 250);  
  
        primaryStage.setScene(scene);  
        primaryStage.show();  
  
        connectMqttClient();  
    }  
  
    private void connectMqttClient() {  
        executorService.submit(() -> {  
            try {  
                client = new MqttClient("tcp://broker.hivemq.com:1883", "JavaFXClient");  
                client.setCallback(new MqttCallback() {  
                    @Override  
                    public void connectionLost(Throwable cause) {  
                        // 处理连接丢失情况  
                        System.err.println("Connection lost: " + cause.getMessage());  
                    }  
  
                    @Override  
                    public void messageArrived(String topic, MqttMessage message) throws Exception {  
                        // 更新UI  
                        Platform.runLater(() -> {
                            String received = new String(message.getPayload());
                            //TODO:转成中文额
                            messageLabel.setText("Received message: " +received );
                        });  
                    }  
  
                    @Override  
                    public void deliveryComplete(IMqttDeliveryToken token) {  
                        // 处理消息传递完成  
                    }  
                });  
  
                MqttConnectOptions connOpts = new MqttConnectOptions();  
                connOpts.setCleanSession(true);  
                client.connect(connOpts);  
                client.subscribe("com.jollitycn/topic");
  
            } catch (MqttException e) {  
                e.printStackTrace();  
            }  
        });  
  
        // 确保在程序退出时关闭MQTT客户端  
        primaryStage.setOnCloseRequest(event -> {  
            if (client != null && client.isConnected()) {  
                try {  
                    client.disconnect();  
                    client.close();  
                } catch (MqttException e) {  
                    e.printStackTrace();  
                }  
                executorService.shutdown();  
            }  
        });  
    }  
  
    public static void main(String[] args) {  
        launch(args);  
    }  
}