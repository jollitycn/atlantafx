package com.jasonhong.fx.main.component;

import com.jasonhong.fx.main.component.txt.SubtitleModuleDemo;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.jasonhong.fx.main.util.FXUtil.*;

public class StagesExample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 获取屏幕尺寸
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // 创建第一个Stage并设置其位置和大小
        Stage leftStage = new Stage();
        leftStage.setTitle("文本阅读器");
        ImageViewerApp imageViewerApp = new ImageViewerApp();
        imageViewerApp.setBasePath(args[0]);
        imageViewerApp.start(leftStage);
//        leftStage.setX(screenBounds.getMinX() + (screenWidth - 200) / 4); // 左侧放置，距离边缘有一定间隔
//        leftStage.setY(screenBounds.getMinY() + (screenHeight - screenHeight / 2) / 2); // 垂直居中
        leftStage.show();

         placeStageAtCenter(leftStage);
        // 创建第二个Stage并设置其位置和大小
        Stage rightStage = new Stage();
        WavPlayerApp app  = new WavPlayerApp();
        app.setBasePath(args[0]);
        app.start(rightStage);
        rightStage.setTitle("文本语音");
//        rightStage.setX(screenBounds.getMinX() + 3 * (screenWidth - 200) / 4); // 右侧放置，距离边缘有一定间隔
//        rightStage.setY(screenBounds.getMinY() + (screenHeight - screenHeight / 2) / 2); // 垂直居中
        rightStage.show();

        placeStageAtRight(rightStage);
        Stage stage = new Stage();
        SubtitleModuleDemo app3  = new SubtitleModuleDemo();

        app3.setBasePath(args[0]);
        app3.start(stage);
        stage.setTitle("字幕");
        stage.show();
        placeStageAtBottom(stage);
//        Stage clockStage = new Stage();
//        ClockApp clockApp  = new ClockApp();
//        clockApp.start(rightStage);
//        clockStage.setTitle("时钟");
//        Screen screen = Screen.getPrimary();
//r
//        // 获取屏幕的边界（包括任务栏等）
//        Rectangle2D bounds = screen.getBounds();
//
//        // 计算Stage的x和y坐标，使其位于右下角
//        double x = bounds.getMaxX() - clockStage.getWidth();
//        double y = bounds.getMaxY() - clockStage.getHeight();
//
//        // 如果存在任务栏或其他可能覆盖屏幕底部的内容，你可能需要进一步调整y坐标
//        // 这里为了示例没有这么做
//
//        // 设置Stage的位置和场景
//        clockStage.setX(x);
//        clockStage.setY(y);
//        clockStage.setX(screenBounds.getMinX() + 3 * (screenWidth - 200) / 4); // 右侧放置，距离边缘有一定间隔
//        clockStage.setY(screenBounds.getMinY() + (screenHeight - screenHeight / 2) / 2); // 垂直居中
//        clockStage.show();

//        primaryStage.show();
        // 注意：primaryStage在这个例子中没有被使用，但你可以根据需要显示它
    }

    private static String[] args = null;
    public static void main(String[] args) {
        StagesExample.args  = args;
        launch(args);
    }
}