package com.dhf.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 剑
 */
public class Sword implements Weapon {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sword.class);

    private final Enchantment enchantment;

    public Sword(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public void wield() {
        LOGGER.info("剑出鞘");
        enchantment.onActivate();
    }

    @Override
    public void swing() {
        LOGGER.info("挥舞剑");
        enchantment.apply();
    }

    @Override
    public void unWield() {
        LOGGER.info("剑收鞘");
        enchantment.onDeactivate();
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }
}
