package com.jasonhong.fx.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FXUtil {

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
}
