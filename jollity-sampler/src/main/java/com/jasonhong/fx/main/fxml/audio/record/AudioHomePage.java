package com.jasonhong.fx.main.fxml.audio.record;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Spacer;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.page.CommonPage;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.page.components.RoundedStackPane;
import com.jasonhong.fx.main.page.components.media.MediaPlayer;
import com.jasonhong.fx.main.util.RecentFilesManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.jasonhong.fx.main.util.DialogUtil.openAudioDailog;

public class AudioHomePage extends CommonPage {

    public static final String NAME = "主页";
    public static final Set<String> SUPPORTED_MEDIA_TYPES = Set.of("mp3","m4a","wav");

    GridPane gridPane = new GridPane();
    RecentFilesManager recentFilesManager = new RecentFilesManager(RecentFilesManager.RecentFileType.ADUIO, 25);
//    List<File> files;
    public AudioHomePage() {
        super();


        //开始录音，停止录音
        //自动停止时间（秒）
        //保存录音
        //我的录音列表
        //展示之前的录音
        //录音保存格式MP3，M4A,WAV
        //录音转格式（另存为）
        //删除 //单选多选功能
        // 创建一个GridPane
//        userContent.getStyleClass().add("user-content");
//        getStyleClass().add("outline-page");
        HBox box  =new HBox(5);
        Button btnOpenFile =    new Button("打开文件");
        btnOpenFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                List<File> files = openAudioDailog(getScene(),SUPPORTED_MEDIA_TYPES);
                if (files == null) return;
                openFiles(files);
            }
        });
//        btnOpenFile.

        Spacer spacer =  new Spacer();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(spacer,btnOpenFile);
        btnOpenFile.setAlignment(Pos.CENTER_RIGHT);
        userContent.getChildren().add(box);
        gridPane.setPadding(new Insets(10, 10, 10, 10)); // 设置内边距
        gridPane.setHgap(10); // 设置水平间隙
        gridPane.setVgap(10); // 设置垂直间隙
        genFiles();
//        Platform.runLater(()->{
//            userContent.getChildren().add(gridPane);
//        });
    }

    private void genFiles() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
                            gridPane.getChildren().clear();
                            userContent.getChildren().remove(gridPane);

//                        });
                List<File> files = recentFilesManager.getRecentFiles();
                Iterator it = files.iterator();
                int i = 0;
                while (it.hasNext()) {

                    for (int j = 0; j < 5; j++) {
                        File file = (File) it.next();
                            gridPane.add(new RoundedStackPane(file), j, i);

                        if(!it.hasNext()){break;}
                    }
                    i++;
                }
//        var haeder=  createHeader();
                var sidebar = new ToolBar();
//        var vb = new VBox(VGAP_10,haeder,listView);
//                Platform.runLater(() -> {
                    userContent.getChildren().add(gridPane);
//                });
//            }
//        }).start();
    }

    private void openFiles(List<File> files) {
//        recentFilesManager。
        recentFilesManager.addRecentFiles(files);
        genFiles();
    }

    private void createHeader() {
       addPageHeader();
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
