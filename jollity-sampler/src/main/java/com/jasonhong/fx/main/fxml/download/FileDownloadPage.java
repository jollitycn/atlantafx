package com.jasonhong.fx.main.fxml.download;

import atlantafx.base.theme.Tweaks;
import com.jasonhong.fx.main.browser.BrowserEvent;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.page.components.progressbar.ColoredProgressBar;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static atlantafx.base.theme.Styles.*;
import static com.jasonhong.fx.main.util.FXUtil.show;
import static java.lang.Double.MAX_VALUE;
import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.scene.layout.Priority.ALWAYS;
import static org.kordamp.ikonli.material2.Material2MZ.*;

public class FileDownloadPage extends StackPane implements Page {

    public static final String NAME =  "文件下载";
    private ObservableList<FileDownloadTask> tasks = FXCollections.observableArrayList();
    private ListView<FileDownloadTask> listView  = new ListView<>();
public FileDownloadPage() {
    // 创建布局和控件
//    root.setAlignment(Pos.CENTER);
//    root.setPadding(new Insets(10));
    var dropdown = new MenuButton("", new FontIcon(Feather.MENU));

    dropdown.getItems().setAll(
            newDownload()
    );
    var header = new HBox(HGAP_10, dropdown);
    header.setAlignment(Pos.CENTER_RIGHT);
    listView = new ListView<>(tasks);
//    listView.setItems(tasks);
    listView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
    listView.setCellFactory(param -> new MediaCell());
    listView.setMaxHeight(MAX_VALUE);
    listView.setMaxWidth(MAX_VALUE);
    listView.setPadding(new Insets(5));
//    listView.
//    root.setTop(dropdown);
//    root.setCenter(listView);
    VBox root = new VBox(VGAP_10,header,listView);
    getChildren().add(root);
    root.setPadding(new Insets(10));
    this.setMaxHeight(MAX_VALUE);
    this.setMaxWidth(MAX_VALUE);
}

