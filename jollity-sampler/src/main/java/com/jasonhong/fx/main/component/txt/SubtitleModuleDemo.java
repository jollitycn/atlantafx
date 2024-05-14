package com.jasonhong.fx.main.component.txt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.nio.file.Path;

public class SubtitleModuleDemo extends Application {
    private String basePath =
            "E:\\studio\\project\\audioPlayer\\data\\books\\eae3219072633303eae86e3\\当你又忙又累，必须人间清醒\\page\\wav";

    @Override
    public void start(Stage primaryStage) {
        // 获取屏幕的高度
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = 50;

        // 字幕模块的高度为屏幕高度的三分之一
        double subtitleWidth = screenWidth / 3;

        // 创建字幕Label
        Label subtitleLabel = new Label("这是半透明的字幕");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        subtitleLabel.setTextFill(Color.web("#ffffff", 0.5)); // 半透明白色
        subtitleLabel.setAlignment(Pos.CENTER);

        // 使用VBox或StackPane包裹Label
        VBox subtitleContainer = new VBox(subtitleLabel);
        subtitleContainer.setAlignment(Pos.CENTER); // 垂直居中
        subtitleContainer.setPadding(new Insets(10)); // 添加内边距
        subtitleContainer.setPrefWidth(subtitleWidth); // 设置高度为屏幕的三分之一

        // 使用BorderPane作为根布局
        BorderPane root = new BorderPane();
        root.setBottom(subtitleContainer); // 将字幕模块放在底部

        // 创建场景并设置到舞台上
        Scene scene = new Scene(root, subtitleWidth, screenHeight); // screenWidth需要根据你的需求设置
        primaryStage.setTitle("字幕模块示例");
        primaryStage.setScene(scene);
        // 显示窗口后，再调整其位置
        primaryStage.show();
    }


    // 注意：这里缺少了screenWidth的定义，你需要在代码中添加或计算它

    public static void main(String[] args) {
        launch(args);
    }

    public void setBasePath(String basePath) {
        this.basePath = String.valueOf(Path.of(basePath,"page","wav"));

    }
}