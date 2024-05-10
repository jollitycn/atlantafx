/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.event;

import java.util.Objects;

public final class PageEvent extends Event {

    public enum Action {
        SOURCE_CODE_ON,
        SOURCE_CODE_OFF
    }

    private final Action action;

    public PageEvent(Action action) {
        super(action);
        this.action = Objects.requireNonNull(action, "action");
    }

    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "ActionEvent{"
            + "action=" + action
            + "} " + super.toString();
    }
}
