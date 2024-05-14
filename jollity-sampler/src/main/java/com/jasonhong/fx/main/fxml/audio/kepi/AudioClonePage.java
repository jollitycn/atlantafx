package com.jasonhong.fx.main.fxml.audio.kepi;

import com.jasonhong.fx.main.page.Page;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Set;

public class AudioClonePage extends StackPane implements Page {

    public static final String NAME = "声音克隆";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3");



    public AudioClonePage(){
        super();
        //开始录音，停止录音
        //自动停止时间（秒）

    }


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Parent getView() {
        return this;
    }

    @Override
    public boolean canDisplaySourceCode() {
        return false;
    }

    @Override
    public @Nullable URI getJavadocUri() {
        return null;
    }

    @Override
    public @Nullable Node getSnapshotTarget() {
        return null;
    }

    @Override
    public void reset() {

    }
}
