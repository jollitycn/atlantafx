/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.layout;

import com.jasonhong.fx.main.page.components.toolbar.BrawserToolbar;
import javafx.scene.layout.HBox;


public final class HeaderBar extends HBox {

    public static final int MIN_WIDTH = 1200;
    public static final int SIDEBAR_WIDTH = 250;
    public static final String MAIN_MODAL_ID = "modal-pane";


    public HeaderBar() {

        getChildren().add(new BrawserToolbar("http://www.google.com"));
    }
}
