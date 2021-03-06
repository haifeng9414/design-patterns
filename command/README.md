# 命令模式

## 目的
命令模式将请求（命令）封装为一个对象，这样可以使用不同的请求参数化其他对象（将不同请求依赖注入到其他对象），并且能够支持请求（命令）的排队执行、记录日志、撤销等（附加控制）功能。

命令模式最核心的实现手段，是将函数封装成对象。C语言支持函数指针，可以把函数当作变量传递来传递去。但是，在大部分编程语言中，函数没法儿作为参数传递给其他函数，也没法儿赋值给变量。借助命令模式，可以将函数封装成对象。具体来说就是，设计一个包含这个函数的类，实例化一个对象传来传去，这样就可以实现把函数像对象一样使用。

当把函数封装成对象之后，对象就可以存储下来，方便控制执行。所以，命令模式的主要作用和应用场景，是用来控制命令的执行，比如，异步、延迟、排队执行命令、撤销重做命令、存储命令、给命令记录日志等等，这才是命令模式能发挥独一无二作用的地方。

## 优点
1. 降低了系统耦合度。
2. 新的命令可以很容易添加到系统中去

## 缺点
使用命令模式可能会导致某些系统有过多的具体命令类。

## 例子
将一个命令封装成一个接口，从而可以用不同的命令对其他对象进行参数化，如巫师能够对Target施放不同的咒语，咒语的效果是使得Target的某些属性被修改了，也就是参数化，下面的命令模式的例子中undo和redo方法并不是必须的，命令模式强调的是将参数化过程抽象出来形成命令接口：
```java
/**
 * 命令接口
 */
public abstract class Command {
    /**
     * 命令能够针对某个目标执行动作
     */
    public abstract void execute(Target target);

    /**
     * 回滚执行的动作
     */
    public abstract void undo();

    /**
     * 重放执行的动作
     */
    public abstract void redo();

    @Override
    public abstract String toString();
}

/**
 * 命令的作用目标，拥有一些属性，具体的命令通过修改目标的属性实现命令
 */
public abstract class Target {
    private static final Logger LOGGER = LoggerFactory.getLogger(Target.class);

    private Size size;

    private Visibility visibility;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public abstract String toString();

    /**
     * Print status
     */
    public void printStatus() {
        LOGGER.info("{}, [size={}] [visibility={}]", this, getSize(), getVisibility());
    }
}

/**
 * 巫师能够发出咒语，也就是发出命令
 */
public class Wizard {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wizard.class);

    // 保存上一次执行的命令，当需要undo时从该队列获取上一次命令并调用命令的undo方法
    private Deque<Command> undoStack = new LinkedList<>();
    // 保存上一次执行undo方法的命令，当需要redo时从该队列获取上一次undo的命令并调用命令的redo方法
    private Deque<Command> redoStack = new LinkedList<>();

    /**
     * 施放咒语
     */
    public void castSpell(Command command, Target target) {
        LOGGER.info("{} casts {} at {}", this, command, target);
        command.execute(target);
        undoStack.offerLast(command);
    }

    /**
     * Undo last spell
     */
    public void undoLastSpell() {
        if (!undoStack.isEmpty()) {
            Command previousSpell = undoStack.pollLast();
            redoStack.offerLast(previousSpell);
            LOGGER.info("{} undoes {}", this, previousSpell);
            previousSpell.undo();
        }
    }

    /**
     * Redo last spell
     */
    public void redoLastSpell() {
        if (!redoStack.isEmpty()) {
            Command previousSpell = redoStack.pollLast();
            undoStack.offerLast(previousSpell);
            LOGGER.info("{} redoes {}", this, previousSpell);
            previousSpell.redo();
        }
    }

    @Override
    public String toString() {
        return "Wizard";
    }
}
```

针对命令目标和命令的实现：
```java
/**
 * 哥布林是一个具体的命令目标，默认拥有一些属性
 */
public class Goblin extends Target {
    public Goblin() {
        setSize(Size.NORMAL);
        setVisibility(Visibility.VISIBLE);
    }

    @Override
    public String toString() {
        return "Goblin";
    }
}

/**
 * 消失咒语是一个具体的命令
 */
public class InvisibilitySpell extends Command {
    private Target target;

    @Override
    public void execute(Target target) {
        target.setVisibility(Visibility.INVISIBLE);
        this.target = target;
    }

    @Override
    public void undo() {
        if (target != null) {
            target.setVisibility(Visibility.VISIBLE);
        }
    }

    @Override
    public void redo() {
        if (target != null) {
            target.setVisibility(Visibility.INVISIBLE);
        }
    }

    @Override
    public String toString() {
        return "消失咒语";
    }
}

/**
 * 收缩咒语是一个具体的命令
 */
public class ShrinkSpell extends Command {
    private Size oldSize;
    private Target target;

    @Override
    public void execute(Target target) {
        oldSize = target.getSize();
        target.setSize(Size.SMALL);
        this.target = target;
    }

    @Override
    public void undo() {
        if (oldSize != null && target != null) {
            Size temp = target.getSize();
            target.setSize(oldSize);
            oldSize = temp;
        }
    }

    @Override
    public void redo() {
        undo();
    }

    @Override
    public String toString() {
        return "收缩咒语";
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        // 巫师能够发出命令
        Wizard wizard = new Wizard();
        // 命令执行时作用的对象
        Goblin goblin = new Goblin();

        goblin.printStatus();

        // 对哥布林施放收缩咒语
        wizard.castSpell(new ShrinkSpell(), goblin);
        goblin.printStatus();

        // 对哥布林施放消失咒语
        wizard.castSpell(new InvisibilitySpell(), goblin);
        goblin.printStatus();

        wizard.undoLastSpell();
        goblin.printStatus();

        wizard.undoLastSpell();
        goblin.printStatus();

        wizard.redoLastSpell();
        goblin.printStatus();

        wizard.redoLastSpell();
        goblin.printStatus();
    }
}

/*
输出：
16:16:52.225 [main] INFO com.dhf.command.Target - Goblin, [size=normal] [visibility=visible]
16:16:52.230 [main] INFO com.dhf.command.Wizard - Wizard casts 收缩咒语 at Goblin
16:16:52.230 [main] INFO com.dhf.command.Target - Goblin, [size=small] [visibility=visible]
16:16:52.230 [main] INFO com.dhf.command.Wizard - Wizard casts 消失咒语 at Goblin
16:16:52.230 [main] INFO com.dhf.command.Target - Goblin, [size=small] [visibility=invisible]
16:16:52.230 [main] INFO com.dhf.command.Wizard - Wizard undoes 消失咒语
16:16:52.230 [main] INFO com.dhf.command.Target - Goblin, [size=small] [visibility=visible]
16:16:52.230 [main] INFO com.dhf.command.Wizard - Wizard undoes 收缩咒语
16:16:52.230 [main] INFO com.dhf.command.Target - Goblin, [size=normal] [visibility=visible]
16:16:52.230 [main] INFO com.dhf.command.Wizard - Wizard redoes 收缩咒语
16:16:52.230 [main] INFO com.dhf.command.Target - Goblin, [size=small] [visibility=visible]
16:16:52.230 [main] INFO com.dhf.command.Wizard - Wizard redoes 消失咒语
16:16:52.230 [main] INFO com.dhf.command.Target - Goblin, [size=small] [visibility=invisible]
*/
```
