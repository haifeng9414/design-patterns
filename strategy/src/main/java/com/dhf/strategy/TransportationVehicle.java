package com.dhf.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 驾车
 */
public class TransportationVehicle implements TransportationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationVehicle.class);

    @Override
    public void go() {
        LOGGER.info("驾车从北京去天津");
    }
}
