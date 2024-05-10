/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.layout;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;


public final class BottomStateBar extends HBox {

    public static final int MIN_WIDTH = 1200;
    public static final int SIDEBAR_WIDTH = 250;
    public static final String MAIN_MODAL_ID = "modal-pane";
    Label statusLabel;
    Label infoLabel;
    Label PGORGRESS_BAR_LABEL;
    ProgressBar PGORGRESS_BAR;

    public BottomStateBar() {
        // this is the place to apply user custom CSS,
        // one level below the ':root'
        setId("bottomHBox");
        setAlignment(Pos.CENTER_RIGHT);
        var buttom = new HBox();
        buttom.setId("bottomHBox");
        buttom.setAlignment(Pos.CENTER_RIGHT);
        infoLabel = new Label();
//        versionLabel.setId("versionLabel");
//        versionLabel.setText("版本 1.0.0");
        PGORGRESS_BAR = new ProgressBar ();
        PGORGRESS_BAR.getStyleClass().add(Styles.SMALL);
        PGORGRESS_BAR.setId("progressBarLabelInfo");
        PGORGRESS_BAR_LABEL = new Label();
        PGORGRESS_BAR_LABEL.setId("progressBarLabel");
//        progressBarLabel.setText("版本 1.0.0");
//        Separator sp  = new Separator();

        statusLabel = new Label();
        statusLabel.setId("statusLabel");
        statusLabel.setText("状态：正常");
        buttom.getStyleClass().add("buttom");


//        body.getChildren().setAll(modalPane);
//        NodeUtils.setAnchors(body, Insets.EMPTY);

        getChildren().addAll(infoLabel, PGORGRESS_BAR, PGORGRESS_BAR_LABEL, new Separator(Orientation.VERTICAL), statusLabel);
    }

    public void updateProgress(String info, int completedSteps, int totalSteps) {
        Platform.runLater(() -> {
            infoLabel.setText(info);
            PGORGRESS_BAR.setProgress((double) completedSteps / totalSteps);
            PGORGRESS_BAR_LABEL.setText(completedSteps + "/" + totalSteps);
        });
    }

    public void initProgress() {
        Platform.runLater(() -> {
        PGORGRESS_BAR.setVisible(true);
        PGORGRESS_BAR_LABEL.setVisible(true);
        PGORGRESS_BAR_LABEL.setText("");
        PGORGRESS_BAR.setProgress(0); });
    }

    public void completeProgress() {
        PGORGRESS_BAR.setVisible(false);
        PGORGRESS_BAR_LABEL.setVisible(false);
    }
}
