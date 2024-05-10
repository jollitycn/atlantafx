/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.event;

import com.jasonhong.fx.main.util.BeanUtil;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;

import java.util.Objects;

public final class TapPaneEvent extends Event {

    public Object getCotrol() {
        return cotrol;
    }

    public enum Action {
        NEW_TAB,
        SOURCE_CODE_OFF
    }

    private final Action action;
private Object cotrol = null;
    public TapPaneEvent(Action action, Control cotrol)
    {
        super(cotrol);
        this.cotrol = cotrol;
        this.action = Objects.requireNonNull(action, "action");
    }

    public TapPaneEvent(Action action, Tab tab) {
        super(action);
        this.cotrol = tab;
        this.action = Objects.requireNonNull(action, "action");
    }


    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return BeanUtil.toString(this);
    }
}
