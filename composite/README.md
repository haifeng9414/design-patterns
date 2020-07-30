# 组合模式

## 目的
将对象组合成树形结构以表示部分与整体的层次结构。它在树型结构的问题中，模糊了简单元素和复杂元素的概念，客户程序可以像处理简单元素一样来处理复杂元素，从而使得客户程序与复杂元素的内部结构解耦。 

## 优点
1. 高层模块调用简单。
2. 节点自由增加。

## 缺点
在使用组合模式时，其叶子和树枝的声明都是实现类，而不是接口，违反了依赖倒置原则。

## 组成部分
1. Component：是组合中的所有对象的统一接口；可以在接口中实现类应当实现的货缺省的行为。
1. Leaf：在组合中表示叶节点对象，顾名思义，叶节点没有子节点。
1. Composite：定义有子部件的那些部件的行为，同时存储子部件，实现Component中与子部件有关的接口。
1. Client：通过Component接口，操纵组合部件的对象。

## 例子
一个句子由若干个单词组成，而单词又由若干个字母组成，句子和单词都能打印，以组合模式实现该功能的代码如下：
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
```

使用：
```java
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Message from the orcs: ");

        // Messenger对象只是方便测试的工具类，默认创建了一个Sentence对象并添加了若干个Word对象
        LetterComposite orcMessage = new Messenger().messageFromOrcs();
        orcMessage.print();

        LOGGER.info("Message from the elves: ");

        LetterComposite elfMessage = new Messenger().messageFromElves();
        elfMessage.print();
    }
}

/*
输出：
19:44:19.846 [main] INFO com.dhf.Application - Message from the orcs: 
 Where there is a whip there is a way.
19:44:19.863 [main] INFO com.dhf.Application - Message from the elves: 
 Much wind pours from your mouth.
*/
```

另外一个例子是设计一个类来表示文件系统中的目录，能方便地实现下面这些功能：
- 动态地添加、删除某个目录下的子目录或文件；
- 统计指定目录下的文件个数；
- 统计指定目录下的文件总大小。

组合模式很适合这个需求：
```java
// 组合模式中的公共父类，文件和文件夹都需要实现该抽象类
public abstract class FileSystemNode {
    protected String path;

    public FileSystemNode(String path) {
        this.path = path;
    }

    // 计算目录下的文件数量
    public abstract int countNumOfFiles();
    // 计算目录下的文件总大小
    public abstract long countSizeOfFiles();

    public String getPath() {
        return path;
    }
}

public class File extends FileSystemNode {
    public File(String path) {
        super(path);
    }

    // 文件直接返回1作为目录下文件数量
    @Override
    public int countNumOfFiles() {
        return 1;
    }

    // 文件直接返回自己的大小作为目录下文件总大小
    @Override
    public long countSizeOfFiles() {
        java.io.File file = new java.io.File(path);
        if (!file.exists()) return 0;
        return file.length();
    }
}

public class Directory extends FileSystemNode {
    // subNodes保存子节点，可能是文件也可能是目录
    private List<FileSystemNode> subNodes = new ArrayList<>();

    public Directory(String path) {
        super(path);
    }

    // 循环求子节点的值
    @Override
    public int countNumOfFiles() {
        int numOfFiles = 0;
        for (FileSystemNode fileOrDir : subNodes) {
        numOfFiles += fileOrDir.countNumOfFiles();
        }
        return numOfFiles;
    }

    // 循环求子节点的值
    @Override
    public long countSizeOfFiles() {
        long sizeofFiles = 0;
        for (FileSystemNode fileOrDir : subNodes) {
        sizeofFiles += fileOrDir.countSizeOfFiles();
        }
        return sizeofFiles;
    }

    public void addSubNode(FileSystemNode fileOrDir) {
        subNodes.add(fileOrDir);
    }

    public void removeSubNode(FileSystemNode fileOrDir) {
        int size = subNodes.size();
        int i = 0;
        for (; i < size; ++i) {
        if (subNodes.get(i).getPath().equalsIgnoreCase(fileOrDir.getPath())) {
            break;
        }
        }
        if (i < size) {
        subNodes.remove(i);
        }
    }
}

// 使用
public class Demo {
    public static void main(String[] args) {
        /**
        * /
        * /wz/
        * /wz/a.txt
        * /wz/b.txt
        * /wz/movies/
        * /wz/movies/c.avi
        * /xzg/
        * /xzg/docs/
        * /xzg/docs/d.txt
        */
        Directory fileSystemTree = new Directory("/");
        Directory node_wz = new Directory("/wz/");
        Directory node_xzg = new Directory("/xzg/");
        fileSystemTree.addSubNode(node_wz);
        fileSystemTree.addSubNode(node_xzg);

        File node_wz_a = new File("/wz/a.txt");
        File node_wz_b = new File("/wz/b.txt");
        Directory node_wz_movies = new Directory("/wz/movies/");
        node_wz.addSubNode(node_wz_a);
        node_wz.addSubNode(node_wz_b);
        node_wz.addSubNode(node_wz_movies);

        File node_wz_movies_c = new File("/wz/movies/c.avi");
        node_wz_movies.addSubNode(node_wz_movies_c);

        Directory node_xzg_docs = new Directory("/xzg/docs/");
        node_xzg.addSubNode(node_xzg_docs);

        File node_xzg_docs_d = new File("/xzg/docs/d.txt");
        node_xzg_docs.addSubNode(node_xzg_docs_d);

        System.out.println("/ files num:" + fileSystemTree.countNumOfFiles());
        System.out.println("/wz/ files num:" + node_wz.countNumOfFiles());
    }
}
```