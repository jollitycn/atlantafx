package com.jasonhong.fx.main.media.video;

import javafx.scene.image.WritableImage;
import javafx.util.Duration;

public class MyWritableImage   {
    public WritableImage getSnapshot() {
        return snapshot;
    }

    private final WritableImage snapshot;

    public MyWritableImage(WritableImage snapshot, Duration currentTime) {
this.currentTime = currentTime;
this.snapshot  = snapshot;
    }

    public Duration getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Duration currentTime) {
        this.currentTime = currentTime;
    }

    private Duration currentTime;

}
