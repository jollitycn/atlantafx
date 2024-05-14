package com.jasonhong.fx.main.media;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioCapture {  
    private static final int SAMPLE_RATE = 44100; // 采样率  
    private static final int SAMPLE_SIZE_IN_BITS = 32; // 采样大小（位）
    private static final int CHANNELS = 2; // 声道数（单声道为1，立体声为2）  
    private static final boolean SIGNED = true; // 数据是否是有符号的  
    private static final boolean BIG_ENDIAN = false; // 数据是否是大端序  
    private static final String OUTPUT_FILE = "output.wav"; // 输出文件名
    public static void main(String[] args) throws LineUnavailableException {
        try {  
            AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);  

            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            for (Mixer.Info info : mixerInfos) {
                Mixer mixer = AudioSystem.getMixer(info);
                System.out.println("Found info: " + info);
                Line.Info[] lineInfos = mixer.getSourceLineInfo();
                for (Line.Info lineInfo : lineInfos) {
                     if (lineInfo.getLineClass().equals(SourceDataLine.class)) {
                        System.out.println("Found SourceLine: " + lineInfo);
                        // 这里可以根据名称或其他属性选择特定的设备
                     }
               } lineInfos = mixer.getTargetLineInfo();
                for (Line.Info lineInfo : lineInfos) {
                    if (lineInfo.getLineClass().equals(TargetDataLine.class)) {
                        System.out.println("Found TargetDataLine: " +lineInfo);
                        // 这里可以根据名称或其他属性选择特定的设备
                    }
                }
            }
            //
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            System.out.println("SourceDataLine info: " + info);
            if (!AudioSystem.isLineSupported(info)) {  
                System.out.println("Line not supported");  
                System.exit(0);  
            }

            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);  
            line.start();  
  
            // 创建一个线程来读取音频数据  
            Thread captureThread = new Thread(() -> {  
                byte[] buffer = new byte[4096]; // 缓冲区大小可以根据需要调整  
                int bytesRead;
              // AudioInputStream audioStream = new AudioInputStream(line);
              //  audioStream.
               // try {
                    line.write(buffer,0,buffer.length);
                //    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(OUTPUT_FILE));
               // } catch (IOException e) {
                    //throw new RuntimeException(e);
               // }

//                while ((bytesRead = line.read(buffer, 0, buffer.length)) != -1) {
//                    // 在这里处理音频数据，比如打印出来或者进行其他操作
//                    System.out.println("Read " + bytesRead + " bytes of audio data.");
//                }
            });
  
            captureThread.start();  
  
            // 让捕获线程运行一段时间，然后停止  
            Thread.sleep(10000); // 捕获10秒音频  
  
            line.stop();  
            line.close();  
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();  
        }  
    }  
}