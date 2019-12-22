# 状态模式

## 目的
在很多情况下，一个对象的行为取决于一个或多个动态变化的属性，这样的属性叫做状态，这样的对象叫做有状态的对象，这样的对象状态是从事先定义好的一系列值中取出的。当一个这样的对象与外部事件产生互动时，其内部状态就会改变，从而使得系统的行为也随之发生变化。

## 优点
1. 封装了状态转换规则。
2. 将所有与某个状态有关的行为放到一个类中，并且可以方便地增加新的状态，只需要改变对象状态即可改变对象的行为。 
3. 智能化。

## 缺点
1. 状态模式的使用必然会增加系统类和对象的个数。
2. 状态模式的结构与实现都较为复杂，如果使用不当将导致程序结构和代码的混乱。
3. 状态模式对“开闭原则”的支持并不太好，对于可以切换状态的状态模式，增加新的状态类需要修改那些负责状态转换的源代码，否则无法切换到新增状态；而且修改某个状态类的行为也需修改对应类的源代码。

## 例子
长毛象有生气和平静状态，这两个状态实现状态接口，分别会有不同的行为，长毛象对象负责状态的转换逻辑：
```java
public interface State {
    void onEnterState();

    void observe();
}

public class AngryState implements State {
    private static final Logger LOGGER = LoggerFactory.getLogger(AngryState.class);

    private Mammoth mammoth;

    public AngryState(Mammoth mammoth) {
        this.mammoth = mammoth;
    }

    @Override
    public void observe() {
        LOGGER.info("{} is furious!", mammoth);
    }

    @Override
    public void onEnterState() {
        LOGGER.info("{} gets angry!", mammoth);
    }
}

public class PeacefulState implements State {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeacefulState.class);

    private Mammoth mammoth;

    public PeacefulState(Mammoth mammoth) {
        this.mammoth = mammoth;
    }

    @Override
    public void observe() {
        LOGGER.info("{} is calm and peaceful.", mammoth);
    }

    @Override
    public void onEnterState() {
        LOGGER.info("{} calms down.", mammoth);
    }

}

public class Mammoth {
    private State state;

    public Mammoth() {
        state = new PeacefulState(this);
    }

    /**
     * Makes time pass for the mammoth
     */
    public void timePasses() {
        if (state.getClass().equals(PeacefulState.class)) {
            changeStateTo(new AngryState(this));
        } else {
            changeStateTo(new PeacefulState(this));
        }
    }

    private void changeStateTo(State newState) {
        this.state = newState;
        this.state.onEnterState();
    }

    @Override
    public String toString() {
        return "The mammoth";
    }

    public void observe() {
        this.state.observe();
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        Mammoth mammoth = new Mammoth();
        mammoth.observe();
        // 转变状态
        mammoth.timePasses();
        mammoth.observe();
        mammoth.timePasses();
        mammoth.observe();
    }
}

/*
13:34:06.927 [main] INFO com.dhf.state.PeacefulState - The mammoth is calm and peaceful.
13:34:06.932 [main] INFO com.dhf.state.AngryState - The mammoth gets angry!
13:34:06.932 [main] INFO com.dhf.state.AngryState - The mammoth is furious!
13:34:06.932 [main] INFO com.dhf.state.PeacefulState - The mammoth calms down.
13:34:06.932 [main] INFO com.dhf.state.PeacefulState - The mammoth is calm and peaceful.
*/
```