/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.browser;

import com.jasonhong.fx.main.page.components.toolbar.BrawserToolbar;
import javafx.scene.layout.HBox;


public final class HeaderBar extends HBox {

    public static final String MAIN_MODAL_ID = "modal-pane";


    public HeaderBar() {

//        BorderPane bp = new BorderPane();
//        var hb1 = new HBox();
//        bp.setLeft(hb1);
        var brawserToolbar = new BrawserToolbar("http://www.google.com");
//        VBox.setVgrow(brawserToolbar, Priority.ALWAYS);
//        bp.setCenter(brawserToolbar);
//        var hb2 = new HBox();
//        bp.setRight(hb2);
        getChildren().addAll(brawserToolbar);
    }
}
