package com.jasonhong.fx.main.component;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;

public class FileSelectedEvent extends Event {
    public static final EventType<FileSelectedEvent> FILE_SELECTED =
            new EventType<>(Event.ANY, "FILE_SELECTED");

    private final File selectedFile;

    public FileSelectedEvent(EventTarget eventSource, EventType<? extends Event> eventType, File selectedFile) {
        super(selectedFile, eventSource, eventType);
        this.selectedFile = selectedFile;
    }

    public File getSelectedFile() {
        return selectedFile;
    }
}