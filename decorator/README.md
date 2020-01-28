# 装饰器模式

## 目的
动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。

## 优点
1. 装饰类和被装饰类可以独立发展，不会相互耦合，装饰模式是继承的一个替代模式，装饰模式可以动态扩展一个实现类的功能。

## 缺点
1. 多层装饰比较复杂。

## 例子
Troll接口定义了一个巨魔的功能，SimpleTroll类只有一些简单功能，ClubbedTroll类使用装饰器模式为Troll接口的实现类提供了额外功能，代码：
```java
/**
 * Interface for trolls
 */
public interface Troll {
    void attack();

    int getAttackPower();

    void fleeBattle();
}

/**
 * SimpleTroll implements {@link Troll} interface directly.
 */
public class SimpleTroll implements Troll {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTroll.class);

    @Override
    public void attack() {
        LOGGER.info("The troll tries to grab you!");
    }

    @Override
    public int getAttackPower() {
        return 10;
    }

    @Override
    public void fleeBattle() {
        LOGGER.info("The troll shrieks in horror and runs away!");
    }
}

/**
 * Decorator that adds a club for the troll
 */
public class ClubbedTroll implements Troll {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClubbedTroll.class);

    private Troll decorated;

    public ClubbedTroll(Troll decorated) {
        this.decorated = decorated;
    }

    @Override
    public void attack() {
        decorated.attack();
        LOGGER.info("The troll swings at you with a club!");
    }

    @Override
    public int getAttackPower() {
        return decorated.getAttackPower() + 10;
    }

    @Override
    public void fleeBattle() {
        decorated.fleeBattle();
    }
}
```

使用：
```java
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // simple troll
        LOGGER.info("A simple looking troll approaches.");
        Troll troll = new SimpleTroll();
        troll.attack();
        troll.fleeBattle();
        LOGGER.info("Simple troll power {}.\n", troll.getAttackPower());

        // change the behavior of the simple troll by adding a decorator
        LOGGER.info("A troll with huge club surprises you.");
        troll = new ClubbedTroll(troll);
        troll.attack();
        troll.fleeBattle();
        LOGGER.info("Clubbed troll power {}.\n", troll.getAttackPower());
    }
}

/*
输出：
20:06:25.568 [main] INFO com.dhf.Application - A simple looking troll approaches.
20:06:25.573 [main] INFO com.dhf.decorator.SimpleTroll - The troll tries to grab you!
20:06:25.573 [main] INFO com.dhf.decorator.SimpleTroll - The troll shrieks in horror and runs away!
20:06:25.573 [main] INFO com.dhf.Application - Simple troll power 10.

20:06:25.575 [main] INFO com.dhf.Application - A troll with huge club surprises you.
20:06:25.575 [main] INFO com.dhf.decorator.SimpleTroll - The troll tries to grab you!
20:06:25.575 [main] INFO com.dhf.decorator.ClubbedTroll - The troll swings at you with a club!
20:06:25.575 [main] INFO com.dhf.decorator.SimpleTroll - The troll shrieks in horror and runs away!
20:06:25.575 [main] INFO com.dhf.Application - Clubbed troll power 20.
*/
```