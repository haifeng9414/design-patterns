package com.dhf.prototype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 餐厅
 */
public class DiningRoom implements Room {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiningRoom.class);

    @Override
    public void enter(Customer customer) {
        LOGGER.info("顾客{}进来了", customer);
    }
}
