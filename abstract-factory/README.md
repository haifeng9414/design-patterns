# 抽象工厂模式

## 简介
抽象工厂接口定义了一系列工厂能够生成的对象类型，具体的工厂实现该接口，客户端只需要选择具体的工厂就能通过该工厂获取其对应的对象类型，如抽象工厂接口定义如下，表示可以生产船、船长和水手，即一个船队：
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