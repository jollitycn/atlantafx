package com.jasonhong.fx.main.page.components.toolbar;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.TapPaneEvent;
import com.jasonhong.fx.main.page.components.tapPanel.WebViewTab;
import javafx.geometry.Orientation;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import static com.jasonhong.fx.main.event.TapPaneEvent.Action.NEW_TAB;
import static com.jasonhong.fx.main.page.components.ToolBarPage.iconButton;

public class BrawserToolbar extends VBox {
    private String url;
public BrawserToolbar(String url){

    // ~
    var textField = new CustomTextField(url);
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
                textField.clear();
                // 阻止事件进一步传播（可选，通常用于防止文本框自己处理Enter键）
                event.consume();
            }
    });


    var dropdown = new MenuButton("", new FontIcon(Feather.MENU));
    dropdown.getItems().setAll(
            new MenuItem("Action 1"),
            new MenuItem("Action 2"),
            new MenuItem("Action 3")
    );

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
            dropdown
    );
    getChildren().add(toolbar3);
}


}
