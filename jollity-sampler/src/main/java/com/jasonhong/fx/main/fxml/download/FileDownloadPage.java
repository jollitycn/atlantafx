package com.jasonhong.fx.main.fxml.download;

import atlantafx.base.theme.Tweaks;
import com.jasonhong.fx.main.page.Page;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static atlantafx.base.theme.Styles.TEXT_CAPTION;
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
    VBox root = new VBox(10);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(10));

    listView = new ListView<>(tasks);
//    listView.setItems(tasks);
    listView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
    listView.setCellFactory(param -> new MediaCell());
    root.getChildren().add(listView);
    getChildren().add(root);
}

    private static class MediaCell extends ListCell<FileDownloadTask> {

        private static final PseudoClass PLAYING = PseudoClass.getPseudoClass("playing");

        //        private final Model model;
        private final HBox root;
        private final Rectangle coverImage;
        private final Label titleLabel;
        private final Label artistLabel;
        private final ProgressBar progessBar;
        private final FontIcon playMark;
        private final FontIcon btnCancel;
        private final FontIcon btnOpenFolder;

        public MediaCell() {
//            this.model = model;

            coverImage = new Rectangle(0, 0, 32, 32);
            coverImage.setArcWidth(10.0);
            coverImage.setArcHeight(10.0);

            titleLabel = new Label();
            titleLabel.setMaxWidth(MAX_VALUE);
            titleLabel.getStyleClass().add(TEXT_CAPTION);

            artistLabel = new Label();
            artistLabel.setMaxWidth(MAX_VALUE);

            progessBar = new ProgressBar();
            VBox.setVgrow(progessBar, ALWAYS);
            var titleBox = new VBox(5, titleLabel, progessBar, artistLabel);
            titleBox.setAlignment(CENTER_LEFT);
            HBox.setHgrow(titleBox, ALWAYS);

            playMark = new FontIcon(PLAYLIST_PLAY);
            btnCancel = new FontIcon(PAUSE);
            btnOpenFolder = new FontIcon(OPEN_IN_BROWSER);
            playMark.setOnMouseClicked(
                    e -> {
                        if (getItem() != null) {
//                    model.play(getItem());
                            getItem().downloadFile();
                            progessBar.setVisible(true);
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
            root.setAlignment(CENTER_LEFT);
//            root.setOnMouseClicked(e -> {
//                if (getItem() != null) {
////                    model.play(getItem());
//                    getItem().downloadFile();
//                }
//            });
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
                setGraphic(root);
                titleLabel.textProperty().bind(mediaFile.fileName);
                artistLabel.textProperty().bind(mediaFile.statusLabel);
                progessBar.progressProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (progessBar.progressProperty().getValue() == 1.0) {
                            progessBar.setVisible(false);
                            playMark.setVisible(false);
                            btnCancel.setVisible(false);
                            btnOpenFolder.setVisible(true);
                        }
                    }
                });
                progessBar.progressProperty().bind(mediaFile.progressBar.get().progressProperty());
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