/* SPDX-License-Identifier: MIT */

package atlantafx.base.controls;

import atlantafx.base.theme.Styles;

public class TileSkin extends TileSkinBase<Tile> {

    public TileSkin(Tile control) {
        super(control);

        control.actionProperty().addListener(actionSlotListener);
        actionSlotListener.changed(control.actionProperty(), null, control.getAction());

        pseudoClassStateChanged(Styles.STATE_INTERACTIVE, control.getActionHandler() != null);
        registerChangeListener(
            control.actionHandlerProperty(),
            o -> pseudoClassStateChanged(Styles.STATE_INTERACTIVE, getSkinnable().getActionHandler() != null)
        );

        root.setOnMouseClicked(e -> {
            if (getSkinnable().getActionHandler() != null) {
                getSkinnable().getActionHandler().run();
            }
        });
    }

    @Override
    public void dispose() {
        getSkinnable().actionProperty().removeListener(actionSlotListener);
        unregisterChangeListeners(getSkinnable().actionHandlerProperty());

        super.dispose();
    }
}