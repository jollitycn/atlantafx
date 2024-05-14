package com.jasonhong.fx.main.media.video;

import javafx.collections.ObservableList;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import org.bytedeco.javacv.*;
import org.bytedeco.ffmpeg.global.avcodec ;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImageToVideoConverter {  
    public static void convert(ObservableList<MyWritableImage> images) throws Exception {
//        // 图片列表，按顺序排列
//        List<String> imagePaths = new ArrayList<>();
//        // 假设你的图片在images文件夹下，且按名称排序（如image001.jpg, image002.jpg, ...）
//        File imagesDir = new File("images");
//        File[] imageFiles = imagesDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
//        for (File file : imageFiles) {
//            imagePaths.add(file.getAbsolutePath());
//        }

        // 视频输出文件路径  
        String outputFilePath = "output.mp4";

        // 视频的宽度和高度（应与图片的尺寸一致）  
        int frameWidth = 640;
        int frameHeight = 480;

        // 视频的帧率  
        int frameRate = 30;

        // 创建FFmpegFrameRecorder  
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath, frameWidth, frameHeight);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 设置视频编解码器
        recorder.setFormat("mp4"); // 设置输出格式  
        recorder.setFrameRate(frameRate); // 设置帧率  
        recorder.setGopSize(frameRate); // 设置关键帧间隔
        recorder.start(); // 开始录制  

        // 读取图片并转换为Frame  
        for (MyWritableImage image : images) {
            OpenCVFrameConverter.ToMat toMat = new OpenCVFrameConverter.ToMat();
            // 创建一个WritableImage用于像素操作

            ByteBuffer  bufferedImage =  getBuffer(image.getSnapshot());

            Mat mat = new Mat((int) image.getSnapshot().getHeight(), (int) image.getSnapshot().getWidth(), CvType.CV_8UC3);
          //  bufferedImage.as
          //  byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, bufferedImage.array());

            Frame frame = toMat.convert(mat);

            recorder.record(frame); // 录制Frame到视频  
        }

        recorder.stop(); // 停止录制并释放资源  
        recorder.release(); // 释放资源  
    }

    private static ByteBuffer getBuffer(WritableImage writableImage) {
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        // 复制原始图像的像素到WritableImage
        PixelReader pixelReader = writableImage.getPixelReader();
       // 获取图像的宽度和高度
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        for (int y = 0; y < writableImage.getHeight(); y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }

        // 获取WritableImage的像素数据
        PixelReader wPixelReader = writableImage.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = PixelFormat.getByteBgraInstance();
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
        buffer.order(ByteOrder.nativeOrder());
        wPixelReader.getPixels(0, 0, width, height, format, buffer, 0);
        return buffer;
    }

//    // 加载图片（使用OpenCV的cvLoadImage函数，注意JavaCV可能已经封装了更简洁的方法）
//    static native long cvLoadImage(String filename);
//
//    // 加载本地库（在程序开始时调用）
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }

    // 加载图片（使用OpenCV的cvLoadImage函数）
//    static native Mat n_Mat(int heigth, int width, int type);

    // 加载本地库（在程序开始时调用）
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}