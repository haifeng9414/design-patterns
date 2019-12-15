package com.dhf.composite;

import java.util.List;

/**
 * 单词由字母组成，打印行为就是打印字母之前打印一个空格
 */
public class Word extends LetterComposite {

    /**
     * Constructor
     */
    public Word(List<Letter> letters) {
        for (Letter l : letters) {
            this.add(l);
        }
    }

    @Override
    protected void printThisBefore() {
        System.out.print(" ");
    }
}
