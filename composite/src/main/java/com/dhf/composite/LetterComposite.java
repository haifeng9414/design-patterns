package com.dhf.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合抽象类，组合中涉及到的类都继承该抽象类，赋予他们成为组合的能力，
 * 同时组合抽象类定义了组合中涉及到的类的行为，如下面的打印行为
 */
public abstract class LetterComposite {
    private List<LetterComposite> children = new ArrayList<>();

    public void add(LetterComposite letter) {
        children.add(letter);
    }

    public int count() {
        return children.size();
    }

    protected void printThisBefore() {
    }

    protected void printThisAfter() {
    }

    /**
     * 组合的行为，这里就是打印
     */
    public void print() {
        printThisBefore();
        for (LetterComposite letter : children) {
            letter.print();
        }
        printThisAfter();
    }
}


