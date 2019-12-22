package com.dhf.prototype;

/**
 * Interface for the factory class.
 */
public interface HeroFactory {
    Mage createMage();

    Warlord createWarlord();

    Beast createBeast();

}
