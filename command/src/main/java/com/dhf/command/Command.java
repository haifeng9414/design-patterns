package com.dhf.command;

/**
 * 命令接口
 */
public abstract class Command {
    /**
     * 命令能够针对某个目标执行动作
     */
    public abstract void execute(Target target);

    /**
     * 回滚执行的动作
     */
    public abstract void undo();

    /**
     * 重放执行的动作
     */
    public abstract void redo();

    @Override
    public abstract String toString();
}
