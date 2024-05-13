package com.jasonhong.fx.main.page.components.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.jasonhong.core.common.Callback;
import com.jasonhong.core.common.Result;
import com.jasonhong.core.common.Status;
import com.jasonhong.fx.main.browser.BrowserEvent;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.ScreenCaptureEvent;
import com.jasonhong.fx.main.event.TapPaneEvent;
import com.jasonhong.fx.main.page.components.tapPanel.WebViewTab;
import com.jasonhong.fx.main.util.MediaPlayerUtil;
import com.jasonhong.fx.main.util.ScreenCaptureApp;
import com.jasonhong.ocr.TesseractOCR;
import com.jasonhong.services.mq.tts.client.TextToSpeakMqttPublisher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.jasonhong.fx.main.event.TapPaneEvent.Action.NEW_TAB;

public class BrawserToolbar extends VBox {

    CustomTextField textField;
public BrawserToolbar(String url){

    // ~ = new CustomTextField(url);
    textField.setPromptText("Search Doodle of type an URL");
    textField.setLeft(new FontIcon(Feather.LOCK));
    textField.setRight(new FontIcon(Feather.STAR));
    HBox.setHgrow(textField, Priority.ALWAYS);
    textField.setOnKeyPressed(event -> {
        if (event.getCode().equals(KeyCode.ENTER)) {
                // 按下Enter键时，触发发送按钮的点击事件
//                sendButton.fire();
                // 如果你想在发送后清空文本框，可以添加以下代码
            TapPaneEvent tp=    new TapPaneEvent(NEW_TAB,new WebViewTab(textField.getText()));
            DefaultEventBus.getInstance().publish(tp);
//                textField.clear();
                // 阻止事件进一步传播（可选，通常用于防止文本框自己处理Enter键）
                event.consume();
            }
    });


    var dropdown = new MenuButton("", new FontIcon(Feather.MENU));

    dropdown.getItems().setAll(
            getDownload()
    );
    Button btnSnager=  iconButton(Feather.INBOX);
    btnSnager.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ScreenCaptureApp.handleSnapshot(mouseEvent);
        }
    });

    DefaultEventBus.getInstance().subscribe(ScreenCaptureEvent.class,this::handleScreen);

    final var toolbar3 = new ToolBar(
            iconButton(Feather.CHEVRON_LEFT),
            iconButton(Feather.CHEVRON_RIGHT),
            new Separator(Orientation.VERTICAL),
            iconButton(Feather.REFRESH_CW),
            new Spacer(10),
            textField,
            new Spacer(10),
            iconButton(Feather.BOOKMARK),
            iconButton(Feather.USER),
            btnSnager,
            dropdown
    );
    getChildren().add(toolbar3);
}

    private MenuItem getDownload() {
        var downlaod = new MenuItem("下载");
        downlaod.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BrowserEvent tp = new BrowserEvent(URI.create(textField.getText()));
                tp.setType(BrowserEvent.EventType.DOWNLOAD);
                DefaultEventBus.getInstance().publish(tp);
            }
        });
        return downlaod;
    }

    private void handleScreen(ScreenCaptureEvent screenCaptureEvent) {
        final File[] file = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = null;
                    text = TesseractOCR.getText((BufferedImage) screenCaptureEvent.getObject());
                System.out.println(text);
                try {
                    Callback<Result> callback = new Callback<Result>() {
                        @Override
                        public void onAction(Result result) {

                        }

                        @Override
                        public void onResult(Result result) {
                            Platform.runLater(()->{
                            if (result.getCode() == Status.SUCCESS) {
                                MediaPlayerUtil.play((File) result.getData());
                            }
                            });
                        }

                        @Override
                        public void onError(Result result, Exception e) {

                        }
                    };
                    file[0] = TextToSpeakMqttPublisher.sendOnes(text,callback);
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

//zusai 可以加回调
//        while (file[0] == null) {
//        }
//        MediaPlayerUtil.play(file[0]);


    }

    public static Button iconButton(Ikon icon) {
        var btn = new Button(null);
        if (icon != null) {
            btn.setGraphic(new FontIcon(icon));
        }
        btn.getStyleClass().addAll(Styles.BUTTON_ICON);

        return btn;
    }


}
