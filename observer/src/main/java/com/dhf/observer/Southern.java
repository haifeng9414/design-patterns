package com.dhf.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 南方人
 */
public class Southern implements TimeObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Southern.class);

    @Override
    public void update(TimePoint time) {

        LOGGER.info("南方人吃饭了");
        switch (time) {
            case MORNING:
                LOGGER.info("热干面");
                break;
            case NOON:
                LOGGER.info("米饭");
                break;
            case AFTERNOON:
                LOGGER.info("茶");
                break;
            case EVENING:
                LOGGER.info("鱼");
                break;
            default:
                break;
        }
    }
}
