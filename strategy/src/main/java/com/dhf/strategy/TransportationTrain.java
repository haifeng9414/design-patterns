package com.dhf.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 乘火车
 */
public class TransportationTrain implements TransportationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationTrain.class);

    @Override
    public void go() {
        LOGGER.info("乘高铁从北京去上海");
    }
}
