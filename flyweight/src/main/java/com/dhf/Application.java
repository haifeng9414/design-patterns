package com.dhf;

import com.dhf.flyweight.AlchemistShop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    public static void main(String[] args) {
        AlchemistShop alchemistShop = new AlchemistShop();
        alchemistShop.enumerate();
    }
}
