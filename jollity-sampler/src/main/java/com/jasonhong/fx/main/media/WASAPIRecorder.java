package com.jasonhong.fx.main.media;

public class WASAPIRecorder {
    static {
        System.loadLibrary("WASAPIRecorderLib");
    }

    public native boolean initializeRecorder();
    public native boolean startRecording();
    public native boolean stopRecording();
    public native void shutdownRecorder();

    public void recordAudio() {
        if (initializeRecorder()) {
            if (startRecording()) {
                stopRecording();
            }
            shutdownRecorder();
        }
    }
}