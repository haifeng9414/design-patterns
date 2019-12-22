# 观察者模式

## 目的
建立一种对象与对象之间的依赖关系，一个对象发生改变时将自动通知其他对象，其他对象将相应做出反应。

## 优点
1. 观察者模式可以实现表示层和数据逻辑层的分离，并定义了稳定的消息更新传递机制，抽象了更新接口，使得可以有各种各样不同的表示层作为具体观察者角色。
2. 观察者模式在观察目标和观察者之间建立一个抽象的耦合。
3. 观察者模式支持广播通信。
4. 观察者模式符合“开闭原则”的要求。

## 缺点
1. 如果一个观察目标对象有很多直接和间接的观察者的话，将所有的观察者都通知到会花费很多时间。
2. 观察者模式没有相应的机制让观察者知道所观察的目标对象是怎么发生变化的，而仅仅只是知道观察目标发生了变化。

## 例子
观察者模式的例子直接看被观察者的实现就能理解，下面的Time表示在特定的时间通知所有观察者时间点到了
```java
public class Time {
    private static final Logger LOGGER = LoggerFactory.getLogger(Time.class);

    private TimePoint point;
    private List<TimeObserver> observers;

    public Time() {
        this.point = TimePoint.MORNING;
        observers = new ArrayList<>();
    }

    public void addObserver(TimeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TimeObserver observer) {
        observers.remove(observer);
    }

    public void passing() {
        TimePoint[] points = TimePoint.values();
        point = points[(point.ordinal() + 1) % points.length];
        LOGGER.info("时间来到了{}", point);
        notifyObservers();
    }

    public void notifyObservers() {
        for (TimeObserver observer : observers) {
            observer.update(point);
        }
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        Time time = new Time();
        time.addObserver(new Northern());
        time.addObserver(new Southern());

        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
        time.passing();
    }
}

/*
16:01:46.990 [main] INFO com.dhf.observer.Time - 时间来到了中午
16:01:46.996 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:46.998 [main] INFO com.dhf.observer.Northern - 炸酱面
16:01:46.998 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:46.999 [main] INFO com.dhf.observer.Southern - 米饭
16:01:46.999 [main] INFO com.dhf.observer.Time - 时间来到了下午
16:01:47.000 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Northern - 牛奶
16:01:47.000 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Southern - 茶
16:01:47.000 [main] INFO com.dhf.observer.Time - 时间来到了晚上
16:01:47.000 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Northern - 包子
16:01:47.000 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Southern - 鱼
16:01:47.000 [main] INFO com.dhf.observer.Time - 时间来到了早晨
16:01:47.000 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Northern - 煎饼果子
16:01:47.000 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Southern - 热干面
16:01:47.000 [main] INFO com.dhf.observer.Time - 时间来到了中午
16:01:47.000 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Northern - 炸酱面
16:01:47.000 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Southern - 米饭
16:01:47.000 [main] INFO com.dhf.observer.Time - 时间来到了下午
16:01:47.000 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Northern - 牛奶
16:01:47.000 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.000 [main] INFO com.dhf.observer.Southern - 茶
16:01:47.000 [main] INFO com.dhf.observer.Time - 时间来到了晚上
16:01:47.000 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.001 [main] INFO com.dhf.observer.Northern - 包子
16:01:47.001 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.001 [main] INFO com.dhf.observer.Southern - 鱼
16:01:47.001 [main] INFO com.dhf.observer.Time - 时间来到了早晨
16:01:47.001 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.001 [main] INFO com.dhf.observer.Northern - 煎饼果子
16:01:47.001 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.001 [main] INFO com.dhf.observer.Southern - 热干面
16:01:47.001 [main] INFO com.dhf.observer.Time - 时间来到了中午
16:01:47.001 [main] INFO com.dhf.observer.Northern - 北方人吃饭了
16:01:47.001 [main] INFO com.dhf.observer.Northern - 炸酱面
16:01:47.001 [main] INFO com.dhf.observer.Southern - 南方人吃饭了
16:01:47.001 [main] INFO com.dhf.observer.Southern - 米饭
*/
```