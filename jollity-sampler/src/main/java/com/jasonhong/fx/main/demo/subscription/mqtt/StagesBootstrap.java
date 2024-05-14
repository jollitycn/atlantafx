package com.jasonhong.fx.main.demo.subscription.mqtt;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.jasonhong.fx.main.util.FXUtil.placeStageAtCenter;
import static com.jasonhong.fx.main.util.FXUtil.placeStageAtRight;


public class StagesBootstrap extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 获取屏幕尺寸
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // 创建第一个Stage并设置其位置和大小
        Stage leftStage = new Stage();
        leftStage.setTitle("发布者");
        MqttPublisherApp imageViewerApp = new MqttPublisherApp();
//        imageViewerApp.setBasePath(args[0]);
        imageViewerApp.start(leftStage);    leftStage.show();

        placeStageAtCenter(leftStage);
        // 创建第二个Stage并设置其位置和大小
        Stage rightStage = new Stage();
        MqttFxApp app  = new MqttFxApp();
//        app.setBasePath(args[0]);
        app.start(rightStage);
        rightStage.setTitle("订阅者");
        rightStage.show();

        placeStageAtRight(rightStage);
    }

    private static String[] args = null;
    public static void main(String[] args) {
        StagesBootstrap.args  = args;
        launch(args);
    }
}