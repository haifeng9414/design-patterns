package com.dhf;

import com.dhf.command.Goblin;
import com.dhf.command.InvisibilitySpell;
import com.dhf.command.ShrinkSpell;
import com.dhf.command.Wizard;

public class Application {
    public static void main(String[] args) {
        // 巫师能够发出命令
        Wizard wizard = new Wizard();
        // 命令执行时作用的对象
        Goblin goblin = new Goblin();

        goblin.printStatus();

        // 对哥布林施放收缩咒语
        wizard.castSpell(new ShrinkSpell(), goblin);
        goblin.printStatus();

        // 对哥布林施放消失咒语
        wizard.castSpell(new InvisibilitySpell(), goblin);
        goblin.printStatus();

        wizard.undoLastSpell();
        goblin.printStatus();

        wizard.undoLastSpell();
        goblin.printStatus();

        wizard.redoLastSpell();
        goblin.printStatus();

        wizard.redoLastSpell();
        goblin.printStatus();
    }
}
