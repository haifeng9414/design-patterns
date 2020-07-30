# 状态模式

## 目的
在很多情况下，一个对象的行为取决于一个或多个动态变化的属性，这样的属性叫做状态，这样的对象叫做有状态的对象，这样的对象状态是从事先定义好的一系列值中取出的。当一个这样的对象与外部事件产生互动时，其内部状态就会改变，从而使得系统的行为也随之发生变化。

状态模式一般用来实现状态机，状态机有3个组成部分：状态（State）、事件（Event）、动作（Action）。其中，事件也称为转移条件（Transition Condition）。事件触发状态的转移及动作的执行。不过，动作不是必须的，也可能只转移状态，不执行任何动作。

超级马里奥游戏中，马里奥可以变身为多种形态，比如小马里奥（Small Mario）、超级马里奥（Super Mario）、火焰马里奥（Fire Mario）、斗篷马里奥（Cape Mario）等等。在不同的游戏情节下，各个形态会互相转化，并相应的增减积分。比如，初始形态是小马里奥，吃了蘑菇之后就会变成超级马里奥，并且增加100积分。实际上，马里奥形态的转变就是一个状态机。其中，马里奥的不同形态就是状态机中的状态，游戏情节（比如吃了蘑菇）就是状态机中的事件，加减积分就是状态机中的动作。比如吃蘑菇这个事件，会触发状态的转移：从小马里奥转移到超级马里奥，以及触发动作的执行（增加100积分）。

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

状态机通常有3种实现方式：分支逻辑法、查表法、状态模式，针对开始说的马里奥状态机，定义如下代码模版：
```java

public enum State {
    SMALL(0),
    SUPER(1),
    FIRE(2),
    CAPE(3);

    private int value;

    private State(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

public class MarioStateMachine {
    private int score;
    private State currentState;

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = State.SMALL;
    }

    public void obtainMushRoom() {
        //TODO
    }

    public void obtainCape() {
        //TODO
    }

    public void obtainFireFlower() {
        //TODO
    }

    public void meetMonster() {
        //TODO
    }

    public int getScore() {
        return this.score;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}

public class ApplicationDemo {
    public static void main(String[] args) {
        MarioStateMachine mario = new MarioStateMachine();
        mario.obtainMushRoom();
        int score = mario.getScore();
        State state = mario.getCurrentState();
        System.out.println("mario score: " + score + "; state: " + state);
    }
}
```

### 使用分支逻辑法实现
```java
public class MarioStateMachine {
    private int score;
    private State currentState;

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = State.SMALL;
    }

    public void obtainMushRoom() {
        if (currentState.equals(State.SMALL)) {
        this.currentState = State.SUPER;
        this.score += 100;
        }
    }

    public void obtainCape() {
        if (currentState.equals(State.SMALL) || currentState.equals(State.SUPER) ) {
        this.currentState = State.CAPE;
        this.score += 200;
        }
    }

    public void obtainFireFlower() {
        if (currentState.equals(State.SMALL) || currentState.equals(State.SUPER) ) {
        this.currentState = State.FIRE;
        this.score += 300;
        }
    }

    public void meetMonster() {
        if (currentState.equals(State.SUPER)) {
        this.currentState = State.SMALL;
        this.score -= 100;
        return;
        }

        if (currentState.equals(State.CAPE)) {
        this.currentState = State.SMALL;
        this.score -= 200;
        return;
        }

        if (currentState.equals(State.FIRE)) {
        this.currentState = State.SMALL;
        this.score -= 300;
        return;
        }
    }

    public int getScore() {
        return this.score;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
```

可以发现使用分支逻辑法实现状态机需要编写各种判断，如果是复杂的状态机，这种实现方式极易漏写或者错写某个状态转移。除此之外，代码中充斥着大量的 if-else或者switch-case分支判断逻辑，可读性和可维护性都很差。

### 使用查表法实现
查表法通过一个二维表来表示状态机，状态机实现类的各个方法只需要查询二维表就能够完成实现，如马里奥状态机的二维表：
|     | E1(Got MushRoom) | E2(Got Cape) | E3(Got Fire Flower) | E4(Met Monster) |
|  ----  | ----  | ----  | ----  | ----  |
| Small  | Super/+100 | Cape/+200 | Fire/+300 | / |
| Super  | / | Cape/+200 | Fire/+300 | Small/-100 |
| Cape  | / | / | / | Small/-200 |
| Fire  | / | / | / | Small/-300 |

这个二维表中，第一维表示当前状态，第二维表示事件，值表示当前状态经过事件之后，转移到的新状态及其执行的动作。

