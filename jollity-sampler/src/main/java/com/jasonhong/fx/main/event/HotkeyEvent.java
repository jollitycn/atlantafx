/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.event;

import javafx.scene.input.KeyCodeCombination;

public final class HotkeyEvent extends Event {

    private final KeyCodeCombination keys;

    public HotkeyEvent(KeyCodeCombination keys) {
        super(keys);
        this.keys = keys;
    }

    public KeyCodeCombination getKeys() {
        return keys;
    }

    @Override
    public String toString() {
        return "HotkeyEvent{"
            + "keys=" + keys
            + "} " + super.toString();
    }
}
