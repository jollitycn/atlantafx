package com.jasonhong.fx.main.fxml.audio.record;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.page.CommonPage;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.page.components.media.MediaPlayer;
import com.jasonhong.fx.main.page.home.HomePage;
import com.jasonhong.fx.main.util.ToolBarUtil;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.commons.collections.FastArrayList;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.net.URI;
import java.util.*;

import static com.jasonhong.fx.main.util.FXUtil.iconButton;
import static com.jasonhong.fx.main.util.ToolBarUtil.rotateToolbar;

public class AudioPage extends VBox implements Page {

    public static final String NAME = "录音机";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3");
    BorderPane  bp;

    private Deque<CommonPage> pages=  new ArrayDeque();
    public AudioPage(){
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
        AudioHomePage homePage = new AudioHomePage();
//        pages.add(homePage);
//        ListView<AudioRecorderInfo> listView = new ListView<AudioRecorderInfo>();
//        var haeder=  createHeader();
//        var sidebar = new ToolBar();
//        var vb = new VBox(VGAP_10,haeder,listView);
//

        var toolbar = new ToolBar(createButtons(Orientation.VERTICAL));
//        toolbar.setDisable(false);
        var toolbarLayer = new BorderPane();
//         toolbarLayer.setTop(new ToolBarUtil.TopBar(toolbar));
         rotateToolbar(toolbarLayer, toolbar, Side.LEFT);
//        getStyleClass().add(Styles.BORDERED);
       ;
        var left = new StackPane(toolbarLayer);
        bp = new BorderPane();
        bp.setLeft(left);
        bp.setCenter(homePage);
        var mainBody = new StackPane(bp ,modalPane);
        getChildren().addAll(mainBody);
//        toolbarLayer.
    }

    public ToggleButton toggleIconButton(@Nullable Ikon icon,
                                         String... styleClasses) {
        return toggleIconButton(icon, null, false, styleClasses);
    }

    public ToggleButton toggleIconButton(@Nullable Ikon icon,
                                         boolean selected,
                                         String... styleClasses) {
        return toggleIconButton(icon, null, selected, styleClasses);
    }

    public ToggleButton toggleIconButton(@Nullable Ikon icon,
                                         @Nullable ToggleGroup group,
                                         boolean selected,
                                         String... styleClasses) {
        var btn = new ToggleButton("");
        if (icon != null) {
            btn.setGraphic(new FontIcon(icon));
        }
        if (group != null) {
            btn.setToggleGroup(group);
        }
        btn.getStyleClass().addAll(styleClasses);
        btn.setSelected(selected);

        return btn;
    }
    private Node[] createButtons(Orientation orientation) {
        var result = new ArrayList<Node>();
        Button prePageButton= iconButton(Feather.CHEVRON_LEFT);
        result.add(prePageButton);
        Button homeButton= iconButton(Feather.HOME);
        result.add(homeButton);
       Button btnOpenRecorder= iconButton(Material2AL.AUDIOTRACK);
        result.add(btnOpenRecorder);
        result.add(iconButton(Feather.FOLDER));
        result.add(iconButton(Feather.SAVE));
        result.add(new Separator());

        if (orientation == Orientation.HORIZONTAL) {
//            result.add(new Button("Undo"));
//            result.add(new Button("Redo"));
//            result.add(new Separator());
//            result.add(toggleIconButton(Feather.BOLD, true));
//            result.add(toggleIconButton(Feather.ITALIC));
//            result.add(toggleIconButton(Feather.UNDERLINE));
        }

        if (orientation == Orientation.VERTICAL) {
//            result.add(iconButton(Feather.CORNER_DOWN_LEFT));
//            result.add(iconButton(Feather.CORNER_DOWN_RIGHT));
//            result.add(new Spacer(orientation));
//            result.add(iconButton(Feather.SETTINGS));
        }
//        prePageButton
        prePageButton.setOnMouseClicked(c->{
//            pages.add(new AudioRecorderPage());
            Platform.runLater(()->{
            try {pages.pop();
                    bp.setCenter(pages.pop());
            }catch (Exception e){}  });
        });
        homeButton.setOnMouseClicked(c->{
     ;
            Platform.runLater(()-> {
            try {

                    AudioHomePage page = new AudioHomePage();
                    bp.setCenter(page);
                    pages.push(page);
            }catch (Exception e){}

        });   });
        btnOpenRecorder.setOnMouseClicked(c->{
            Platform.runLater(()->{
                CommonPage page = new AudioRecorderPage();

//                bp.setCenter(null);
                bp.setCenter(page);
//                page.toFront();
                pages.push(page);
            });
        });
        return result.toArray(Node[]::new);
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
    private Side toolbarPos = Side.LEFT;

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
