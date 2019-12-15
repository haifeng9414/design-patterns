package com.dhf;

import com.dhf.bridge.FlyingEnchantment;
import com.dhf.bridge.Hammer;
import com.dhf.bridge.SoulEatingEnchantment;
import com.dhf.bridge.Sword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("骑士收到一把噬魂的附魔剑");
        Sword enchantedSword = new Sword(new SoulEatingEnchantment());
        enchantedSword.wield();
        enchantedSword.swing();
        enchantedSword.unWield();

        LOGGER.info("骑士收到一把会飞的附魔剑");
        enchantedSword = new Sword(new FlyingEnchantment());
        enchantedSword.wield();
        enchantedSword.swing();
        enchantedSword.unWield();

        LOGGER.info("女武神得到了一个会飞的附魔锤子");
        Hammer hammer = new Hammer(new FlyingEnchantment());
        hammer.wield();
        hammer.swing();
        hammer.unWield();
    }
}
