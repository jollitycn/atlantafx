/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.fxml.audio.musicplayer;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.jasonhong.fx.main.fxml.audio.musicplayer.MusicPlayerPage.SUPPORTED_MEDIA_TYPES;


final class StartScreen extends BorderPane {

    private final Model model;

    public StartScreen(Model model) {
        super();

        this.model = model;

        createView();
    }

    private void createView() {
        var noteText = new TextFlow(new Text(
            "选择一个文件或者目录"
        ));
        noteText.setMaxWidth(400);
        noteText.setTextAlignment(TextAlignment.CENTER);

        var addFolderBtn = new Button("添加目录");
        addFolderBtn.getStyleClass().add(Styles.ACCENT);
        addFolderBtn.setPrefWidth(150);
        addFolderBtn.setOnAction(e -> addFolder());

        var addFileBtn = new Button("添加文件");
        addFileBtn.setPrefWidth(150);
        addFileBtn.setOnAction(e -> addFile());

//        var addDemoBtn = new Button("Play Demo");
//        addDemoBtn.getStyleClass().add(Styles.SUCCESS);
//        addDemoBtn.setPrefWidth(150);
//        addDemoBtn.setOnAction(e -> model.playDemo());

        var controls = new VBox(10, addFolderBtn, addFileBtn);
        controls.setAlignment(Pos.CENTER);
        controls.setFillWidth(true);

        var jumboIcon = new FontIcon(Feather.MUSIC);

        var content = new VBox(30, jumboIcon, noteText, controls);
        content.getStyleClass().add("content");
        content.setAlignment(Pos.CENTER);
        content.setFillWidth(true);

        setCenter(content);
        setPadding(new Insets(100));
        getStyleClass().add("start-screen");
//        model.playDemo();
    }

    private void addFile() {
        var extensions = SUPPORTED_MEDIA_TYPES.stream().map(s -> "*." + s).toList();
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter(
            "音频文件 (" + String.join(", ", extensions) + ")",
            extensions
        ));
        List<File> files = fileChooser.showOpenMultipleDialog(getScene().getWindow());
        if (files == null || files.isEmpty()) {
            return;
        }

        for (File file : files) {
            model.addFile(new MediaFile(file.toPath()));
        }
    }

    private void addFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(getScene().getWindow());
        if (dir == null) {
            return;
        }

        var path = dir.toPath();
        if (!Files.isDirectory(path) || !Files.isReadable(path)) {
            return;
        }

        try (Stream<Path> stream = Files.list(path)) {
            stream.forEach(p -> {
                for (String s : SUPPORTED_MEDIA_TYPES) {
                    if (!p.toAbsolutePath().toString().endsWith(s)) {
                        continue;
                    }
                    model.addFile(new MediaFile(p));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
