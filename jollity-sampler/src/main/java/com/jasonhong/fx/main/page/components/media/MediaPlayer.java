package com.jasonhong.fx.main.page.components.media;

import atlantafx.base.controls.Popover;
import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.jasonhong.fx.main.fxml.audio.musicplayer.MediaFile;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

import static atlantafx.base.theme.Styles.*;
import static com.jasonhong.fx.main.fxml.audio.musicplayer.MediaFile.Metadata.*;
import static com.jasonhong.fx.main.fxml.audio.musicplayer.MediaFile.Metadata.NO_ALBUM;
import static com.jasonhong.fx.main.fxml.audio.musicplayer.Utils.formatDuration;
import static com.jasonhong.fx.main.fxml.audio.musicplayer.Utils.getDominantColor;
import static java.lang.Double.MAX_VALUE;
import static javafx.geometry.Orientation.VERTICAL;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.media.MediaPlayer.INDEFINITE;
import static org.kordamp.ikonli.material2.Material2AL.EQUALS;
import static org.kordamp.ikonli.material2.Material2MZ.*;
import static org.kordamp.ikonli.material2.Material2OutlinedAL.FAST_FORWARD;
import static org.kordamp.ikonli.material2.Material2OutlinedAL.FAST_REWIND;

public class MediaPlayer extends VBox {
    private static final int PANEL_MAX_WIDTH = 220;
    private MediaFile mediaFile;
    private Rectangle coverImage;
    private Label trackTitle;
    private Label trackArtist;
    private Label trackAlbum;
    private Label currentTimeLabel;
    private Label endTimeLabel;
    private static final String STYLESHEET_URL =
            Objects.requireNonNull(MediaPlayer.class.getResource("music-player.css")).toExternalForm();

    private static  ObjectProperty<javafx.scene.media.MediaPlayer> currentPlayer = new SimpleObjectProperty<>();

    private Slider timeSlider;
    private Slider volumeSlider;
    private FontIcon playIcon;
    private Button playBtn;
    public MediaPlayer(int width,int height,MediaFile mediaFile) {
        super();

        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinSize(width, height);
        setMaxSize(width, height);
        setStyle("-fx-background-color: -color-bg-default;");
        this.mediaFile = mediaFile;
        setId("music-player-showcase");
//        #music-player-showcase .player > .media-controls > .play
        createView();
        init();
        getStylesheets().add(STYLESHEET_URL);

    }  private <T> T getTag(Media media, String key, Class<T> type, T defaultValue) {
        if (media == null || key == null || type == null) {
            return defaultValue;
        }
        Object tag = media.getMetadata().get(key);
        return type.isInstance(tag) ? type.cast(tag) : defaultValue;
    }


