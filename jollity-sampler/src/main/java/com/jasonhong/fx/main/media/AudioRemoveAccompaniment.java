package com.jasonhong.fx.main.media;//package com.jasonhong.application.media;
//
//import be.tarsos.dsp.AudioDispatcher;
//import be.tarsos.dsp.AudioEvent;
//import be.tarsos.dsp.AudioProcessor;
//import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
//import be.tarsos.dsp.io.jvm.AudioPlayer;
//
//import javax.sound.sampled.*;
//import java.io.File;
//import java.io.IOException;
//
//public class AudioRemoveAccompaniment {
//    public static void main(String[] args) {
//        String source = "src/main/resources/happy.wav";
//        File audioFile = new File(source);
//        AudioInputStream audioStream = null;
//        try {
//            audioStream = AudioSystem.getAudioInputStream(audioFile);
//            AudioFormat format = audioStream.getFormat();
//            final int bufferSize = 4096; // 可以根据需要调整缓冲区大小
//            final int bufferOverlap = 2048;//	 *            How much consecutive buffers overlap (in samples). Half of theAudioBufferSize is common (512, 1024) for an FFT.
//            AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(source, (int)format.getFrameRate(),bufferSize,bufferOverlap);
//
//            dispatcher.addAudioProcessor(new AudioProcessor() {
//                @Override
//                public boolean process(AudioEvent audioEvent) {
//                    float[] buffer = audioEvent.getFloatBuffer();
//                    // 在这里处理音频数据
//                    // 例如：应用滤波器、分析频谱等
//                    return true; // 返回 true 继续处理，返回 false 停止处理
//                }
//
//                @Override
//                public void processingFinished() {
//                    // 所有音频数据都处理完毕后调用此方法
//                }
//            });
//
//            // 如果需要播放处理后的音频，可以添加 AudioPlayer
//            AudioPlayer player = new AudioPlayer(dispatcher.getFormat());
//            //player.start();
//
//            // 开始处理音频数据
//            new Thread(dispatcher).start();
//
//        } catch (UnsupportedAudioFileException | IOException e) {
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (audioStream != null) {
//                try {
//                    audioStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}