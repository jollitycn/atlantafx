/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.browser;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import com.jasonhong.core.common.Callback;
import com.jasonhong.core.common.Result;
import com.jasonhong.core.common.Status;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.ScreenCaptureEvent;
import com.jasonhong.fx.main.event.TapPaneEvent;
import com.jasonhong.fx.main.page.components.tapPanel.WebViewTab;
import com.jasonhong.fx.main.page.components.toolbar.BrawserToolbar;
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
import javafx.scene.layout.Region;
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
import static java.lang.Double.MAX_VALUE;


public final class HeaderBar extends ToolBar {
    public HeaderBar() {
        textField = new TextField("http://weread.qq.com");
//        textField.setPromptText("Search Doodle of type an URL");
//        textField.setLeft(new FontIcon(Feather.LOCK));
//        textField.setRight(new FontIcon(Feather.STAR));
        textField.setMaxWidth(MAX_VALUE);
        textField.setMinWidth(800);
//        textField.setMinWidth(MAX_VALUE);
        // 创建一个 HBox 来包装 textField，并设置其 Hgrow 为 ALWAYS
        textFieldContainer = new HBox(textField);
        HBox.setHgrow(textField, Priority.ALWAYS); // 让 textField 撑满 HBox
        textFieldContainer.setMaxWidth(MAX_VALUE);

        Button btnSnager=  iconButton(Feather.INBOX);

        var dropdown = new MenuButton("", new FontIcon(Feather.MENU));

        dropdown.getItems().setAll(
                getDownload()
        );

        DefaultEventBus.getInstance().subscribe(ScreenCaptureEvent.class,this::handleScreen);



        getItems().addAll(iconButton(Feather.CHEVRON_LEFT),
                iconButton(Feather.CHEVRON_RIGHT),
                new Separator(Orientation.VERTICAL),
                iconButton(Feather.REFRESH_CW),
                textFieldContainer,
                new Spacer(),
                iconButton(Feather.BOOKMARK),
                iconButton(Feather.USER),
                btnSnager,
                dropdown);
//        this.maxHeight(MAX_VALUE);


        btnSnager.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ScreenCaptureApp.handleSnapshot(mouseEvent);
            }
        });

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
    }

    Region spacer;
    HBox textFieldContainer;
    TextField textField;

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
