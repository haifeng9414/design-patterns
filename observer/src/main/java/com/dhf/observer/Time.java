package com.dhf.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间
 */
public class Time {
    private static final Logger LOGGER = LoggerFactory.getLogger(Time.class);

    private TimePoint point;
    private List<TimeObserver> observers;

    public Time() {
        this.point = TimePoint.MORNING;
        observers = new ArrayList<>();
    }

    public void addObserver(TimeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TimeObserver observer) {
        observers.remove(observer);
    }

    public void passing() {
        TimePoint[] points = TimePoint.values();
        point = points[(point.ordinal() + 1) % points.length];
        LOGGER.info("时间来到了{}", point);
        notifyObservers();
    }

    public void notifyObservers() {
        for (TimeObserver observer : observers) {
            observer.update(point);
        }
    }
}
