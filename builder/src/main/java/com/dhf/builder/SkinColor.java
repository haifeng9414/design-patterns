package com.dhf.builder;

/**
 * 肤色
 */
public enum SkinColor {
    YELLOW("黄色"), BLACK("黑色"), WHITE("白色");

    private String color;

    SkinColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
