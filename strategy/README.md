# 策略模式

## 目的
完成一项任务，往往可以有多种不同的方式，每一种方式称为一个策略，可以根据环境或者条件的不同选择不同的策略来完成该项任务。

## 优点
1. 策略模式提供了对“开闭原则”的完美支持，用户可以在不修改原有系统的基础上选择算法或行为，也可以灵活地增加新的算法或行为。
2. 策略模式提供了管理相关的算法族的办法。
3. 策略模式提供了可以替换继承关系的办法。
4. 使用策略模式可以避免使用多重条件转移语句。

## 缺点
1. 客户端必须知道所有的策略类，并自行决定使用哪一个策略类。
2. 策略模式将产生很多策略类。

## 例子
商人出行可以有多种选择，每种选择都是一个策略：
```java
public class BusinessMan {
    private TransportationStrategy strategy;

    public BusinessMan(TransportationStrategy strategy) {
        this.strategy = strategy;
    }

    public void changeStrategy(TransportationStrategy strategy) {
        this.strategy = strategy;
    }

    public void transport() {
        this.strategy.go();
    }
}

@FunctionalInterface
public interface TransportationStrategy {
    void go();
}

public class TransportationAirplane implements TransportationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationAirplane.class);

    @Override
    public void go() {
        LOGGER.info("乘飞机从北京去广州");
    }
}

public class TransportationTrain implements TransportationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationTrain.class);

    @Override
    public void go() {
        LOGGER.info("乘高铁从北京去上海");
    }
}

public class TransportationVehicle implements TransportationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportationVehicle.class);

    @Override
    public void go() {
        LOGGER.info("驾车从北京去天津");
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        BusinessMan man = new BusinessMan(new TransportationAirplane());
        man.transport();

        man.changeStrategy(new TransportationTrain());
        man.transport();

        man.changeStrategy(new TransportationVehicle());
        man.transport();
    }
}
/*
输出：
13:51:07.521 [main] INFO com.dhf.strategy.TransportationAirplane - 乘飞机从北京去广州
13:51:07.525 [main] INFO com.dhf.strategy.TransportationTrain - 乘高铁从北京去上海
13:51:07.525 [main] INFO com.dhf.strategy.TransportationVehicle - 驾车从北京去天津
*/
```