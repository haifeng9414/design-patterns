package com.dhf.bridge;

/**
 * 武器
 */
public interface Weapon {
    /**
     * 出鞘
     */
    void wield();

    /**
     * 挥舞
     */
    void swing();

    /**
     * 收鞘
     */
    void unWield();

    /**
     * 武器可以有魔法动作
     */
    Enchantment getEnchantment();
}
