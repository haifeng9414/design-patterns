# 门面模式

## 目的
外部与一个子系统的通信必须通过一个统一的外观对象进行，为子系统中的一组接口提供一个高层接口，这个接口使得这一子系统更加容易使用，门面模式又称为外观模式。

更通俗点讲，假设有一个系统A，提供了a、b、c、d四个接口。系统B完成某个业务功能，需要调用A系统的a、b、d接口。利用门面模式，提供一个包裹a、b、d接口调用的门面接口x，给系统B直接使用，这样不仅提高了系统的易用性，还减少了系统间交互的次数，如果系统B需要通过网络请求调用系统A的接口，那么通过门面模式包裹的a、b、d接口调用，将3次网络请求减少为1次。

门面模式通常用于解决：
- 易用性问题，门面模式可以用来封装系统的底层实现，隐藏系统的复杂性，提供一组更加简单易用、更高层的接口。比如，Linux系统调用函数就可以看作一种门面。它是Linux操作系统暴露给开发者的一组特殊的编程接口，它封装了底层更基础的Linux内核调用。从隐藏实现复杂性，提供更易用接口这个意图来看，门面模式有点类似之前讲到的迪米特法则（最少知识原则）和接口隔离原则
- 性能问题，如之前的例子，门面模式可以减少系统间调用的次数，减少网络通信成本。从代码实现的角度来看，通过门面模式提供的门面接口和普通的接口方法应该如何组织呢？如果门面接口不多，完全可以将它跟非门面接口放到一块，也不需要特殊标记，当作普通接口来用即可。如果门面接口很多，可以在已有的接口之上，再重新抽象出一层，专门放置门面接口，从类、包的命名上跟原来的接口层做区分。如果门面接口特别多，并且很多都是跨多个子系统的，可以将门面接口放到一个新的子系统中。

## 优点
1. 对客户屏蔽子系统组件，减少了客户处理的对象数目并使得子系统使用起来更加容易。通过引入们米娜模式，客户代码将变得很简单，与之关联的对象也很少。
2. 实现了子系统与客户之间的松耦合关系，这使得子系统的组件变化不会影响到调用它的客户类，只需要调整外观类即可。

## 缺点
1. 不能很好地限制客户使用子系统类，如果对客户访问子系统类做太多的限制则减少了可变性和灵活性。

## 例子
DwarvenMineWorker抽象类有相对复杂的实现和方法，同时其具有多个实现类，为了方便客户端的调用，提供一个Facade类，该类提供了若干个简介的方法，方法的实现封装了对DwarvenMineWorker抽象类的操作逻辑，客户端只需要调用Facade类的方法就能完成对应的对DwarvenMineWorker抽象类的操作：
```java
// 复杂的抽象类
public abstract class DwarvenMineWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DwarvenMineWorker.class);

    public void goToSleep() {
        LOGGER.info("{} goes to sleep.", name());
    }

    public void wakeUp() {
        LOGGER.info("{} wakes up.", name());
    }

    public void goHome() {
        LOGGER.info("{} goes home.", name());
    }

    public void goToMine() {
        LOGGER.info("{} goes to the mine.", name());
    }

    private void action(Action action) {
        switch (action) {
            case GO_TO_SLEEP:
                goToSleep();
                break;
            case WAKE_UP:
                wakeUp();
                break;
            case GO_HOME:
                goHome();
                break;
            case GO_TO_MINE:
                goToMine();
                break;
            case WORK:
                work();
                break;
            default:
                LOGGER.info("Undefined action");
                break;
        }
    }

    /**
     * Perform actions
     */
    public void action(Action... actions) {
        for (Action action : actions) {
            action(action);
        }
    }

    public abstract void work();

    public abstract String name();

    static enum Action {
        GO_TO_SLEEP, WAKE_UP, GO_HOME, GO_TO_MINE, WORK
    }
}

// 复杂抽象类的实现
public class DwarvenGoldDigger extends DwarvenMineWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DwarvenGoldDigger.class);

    @Override
    public void work() {
        LOGGER.info("{} digs for gold.", name());
    }

    @Override
    public String name() {
        return "Dwarf gold digger";
    }
}

// 复杂抽象类的实现
public class DwarvenCartOperator extends DwarvenMineWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DwarvenCartOperator.class);

    @Override
    public void work() {
        LOGGER.info("{} moves gold chunks out of the mine.", name());
    }

    @Override
    public String name() {
        return "Dwarf cart operator";
    }
}

// 复杂抽象类的实现
public class DwarvenTunnelDigger extends DwarvenMineWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DwarvenTunnelDigger.class);

    @Override
    public void work() {
        LOGGER.info("{} creates another promising tunnel.", name());
    }

    @Override
    public String name() {
        return "Dwarven tunnel digger";
    }
}

// Facade类封装了对DwarvenMineWorker抽象类的操作
public class DwarvenGoldmineFacade {
    private final List<DwarvenMineWorker> workers;

    /**
     * Constructor
     */
    public DwarvenGoldmineFacade() {
        workers = new ArrayList<>();
        workers.add(new DwarvenGoldDigger());
        workers.add(new DwarvenCartOperator());
        workers.add(new DwarvenTunnelDigger());
    }

    private static void makeActions(Collection<DwarvenMineWorker> workers,
                                    DwarvenMineWorker.Action... actions) {
        for (DwarvenMineWorker worker : workers) {
            worker.action(actions);
        }
    }

    public void startNewDay() {
        makeActions(workers, DwarvenMineWorker.Action.WAKE_UP, DwarvenMineWorker.Action.GO_TO_MINE);
    }

    public void digOutGold() {
        makeActions(workers, DwarvenMineWorker.Action.WORK);
    }

    public void endDay() {
        makeActions(workers, DwarvenMineWorker.Action.GO_HOME, DwarvenMineWorker.Action.GO_TO_SLEEP);
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        DwarvenGoldmineFacade facade = new DwarvenGoldmineFacade();
        facade.startNewDay();
        facade.digOutGold();
        facade.endDay();
    }
}

/*
输出：
22:23:02.125 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf gold digger wakes up.
22:23:02.129 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf gold digger goes to the mine.
22:23:02.129 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf cart operator wakes up.
22:23:02.129 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf cart operator goes to the mine.
22:23:02.129 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarven tunnel digger wakes up.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarven tunnel digger goes to the mine.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenGoldDigger - Dwarf gold digger digs for gold.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenCartOperator - Dwarf cart operator moves gold chunks out of the mine.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenTunnelDigger - Dwarven tunnel digger creates another promising tunnel.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf gold digger goes home.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf gold digger goes to sleep.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf cart operator goes home.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarf cart operator goes to sleep.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarven tunnel digger goes home.
22:23:02.130 [main] INFO com.dhf.facade.DwarvenMineWorker - Dwarven tunnel digger goes to sleep.
*/
```