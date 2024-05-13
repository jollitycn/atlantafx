/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.browser;

import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.Listener;
import com.jasonhong.fx.main.event.TapPaneEvent;
import com.jasonhong.fx.main.fxml.download.FileDownloadPage;
import com.jasonhong.fx.main.layout.HeaderBar;
import com.jasonhong.fx.main.page.AbstractPage;
import com.jasonhong.fx.main.page.OutlinePage;
import com.jasonhong.fx.main.page.showcase.blueprints.BlueprintsPage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.HashMap;

public  final class BrowserPage extends AbstractPage {
    private final HeaderBar headerBar = new HeaderBar();
    private TabPane tp = new TabPane();
    private Tab downloadTab=  new Tab();
    public static final String NAME = "网页浏览器";
    HashMap<TabPane, Control> panel = new HashMap<TabPane, Control>();
//    private

    @Override
    public String getName() {
        return NAME;
    }

    public BrowserPage() {
        super();
//        addPageHeader();
//        addSection("Playground", playground());//
//        getChildren().add(playground());
//       var tab = new Tab();
//        tab.setText(this.getName());
//        tab.setContent(new BlueprintsPage());
//        tab.setClosable(false);
//        tp.getTabs().add(tab);
        ;
        downloadTab.setText("下载");
        downloadTab.setContent(new FileDownloadPage());
        DefaultEventBus.getInstance().subscribe(TapPaneEvent.class,this::onTapPaneEvent);
        DefaultEventBus.getInstance().subscribe(BrowserEvent.class,this::openDownload);
        BorderPane  bp = new BorderPane();
//        headerBar
        bp.setTop(headerBar);
        bp.setCenter(tp);

//        this.
        getChildren().addAll(bp);
    }

    private void openDownload(BrowserEvent browserEvent) {
        if (browserEvent.getType() == BrowserEvent.EventType.DOWNLOAD) {

            final Tab prevPage = (Tab) tp.getTabs().stream()
                    .filter(c -> c == downloadTab)
                    .findFirst()
                    .orElse(null);

            if (prevPage == null) {
                tp.getTabs().add(downloadTab);
                tp.getSelectionModel().select(downloadTab);
            }
        }
    }


    @Listener
    private void onTapPaneEvent(TapPaneEvent event) {
        Tab tab = (Tab) event.getCotrol();
        tp.getTabs().add(tab);
        tp.getSelectionModel().select(tab);
    }

    private Side tabSide = Side.TOP;
    private boolean fullWidth = false;

    private Pane newTab(Control control) {
        var tabs = createTab(control);

        var tabsLayer = new BorderPane();
        tabsLayer.setTop(this);
        tp.getTabs().addListener((ListChangeListener<Tab>) c ->
                updateTabsWidth(tabsLayer, tp, fullWidth)
        );
        return tabsLayer;
//
//        var stack = new StackPane(tabsLayer);
//        stack.getStyleClass().add(Styles.BORDERED);
//        stack.setMinSize(600, 500);
//        return new VBox( stack);

    }



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

    public ObservableList<Tab> createTab(Control control) {
        Tab tab = new Tab(control.getAccessibleText());
        tab.setContent(control);
        tp.getTabs().add(1, tab
        );
        return  tp.getTabs();
    }


}
