package com.dhf.prototype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 餐厅代理
 */
public class DiningRoomProxy implements Room {
        private static final Logger LOGGER = LoggerFactory.getLogger(DiningRoomProxy.class);

        private Room room;
        private int customerNumber;

    public DiningRoomProxy(Room room) {
        this.room = room;
    }


    @Override
    public void enter(Customer customer) {
        if (customerNumber < 10) {
            room.enter(customer);
            customerNumber++;
            LOGGER.info("目前房间里有 {} 人", customerNumber);
        } else {
            LOGGER.info("抱歉{}，房间已经满了", customer);
        }
    }
}
