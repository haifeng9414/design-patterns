package com.dhf.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 飞行魔法
 */
public class FlyingEnchantment implements Enchantment {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlyingEnchantment.class);

    @Override
    public void onActivate() {
        LOGGER.info("物品开始微弱地发光");
    }

    @Override
    public void apply() {
        LOGGER.info("该物品飞走并击中了敌人，最终回到了主人的手中");
    }

    @Override
    public void onDeactivate() {
        LOGGER.info("物品的光晕消失");
    }
}
