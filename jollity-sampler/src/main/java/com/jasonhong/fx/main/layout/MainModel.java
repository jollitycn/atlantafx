/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.layout;

import com.jasonhong.fx.main.event.DefaultEventBus;
import com.jasonhong.fx.main.event.NavEvent;
import com.jasonhong.fx.main.fxml.audio.musicplayer.MusicPlayerPage;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.page.general.ThemePage;
import com.jasonhong.fx.main.page.home.HomePage;
import com.jasonhong.fx.main.fxml.ocr.ImageToText;
import com.jasonhong.fx.main.page.showcase.blueprints.BlueprintsPage;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.jasonhong.fx.main.layout.MainModel.SubLayer.PAGE;
import static com.jasonhong.fx.main.layout.MainModel.SubLayer.SOURCE_CODE;

public class MainModel {

    public static final Class<? extends Page> DEFAULT_PAGE = HomePage.class;

    private static final Map<Class<? extends Page>, NavTree.Item> NAV_TREE = createNavItems();

    public enum SubLayer {
        PAGE,
        SOURCE_CODE
    }

    NavTree.Item getTreeItemForPage(Class<? extends Page> pageClass) {
        return NAV_TREE.getOrDefault(pageClass, NAV_TREE.get(DEFAULT_PAGE));
    }

    List<NavTree.Item> findPages(String filter) {
        return NAV_TREE.values().stream()
            .filter(item -> item.getValue() != null && item.getValue().matches(filter))
            .toList();
    }

    public MainModel() {
        DefaultEventBus.getInstance().subscribe(NavEvent.class, e -> navigate(e.getPage()));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Properties                                                            //
    ///////////////////////////////////////////////////////////////////////////

    // ~
    private final ReadOnlyObjectWrapper<Class<? extends Page>> selectedPage = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Class<? extends Page>> selectedPageProperty() {
        return selectedPage.getReadOnlyProperty();
    }

    // ~
    private final ReadOnlyObjectWrapper<SubLayer> currentSubLayer = new ReadOnlyObjectWrapper<>(PAGE);

    public ReadOnlyObjectProperty<SubLayer> currentSubLayerProperty() {
        return currentSubLayer.getReadOnlyProperty();
    }

    // ~
    private final ReadOnlyObjectWrapper<NavTree.Item> navTree = new ReadOnlyObjectWrapper<>(createTree());

    public ReadOnlyObjectProperty<NavTree.Item> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    private NavTree.Item createTree() {
        var home = NavTree.Item.group("首页", new FontIcon(Material2OutlinedMZ.SPEED));
        home.getChildren().setAll(
                NAV_TREE.get(HomePage.class)
        );
        var general = NavTree.Item.group("工具箱", new FontIcon(Material2OutlinedMZ.SPEED));
        general.getChildren().setAll(
                NAV_TREE.get(ImageToText.class),
                NAV_TREE.get(com.jasonhong.fx.main.fxml.tts.App.class),
                NAV_TREE.get(MusicPlayerPage.class)
        );
        general.setExpanded(false);
        var setting = NavTree.Item.group("设置", new FontIcon(Material2OutlinedMZ.SPEED));
        setting.getChildren().setAll(
                NAV_TREE.get(ThemePage.class)

        );
        general.setExpanded(false);
        var root = NavTree.Item.root();
        root.getChildren().setAll(
                home,general,setting
        );

        return root;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Nav Tree                                                              //
    ///////////////////////////////////////////////////////////////////////////

    public static Map<Class<? extends Page>, NavTree.Item> createNavItems() {
        var map = new HashMap<Class<? extends Page>, NavTree.Item>();

        // general
        map.put(HomePage.class, NavTree.Item.page(HomePage.NAME, HomePage.class));
        map.put(ThemePage.class, NavTree.Item.page(ThemePage.NAME, ThemePage.class));
        map.put(BlueprintsPage.class, NavTree.Item.page(BlueprintsPage.NAME, BlueprintsPage.class));
        map.put(com.jasonhong.fx.main.fxml.tts.App.class, NavTree.Item.page(com.jasonhong.fx.main.fxml.tts.App.NAME, com.jasonhong.fx.main.fxml.tts.App.class));
        map.put(ImageToText.class, NavTree.Item.page(ImageToText.NAME, ImageToText.class));
        map.put(MusicPlayerPage.class, NavTree.Item.page(MusicPlayerPage.NAME, MusicPlayerPage.class));
        return map;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Commands                                                              //
    ///////////////////////////////////////////////////////////////////////////

    public void navigate(Class<? extends Page> page) {
        selectedPage.set(Objects.requireNonNull(page));
        currentSubLayer.set(PAGE);
    }

    public void showSourceCode() {
        currentSubLayer.set(SOURCE_CODE);
    }

    public void hideSourceCode() {
        currentSubLayer.set(PAGE);
    }
}
