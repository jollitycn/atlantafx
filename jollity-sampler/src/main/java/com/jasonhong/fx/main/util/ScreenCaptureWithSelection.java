package com.jasonhong.fx.main.util;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScreenCaptureWithSelection extends Application {

    private Stage captureStage;
    private Canvas captureCanvas;
    private Rectangle captureAreaRectangle;
    private Point2D dragStart;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Stage primaryStage;
private ImageView
      imageView;;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // 主Stage的布局和按钮设置
        Button captureButton = new Button("Capture Screen");
        captureButton.setOnAction(e -> showCaptureStage());

        StackPane root = new StackPane(captureButton);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Screen Capture App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void showCaptureStage() {
        if (captureStage == null) {
            // 创建遮罩层Stage和Canvas
            captureStage = new Stage();
            captureCanvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
            captureCanvas.setOnMousePressed(this::handleMousePressed);
            captureCanvas.setOnMouseDragged(this::handleMouseDragged);
            captureCanvas.setOnMouseReleased(this::handleMouseReleased);

            StackPane captureRoot = new StackPane(captureCanvas);
            Scene captureScene = new Scene(captureRoot, Color.TRANSPARENT);

            captureStage.setTitle("Capture Area");
            captureStage.setScene(captureScene);
            captureStage.initStyle(StageStyle.TRANSPARENT);

            // 初始化截图区域矩形（可选，用于可视化）
            captureAreaRectangle = new Rectangle();
            captureAreaRectangle.setFill(Color.TRANSPARENT);
            captureAreaRectangle.setStroke(Color.RED);
            captureAreaRectangle.setStrokeWidth(2);

            // 将截图区域矩形添加到Canvas的GraphicsContext中（可选）
            // GraphicsContext gc = captureCanvas.getGraphicsContext2D();
            // ... 在需要时更新gc以绘制captureAreaRectangle

            // 显示遮罩层Stage
            captureStage.show();
        }
        // 监听鼠标释放事件以进行屏幕捕获
        captureCanvas.setOnMouseReleased(event -> {
            if (dragStart != null) {
                double minX = Math.min(dragStart.getX(), event.getSceneX());
                double minY = Math.min(dragStart.getY(), event.getSceneY());
                double width = Math.abs(event.getSceneX() - dragStart.getX());
                double height = Math.abs(event.getSceneY() - dragStart.getY());

                // 在后台线程中执行屏幕捕获以避免阻塞JavaFX线程
                executorService.submit(() -> {
                    try {
                        // 使用java.awt.Robot类捕获屏幕区域
                        Robot robot = new Robot();
                        java.awt.Rectangle screenRect = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
                        BufferedImage capturedImage = robot.createScreenCapture(
                                new java.awt.Rectangle(
                                        (int) (screenRect.getX() + minX),
                                        (int) (screenRect.getY() + minY),
                                        (int) width,
                                        (int) height
                                )
                        );

                        // 将捕获的图像转换为JavaFX的Image对象并在UI线程中显示
                        Image fxImage = SwingFXUtils.toFXImage(capturedImage, null);
                        Platform.runLater(() -> {
                            // 这里可以添加代码将fxImage显示在某个ImageView或Image区域中
                            // 例如: imageView.setImage(fxImage);
                            // 注意：imageView需要在JavaFX的UI组件树中

                            // 关闭遮罩层Stage
                            captureStage.close();
                            dragStart = null; // 重置dragStart以便进行下一次捕获
                        });
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        // 显示遮罩层Stage
        captureStage.show();
    }

    // ... 其他方法保持不变 ...
    private void handleMousePressed(MouseEvent event) {
        dragStart = new Point2D(event.getSceneX(), event.getSceneY());
    }
    private void handleMouseDragged(MouseEvent event) {
        if (dragStart != null) {
            double minX = Math.min(dragStart.getX(), event.getSceneX());
            double minY = Math.min(dragStart.getY(), event.getSceneY());
            double maxX = Math.max(dragStart.getX(), event.getSceneX());
            double maxY = Math.max(dragStart.getY(), event.getSceneY());

            // 更新截图区域矩形
            captureAreaRectangle.setX(minX);
            captureAreaRectangle.setY(minY);
            captureAreaRectangle.setWidth(maxX - minX);
            captureAreaRectangle.setHeight(maxY - minY);
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (dragStart != null) {
            double minX = Math.min(dragStart.getX(), event.getSceneX());
            double minY = Math.min(dragStart.getY(), event.getSceneY());
            double width = Math.abs(event.getSceneX() - dragStart.getX());
            double height = Math.abs(event.getSceneY() - dragStart.getY());

            // 将屏幕坐标转换为AWT坐标（如果Screen的坐标系统和JavaFX的不一致的话）
            // 这通常涉及屏幕DPI、缩放等因素，但在这里我们假设它们是1:1
            int screenX = (int) minX;
            int screenY = (int) minY;

            executorService.submit(() -> {
                try {
                    // 使用Robot类捕获屏幕截图
                    Robot robot = new Robot();
                    BufferedImage capture = robot.createScreenCapture(new java.awt.Rectangle(screenX, screenY, (int) width, (int) height));

                    // 将BufferedImage转换为JavaFX的Image
                    Image fxImage = SwingFXUtils.toFXImage(capture, null);

                    // 在这里处理截图，例如显示在ImageView中或保存到文件
                    // 在JavaFX UI线程中更新ImageView
                    Platform.runLater(() -> {
                        imageView.setImage(SwingFXUtils.toFXImage(capture, null));
                    });

                    // 清理（可选）
                    dragStart = null; // 重置dragStart以便下一次拖动

                } catch (AWTException e) {
                    e.printStackTrace();
                    // 处理异常
                }
            });
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}