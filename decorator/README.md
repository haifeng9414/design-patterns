# 组合模式

## 目的
将对象组合成树形结构以表示“部分-整体”的层次结构。组合模式使得用户对单个对象和组合对象的使用具有一致性。

## 优点
1. 高层模块调用简单。
2. 节点自由增加。

## 缺点
在使用组合模式时，某个组件使用其他组件时使用的是实现类，而不是接口，违反了依赖倒置原则。

## 例子
字母可以组成单词，单词可以组成句子，单词、单词和句子都可以打印：
```java
/**
* 组合抽象类，组合中涉及到的类都继承该抽象类，赋予他们成为组合的能力，
* 同时组合抽象类定义了组合中涉及到的类的行为，如下面的打印行为
*/
public abstract class LetterComposite {
    private List<LetterComposite> children = new ArrayList<>();

    public void add(LetterComposite letter) {
        children.add(letter);
    }

    public int count() {
        return children.size();
    }

    protected void printThisBefore() {
    }

    protected void printThisAfter() {
    }

    /**
     * 组合的行为，这里就是打印
     */
    public void print() {
        printThisBefore();
        for (LetterComposite letter : children) {
            letter.print();
        }
        printThisAfter();
    }
}

/**
 * 字母，打印行为就是打印自己，字母也继承自LetterComposite，使其具有成为组合的能力，
 * 但实际上一个字母不可再分割
 */
public class Letter extends LetterComposite {
    private char c;

    public Letter(char c) {
        this.c = c;
    }

    // 重写不是必须的，这里重写只是为了明确字母不可分割
    @Override
    public void add(LetterComposite letter) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void printThisBefore() {
        System.out.print(c);
    }
}

/**
 * 单词由字母组成，打印行为就是打印字母之前打印一个空格
 */
public class Word extends LetterComposite {

    /**
     * Constructor
     */
    public Word(List<Letter> letters) {
        for (Letter l : letters) {
            this.add(l);
        }
    }

    @Override
    protected void printThisBefore() {
        System.out.print(" ");
    }
}

/**
 * 句子由单词构成，打印行为就是打印完单词后打印句号
 */
public class Sentence extends LetterComposite {

    /**
     * Constructor
     */
    public Sentence(List<Word> words) {
        for (Word w : words) {
            this.add(w);
        }
    }

    @Override
    protected void printThisAfter() {
        System.out.print(".\n");
    }
}

/**
 * 方便返回组合对象
 */
public class Messenger {
    public LetterComposite messageFromOrcs() {
        List<Word> words = new ArrayList<>();

        words.add(new Word(Arrays.asList(new Letter('W'), new Letter('h'), new Letter('e'), new Letter(
                'r'), new Letter('e'))));
        words.add(new Word(Arrays.asList(new Letter('t'), new Letter('h'), new Letter('e'), new Letter(
                'r'), new Letter('e'))));
        words.add(new Word(Arrays.asList(new Letter('i'), new Letter('s'))));
        words.add(new Word(Arrays.asList(new Letter('a'))));
        words.add(new Word(Arrays.asList(new Letter('w'), new Letter('h'), new Letter('i'), new Letter(
                'p'))));
        words.add(new Word(Arrays.asList(new Letter('t'), new Letter('h'), new Letter('e'), new Letter(
                'r'), new Letter('e'))));
        words.add(new Word(Arrays.asList(new Letter('i'), new Letter('s'))));
        words.add(new Word(Arrays.asList(new Letter('a'))));
        words.add(new Word(Arrays.asList(new Letter('w'), new Letter('a'), new Letter('y'))));

        return new Sentence(words);

    }

    public LetterComposite messageFromElves() {
        List<Word> words = new ArrayList<>();

        words.add(new Word(Arrays.asList(new Letter('M'), new Letter('u'), new Letter('c'), new Letter(
                'h'))));
        words.add(new Word(Arrays.asList(new Letter('w'), new Letter('i'), new Letter('n'), new Letter(
                'd'))));
        words.add(new Word(Arrays.asList(new Letter('p'), new Letter('o'), new Letter('u'), new Letter(
                'r'), new Letter('s'))));
        words.add(new Word(Arrays.asList(new Letter('f'), new Letter('r'), new Letter('o'), new Letter(
                'm'))));
        words.add(new Word(Arrays.asList(new Letter('y'), new Letter('o'), new Letter('u'), new Letter(
                'r'))));
        words.add(new Word(Arrays.asList(new Letter('m'), new Letter('o'), new Letter('u'), new Letter(
                't'), new Letter('h'))));

        return new Sentence(words);
    }
}
```

使用：
```java
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Message from the orcs: ");

        LetterComposite orcMessage = new Messenger().messageFromOrcs();
        orcMessage.print();

        LOGGER.info("Message from the elves: ");

        LetterComposite elfMessage = new Messenger().messageFromElves();
        elfMessage.print();
    }
}

/*
输出：
17:35:07.610 [main] INFO com.dhf.Application - Message from the orcs: 
 Where there is a whip there is a way.
17:35:07.617 [main] INFO com.dhf.Application - Message from the elves: 
 Much wind pours from your mouth.
*/
```
