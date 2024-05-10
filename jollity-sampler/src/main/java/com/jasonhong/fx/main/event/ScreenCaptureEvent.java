/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.event;

import java.util.UUID;

public   class ScreenCaptureEvent extends  Event {

    protected final UUID id = UUID.randomUUID();
    private final Action action;

    public Action getAction() {
        return action;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public enum Action{
    CAPTURED;

}

private Object object;
    public ScreenCaptureEvent(Action action1, Object object) {
        super(object);
        this.setObject(object);
        this.action = action1;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScreenCaptureEvent event)) {
            return false;
        }
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + '}';
    }

    public static <E extends ScreenCaptureEvent> void publish(E event) {
        DefaultEventBus.getInstance().publish(event);
    }
}
