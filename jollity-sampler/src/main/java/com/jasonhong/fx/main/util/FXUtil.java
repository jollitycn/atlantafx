package com.jasonhong.fx.main.util;

import atlantafx.base.theme.Styles;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FXUtil {
    public  static  void placeStageAtBottom(Stage stage) {
        // 获取主屏幕
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // 计算窗口的x和y坐标，使窗口的底部与屏幕的底部对齐
        double x = (bounds.getMaxX() - stage.getWidth()) / 2; // 居中显示
        double y = bounds.getMinY() + bounds.getHeight() - stage.getHeight(); // 底部对齐

        // 将窗口移动到指定位置
        stage.setX(x);
        stage.setY(y);
    }

    public static void placeStageAtCenter(Stage stage) { // 获取主屏幕
        // 获取主屏幕
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // 计算窗口的x和y坐标，使窗口居中显示
        double x = (bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) - stage.getWidth()) / 2;
        double y = (bounds.getMinY() + (bounds.getMaxY() - bounds.getMinY()) - stage.getHeight()) / 2;

        // 将窗口移动到指定位置
        stage.setX(x);
        stage.setY(y);

    }

    public static void placeStageAtRight(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // 计算窗口的x和y坐标，使窗口的底部与屏幕的底部对齐
        double x = bounds.getMaxX() - stage.getWidth();
        double y = (bounds.getMinY() + stage.getHeight())/ 2; // 底部对齐

        // 将窗口移动到指定位置
        stage.setX(x);
        stage.setY(y);
    }
    public static Button iconButton(Ikon icon) {
        var btn = new Button(null);
        if (icon != null) {
            btn.setGraphic(new FontIcon(icon));
        }
        btn.getStyleClass().addAll(Styles.BUTTON_ICON);

        return btn;
    }
    public static void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null); // 可以设置标题上方的描述性文本，如果不需要则为null
        alert.setContentText(errorMessage); // 设置对话框的内容文本

        // 可选：显示一个异常信息（如果有的话）
        // Throwable exception = ...; // 获取你的异常对象
        // alert.getDialogPane().setExpandableContent(new StackPane(new TextArea(exception.toString())));

        // 显示对话框并等待用户关闭它
        alert.showAndWait();
    }

    public static Image loadImageFromFile(File file) {
        try {
            return new Image(file.toURI().toString());
        } catch (IllegalArgumentException e) {
            // 文件可能不存在或无法读取，处理异常
            e.printStackTrace();
            return null;
        }
    }

    // 如果你需要BufferedImage（例如，用于更复杂的图像处理），可以这样返回：
    public static BufferedImage loadBufferedImageFromFile(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            // 文件可能不存在或无法读取，处理异常
            e.printStackTrace();
            return null;
        }
    }

    public static Optional<?> show(Dialog<?> alert, Scene scene) {
        // copy customized styles, like changed accent color etc
        try {
            for (var pc : scene.getRoot().getPseudoClassStates()) {
                alert.getDialogPane().pseudoClassStateChanged(pc, true);
            }
            alert.getDialogPane().getStylesheets().addAll(scene.getRoot().getStylesheets());
        } catch (Exception ignored) {
            // yes, ignored
        }

        return alert.showAndWait();
    }
}