    private void init() {
        heightProperty().addListener((obs, old, val) -> {
            if (val == null) {
                return;
            }
            int size = val.intValue() < 150 ? 80 : 100;
            coverImage.setWidth(size);
            coverImage.setHeight(size);
        });

        playBtn.setOnAction(e -> {
            javafx.scene.media.MediaPlayer player = currentPlayer.get();
            if (player == null) {
                return;
            }
            switch (player.getStatus()) {
                case READY, PAUSED, STOPPED -> player.play();
                case PLAYING -> player.pause();
                default -> {
                }
            }
        });
        InvalidationListener mediaTimeChangeListener = obs -> {
            if (currentPlayer.get() == null) {
                return;
            }

            var duration = currentPlayer.get().getCurrentTime();
            var seconds = duration != null && !duration.equals(Duration.ZERO) ? duration.toSeconds() : 0;

            if (!timeSlider.isValueChanging()) {
                timeSlider.setValue(seconds);
            }
            currentTimeLabel.setText(seconds > 0 ? formatDuration(duration) : "0.0");
        };

        timeSlider.valueProperty().addListener(obs -> {
            if (currentPlayer.get() == null) {
                return;
            }
            long max = (long) currentPlayer.get().getMedia().getDuration().toSeconds();
            long sliderVal = (long) timeSlider.getValue();
            if (sliderVal <= max && timeSlider.isValueChanging()) {
                currentPlayer.get().seek(Duration.seconds((double) sliderVal));
            }
        });

        // remove all listeners and dispose old player
        currentPlayer.addListener((obs, old, val) -> {
            if (old != null) {
                old.stop();
                old.volumeProperty().unbind();
                old.currentTimeProperty().removeListener(mediaTimeChangeListener);
                playIcon.iconCodeProperty().unbind();
                old.dispose();
            }
        });

        Media media = mediaFile.createMedia();
        javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
       if( currentPlayer.get()!=null) {
//           Platform.runLater(() -> {
               currentPlayer.get().dispose();
//           });
       }
        currentPlayer.set(mediaPlayer);
        mediaPlayer.setOnReady(() -> {
            Image image = getTag(media, "image", Image.class, NO_IMAGE);
            coverImage.setFill(new ImagePattern(image));
//            model.setBackgroundColor(image != NO_IMAGE ? getDominantColor(image, 1.0) : null);

            trackTitle.setText(getTag(media, "title", String.class, NO_TITLE));
            trackArtist.setText(getTag(media, "artist", String.class, NO_ARTIST));
            trackAlbum.setText(getTag(media, "album", String.class, NO_ALBUM));

            timeSlider.setMax(media.getDuration().toSeconds());
            endTimeLabel.setText(formatDuration(media.getDuration()));

            playIcon.iconCodeProperty().bind(Bindings.createObjectBinding(() -> {
                if (mediaPlayer.statusProperty().get() == null) {
                    return EQUALS;
                }
                return switch (mediaPlayer.getStatus()) {
                    case READY, PAUSED, STOPPED -> PLAY_ARROW;
                    case PLAYING -> PAUSE;
                    default -> EQUALS;
                };
            }, mediaPlayer.statusProperty()));

            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
            mediaPlayer.currentTimeProperty().addListener(mediaTimeChangeListener);
        });
//        mediaPlayer.setOnEndOfMedia(model::playNext);

        currentPlayer.set(mediaPlayer);
        mediaPlayer.play();
    }
    private void createView(){

        coverImage = new Rectangle(0, 0, 150, 150);
        coverImage.setArcWidth(20.0);
        coverImage.setArcHeight(20.0);
        coverImage.setFill(new ImagePattern(NO_IMAGE));

        trackTitle = new Label(NO_TITLE);
        trackTitle.getStyleClass().add(TITLE_3);
        trackTitle.setAlignment(Pos.CENTER_LEFT);
        trackTitle.setMaxWidth(MAX_VALUE);

        trackArtist = new Label(NO_ARTIST);
        trackArtist.getStyleClass().add(TEXT_SMALL);
        trackArtist.setAlignment(Pos.CENTER_LEFT);
        trackArtist.setMaxWidth(MAX_VALUE);

        trackAlbum = new Label(NO_ALBUM);
        trackAlbum.setAlignment(Pos.CENTER_LEFT);
        trackAlbum.setMaxWidth(MAX_VALUE);
        trackAlbum.getStyleClass().add(TEXT_SMALL);

        // == MEDIA CONTROLS ==

//        var prevBtn = new Button(null, new FontIcon(FAST_REWIND));
//        prevBtn.getStyleClass().addAll(BUTTON_CIRCLE);
//        prevBtn.setTooltip(new Tooltip("Previous"));
//        prevBtn.disableProperty().bind(model.canGoBackProperty().not());
//        prevBtn.setOnAction(e -> model.playPrevious());

        playIcon = new FontIcon(PLAY_ARROW);

        playBtn = new Button(null, playIcon);
        playBtn.getStyleClass().addAll("play", BUTTON_CIRCLE);

//        var nextBtn = new Button(null, new FontIcon(FAST_FORWARD));
//        nextBtn.getStyleClass().addAll(BUTTON_CIRCLE);
//        nextBtn.disableProperty().bind(model.canGoForwardProperty().not());
//        nextBtn.setOnAction(e -> model.playNext());
//        nextBtn.setTooltip(new Tooltip("Next"));

//        var mediaControls = new HBox(20);
//        mediaControls.getStyleClass().add("media-controls");
//        mediaControls.getChildren().setAll( playBtn);
//        mediaControls.setAlignment(CENTER);

        // == TIME CONTROLS ==

        timeSlider = new Slider(0, 1, 0);
        timeSlider.setSkin(new ProgressSliderSkin(timeSlider));
        timeSlider.getStyleClass().add("time-slider");
        timeSlider.setMinWidth(PANEL_MAX_WIDTH);
        timeSlider.setMaxWidth(PANEL_MAX_WIDTH);

        currentTimeLabel = new Label("0.0");
        currentTimeLabel.getStyleClass().add(TEXT_SMALL);

        endTimeLabel = new Label("5.0");
        endTimeLabel.getStyleClass().add(TEXT_SMALL);

        var timeMarkersBox = new HBox(5);
        timeMarkersBox.getChildren().setAll(currentTimeLabel, new Spacer(), endTimeLabel);
        timeMarkersBox.setMaxWidth(PANEL_MAX_WIDTH);


        var shuffleBtn = new Button(null, new FontIcon(SHUFFLE));
        shuffleBtn.getStyleClass().addAll(BUTTON_CIRCLE);
        shuffleBtn.setTooltip(new Tooltip("Circle"));
        shuffleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               var value = currentPlayer.get().getCycleCount();
                if(value==INDEFINITE)
                {
                    currentPlayer.get().setCycleCount(0);
                }else{
                    currentPlayer.get().setCycleCount(INDEFINITE);
                }
            }
        });

        volumeSlider = new Slider(0, 1, 0.75);
        volumeSlider.setSkin(new ProgressSliderSkin(volumeSlider));
        volumeSlider.getStyleClass().add(SMALL);
        volumeSlider.setOrientation(VERTICAL);

        var volumeBar = new VBox(5);
        volumeBar.getChildren().setAll(new FontIcon(VOLUME_UP), volumeSlider, new FontIcon(VOLUME_OFF));
        volumeBar.setAlignment(CENTER);

        var volumePopover = new Popover(volumeBar);
        volumePopover.setHeaderAlwaysVisible(false);
        volumePopover.setArrowLocation(Popover.ArrowLocation.TOP_LEFT);

        var volumeBtn = new Button(null, new FontIcon(VOLUME_UP));
        volumeBtn.getStyleClass().addAll(BUTTON_CIRCLE);
        volumeBtn.setOnAction(e -> volumePopover.show(volumeBtn));

        var extraControls = new HBox(5);
        extraControls.getStyleClass().add("media-controls");
        var tackInfo = new HBox(5);
        var trackTextBox = new VBox(5);
        tackInfo.getChildren().addAll(new StackPane(coverImage),trackTextBox);
        trackTextBox.getChildren().addAll(trackTitle,trackArtist,trackAlbum);
        extraControls.getChildren().setAll( shuffleBtn,playBtn,  volumeBtn);
        extraControls.setMaxWidth(PANEL_MAX_WIDTH);
        extraControls.setAlignment(CENTER);
        BorderPane bp = new BorderPane();
//        bp.setId("player");
        timeSlider.setMaxWidth(MAX_VALUE);
        BorderPane   timeBp = new BorderPane();
        currentTimeLabel.setAlignment(CENTER);
        endTimeLabel.setAlignment(CENTER);
        timeBp.setLeft(currentTimeLabel);
        timeBp.setCenter(timeSlider);
        timeBp.setRight(endTimeLabel);
        timeBp.setPadding(new Insets(5));
        bp.setTop(timeBp);
//        bp.setA
        bp.setLeft(tackInfo);
        bp.setCenter(extraControls);

        bp.getStyleClass().add("player");
//        bp.?
        setAlignment(CENTER);
        setSpacing(5);
        setMargin(this,new Insets(10));
//        setMinWidth(200);
        getChildren().setAll(bp
        );
    }
}
