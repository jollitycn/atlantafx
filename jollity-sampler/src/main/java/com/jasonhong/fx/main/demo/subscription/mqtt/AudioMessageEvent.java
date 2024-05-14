package com.jasonhong.fx.main.demo.subscription.mqtt;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;

public class AudioMessageEvent extends Event {
    public static final EventType<AudioMessageEvent> RECEIVED =
            new EventType<>(ANY, "RECEIVED");

    private final File file;

    public AudioMessageEvent(EventTarget eventSource, EventType<? extends Event> eventType, File file) {
        super(file, eventSource, eventType);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}