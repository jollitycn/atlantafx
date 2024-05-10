/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.page.general;

import com.jasonhong.fx.main.layout.ModalDialog;
import com.jasonhong.fx.main.util.JColorUtils;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.paint.Color;

class ContrastCheckerDialog extends ModalDialog {

    private final ContrastChecker contrastChecker;

    public ContrastCheckerDialog(ReadOnlyObjectProperty<Color> bgBaseColor) {
        super();

        this.contrastChecker = new ContrastChecker(bgBaseColor);

        contrastChecker.bgColorProperty().addListener((obs, old, val) -> updateStyle());
        contrastChecker.fgColorProperty().addListener((obs, old, val) -> updateStyle());

        getStyleClass().add("contrast-checker-dialog");
        header.setTitle("Contrast Checker");
        content.setBody(contrastChecker);
        content.setFooter(null);
    }

    private void updateStyle() {
        setStyle(String.format("-color-contrast-checker-bg:%s;-color-contrast-checker-fg:%s;",
            JColorUtils.toHexWithAlpha(contrastChecker.getFlatBgColor()),
            JColorUtils.toHexWithAlpha(contrastChecker.getSafeFgColor())
        ));
    }

    public ContrastChecker getContent() {
        return contrastChecker;
    }
}
