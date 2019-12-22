package com.dhf;

import com.dhf.observer.Northern;
import com.dhf.observer.Southern;
import com.dhf.observer.Time;

public class Application {
    public static void main(String[] args) {
        Time time = new Time();
        time.addObserver(new Northern());
        time.addObserver(new Southern());

        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
    }
}
