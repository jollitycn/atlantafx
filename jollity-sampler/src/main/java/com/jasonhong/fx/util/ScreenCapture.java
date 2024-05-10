package com.jasonhong.fx.util;

import java.awt.*;
import java.awt.image.BufferedImage;  
import java.io.File;  
import javax.imageio.ImageIO;  
  
public class ScreenCapture {  
  
    public static void main(String[] args) {  
        try {  
            // 获取屏幕大小  
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
            Rectangle screenRectangle = new Rectangle(screenSize);  
  
            // 创建一个Robot对象  
            Robot robot = new Robot();  
  
            // 使用Robot对象捕获屏幕截图  
            BufferedImage image = robot.createScreenCapture(screenRectangle);  
  
            // 将截图保存为PNG文件  
            File screenshot = new File("screenshot.png");  
            ImageIO.write(image, "png", screenshot);  
  
            System.out.println("Screenshot saved as " + screenshot.getAbsolutePath());  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}