package com.dhf.interpreter;

/**
 * Expression
 */
public abstract class Expression {
    public abstract int interpret();

    @Override
    public abstract String toString();
}
