package com.dhf.composite;

/**
 * 字母，打印行为就是打印自己，字母也继承自LetterComposite，使其具有成为组合的能力，
 * 但实际上一个字母不可再分割
 */
public class Letter extends LetterComposite {
    private char c;

    public Letter(char c) {
        this.c = c;
    }

    @Override
    public void add(LetterComposite letter) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void printThisBefore() {
        System.out.print(c);
    }
}
