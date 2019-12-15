package com.dhf.adaptor;

/**
 * 火鸡适配器，将火鸡转为鸭子
 */
public class TurkeyAdapter implements Duck {
    Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    /**
     * 火鸡要飞3次才能到鸭子的距离，这里要表达的意思是火鸡执行鸭子的动作可能需要些变化
     */
    @Override
    public void fly() {
        this.turkey.fly();
        this.turkey.fly();
        this.turkey.fly();
    }

    @Override
    public void quack() {
        this.turkey.gobble();
    }
}
