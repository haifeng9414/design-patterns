# 模版方法模式

## 目的
定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

## 优点
1. 提高代码复用性，将相同部分的代码放在抽象的父类中。
2. 提高了拓展性，将不同的代码放入不同的子类中，通过对子类的扩展增加新的行为。
3. 实现了反向控制，通过一个父类调用其子类的操作，通过对子类的扩展增加新的行为，实现了反向控制&符合“开闭原则”。

## 缺点
1. 每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大。

## 例子
抽象类定义了方法的执行逻辑，子类实现逻辑中的抽象方法
```java
public abstract class Game {
    abstract void initialize();

    abstract void startPlay();

    abstract void endPlay();

    //模板
    public final void play() {
        //初始化游戏
        initialize();

        //开始游戏
        startPlay();

        //结束游戏
        endPlay();
    }
}

public class Football extends Game {
    @Override
    void endPlay() {
        System.out.println("Football Game Finished!");
    }

    @Override
    void initialize() {
        System.out.println("Football Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        System.out.println("Football Game Started. Enjoy the game!");
    }
}

public class Cricket extends Game {
    @Override
    void endPlay() {
        System.out.println("Cricket Game Finished!");
    }

    @Override
    void initialize() {
        System.out.println("Cricket Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        System.out.println("Cricket Game Started. Enjoy the game!");
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        Game game = new Cricket();
        game.play();
        System.out.println();
        game = new Football();
        game.play();
    }
}

/*
输出：
Cricket Game Initialized! Start playing.
Cricket Game Started. Enjoy the game!
Cricket Game Finished!

Football Game Initialized! Start playing.
Football Game Started. Enjoy the game!
Football Game Finished!
*/
```