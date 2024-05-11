package com.jasonhong.fx.main.fxml.tts;

//import com.jasonhong.media.audio.AudioTagWriter;
import com.jasonhong.services.mq.tts.client.TextToSpeakInfo;
import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.tools.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.jasonhong.fx.main.fxml.tts.Constants.AUDIO_ARTIST;

public class Main {
//    public static void main(String [] args){try {
//        TextToSpeakInfo info = new TextToSpeakInfo();
//        info.setOutFile(new File("E:\\studio\\project\\audioPlayer\\data\\service\\wav\\1c57ca9a-0502-4eb7-b574-11ca4261dbd6\\1715403697296\\merge\\merged.wav"));
//        info.setFile(new File("E:\\studio\\project\\audioPlayer\\data\\service\\wav\\1c57ca9a-0502-4eb7-b574-11ca4261dbd6\\1715403697296\\received.txt"));
//
//            info.setOrgText(FileUtils.readFileToString(info.getFile(), StandardCharsets.UTF_8));
//
//        AudioTagWriter.setTag(info.getOutFile(),info.getOutFile().getName(),Constants.AUDIO_COVER_ART,AUDIO_ARTIST,Constants.AUDIO_ALBUM, info.getOrgText(),0);
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }  }
}
