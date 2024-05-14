package com.jasonhong.fx.main.component.smart.smartHome;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClockController {
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label tempHumidityLabel; // 假设温湿度数据来自某个外部源，这里只是模拟数据

        @FXML
        void init() {
            updateClock();
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    updateClock();
                }
            }.start();
        }

        private void updateClock() {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            dateLabel.setText("Date: " + now.format(dateFormatter));
            timeLabel.setText("Time: " + now.format(timeFormatter));

            // 假设的温湿度数据，实际应用中需要从外部源获取
            String tempHumidity = "Temp: 25°C / Humidity: 50%";
            tempHumidityLabel.setText("Temp/Humidity: " + tempHumidity);
        }
    }