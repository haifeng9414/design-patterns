package com.dhf;

import com.dhf.decorator.ClubbedTroll;
import com.dhf.decorator.SimpleTroll;
import com.dhf.decorator.Troll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // simple troll
        LOGGER.info("A simple looking troll approaches.");
        Troll troll = new SimpleTroll();
        troll.attack();
        troll.fleeBattle();
        LOGGER.info("Simple troll power {}.\n", troll.getAttackPower());

        // change the behavior of the simple troll by adding a decorator
        LOGGER.info("A troll with huge club surprises you.");
        troll = new ClubbedTroll(troll);
        troll.attack();
        troll.fleeBattle();
        LOGGER.info("Clubbed troll power {}.\n", troll.getAttackPower());
    }
}
