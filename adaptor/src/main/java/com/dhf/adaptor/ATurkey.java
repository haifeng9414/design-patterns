package com.dhf.adaptor;

/**
 * 一个具体的会飞会叫的火鸡
 */
public class ATurkey implements Turkey {
    @Override
    public void fly() {
        System.out.println("a turkey fly...");
    }

    @Override
    public void gobble() {
        System.out.println("a turkey gobble...");
    }
}