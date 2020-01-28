package com.dhf;

import com.dhf.composite.LetterComposite;
import com.dhf.composite.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Message from the orcs: ");

        // Messenger对象只是方便测试的工具类，默认创建了一个Sentence对象并添加了若干个Word对象
        LetterComposite orcMessage = new Messenger().messageFromOrcs();
        orcMessage.print();

        LOGGER.info("Message from the elves: ");

        LetterComposite elfMessage = new Messenger().messageFromElves();
        elfMessage.print();
    }
}
