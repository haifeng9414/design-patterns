package com.dhf.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 巫师能够发出咒语，也就是发出命令
 */
public class Wizard {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wizard.class);

    // 保存上一次执行的命令，当需要undo时从该队列获取上一次命令并调用命令的undo方法
    private Deque<Command> undoStack = new LinkedList<>();
    // 保存上一次执行undo方法的命令，当需要redo时从该队列获取上一次undo的命令并调用命令的redo方法
    private Deque<Command> redoStack = new LinkedList<>();

    /**
     * 施放咒语
     */
    public void castSpell(Command command, Target target) {
        LOGGER.info("{} casts {} at {}", this, command, target);
        command.execute(target);
        undoStack.offerLast(command);
    }

    /**
     * Undo last spell
     */
    public void undoLastSpell() {
        if (!undoStack.isEmpty()) {
            Command previousSpell = undoStack.pollLast();
            redoStack.offerLast(previousSpell);
            LOGGER.info("{} undoes {}", this, previousSpell);
            previousSpell.undo();
        }
    }

    /**
     * Redo last spell
     */
    public void redoLastSpell() {
        if (!redoStack.isEmpty()) {
            Command previousSpell = redoStack.pollLast();
            undoStack.offerLast(previousSpell);
            LOGGER.info("{} redoes {}", this, previousSpell);
            previousSpell.redo();
        }
    }

    @Override
    public String toString() {
        return "Wizard";
    }
}
