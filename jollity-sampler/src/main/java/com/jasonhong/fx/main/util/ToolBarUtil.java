package com.jasonhong.fx.main.util;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ToolBarUtil {
    public static class TopBar extends VBox {

        private static final Region DUMMY_MENUBAR = new Region();
        private static final Region DUMMY_TOOLBAR = new Region();

        public TopBar(ToolBar toolBar) {
            super();
            getChildren().setAll(DUMMY_MENUBAR, toolBar);
        }

//        public void showOrCreateMenuBar() {
//            if (getChildren().get(0) instanceof MenuBar menuBar) {
//                menuBar.setVisible(true);
//                menuBar.setManaged(true);
//            } else {
//                getChildren().set(0, new SampleMenuBar(FAKER));
//            }
//        }

        public void hideMenuBar() {
            var any = getChildren().get(0);
            any.setVisible(false);
            any.setManaged(false);
        }

        public void setToolBar(ToolBar toolBar) {
            getChildren().set(1, toolBar);
        }

        public void removeToolBar() {
            getChildren().set(1, DUMMY_TOOLBAR);
        }
    }

    public  static void rotateToolbar(BorderPane borderPane, ToolBar toolbar, Side pos) {

        try {
            var topBar = (TopBar) borderPane.getTop();

            boolean changed = borderPane.getChildren().removeAll(toolbar);
            if (!changed) {
                topBar.removeToolBar();
            }
        }catch (Exception e){
//            e.printStackTrace();
        }
        // WARNING:
        // Rotating existing buttons seems tempting, but it won't work.
        // JavaFX doesn't recalculate their size correctly (even after
        // reattaching controls to the scene), and you'll end up creating
        // new objects anyway.

        Platform.runLater(() -> {
            switch (pos) {
                case TOP -> {
                    toolbar.setOrientation(Orientation.HORIZONTAL);
                    Styles.addStyleClass(
                            toolbar, Styles.TOP, Styles.RIGHT, Styles.BOTTOM, Styles.LEFT
                    );
//                    toolbar.getItems().setAll(createButtons(Orientation.HORIZONTAL));
//                    topBar.setToolBar(toolbar);
                }
                case RIGHT -> {
                    toolbar.setOrientation(Orientation.VERTICAL);
                    Styles.addStyleClass(
                            toolbar, Styles.RIGHT, Styles.TOP, Styles.BOTTOM, Styles.LEFT
                    );
//                    toolbar.getItems().setAll(createButtons(Orientation.VERTICAL));
                    borderPane.setRight(toolbar);
                }
                case BOTTOM -> {
                    toolbar.setOrientation(Orientation.HORIZONTAL);
                    Styles.addStyleClass(
                            toolbar, Styles.BOTTOM, Styles.TOP, Styles.RIGHT, Styles.LEFT
                    );
//                    toolbar.getItems().setAll(createButtons(Orientation.HORIZONTAL));
                    borderPane.setBottom(toolbar);
                }
                case LEFT -> {
                    toolbar.setOrientation(Orientation.VERTICAL);
                    Styles.addStyleClass(
                            toolbar, Styles.LEFT, Styles.RIGHT, Styles.TOP, Styles.BOTTOM
                    );
//                    toolbar.getItems().setAll(createButtons(Orientation.VERTICAL));
                    borderPane.setLeft(toolbar);
                }
            }
        });
    }
}
