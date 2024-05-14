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
import javafx.scene.layout.Region;
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
import static java.lang.Double.MAX_VALUE;

public class BrawserToolbar extends VBox {

    ;

}
