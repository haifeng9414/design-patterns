# 抽象工厂模式

## 目的
提供一个能够创建一系列相关或相互依赖的接口的工厂，客户端通过某个工厂能够获取到一系列接口实现类，客户端无需指定接口的具体实现类，解决了客户端接口选择的问题。

## 优点
当一个产品族中的多个对象被设计成一起工作时，它能保证客户端始终只使用同一个产品族中的对象。

## 缺点
产品族扩展非常困难，增加一个新的产品族接口类需要修改抽象工厂和所有的具体工厂。

## 例子
抽象工厂接口定义了一系列工厂能够生成的对象类型，具体的工厂实现该接口，客户端只需要选择具体的工厂就能通过该工厂获取其对应的对象类型，如船队工厂可以生产船、船长和水手：
```java
public interface TeamFactory {
    Ship createShip();

    Captain createCaptain();

    Sailor createSailor();
}
```

针对该接口，可以有一个生产老船队的实现：
```java
public class PermanentTeamFactory implements TeamFactory {

    public Ship createShip() {
        return new OldShip();
    }

    public Captain createCaptain() {
        return new OldCaptain();
    }

    public Sailor createSailor() {
        return new OldSailor();
    }
}
```

同样也可以有个生产年轻船队的实现，客户端只需要实例化具体的工厂实现就能获取到对应类型的船队的对象：
```java
TeamFactory factory = new PermanentTeamFactory();
LOGGER.info("正在创建一支久经考验的队伍...");
LOGGER.info("-->" + factory.getCaptain());
LOGGER.info("-->" + factory.getShip());
LOGGER.info("-->" + factory.getSailor());
```