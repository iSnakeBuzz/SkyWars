package com.isnakebuzz.skywars.QueueEvents;

import com.isnakebuzz.skywars.QueueEvents.enums.QueueType;

public class QueueEvent {

    private String name;
    private Integer eventTime;
    private QueueType queueType;
    private String placeholder;

    public QueueEvent(String name, Integer eventTime, QueueType queueType, String placeholder) {
        this.name = name;
        this.eventTime = eventTime;
        this.queueType = queueType;
        this.placeholder = placeholder;
    }


    public String getName() {
        return name;
    }

    public Integer getEventTime() {
        return eventTime;
    }

    public QueueType getQueueType() {
        return queueType;
    }

    public void setEventTime(Integer eventTime) {
        this.eventTime = eventTime;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
