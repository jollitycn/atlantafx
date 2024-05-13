package com.jasonhong.fx.main.page.components.progressbar;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;

import java.util.Random;

public class ColoredProgressBar extends ProgressBar {

    private static final Color[] COLORS = {Color.BLUE,Color.PINK, Color.ORANGE,Color.YELLOW, Color.GREEN}; // 示例颜色

    public ColoredProgressBar() {
//        setStyle( "-color-progress-bar-fill: -color-danger-emphasis");
        // 监听进度属性的变化
        progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateColor(newValue.doubleValue());
            }
        });

    }

    private void updateColor(double progress) {
//
//            if (progress < 0) {
//                progress = 0;
//            } else if (progress > 1) {
//                progress = 1;
//            }
//
//// 计算索引并确保不越界
////            int index = (int) Math.ceil(progress * (COLORS.length - 1));
////            index = Math.min(index, COLORS.length - 1);
//int index= new Random().nextInt(COLORS.length - 1);
//// 获取颜色值并转换为Web颜色代码（#RRGGBB 或 #AARRGGBB）
//            String webColorCode = rgbToHexString(COLORS[index], true); // 最后一个参数为true表示包含透明度
////
////            setStyle("-fx-progress-color: " + value);
////            setStyle("-color-progress-bar-fill: " + value);
//            setStyle("-fx-progress-color: " + webColorCode + ";");
    }




    /**
     * 将 JavaFX Color 对象转换为十六进制字符串（#AARRGGBB 格式）
     *
     * @param color 要转换的 Color 对象
     * @return 十六进制字符串
     */
    public static String rgbToHexString(Color color, boolean includeOpacity) {
        int alpha = (int) (color.getOpacity() * 255);
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        if (includeOpacity) {
            return String.format("#%02X%02X%02X%02X", alpha, red, green, blue);
        } else {
            return String.format("#%02X%02X%02X", red, green, blue);
        }
    }


    // 示例用法
    public static void main(String[] args) {
        Color color = Color.web("#FF0000", 0.5); // 半透明的红色
        String hexString = rgbToHexString(color, true); // 包含透明度的十六进制字符串
        System.out.println(hexString); // 输出：#80FF0000
    }
}