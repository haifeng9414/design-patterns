package com.dhf.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 锤子
 */
public class Hammer implements Weapon {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hammer.class);

    private final Enchantment enchantment;

    public Hammer(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public void wield() {
        LOGGER.info("拿出锤子");
        enchantment.onActivate();
    }

    @Override
    public void swing() {
        LOGGER.info("挥舞锤子");
        enchantment.apply();
    }

    @Override
    public void unWield() {
        LOGGER.info("收起锤子");
        enchantment.onDeactivate();
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }
}
