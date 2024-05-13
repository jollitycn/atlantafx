/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.layout;

import atlantafx.base.controls.ModalPane;
import com.jasonhong.fx.main.util.NodeUtils;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.lang.reflect.InvocationTargetException;

public final class ApplicationWindow extends AnchorPane {

    public static final int MIN_WIDTH = 1200;
    public static final int SIDEBAR_WIDTH = 250;
    public static final int BOTTOMBAR_HEIGHT = 50;
    public static final String MAIN_MODAL_ID = "modal-pane";


    public ApplicationWindow() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // this is the place to apply user custom CSS,
        // one level below the ':root'
        var body = new StackPane();
        body.getStyleClass().add("body");

        var modalPane = new ModalPane();
        modalPane.setId(MAIN_MODAL_ID);

//        body.getChildren().setAll(modalPane, new MainLayer());
        body.getChildren().setAll(modalPane, new MainLayerTab());
        NodeUtils.setAnchors(body, Insets.EMPTY);

        getChildren().setAll(body);
    }
}
