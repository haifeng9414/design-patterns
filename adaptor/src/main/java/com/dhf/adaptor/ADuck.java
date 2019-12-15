package com.dhf.adaptor;

/**
 * 一个具体的会飞会叫的鸭子
 */
public class ADuck implements Duck {
    @Override
    public void fly() {
        System.out.println("a duck fly...");
    }

    @Override
    public void quack() {
        System.out.println("a duck quack");
    }
}