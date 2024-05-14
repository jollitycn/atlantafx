package com.jasonhong.fx.main.fxml.audio.record;

import com.jasonhong.fx.main.event.Event;
import com.jasonhong.fx.main.fxml.audio.musicplayer.MediaFile;

public class AudioRecordEvent  extends Event {
    private MediaFile mediaFile;
    public AudioRecordEvent(MediaFile mediaFile) {
        super(mediaFile);
        this.setMediaFile(mediaFile);
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(MediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }
}
