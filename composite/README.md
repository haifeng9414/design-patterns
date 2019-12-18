# 装饰者模式

## 目的
用于动态地给一个对象添加一些额外的职责。就增加功能来说，Decorator模式相比生成子类更为灵活。装饰者模式以对客户端透明的方式扩展对象的功能，是继承关系的一个替代方案。

## 优点
1. 装饰者模式和继承的作用都是对现有的类增加新的功能，但装饰者模式有着比继承更灵活的组合方式。装饰者模式可以在运行的时候决定需要增加还是去除一种“装饰”以及什么“装饰”。继承则没有这样的灵活性，它对类功能的扩展是在运行之前就确定了的。
2. 得益于装饰者模式在组合上的灵活性和便利性，可以将各种装饰类进行组合，从而较为简单的创造各种不同的行为集合，实现多种多样的功能。

## 缺点
1. 装饰者的对象和它装饰的对象本质上是完全不同的，装饰模式会生成许多的对象，导致区分各种对象变得困难。
2. 由于使用相同的标识，对于程序的理解和排错过程的难度也会随之增加。

## 例子
```java
public interface Troll {
    void attack();

    int getAttackPower();

    void fleeBattle();
}

// 普通的Troll接口实现
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

// 装饰者
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
        LOGGER.info("Message from the orcs: ");

        LetterComposite orcMessage = new Messenger().messageFromOrcs();
        orcMessage.print();

        LOGGER.info("Message from the elves: ");

        LetterComposite elfMessage = new Messenger().messageFromElves();
        elfMessage.print();
    }
}

/*
输出：
21:59:11.321 [main] INFO com.dhf.Application - A simple looking troll approaches.
21:59:11.325 [main] INFO com.dhf.decorator.SimpleTroll - The troll tries to grab you!
21:59:11.325 [main] INFO com.dhf.decorator.SimpleTroll - The troll shrieks in horror and runs away!
21:59:11.325 [main] INFO com.dhf.Application - Simple troll power 10.

21:59:11.327 [main] INFO com.dhf.Application - A troll with huge club surprises you.
21:59:11.328 [main] INFO com.dhf.decorator.SimpleTroll - The troll tries to grab you!
21:59:11.328 [main] INFO com.dhf.decorator.ClubbedTroll - The troll swings at you with a club!
21:59:11.328 [main] INFO com.dhf.decorator.SimpleTroll - The troll shrieks in horror and runs away!
21:59:11.328 [main] INFO com.dhf.Application - Clubbed troll power 20.
*/
```