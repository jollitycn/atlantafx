package com.jasonhong.fx.main.util;

import com.jasonhong.core.io.txt.FileExtension;
import com.jasonhong.media.audio.util.AudioUtil;
import com.jasonhong.media.audio.util.ID3v24UtilTag;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

public class MediaPlayerUtil {
    static MediaPlayer mediaPlayer;
    public static String AUDIO_HANDLING = "app/audio/0001.wav";
    public static String AUDIO_HANDLED = "app/audio/0002.wav";
    public static String AUDIO_EORROR = "app/audio/0003.wav";
    public static String AUDIO_TEXT_FILE_ERROR = "app/audio/0004.wav";
    public static String AUDIO_TEXT_FILE_ERROR_256 = "app/audio/0005.wav";
    public static String[] AUDIO_TEXT_FILE_ERRORS = new String[]{AUDIO_TEXT_FILE_ERROR, AUDIO_TEXT_FILE_ERROR_256};

    private static MediaPlayer getInstance(Media media) {

        if (mediaPlayer == null || !mediaPlayer.getMedia().equals(media)) {
            mediaPlayer = new MediaPlayer(media);
        }
        return mediaPlayer;
    }

    public static void playRandom(String[] audioFiles) {
        Random random = new Random(audioFiles.length);
        String audioFile = audioFiles[random.nextInt()];
        AudioClip audioClip = null;
        audioClip = new AudioClip(new File(audioFile).toURI().toString());
        if (audioClip != null) {
            // 播放声音
            audioClip.play();
        }
    }

    public static void play(String audioFile) {
        AudioClip audioClip = null;
        audioClip = new AudioClip(new File(audioFile).toURI().toString());
        if (audioClip != null) {
            // 播放声音
            audioClip.play();
        }
    }

    public static void play(File audioFile) {
        AudioClip audioClip = null;
        audioClip = new AudioClip(audioFile.toURI().toString());
        if (audioClip != null) {
            // 播放声音
            audioClip.play();
        }
    }

    private static void playAudio(String audio) {
        // 创建Media对象，指定音频文件的URL
        Media media = new Media(MediaPlayerUtil.class.getResource(audio).toExternalForm());

        // 创建MediaPlayer对象并设置其media属性

        getInstance(media);
        // 播放音频
        mediaPlayer.play();

        // 如果需要，你可以添加监听器来检测播放何时结束
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                // 播放结束后的处理逻辑
                System.out.println("Audio playback finished.");
            }
        });

    }

    public static void setTag(File outFile, String name, String audioArtist, String audioAlbum, String orgText) throws Exception {
//        ID3v24UtilTag tag = (ID3v24UtilTag) AudioUtil.getTag(outFile);
//        tag.setTrack(String.valueOf(System.currentTimeMillis()));
//        tag.setArtist(audioArtist);
//        tag.setTitle(name);
//        tag.setAlbum(audioAlbum);
        File sourceFile = File.createTempFile(outFile.getName(), FileExtension.getExtension(outFile.getName()), new File("data/temp/client"));
        FileUtils.copyFile(outFile, sourceFile);
        AudioUtil.updateAudioTagSource(sourceFile, outFile.getAbsolutePath(), String.valueOf(System.currentTimeMillis()), audioArtist, name, audioAlbum, orgText);

    }
}
