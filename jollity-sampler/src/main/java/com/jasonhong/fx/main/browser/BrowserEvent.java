/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.browser;

import com.jasonhong.fx.main.event.Event;

import java.net.URI;

public final class BrowserEvent extends Event {
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public enum EventType{
    NONE,
    DOWNLOAD
}
private EventType type;
    private final URI uri;

    public BrowserEvent(URI uri) {
        super(uri);
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "BrowseEvent{"
            + "uri=" + uri
            + "} " + super.toString();
    }

    public static void fire(String url) {
        Event.publish(new BrowserEvent(URI.create(url)));
    }
}
