package com.jasonhong.fx.main.media;

import javax.sound.sampled.*;
import java.io.IOException;

public class AudioOutputStreamExample {
    public static void main(String[] args) {
        try {
            // 定义音频格式
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            // 创建数据行信息
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            // 获取音频系统并获取音频输出流
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(format);
            sourceDataLine.start();

            // 计算一秒钟需要的帧数
            int frameSize = format.getFrameSize();
            int sampleRate = (int) format.getSampleRate();
            int seconds = 60; // 播放时间为一分钟
            int totalFrames = seconds * sampleRate;
            byte[] buffer = new byte[frameSize];

            // 生成并播放正弦波音频
            double frequency = 440.0; // 假设生成A4音符的正弦波
            double twoPiFreq = 2.0 * Math.PI * frequency;
            double sample = 0.0;
            for (int i = 0; i < totalFrames; i++) {
                // 生成正弦波样本
                sample += Math.sin(twoPiFreq * (i / (double) sampleRate));
                // 将样本值转换为16位PCM格式
                short pcmValue = (short) (sample * 32767);
                // 将PCM样本写入缓冲区
                buffer[0] = (byte) (pcmValue & 0x00FF);
                buffer[1] = (byte) ((pcmValue >> 8) & 0x00FF);
                // 写入输出流
             //   sourceDataLine.
                sourceDataLine.write(buffer, 0, frameSize);
            }

            // 等待足够的时间以确保音频播放完毕
            Thread.sleep(1000); // 额外的等待时间

            // 停止并关闭音频输出流
            sourceDataLine.stop();
            sourceDataLine.close();
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}