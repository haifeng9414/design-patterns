package com.dhf.composite;

import java.util.List;

/**
 * 句子由单词构成，打印行为就是打印完单词后打印句号
 */
public class Sentence extends LetterComposite {

    /**
     * Constructor
     */
    public Sentence(List<Word> words) {
        for (Word w : words) {
            this.add(w);
        }
    }

    @Override
    protected void printThisAfter() {
        System.out.print(".\n");
    }
}
