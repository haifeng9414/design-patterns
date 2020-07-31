# 备忘录模式

## 目的
在不违背封装原则的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，以便之后恢复对象为先前的状态。

## 优点
1. 给用户提供了一种可以恢复状态的机制，可以使用户能够比较方便地回到某个历史的状态。 
2. 实现了信息的封装，使得用户不需要关心状态的保存细节。

## 缺点
消耗资源。如果类的成员变量过多，势必会占用比较大的资源，而且每一次保存都会消耗一定的内存。

## 例子
假设需要编写一个程序，可以接收命令行的输入。用户输入文本时，程序将其追加存储在内存文本中；用户输入`:list`，程序在命令行中输出内存文本的内容；用户输入`:undo`，程序会撤销上一次输入的文本，也就是从内存文本中将上次输入的文本删除掉。
如：
```
>hello
>:list
hello
>world
>:list
helloworld
>:undo
>:list
hello
```

针对上面的需求，有如下实现：
```java

public class InputText {
    private StringBuilder text = new StringBuilder();

    public String getText() {
        return text.toString();
    }

    public void append(String input) {
        text.append(input);
    }

    public void setText(String text) {
        this.text.replace(0, this.text.length(), text);
    }
}

public class SnapshotHolder {
    private Stack<InputText> snapshots = new Stack<>();

    public InputText popSnapshot() {
        return snapshots.pop();
    }

    public void pushSnapshot(InputText inputText) {
        InputText deepClonedInputText = new InputText();
        deepClonedInputText.setText(inputText.getText());
        snapshots.push(deepClonedInputText);
    }
}

public class ApplicationMain {
    public static void main(String[] args) {
        InputText inputText = new InputText();
        SnapshotHolder snapshotsHolder = new SnapshotHolder();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
        String input = scanner.next();
        if (input.equals(":list")) {
            System.out.println(inputText.toString());
        } else if (input.equals(":undo")) {
            InputText snapshot = snapshotsHolder.popSnapshot();
            inputText.setText(snapshot.getText());
        } else {
            snapshotsHolder.pushSnapshot(inputText);
            inputText.append(input);
        }
        }
    }
}
```

上面的代码虽然能够实现功能，但是存在一些问题，上面的代码破坏了封装性：
- 为了能用快照恢复InputText对象，在InputText类中定义了setText()函数，但这个函数有可能会被其他业务使用，所以，暴露不应该暴露的函数违背了封装原则；
- 快照本身是不可变的，不应该包含任何set()等修改内部状态的函数，但在上面的代码实现中，快照这个业务模型复用了InputText类的定义，而InputText类本身有一系列修改内部状态的函数，所以，用InputText类来表示快照违背了封装原则；

针对以上问题，对代码做两点修改。其一，定义一个独立的类（Snapshot类）来表示快照，而不是复用InputText类。这个类只暴露get()方法，没有set()等任何修改内部状态的方法。其二，在InputText类中，把setText()方法重命名为restoreSnapshot()方法，用意更加明确，只用来恢复对象。重构后代码如下：
```java

public class InputText {
    private StringBuilder text = new StringBuilder();

    public String getText() {
        return text.toString();
    }

    public void append(String input) {
        text.append(input);
    }

    public Snapshot createSnapshot() {
        return new Snapshot(text.toString());
    }

    public void restoreSnapshot(Snapshot snapshot) {
        this.text.replace(0, this.text.length(), snapshot.getText());
    }
}

public class Snapshot {
    private String text;

    public Snapshot(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}

public class SnapshotHolder {
    private Stack<Snapshot> snapshots = new Stack<>();

    public Snapshot popSnapshot() {
        return snapshots.pop();
    }

    public void pushSnapshot(Snapshot snapshot) {
        snapshots.push(snapshot);
    }
}

public class ApplicationMain {
    public static void main(String[] args) {
        InputText inputText = new InputText();
        SnapshotHolder snapshotsHolder = new SnapshotHolder();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
        String input = scanner.next();
        if (input.equals(":list")) {
            System.out.println(inputText.toString());
        } else if (input.equals(":undo")) {
            Snapshot snapshot = snapshotsHolder.popSnapshot();
            inputText.restoreSnapshot(snapshot);
        } else {
            snapshotsHolder.pushSnapshot(inputText.createSnapshot());
            inputText.append(input);
        }
        }
    }
}
```

以上就是备忘录模式。