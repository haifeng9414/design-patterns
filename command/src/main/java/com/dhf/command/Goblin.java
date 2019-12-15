package com.dhf.command;

/**
 * 哥布林是一个具体的命令目标，默认拥有一些属性
 */
public class Goblin extends Target {
    public Goblin() {
        setSize(Size.NORMAL);
        setVisibility(Visibility.VISIBLE);
    }

    @Override
    public String toString() {
        return "Goblin";
    }
}
