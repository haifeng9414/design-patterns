package com.dhf;

import com.dhf.facade.DwarvenGoldmineFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    public static void main(String[] args) {
        DwarvenGoldmineFacade facade = new DwarvenGoldmineFacade();
        facade.startNewDay();
        facade.digOutGold();
        facade.endDay();
    }
}
