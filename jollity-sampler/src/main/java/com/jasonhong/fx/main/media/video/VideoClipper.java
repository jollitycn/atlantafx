package com.jasonhong.fx.main.media.video;

import org.bytedeco.javacv.*;

import java.io.File;

public class VideoClipper {
    public static void main(String[] args) throws Exception {
        String inputFile = "E:/studio/project/audioPlayer/src/assets/vedios/vedio.mp4";
        String outputFile = "E:/studio/project/audioPlayer/src/assets/vedios/vedio.mp4";
        int startTime = 10 * 1000; // 开始时间（毫秒）
        int duration = 5 * 1000; // 剪辑时长（毫秒）

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(new File(outputFile), grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
        recorder.start();

        long startTimeMillis = System.currentTimeMillis();
        Frame frame;
        while ((frame = grabber.grabFrame()) != null && (System.currentTimeMillis() - startTimeMillis) < (startTime + duration)) {
            if ((System.currentTimeMillis() - startTimeMillis) >= startTime) {
                recorder.record(frame);
            }
        }

        recorder.stop();
        grabber.stop();
    }
}