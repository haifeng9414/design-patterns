package com.dhf.observer;

/**
 * 一天中的几个时间点
 */
public enum TimePoint {
    MORNING("早晨"), NOON("中午"), AFTERNOON("下午"), EVENING("晚上");
    private String name;

    TimePoint(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
