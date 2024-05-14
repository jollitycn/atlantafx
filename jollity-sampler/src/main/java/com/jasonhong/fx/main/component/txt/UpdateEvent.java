package com.jasonhong.fx.main.component.txt;

import javafx.event.Event;
import javafx.event.EventType;

// 假设我们有一个自定义的事件类
    public   class UpdateEvent extends Event {
        public static final EventType<UpdateEvent> UPDATE_EVENT =
            new EventType<>(Event.ANY, "UPDATE_EVENT");
  
        public UpdateEvent() {  
            super(UPDATE_EVENT);  
        }  
  
        @Override  
        public EventType<? extends Event> getEventType() {  
            return UPDATE_EVENT;  
        }  
    }  