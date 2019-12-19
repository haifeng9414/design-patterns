package com.dhf.factory;

/**
 * The interface containing method for producing objects.
 */
public interface Blacksmith {
    Weapon manufactureWeapon(WeaponType weaponType);
}
