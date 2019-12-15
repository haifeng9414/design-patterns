package com.dhf.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令的作用目标，拥有一些属性，具体的命令通过修改目标的属性实现命令
 */
public abstract class Target {
    private static final Logger LOGGER = LoggerFactory.getLogger(Target.class);

    private Size size;

    private Visibility visibility;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public abstract String toString();

    /**
     * Print status
     */
    public void printStatus() {
        LOGGER.info("{}, [size={}] [visibility={}]", this, getSize(), getVisibility());
    }
}
