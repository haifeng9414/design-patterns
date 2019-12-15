package com.dhf;

import com.dhf.adaptor.*;

public class Application {
    public static void main(String[] args) {
        Duck duck = new ADuck();
        Turkey turkey = new ATurkey();
        TurkeyAdapter turkeyAdapter = new TurkeyAdapter(turkey);
        duck.fly();
        turkeyAdapter.fly();
        duck.quack();
        turkeyAdapter.quack();
    }
}
