package com.jasonhong.fx.main.demo.subscription.mqtt;

import javafx.application.Application;
import javafx.geometry.Insets;  
import javafx.geometry.Pos;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.scene.control.Label;  
import javafx.scene.control.TextField;  
import javafx.scene.layout.GridPane;  
import javafx.stage.Stage;  
import org.eclipse.paho.client.mqttv3.MqttClient;  
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;  
import org.eclipse.paho.client.mqttv3.MqttException;  
import org.eclipse.paho.client.mqttv3.MqttMessage;  
  
public class MqttPublisherApp extends Application {

    private TextField brokerUrlField;
    private TextField clientIdField;
    private TextField topicField;
    private TextField messageField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // 创建UI组件  
        brokerUrlField = new TextField("tcp://broker.hivemq.com:1883");
        clientIdField = new TextField("JavaFXPublisher");
        topicField = new TextField("com.jollitycn/topic");
        messageField = new TextField("Hello MQTT!");
        Button publishButton = new Button("Publish");
        statusLabel = new Label("Ready to publish");

        // 设置按钮点击事件处理器  
        publishButton.setOnAction(event -> {
            String brokerUrl = brokerUrlField.getText();
            String clientId = clientIdField.getText();
            String topic = topicField.getText();
            String message = messageField.getText();

            if (!brokerUrl.isEmpty() && !clientId.isEmpty() && !topic.isEmpty() && !message.isEmpty()) {
                publishMessage(brokerUrl, clientId, topic, message);
                statusLabel.setText("Message published!");
            } else {
                statusLabel.setText("Please fill in all required fields!");
            }
        });

        // 布局组件  
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(new Label("Broker URL:"), 0, 0);
        gridPane.add(brokerUrlField, 1, 0);

        gridPane.add(new Label("Client ID:"), 0, 1);
        gridPane.add(clientIdField, 1, 1);

        gridPane.add(new Label("Topic:"), 0, 2);
        gridPane.add(topicField, 1, 2);

        gridPane.add(new Label("Message:"), 0, 3);
        gridPane.add(messageField, 1, 3);

        gridPane.add(publishButton, 1, 4);
        gridPane.add(statusLabel, 1, 5);

        gridPane.setAlignment(Pos.CENTER_RIGHT);

        // 创建并显示舞台  
        Scene scene = new Scene(gridPane, 400, 250);
        primaryStage.setTitle("MQTT Publisher with JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void publishMessage(String brokerUrl, String clientId, String topic, String message) {
        try {
            // 创建MQTT客户端  
            MqttClient client = new MqttClient(brokerUrl, clientId);

            // 设置连接选项（可选）  
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // 连接到MQTT代理  
            client.connect(connOpts);

            // 创建一个MQTT消息对象  
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());

            // 发布消息到指定主题  
            client.publish(topic, mqttMessage);

            // 断开连接（可选）  
            client.disconnect();
            client.close();

        } catch (MqttException e) {
            e.printStackTrace();
            statusLabel.setText("Error publishing message: " + e.getMessage());
        }
    }
}