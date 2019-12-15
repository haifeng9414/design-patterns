package com.dhf.factory;

public class NewShip implements Ship {

    static final String DESCRIPTION = "我是一艘崭新的船";

    public String getDescription() {
        return DESCRIPTION;
    }
}
