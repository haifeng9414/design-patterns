# 适配器模式 (包装器模式)

## 简介
将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作，如鸭子和火鸡是两个没有直接关联的类型，但是他们能做相似的事，可以通过适配器将火鸡的行为适配成鸭子的行为，使得操作火鸡能够和操作鸭子一样：
```java
public interface Duck {
    /**
     * 鸭子会飞
     */
    void fly();

    /**
     * 鸭子会嘎嘎叫
     */
    void quack();
}

public interface Turkey {
    /**
     * 火鸡会飞
     */
    void fly();

    /**
     * 火鸡会咯咯叫
     */
    void gobble();
}

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

public static void main(String[] args) {
    Duck duck = new ADuck();
    Turkey turkey = new ATurkey();
    TurkeyAdapter turkeyAdapter = new TurkeyAdapter(turkey);
    duck.fly();
    turkeyAdapter.fly();
    duck.quack();
    turkeyAdapter.quack();
}
```


