/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.layout;

import com.jasonhong.fx.main.event.*;
import com.jasonhong.fx.main.layout.MainModel.SubLayer;
import com.jasonhong.fx.main.page.Page;
import com.jasonhong.fx.main.theme.ThemeManager;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Objects;

import static com.jasonhong.fx.main.event.PageEvent.Action;
import static javafx.scene.layout.Priority.ALWAYS;

public class MainLayerTab extends BorderPane {

    static final int PAGE_TRANSITION_DURATION = 500; // ms

    public  static final MainModel model = new MainModel();
    private final TabPane subLayerPane = new TabPane();
    public static final BottomStateBar BOTTOM_STATE_BAR = new BottomStateBar();

    private CodeViewer codeViewer;
    private StackPane codeViewerWrapper;
//    private  Page DEFAULT_PAGE_INSTANCE;
    public MainLayerTab() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        super();

        createView();
        initListeners();
//        DEFAULT_PAGE_INSTANCE =MainModel.DEFAULT_PAGE.getDeclaredConstructor().newInstance();
        model.navigate(MainModel.DEFAULT_PAGE);

        // keyboard navigation won't work without focus
//        Platform.runLater(sidebar::begForFocus);

        var snapshotKeys = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
        DefaultEventBus.getInstance().subscribe(HotkeyEvent.class, e -> {
            if (Objects.equals(e.getKeys(), snapshotKeys)) {
                final Page page = (Page) subLayerPane.getTabs().stream()
                    .filter(c -> c instanceof Page)
                    .findFirst()
                    .orElse(null);
                if (page == null || page.getSnapshotTarget() == null) {
                    return;
                }

                var fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
                fileChooser.setInitialFileName("snapshot.png");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png")
                );

                var file = fileChooser.showSaveDialog(getScene().getWindow());
                if (file == null) {
                    return;
                }

                try {
                    SnapshotParameters sp = new SnapshotParameters();
                    WritableImage img = page.getSnapshotTarget().snapshot(sp, null);
                    ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void createView() {
//        sidebar.setMinWidth(ApplicationWindow.SIDEBAR_WIDTH);
//        sidebar.setMaxWidth(ApplicationWindow.SIDEBAR_WIDTH);

//        codeViewer = new CodeViewer();
//
//        codeViewerWrapper = new StackPane();
//        codeViewerWrapper.getStyleClass().add("source-code");
//        codeViewerWrapper.getChildren().setAll(codeViewer);
//
//        subLayerPane.getChildren().setAll(codeViewerWrapper);
        HBox.setHgrow(subLayerPane, ALWAYS);

        // ~

        setId("main");
//         setTop(headerBar);

//        setLeft(sidebar);
        setCenter(subLayerPane);
        BOTTOM_STATE_BAR.setMaxHeight(ApplicationWindow.BOTTOMBAR_HEIGHT);
        setBottom(BOTTOM_STATE_BAR);
    }

    private void initListeners() {
        model.selectedPageProperty().addListener((obs, old, val) -> {
            if (val != null) {
                System.out.println("selectedPageProperty：" + obs+ old+ val);
                loadPage(val);
            }
        });

        DefaultEventBus.getInstance().subscribe(NavReloadEvent.class, e -> {
            System.out.println("NavReloadEvent：" + e.getPage());
            loadPage(e.getPage());
        });
        model.currentSubLayerProperty().addListener((obs, old, val) -> {
            switch (val) {
                case PAGE -> hideSourceCode();
                case SOURCE_CODE -> showSourceCode();
            }
        });

        // update code view color theme on app theme change
        DefaultEventBus.getInstance().subscribe(ThemeEvent.class, e -> {
            if (ThemeManager.getInstance().getTheme() != null
                && model.currentSubLayerProperty().get() == SubLayer.SOURCE_CODE) {
                showSourceCode();
            }
        });

        // switch to the source code and back
        DefaultEventBus.getInstance().subscribe(PageEvent.class, e -> {
            if (e.getAction() == Action.SOURCE_CODE_ON) {
                model.showSourceCode();
            }
            if (e.getAction() == Action.SOURCE_CODE_OFF) {
                model.hideSourceCode();
            }
        });
    }

    private void loadPage(Class<? extends Page> pageClass) {
        System.out.println("loadPage: " + pageClass);
        try {
//            final Tab prevPage = (Tab) subLayerPane.getTabs().stream()
//                .filter(c -> c.getContent() instanceof Page )
//                    .findFirst()
//                .orElse(null);
            Page nextPage = pageClass.getDeclaredConstructor().newInstance();
            final Tab prevPage = (Tab) subLayerPane.getTabs().stream()
                .filter(c -> c.getContent() instanceof Page && c.getText().equals(nextPage.getName()) )
                    .findFirst()
                .orElse(null);


            if(prevPage!=null) {
                var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextPage.getView());
                transition.setFromValue(0.0);
                transition.setToValue(1.0);
                transition.setOnFinished(t -> {
                    if (nextPage instanceof Pane nextPane) {
                        subLayerPane.getSelectionModel().select(prevPage);
                    }
                });
                transition.play();
                return ;
            }

            // startup, no prev page, no animation
//            if (getScene() == null || prevPage==null) {
//                Tab tab = new Tab();
//                tab.setContent(nextPage.getView());
//                subLayerPane.getTabs().add(tab);
//                return;
//            }


//            if(prevPage.getClass() == MainModel.DEFAULT_PAGE) {
//                //默認頁面從緩存裏拿
//
//            }
            // reset previous page, e.g. to free resources

//            Objects.requireNonNull(prevPage);
//            prevPage
//            prevPage.reset();
//
            // animate switching between pages
//                nextPage = DEFAULT_PAGE_INSTANCE;

            Tab tab = new Tab();
            tab.setText(nextPage.getName());
            tab.setContent(nextPage.getView());
            subLayerPane.getTabs().add(tab);
            if(nextPage.getClass().equals(MainModel.DEFAULT_PAGE)){
                tab.setClosable(false);
            }

//                subLayerPane.getTabs().remove(prevPage.getContent());

            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextPage.getView());
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(t -> {
                if (nextPage instanceof Pane nextPane) {
                    subLayerPane.getSelectionModel().select(tab);
                }
            });
            transition.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showSourceCode() {
        var sourceClass = Objects.requireNonNull(model.selectedPageProperty().get());
        var sourceFileName = sourceClass.getSimpleName() + ".java";
        try (var stream = sourceClass.getResourceAsStream(sourceFileName)) {
            Objects.requireNonNull(stream, "Missing source file '" + sourceFileName + "';");

            // set syntax highlight theme according to JavaFX theme
            ThemeManager tm = ThemeManager.getInstance();
            codeViewer.setContent(stream, tm.getMatchingSourceCodeHighlightTheme(tm.getTheme()));
            codeViewer.setVisible(true);
            codeViewerWrapper.toFront();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void hideSourceCode() {
        codeViewerWrapper.toBack();
        codeViewer.setVisible(false);
    }
}
