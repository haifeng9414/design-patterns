# 门面模式

## 目的
外部与一个子系统的通信必须通过一个统一的外观对象进行，为子系统中的一组接口提供一个高层接口，这个接口使得这一子系统更加容易使用。门面模式又称为外观模式。

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