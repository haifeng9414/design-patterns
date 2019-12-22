package com.dhf.strategy;

/**
 * 交通方式策略
 */
@FunctionalInterface
public interface TransportationStrategy {
    void go();
}
