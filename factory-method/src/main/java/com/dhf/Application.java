package com.dhf;

import com.dhf.factory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final Blacksmith blacksmith;

    private Application(Blacksmith blacksmith) {
        this.blacksmith = blacksmith;
    }

    public static void main(String[] args) {
        Application app = new Application(new OrcBlacksmith());
        app.manufactureWeapons();

        app = new Application(new ElfBlacksmith());
        app.manufactureWeapons();
    }

    private void manufactureWeapons() {
        Weapon weapon;
        weapon = blacksmith.manufactureWeapon(WeaponType.SPEAR);
        LOGGER.info(weapon.toString());
        weapon = blacksmith.manufactureWeapon(WeaponType.AXE);
        LOGGER.info(weapon.toString());
    }
}
