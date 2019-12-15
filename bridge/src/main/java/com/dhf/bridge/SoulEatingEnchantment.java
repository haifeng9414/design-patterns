package com.dhf.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 噬魂魔法
 */
public class SoulEatingEnchantment implements Enchantment {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoulEatingEnchantment.class);

    @Override
    public void onActivate() {
        LOGGER.info("该物品散布嗜血的味道");
    }

    @Override
    public void apply() {
        LOGGER.info("该物品吞噬了敌人的灵魂");
    }

    @Override
    public void onDeactivate() {
        LOGGER.info("嗜血慢慢消失");
    }
}
