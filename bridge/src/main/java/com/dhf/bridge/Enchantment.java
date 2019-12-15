package com.dhf.bridge;

/**
 * 魔法动作
 */
public interface Enchantment {
    /**
     * 激活魔法
     */
    void onActivate();

    /**
     * 使用魔法
     */
    void apply();

    /**
     * 取消魔法
     */
    void onDeactivate();
}
