package com.dhf.singleton;

/**
 * “饿汉式” 的单例实现方式
 * 可以保证线程安全
 */
public final class Director {
    /**
     * 静态的本类实例
     */
    private static final Director INSTANCE = new Director();

    /**
     * 私有化的构造方法保证不被其它类调用
     */
    private Director() {
    }

    /**
     * 客户端调用获取单例实例
     *
     * @return 单例实例
     */
    public static Director getInstance() {
        return INSTANCE;
    }
}
