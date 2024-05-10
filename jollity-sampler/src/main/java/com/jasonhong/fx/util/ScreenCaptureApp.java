package com.jasonhong.fx.util;

import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.ScreenCaptureEvent;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;

import java.awt.image.MultiResolutionImage;
import java.io.IOException;

public class ScreenCaptureApp extends Application {

    private Pane selectionPane;
    private Rectangle selectionBox;
    private Rectangle2D screenBounds;
    private Point2D startPoint;
    public ScreenCaptureApp(){

    }
    Pane childPane;
    @Override
    public void start(Stage primaryStage) {
//        screenBounds = Screen.getPrimary().getBounds();
          selectionPane = new Pane();
//        selectionPane.setOpacity(0.1);
        selectionPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 为了可视化效果，我们可以添加一个半透明的矩形作为子节点
        // 注意：这只是一个示例，实际情况下你可能不需要这个子节点
          childPane = new Pane();
//        childPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");?

        childPane.setStyle("-fx-background-color: rgba(0, 0,0, 0.01);");
        childPane.setPrefSize(screenSize.width, screenSize.height);
        selectionPane.getChildren().add(childPane);
        selectionBox = new Rectangle();
//        selectionBox.setFill(Color.color(0, 0, 0, 0));
        selectionBox.setFill(Color.LIGHTBLUE.deriveColor(0, 0, 1, 0.01));
//        selectionBox.setb(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        selectionBox.setStroke(Color.BLUE);
        selectionBox.setStrokeWidth(1);
        selectionBox.setManaged(false);
//        selectionBox.setOnMouseClicked(this::captureScreen);
        selectionPane.getChildren().add(selectionBox);
        selectionPane.setOnMousePressed(this::handleMousePressed);
        selectionPane.setOnMouseDragged(this::handleMouseDragged);
        selectionPane.setOnMouseReleased(this::handleMouseReleased);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene  s=  new Scene( selectionPane);
        s.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (  event.getCode() == KeyCode.ESCAPE) {
                // 按下Alt+A时执行的操作
                System.out.println("ESCAPE is pressed!");

                // 这里可以执行任何你想要的代码，例如打开一个菜单项等
primaryStage.close();
                // 阻止事件进一步传播（可选）
                event.consume();
            }
        });
        s.setFill(Color.TRANSPARENT);
        primaryStage.setScene(s);
        primaryStage.show();

    }

    private void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            startPoint = new Point2D(event.getSceneX(), event.getSceneY());
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (startPoint != null) {
            double minX = Math.min(startPoint.getX(), event.getSceneX());
            double minY = Math.min(startPoint.getY(), event.getSceneY());
            double width = Math.abs(startPoint.getX() - event.getSceneX());
            double height = Math.abs(startPoint.getY() - event.getSceneY());

            if (width > 0 && height > 0) {
                selectionBox.setX(minX);
                selectionBox.setY(minY);
                selectionBox.setWidth(width);
                selectionBox.setHeight(height);
                selectionBox.setVisible(true);
            }
        }
    }


    private void handleMouseReleased(MouseEvent event) {
        if (startPoint != null && selectionBox.isVisible()) {
            // 计算截图区域的坐标和大小
            double minX = Math.min(startPoint.getX(), event.getSceneX());
            double minY = Math.min(startPoint.getY(), event.getSceneY());
            double width = Math.abs(startPoint.getX() - event.getSceneX());
            double height = Math.abs(startPoint.getY() - event.getSceneY());

            // 转换为屏幕坐标（如果Stage有偏移或缩放，这里需要相应调整）
            Rectangle2D screenRect = new Rectangle2D(
                    minX + selectionPane.getScene().getX() + selectionPane.getScene().getWindow().getX(),
                    minY + selectionPane.getScene().getY() + selectionPane.getScene().getWindow().getY(),
                    width, height
            );

            // 使用Java AWT的Robot类来截取屏幕
            BufferedImage capture = null;
            try {
                capture = captureScreenRegion(screenRect);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
//
//            // 将BufferedImage转换为JavaFX的WritableImage（如果需要的话）
//             WritableImage fxImage = SwingFXUtils.toFXImage(capture, null);
//            // 创建一个ImageView来显示截图结果
//            ImageView imageView = new ImageView(fxImage);
//
//            // 设置ImageView的适应方式，比如FitWidth或FitHeight
//            imageView.setFitWidth(screenRect.getWidth()); // 根据需要设置宽度
//            imageView.setFitHeight(screenRect.getHeight()); // 根据需要设置宽度
//            imageView.setPreserveRatio(true); // 保持图像比例
//
//            // 假设你有一个容器（比如一个Pane）来放置这个ImageView
//            // 你可以将imageView添加到你的UI中
//            selectionPane.getChildren().add(imageView);
            // 将BufferedImage复制到剪贴板
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable imageSelection = new BufferedImageSelection(capture);
            clipboard.setContents(imageSelection, null);

            // 隐藏选择框
            selectionBox.setVisible(false);

            // 重置选择框
            startPoint = null;
            DefaultEventBus.getInstance().publish(new ScreenCaptureEvent(ScreenCaptureEvent.Action.CAPTURED, capture));

            // 如果需要，可以在这里更新UI或执行其他操作
            // ...
        }
        try {
            this.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 自定义的Transferable类，用于将BufferedImage传输到剪贴板
    class BufferedImageSelection implements Transferable {

        private BufferedImage image;

        public BufferedImageSelection(BufferedImage image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }

    // 示例：执行截图（注意：JavaFX本身不提供直接截图的功能，需要使用Java AWT或Robot类）
    private BufferedImage captureScreenRegion(Rectangle2D rect) throws AWTException {
        Robot robot = new Robot();
        MultiResolutionImage imageM = robot.createMultiResolutionScreenCapture(
                new java.awt.Rectangle((int) rect.getMinX(), (int) rect.getMinY(),
                        (int) rect.getWidth(), (int) rect.getHeight()));
     Image image=   imageM.getResolutionVariants().getLast();
          return (BufferedImage) image;
    }

    public static void main(String[] args) {
        launch(args);
    }
}