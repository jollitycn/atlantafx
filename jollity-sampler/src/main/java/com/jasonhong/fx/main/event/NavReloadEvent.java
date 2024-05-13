/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.event;

import com.jasonhong.fx.main.page.Page;

public final class NavReloadEvent extends Event {

    private final Class<? extends Page> page;

    public NavReloadEvent(Class<? extends Page> page) {
        super(page);
        this.page = page;
    }

    public Class<? extends Page> getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "NavEvent{"
            + "page=" + page
            + "} " + super.toString();
    }
}
