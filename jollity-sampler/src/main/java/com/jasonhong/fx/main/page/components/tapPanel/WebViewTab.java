/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.page.components.tapPanel;

import javafx.geometry.Side;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.HashMap;

public final class WebViewTab extends Tab {
    public static final String NAME = "TabPane";
HashMap<TabPane,Control> panel = new HashMap<TabPane,Control>();

    public WebViewTab(String url) {
        super();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // 加载网页
        webEngine.load(url);

        StackPane root = new StackPane();
        setText(url);
        setContent(webView);
    }
    private boolean fullWidth = false;


    private void updateTabsWidth(BorderPane borderPane, TabPane tabs, boolean val) {
        tabs.tabMinWidthProperty().unbind();

        // reset tab width
        if (!val) {
            tabs.setTabMinWidth(Region.USE_COMPUTED_SIZE);
            return;
        }

        // There are two issues with full-width tabs.
        // - minWidth is applied to the tab itself but to internal .tab-container,
        //   thus we have to subtract tab paddings that are normally set via CSS.
        // - .control-buttons-tab appears automatically and can't be disabled via
        //   TabPane property.
        // Overall this feature should be supported by the TabPane internally, otherwise
        // it's hard to make it work properly.

        if (tabs.getSide() == Side.TOP || tabs.getSide() == Side.BOTTOM) {
            tabs.tabMinWidthProperty().bind(borderPane.widthProperty()
                    .subtract(18) // .control-buttons-tab width
                    .divide(tabs.getTabs().size())
                    .subtract(28) // .tab paddings
            );
        }
        if (tabs.getSide() == Side.LEFT || tabs.getSide() == Side.RIGHT) {
            tabs.tabMinWidthProperty().bind(borderPane.heightProperty()
                    .subtract(18) // same as above
                    .divide(tabs.getTabs().size())
                    .subtract(28)
            );
        }
    }

    public TabPane createTabPane(Control control) {
        var tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setMinHeight(60);

        // NOTE: Individually disabled tab is still closeable even while it looks
        //       like disabled. To prevent it from closing one can use "black hole"
        //       event handler. #javafx-bug
        tabs.getTabs().addAll(
//                createRandomTab(),
//                createRandomTab(),
//                createRandomTab()
        );
//        tabs.setStyle("");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        return tabs;
    }
}
