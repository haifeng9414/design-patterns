package com.dhf.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 乘飞机
 */
public class TransportationAirplane implements TransportationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationAirplane.class);

    @Override
    public void go() {
        LOGGER.info("乘飞机从北京去广州");
    }
}
