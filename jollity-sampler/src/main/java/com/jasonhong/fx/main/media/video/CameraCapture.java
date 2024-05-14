package com.jasonhong.fx.main.media.video;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.opencv.core.Core;

public class CameraCapture {  
  
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); } // 加载本地库
  
    public static void main(String[] args) {  
        // 0通常代表默认的摄像头，如果有多个摄像头，可以通过其他数字来指定  
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);   
  
        try {  
            grabber.start(); // 开始捕获  
  
            CanvasFrame canvas = new CanvasFrame("Camera Capture", 1); // 创建一个窗口来显示视频  
            canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);  
            canvas.setCanvasSize(grabber.getImageWidth(), grabber.getImageHeight()); // 设置窗口大小  
  
            while (canvas.isVisible()) {  
                Frame frame = grabber.grab(); // 捕获一帧  
                if (frame != null) {  
                    canvas.showImage(frame); // 在窗口中显示这一帧  
                }  
                Thread.sleep(30); // 稍微暂停一下，以减少CPU占用率  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                grabber.stop(); // 停止捕获并释放资源  
            } catch (FrameGrabber.Exception ex) {  
                ex.printStackTrace();  
            }  
        }  
    }  
}