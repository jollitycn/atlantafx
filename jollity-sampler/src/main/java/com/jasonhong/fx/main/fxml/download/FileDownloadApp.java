package com.jasonhong.fx.main.fxml.download;

import com.jasonhong.fx.main.page.Page;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.jasonhong.fx.main.fxml.ocr.ImageToText.NAME;
import static com.jasonhong.fx.main.fxml.ocr.ImageToText.SUPPORTED_MEDIA_TYPES;

public class FileDownloadApp extends Application{

    public static final String NAME =  "文件下载";
    private ProgressBar progressBar;
    private Label statusLabel;
    FileDownloadPage page;
    @Override
    public void start(Stage primaryStage) {
        // 创建布局和控件
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        TextField url = new TextField("https://tor.zilog.es/dist/torbrowser/13.0.14/tor-browser-windows-x86_64-portable-13.0.14.exe");

        Button startDownloadButton = new Button("开始下载");
        startDownloadButton.setOnAction(e -> downloadFile( url.getText()));
//        FileDownloadPage page= new FileDownloadPage();
        BorderPane bp = new BorderPane();
        HBox hb = new HBox();
        hb.getChildren().addAll( url,startDownloadButton);

        bp.setTop(hb);
        page = new FileDownloadPage();
        bp.setCenter(page);
        root.getChildren().addAll(bp);

        // 创建场景和舞台
        Scene scene = new Scene(root, 800, 680);
        primaryStage.setTitle("文件下载");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void downloadFile(String fileUrl) {

        FileDownloadTask task = new FileDownloadTask(fileUrl );
        page.addTask(task);

    }

    public static void main(String[] args) {
        launch(args);
    }

}