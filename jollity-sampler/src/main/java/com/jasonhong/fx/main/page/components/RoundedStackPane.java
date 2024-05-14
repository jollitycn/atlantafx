package com.jasonhong.fx.main.page.components;

import atlantafx.base.controls.Spacer;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.fxml.audio.musicplayer.MediaFile;
import com.jasonhong.fx.main.fxml.audio.record.AudioRecordEvent;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

import static com.jasonhong.fx.main.fxml.audio.musicplayer.MediaFile.Metadata.NO_IMAGE_ALT;

public class RoundedStackPane extends Button {
    Rectangle coverImage;
    Label fileNameLabel;
    StackPane stackPane ;
    MediaFile mediaFile;
    public RoundedStackPane(File file) {
        // 创建标题  


        // 创建矩形（或Region）作为按钮的容器  
        coverImage = new Rectangle(150, 150 );
        coverImage.setArcWidth(10);
        coverImage.setArcHeight(10);


        // 创建按钮  
        FontIcon playButton = new FontIcon(Feather.PLAY);
        FontIcon deleteButton = new FontIcon(Feather.HASH.DELETE);
        CheckBox selectButton = new CheckBox();

        // 布局按钮（例如使用HBox）  
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.getChildren().add(selectButton);

        // 创建一个HBox来放置播放和删除按钮  
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);
        bottomBox.getChildren().addAll(playButton, new Spacer(), deleteButton);

        // 初始时隐藏按钮  
        playButton.setVisible(false);
        deleteButton.setVisible(false);
        selectButton.setVisible(false);

        // 将按钮添加到容器中

        // 创建StackPane并设置圆角
        fileNameLabel = new Label( );
//        title.setPadding(new Insets(5));
        StackPane boxR = new StackPane(coverImage);
        boxR.getChildren().addAll(buttonBox, bottomBox);
        VBox box = new VBox();
        box.getChildren().addAll(boxR, fileNameLabel);
          stackPane = new StackPane(box);

        stackPane.setStyle("-fx-background-radius: 10;");
//        stackPane.setPadding(new Insets(10));

        // 添加鼠标事件以显示/隐藏按钮  
        stackPane.setOnMouseEntered(e -> {
            playButton.setVisible(true);
            deleteButton.setVisible(true);
            selectButton.setVisible(true);
        });
        stackPane.setOnMouseExited(e -> {
            playButton.setVisible(false);
            deleteButton.setVisible(false);
            selectButton.setVisible(false);
        });
        // ~

        stackPane.setOnMouseClicked(e -> {
            AudioRecordEvent event = new AudioRecordEvent(mediaFile);
            DefaultEventBus.getInstance().publish(event );
        });

        this.setGraphic(stackPane);
        loadAudio(file);
    }

    private void loadAudio(File file) {
        Platform.runLater(() -> {
            mediaFile = new MediaFile(file.toPath());
            mediaFile.readMetadata(metadata -> {
                coverImage.setFill(new ImagePattern(
                        metadata.image() != null ? metadata.image() : NO_IMAGE_ALT
                ));
                fileNameLabel.setText(metadata.fileName());
//            artistLabel.setText(metadata.artist());
            });

        });
    }
}