    private MenuItem newDownload() {
            var downlaod = new MenuItem("新增下载", new FontIcon(Feather.DOWNLOAD));
            downlaod.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                        var dialog = new TextInputDialog ();
                        dialog.setTitle("新增下载");
                        dialog.setHeaderText(null);
                        dialog.setContentText("请输入您的链接地址:");
                        dialog.initOwner(getScene().getWindow());
                    // 使用showAndWait()来阻塞当前线程并等待用户输入
                    Optional<String> result = (Optional<String>) show(dialog,getScene());

                    if (result.isPresent()) {
                        // 用户点击了"OK"按钮，result.get()将返回用户输入的文本
                        String userInput = result.get();
                        addTask(new FileDownloadTask(userInput));
                        System.out.println("用户输入的链接地址是: " + userInput);
                        // 在这里你可以使用userInput变量来进行你的操作
                    } else {
                        // 用户点击了"取消"按钮或关闭了对话框
                        System.out.println("用户取消了操作");
                    }
                }
            });
            return downlaod;
    }



    private static class MediaCell extends ListCell<FileDownloadTask> {

        private static final PseudoClass PLAYING = PseudoClass.getPseudoClass("playing");

        //        private final Model model;
        private final HBox root;
        private final StackPane roundedContainer;
        private final Rectangle coverImage;
        private final Label titleLabel;
        private final Label artistLabel;
        private final  ProgressBar progressBar;
        private final FontIcon playMark;
        private final FontIcon btnCancel;
        private final FontIcon btnOpenFolder;

        public MediaCell() {
//            this.model = model;

            coverImage = new Rectangle(0, 0, 32, 32);
            coverImage.setArcWidth(10);
            coverImage.setArcHeight(10);

            titleLabel = new Label();
            titleLabel.setMaxWidth(MAX_VALUE);
            titleLabel.getStyleClass().add(TEXT_SMALL);

            artistLabel = new Label();
            artistLabel.setMaxWidth(MAX_VALUE);
            artistLabel.getStyleClass().add(TEXT_SMALL);
            progressBar = new  ProgressBar();
            progressBar.getStyleClass().add(SMALL);
            progressBar.setMaxWidth(MAX_VALUE);


//            progressBar.setMinHeight(10);
//            HBox mainBox = new HBox(10, progressBar); // 10是间距
//            HBox.setHgrow(progressBar, Priority.ALWAYS);
            var titleBox = new VBox(5, titleLabel, progressBar, artistLabel);
            titleBox.setAlignment(CENTER_LEFT);
            titleBox.setMaxHeight(MAX_VALUE);
//            titleBox.setMinHeight(50);
            HBox.setHgrow(titleBox, ALWAYS);

            playMark = new FontIcon(PLAYLIST_PLAY);
            btnCancel = new FontIcon(PAUSE);
            btnOpenFolder = new FontIcon(OPEN_IN_BROWSER);
            playMark.setOnMouseClicked(
                    e -> {
                        if (getItem() != null) {
//                    model.play(getItem());
                            getItem().downloadFile();
                            progressBar.setVisible(true);
                            btnCancel.setVisible(true);
                            btnOpenFolder.setVisible(false);
                            playMark.setVisible(false);
                        }
                    }
            );
            playMark.setVisible(false);
            btnOpenFolder.setVisible(false);
            btnCancel.setOnMouseClicked(
                    e -> {
                        if (getItem() != null) {
//                    model.play(getItem());
                            getItem().cancel();
                            playMark.setVisible(true);
                            btnCancel.setVisible(false);
                            btnOpenFolder.setVisible(false);

                        }
                    }
            );

            playMark.setVisible(false);

            btnCancel.setOnMouseClicked(
                    e -> {
                        if (getItem() != null) {
//                    model.play(getItem());
                            getItem().cancel();
                            playMark.setVisible(true);
                            btnCancel.setVisible(false);
                            btnOpenFolder.setVisible(false);

                        }
                    }
            );

            btnOpenFolder.setOnMouseClicked(
                    e -> {
                        if (getItem() != null) {
//                    model.play(getItem());
                            getItem().open();

                        }
                    }
            );
//            playMark.setVisible(TRUE);

            root = new HBox(10, coverImage, titleBox, playMark, btnCancel, btnOpenFolder);
            root.setMinHeight(50);
            root.setAlignment(CENTER_LEFT);
            roundedContainer = new StackPane(root);
            StackPane.setMargin(roundedContainer,new Insets(5));
            roundedContainer.getStyleClass().add("-fx-background-color: white; -fx-background-radius: 10px;");
///
//            root.setOnMouseClicked(e -> {
//                if (getItem() != null) {
////                    model.play(getItem());
//                    getItem().downloadFile();
//                }
//            });

//            ListCell.
        }

        @Override
        protected void updateItem(FileDownloadTask mediaFile, boolean empty) {
            super.updateItem(mediaFile, empty);
            if (empty || mediaFile == null) {
//                setGraphic(null);
//                coverImage.setFill(null);
//                titleLabel.setText(null);
//                artistLabel.setText(null);
            } else {
                setGraphic(roundedContainer);
                titleLabel.textProperty().bind(mediaFile.fileDisplayName);
                artistLabel.textProperty().bind(mediaFile.statusLabel);
                progressBar.progressProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (progressBar.progressProperty().getValue() == 1.0) {
                            progressBar.setVisible(false);
                            playMark.setVisible(false);
                            btnCancel.setVisible(false);
                            btnOpenFolder.setVisible(true);
                        }
                    }
                });
                progressBar.progressProperty().bind(mediaFile.progressBar.get().progressProperty());
            }
        }
    }



//    public static void main(String[] args) {
//        launch(args);
//    }

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

    public void addTask(FileDownloadTask task) {
        this.tasks.add(task);
        task.downloadFile();
    }
}