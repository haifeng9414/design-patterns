package com.dhf.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 北方人
 */
public class Northern implements TimeObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Northern.class);

    @Override
    public void update(TimePoint time) {

        LOGGER.info("北方人吃饭了");
        switch (time) {
            case MORNING:
                LOGGER.info("煎饼果子");
                break;
            case NOON:
                LOGGER.info("炸酱面");
                break;
            case AFTERNOON:
                LOGGER.info("牛奶");
                break;
            case EVENING:
                LOGGER.info("包子");
                break;
            default:
                break;
        }
    }
}