有了这个二维表，就可以实现状态机了：
```java
public enum Event {
    GOT_MUSHROOM(0),
    GOT_CAPE(1),
    GOT_FIRE(2),
    MET_MONSTER(3);

    private int value;

    private Event(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

public class MarioStateMachine {
    private int score;
    private State currentState;

    // 状态不变时直接使用当前状态作为新的状态
    private static final State[][] transitionTable = {
            {SUPER, CAPE, FIRE, SMALL},
            {SUPER, CAPE, FIRE, SMALL},
            {CAPE, CAPE, CAPE, SMALL},
            {FIRE, FIRE, FIRE, SMALL}
    };

    private static final int[][] actionTable = {
            {+100, +200, +300, +0},
            {+0, +200, +300, -100},
            {+0, +0, +0, -200},
            {+0, +0, +0, -300}
    };

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = State.SMALL;
    }

    public void obtainMushRoom() {
        executeEvent(Event.GOT_MUSHROOM);
    }

    public void obtainCape() {
        executeEvent(Event.GOT_CAPE);
    }

    public void obtainFireFlower() {
        executeEvent(Event.GOT_FIRE);
    }

    public void meetMonster() {
        executeEvent(Event.MET_MONSTER);
    }

    private void executeEvent(Event event) {
        int stateValue = currentState.getValue();
        int eventValue = event.getValue();
        this.currentState = transitionTable[stateValue][eventValue];
        this.score = actionTable[stateValue][eventValue];
    }

    public int getScore() {
        return this.score;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
```

当修改状态机时，只需要修改transitionTable和actionTable两个二维数组即可。如果把这两个二维数组存储在配置文件中，当需要修改状态机时，甚至可以不修改任何代码，只需要修改配置文件就可以了。

### 使用状态模式实现
在查表法的代码实现中，事件触发的动作只是简单的积分加减，用一个int类型的二维数组actionTable就能表示，二维数组中的值表示积分的加减值。但是，如果要执行的动作并非这么简单，而是一系列复杂的逻辑操作（比如加减积分、写数据库，还有可能发送消息通知等等），就没法用如此简单的二维数组来表示了。

状态模式通过将事件触发的状态转移和动作执行，拆分到不同的状态类中，来避免分支判断逻辑。
```java

public interface IMario { //所有状态类的接口
    State getName();
    // 以下是定义的事件
    void obtainMushRoom();
    void obtainCape();
    void obtainFireFlower();
    void meetMonster();
}

// 马里奥每个可能的状态都是IMario接口的实现类，每个状态实现类在实现的方法中修改状态机的状态和属性
public class SmallMario implements IMario {
    private MarioStateMachine stateMachine;

    public SmallMario(MarioStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getName() {
        return State.SMALL;
    }

    @Override
    public void obtainMushRoom() {
        stateMachine.setCurrentState(new SuperMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 100);
    }

    @Override
    public void obtainCape() {
        stateMachine.setCurrentState(new CapeMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 200);
    }

    @Override
    public void obtainFireFlower() {
        stateMachine.setCurrentState(new FireMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 300);
    }

    @Override
    public void meetMonster() {
        // do nothing...
    }
}

public class SuperMario implements IMario {
    private MarioStateMachine stateMachine;

    public SuperMario(MarioStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public State getName() {
        return State.SUPER;
    }

    @Override
    public void obtainMushRoom() {
        // do nothing...
    }

    @Override
    public void obtainCape() {
        stateMachine.setCurrentState(new CapeMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 200);
    }

    @Override
    public void obtainFireFlower() {
        stateMachine.setCurrentState(new FireMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() + 300);
    }

    @Override
    public void meetMonster() {
        stateMachine.setCurrentState(new SmallMario(stateMachine));
        stateMachine.setScore(stateMachine.getScore() - 100);
    }
}

// 省略CapeMario、FireMario类...

// 状态机只需要调用当前状态的对于方法即可
public class MarioStateMachine {
    private int score;
    private IMario currentState; // 不再使用枚举来表示状态

    public MarioStateMachine() {
        this.score = 0;
        this.currentState = new SmallMario(this);
    }

    public void obtainMushRoom() {
        this.currentState.obtainMushRoom();
    }

    public void obtainCape() {
        this.currentState.obtainCape();
    }

    public void obtainFireFlower() {
        this.currentState.obtainFireFlower();
    }

    public void meetMonster() {
        this.currentState.meetMonster();
    }

    public int getScore() {
        return this.score;
    }

    public State getCurrentState() {
        return this.currentState.getName();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCurrentState(IMario currentState) {
        this.currentState = currentState;
    }
}
```

上面的实现就是状态模式，状态模式比使用分支逻辑法和使用查表法实现的状态机更灵活，可读性和可维护性更好，状态模式通过多态解决了分支逻辑法中各种状态判断，同时也通过多态将状态机的修改过程实现到各个状态实现类中。

实际上，像游戏这种比较复杂的状态机，包含的状态比较多，但是事件对应的执行动作简单，则优先推荐使用查表法，使用状态模式会引入非常多的状态类，会导致代码比较难维护。相反，像电商下单、外卖下单这种类型的状态机，它们的状态并不多，状态转移也比较简单，但事件触发执行的动作包含的业务逻辑可能会比较复杂，所以更加推荐使用状态模式。