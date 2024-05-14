package com.jasonhong.fx.main.fxml.audio.record;

import atlantafx.base.controls.ModalPane;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.page.components.media.MediaPlayer;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Set;

public class AudioRecorderPage extends StackPane implements Page {

    public static final String NAME = "录音机";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3");

    public AudioRecorderPage(){
        super();
        //开始录音，停止录音
        //自动停止时间（秒）
        //保存录音
        //我的录音列表
        //展示之前的录音
        //录音保存格式MP3，M4A,WAV
        //录音转格式（另存为）
        //删除 //单选多选功能
        DefaultEventBus.getInstance().subscribe(AudioRecordEvent.class,this::onAudioRecordEvent);
        // reset side and transition to reuse a single modal pane between different examples
        modalPane.displayProperty().addListener((obs, old, val) -> {
            if (!val) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
        AudioRecorderHomePage homePage = new AudioRecorderHomePage();
//        ListView<AudioRecorderInfo> listView = new ListView<AudioRecorderInfo>();
//        var haeder=  createHeader();
//        var sidebar = new ToolBar();
//        var vb = new VBox(VGAP_10,haeder,listView);
//


        this.getChildren().addAll(homePage,modalPane);

    }

    private static class Dialog extends VBox {

        public Dialog(int width, int height) {
            super();

            setSpacing(10);
            setAlignment(Pos.CENTER);
            setMinSize(width, height);
            setMaxSize(width, height);
            setStyle("-fx-background-color: -color-bg-default;");
        }
    }

    private final ModalPane modalPane = new ModalPane();

    private void onAudioRecordEvent(AudioRecordEvent audioRecordEvent) {
//        var bottomDialog = new Dialog(-1, 150);
//        bottomDialog.getChildren().setAll();
        modalPane.setAlignment(Pos.BOTTOM_CENTER);
        modalPane.usePredefinedTransitionFactories(Side.BOTTOM);
        modalPane.show(new MediaPlayer(-1, 100,audioRecordEvent.getMediaFile()));
//        modalPane.show();
    }


    private Node createHeader() {
        return null;
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
