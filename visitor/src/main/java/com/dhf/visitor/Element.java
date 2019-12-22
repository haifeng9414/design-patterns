package com.dhf.visitor;

/**
 * 抽象元素角色，定义了一个accept操作，以Visitor作为参数。
 */
public interface Element {
    // 接受一个抽象访问者访问
    void accept(Visitor visitor);
}