# 代理模式

## 目的
为其他对象提供一种代理以控制对这个对象的访问，代码形式上可能代理模式和装饰器模式很像，但是从语意上讲，代理模式的目标是控制对被代理对象的访问，而装饰模式是给原对象增加额外功能。

## 优点
1. 职责清晰。
2. 高扩展性。 
3. 智能化。

## 缺点
1. 由于在客户端和真实主题之间增加了代理对象，因此有些类型的代理模式可能会造成请求的处理速度变慢。
2. 实现代理模式需要额外的工作，有些代理模式的实现非常复杂。

## 例子
Room接口定义了顾客进入房子的方法，普通的Room接口实现没有记录顾客人数，通过代理对象实现顾客人数的统计：
```java
public class DiningRoomProxy implements Room {
        private static final Logger LOGGER = LoggerFactory.getLogger(DiningRoomProxy.class);

        private Room room;
        private int customerNumber;

    public DiningRoomProxy(Room room) {
        this.room = room;
    }


    @Override
    public void enter(Customer customer) {
        if (customerNumber < 10) {
            room.enter(customer);
            customerNumber++;
            LOGGER.info("目前房间里有 {} 人", customerNumber);
        } else {
            LOGGER.info("抱歉{}，房间已经满了", customer);
        }
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        DiningRoomProxy proxy = new DiningRoomProxy(new DiningRoom());
        proxy.enter(new Customer("赵先森"));
        proxy.enter(new Customer("钱先森"));
        proxy.enter(new Customer("孙先森"));
        proxy.enter(new Customer("李先森"));
        proxy.enter(new Customer("周先森"));
        proxy.enter(new Customer("吴先森"));
        proxy.enter(new Customer("正先森"));
        proxy.enter(new Customer("王先森"));
        proxy.enter(new Customer("冯先森"));
        proxy.enter(new Customer("陈先森"));
        proxy.enter(new Customer("褚先森"));
        proxy.enter(new Customer("魏先森"));
    }
}
/*
输出：
16:32:26.825 [main] INFO com.dhf.prototype.DiningRoom - 顾客赵先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 1 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客钱先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 2 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客孙先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 3 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客李先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 4 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客周先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 5 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客吴先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 6 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客正先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 7 人
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoom - 顾客王先森进来了
16:32:26.836 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 8 人
16:32:26.837 [main] INFO com.dhf.prototype.DiningRoom - 顾客冯先森进来了
16:32:26.837 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 9 人
16:32:26.837 [main] INFO com.dhf.prototype.DiningRoom - 顾客陈先森进来了
16:32:26.838 [main] INFO com.dhf.prototype.DiningRoomProxy - 目前房间里有 10 人
16:32:26.838 [main] INFO com.dhf.prototype.DiningRoomProxy - 抱歉褚先森，房间已经满了
16:32:26.838 [main] INFO com.dhf.prototype.DiningRoomProxy - 抱歉魏先森，房间已经满了
*/
```