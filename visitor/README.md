# 访问者模式

## 目的
供一个作用于某种对象结构上的各元素的操作方式，可以不改变元素结构的前提下，定义作用于元素的新操作。

换言之，如果系统的数据结构是比较稳定的，但其操作（算法）是易于变化的，那么使用访问者模式是个不错的选择；如果数据结构是易于变化的，则不适合使用访问者模式。

## 优点
1. 符合单一职责原则。
2. 优秀的扩展性。
3. 灵活性。

## 缺点
1. 具体元素对访问者公布细节，违反了迪米特原则。
2. 具体元素变更比较困难。
3. 违反了依赖倒置原则，依赖了具体类，没有依赖抽象。

## 例子
如果老师教学反馈得分大于等于85分、学生成绩大于等于90分，则可以入选成绩优秀奖；如果老师论文数目大于8、学生论文数目大于2，则可以入选科研优秀奖。

在这个例子中，老师和学生就是Element，他们的数据结构稳定不变，也就是拥有固定的属性，可以被访问者访问。从上面的描述中，可以发现，对数据结构的操作是多变的，一会儿评选成绩，一会儿评选科研，这样就适合使用访问者模式来分离数据结构和操作。
```java
public interface Visitor {
    void visit(Student element);

    void visit(Teacher element);
}

/**
 * 具体访问者，实现了Visitor中定义的操作。
 */
public class GradeSelection implements Visitor {
    private String awardWords = "[%s]的分数是%d，荣获了成绩优秀奖。";

    @Override
    public void visit(Student element) {
        // 如果学生考试成绩超过90，则入围成绩优秀奖。
        if (element.getGrade() >= 90) {
            System.out.println(String.format(awardWords,
                    element.getName(), element.getGrade()));
        }
    }

    @Override
    public void visit(Teacher element) {
        // 如果老师反馈得分超过85，则入围成绩优秀奖。
        if (element.getScore() >= 85) {
            System.out.println(String.format(awardWords,
                    element.getName(), element.getScore()));
        }
    }
}

public class ResearcherSelection implements Visitor {
    private String awardWords = "[%s]的论文数是%d，荣获了科研优秀奖。";

    @Override
    public void visit(Student element) {
        // 如果学生发表论文数超过2，则入围科研优秀奖。
        if (element.getPaperCount() > 2) {
            System.out.println(String.format(awardWords,
                    element.getName(), element.getPaperCount()));
        }
    }

    @Override
    public void visit(Teacher element) {
        // 如果老师发表论文数超过8，则入围科研优秀奖。
        if (element.getPaperCount() > 8) {
            System.out.println(String.format(awardWords,
                    element.getName(), element.getPaperCount()));
        }
    }
}

/**
 * 抽象元素角色，定义了一个accept操作，以Visitor作为参数，表示可以被访问。
 */
public interface Element {
    // 接受一个抽象访问者访问
    void accept(Visitor visitor);
}

/**
 * 具体元素，允许visitor访问本对象的数据结构。
 */
public class Student implements Element {
    private String name; // 学生姓名
    private int grade; // 成绩
    private int paperCount; // 论文数

    // 构造器
    public Student(String name, int grade, int paperCount) {
        this.name = name;
        this.grade = grade;
        this.paperCount = paperCount;
    }

    // visitor访问本对象的数据结构
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(int paperCount) {
        this.paperCount = paperCount;
    }
}

/**
 * 具体元素，允许visitor访问本对象的数据结构。
 */
public class Teacher implements Element {
    private String name; // 教师姓名
    private int score; // 评价分数
    private int paperCount; // 论文数

    // 构造器
    public Teacher(String name, int score, int paperCount) {
        this.name = name;
        this.score = score;
        this.paperCount = paperCount;
    }

    // visitor访问本对象的数据结构
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(int paperCount) {
        this.paperCount = paperCount;
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        // 初始化元素
        Element stu1 = new Student("Student Jim", 92, 3);
        Element stu2 = new Student("Student Ana", 89, 1);
        Element t1 = new Teacher("Teacher Mike", 83, 10);
        Element t2 = new Teacher("Teacher Lee", 88, 7);
        // 初始化对象结构
        ObjectStructure objectStructure = new ObjectStructure();
        objectStructure.addElement(stu1);
        objectStructure.addElement(stu2);
        objectStructure.addElement(t1);
        objectStructure.addElement(t2);
        // 定义具体访问者，选拔成绩优秀者
        Visitor gradeSelection = new GradeSelection();
        // 具体的访问操作，打印输出访问结果
        objectStructure.accept(gradeSelection);
        System.out.println("----结构不变，操作易变----");
        // 数据结构是没有变化的，如果我们还想增加选拔科研优秀者的操作，那么如下。
        Visitor researcherSelection = new ResearcherSelection();
        objectStructure.accept(researcherSelection);
    }
}

/*
输出：
[Student Jim]的分数是92，荣获了成绩优秀奖。
[Teacher Lee]的分数是88，荣获了成绩优秀奖。
----结构不变，操作易变----
[Student Jim]的论文数是3，荣获了科研优秀奖。
[Teacher Mike]的论文数是10，荣获了科研优秀奖。
*/
```

下面再举一个例子，进一步理解访问者模式解决的问题，假设需要从网站上爬取了很多资源文件，它们的格式有三种：PDF、PPT、Word。现在要开发一个工具来处理这批资源文件。这个工具的其中一个功能是，把这些资源文件中的文本内容抽取出来放到txt文件中。下面是一个实现的思路，ResourceFile是一个抽象类，包含一个抽象函数extract2txt()。PdfFile、PPTFile、WordFile都继承ResourceFile类，并且重写了extract2txt()函数。在ToolApplication中，可以利用多态特性，根据对象的实际类型，来决定执行哪个方法。
```java
public abstract class ResourceFile {
    protected String filePath;

    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }

    public abstract void extract2txt();
}

public class PPTFile extends ResourceFile {
    public PPTFile(String filePath) {
        super(filePath);
    }

    @Override
    public void extract2txt() {
        //...省略一大坨从PPT中抽取文本的代码...
        //...将抽取出来的文本保存在跟filePath同名的.txt文件中...
        System.out.println("Extract PPT.");
    }
}

public class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }

    @Override
    public void extract2txt() {
        //...
        System.out.println("Extract PDF.");
    }
}

public class WordFile extends ResourceFile {
    public WordFile(String filePath) {
        super(filePath);
    }

    @Override
    public void extract2txt() {
        //...
        System.out.println("Extract WORD.");
    }
}

// 运行结果是：
// Extract PDF.
// Extract WORD.
// Extract PPT.
public class ToolApplication {
    public static void main(String[] args) {
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for (ResourceFile resourceFile : resourceFiles) {
        resourceFile.extract2txt();
        }
    }

    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //...根据后缀(pdf/ppt/word)由工厂方法创建不同的类对象(PdfFile/PPTFile/WordFile)
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new PPTFile("c.ppt"));
        return resourceFiles;
    }
}
```

上面的代码确实实现了功能，但是如果工具的功能不停地扩展，不仅要能抽取文本内容，还要支持压缩、提取文件元信息（文件名、大小、更新时间等等）构建索引等一系列的功能，如果继续按照上面的实现思路，就会存在这样几个问题：
- 违背开闭原则，添加一个新的功能，所有类的代码都要修改；
- 每个类的代码都不断膨胀，可读性和可维护性都变差了；
- 把所有比较上层的业务逻辑都耦合到PdfFile、PPTFile、WordFile类中，导致这些类的职责不够单一，变成了大杂烩。

针对上面的问题，常用的解决方法就是拆分解耦，把业务操作跟具体的数据结构解耦，设计成独立的类。这里按照访问者模式的演进思路来对上面的代码进行重构，新增一个Extractor类用于对PdfFile、PPTFile、WordFile执行extract2txt过程。重构之后的代码如下：
```java
public abstract class ResourceFile {
    protected String filePath;
    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }
}

public class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }
    //...
}
//...PPTFile、WordFile代码省略...
public class Extractor {
    // 通过重载实现对PdfFile、PPTFile、WordFile的处理
    public void extract2txt(PPTFile pptFile) {
        //...
        System.out.println("Extract PPT.");
    }

    public void extract2txt(PdfFile pdfFile) {
        //...
        System.out.println("Extract PDF.");
    }

    public void extract2txt(WordFile wordFile) {
        //...
        System.out.println("Extract WORD.");
    }
}

public class ToolApplication {
    public static void main(String[] args) {
        Extractor extractor = new Extractor();
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for (ResourceFile resourceFile : resourceFiles) {
            extractor.extract2txt(resourceFile);
        }
    }

    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //...根据后缀(pdf/ppt/word)由工厂方法创建不同的类对象(PdfFile/PPTFile/WordFile)
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new PPTFile("c.ppt"));
        return resourceFiles;
    }
}
```

上面的代码实际上是变异不了的，出错的行在ToolApplication类的main函数中：`extractor.extract2txt(resourceFile);`。出错是因为多态是一种动态绑定，可以在运行时获取对象的实际类型，来运行实际类型对应的方法。而函数重载是一种静态绑定，在编译时并不能获取对象的实际类型，而是根据声明类型执行声明类型对应的方法。`extractor.extract2txt(resourceFile);`语句传入的参数是ResourceFile类型的，Extractor类中没有定义参数类型是ResourceFile的extract2txt()方法，为了解决这个问题，再次修改代码：
```java

public abstract class ResourceFile {
    protected String filePath;
    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }
    abstract public void accept(Extractor extractor);
}

public class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }

    @Override
    public void accept(Extractor extractor) {
        extractor.extract2txt(this);
    }

    //...
}

//...PPTFile、WordFile跟PdfFile类似，这里就省略了...
//...Extractor代码不变...

public class ToolApplication {
    public static void main(String[] args) {
        Extractor extractor = new Extractor();
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for (ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(extractor);
        }
    }

    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //...根据后缀(pdf/ppt/word)由工厂方法创建不同的类对象(PdfFile/PPTFile/WordFile)
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new PPTFile("c.ppt"));
        return resourceFiles;
    }
}
```

这次将Extractor的extract2txt方法调用推迟到了各个ResourceFile中的实现类中，这样Extractor的extract2txt方法就有足够的类信息进行重载调用，上面的代码已经很接近访问者模式了，访问者模式实际上主要就是为了解决无法重载的问题。

上面的代码实际上还有问题，如果要继续添加新的功能，比如压缩功能，根据不同的文件类型，使用不同的压缩算法来压缩资源文件，那么就需要实现一个类似Extractor类的新类Compressor类，在其中定义三个重载函数，实现对不同类型资源文件的压缩。除此之外，还要在每个资源文件类中定义新的accept重载函数，代码如下：
```java

public abstract class ResourceFile {
    protected String filePath;
    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }
    abstract public void accept(Extractor extractor);
    abstract public void accept(Compressor compressor);
}

public class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }

    @Override
    public void accept(Extractor extractor) {
        extractor.extract2txt(this);
    }

    @Override
    public void accept(Compressor compressor) {
        compressor.compress(this);
    }

    //...
}

//...PPTFile、WordFile跟PdfFile类似，这里就省略了...
//...Extractor代码不变

public class ToolApplication {
    public static void main(String[] args) {
        Extractor extractor = new Extractor();
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for (ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(extractor);
        }

        Compressor compressor = new Compressor();
        for(ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(compressor);
        }
    }

    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //...根据后缀(pdf/ppt/word)由工厂方法创建不同的类对象(PdfFile/PPTFile/WordFile)
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new PPTFile("c.ppt"));
        return resourceFiles;
    }
}
```

为了添加一个新的业务，需要修改每个资源文件类，违反了开闭原则。针对这个问题，抽象出来一个Visitor接口，包含是三个命名非常通用的visit()重载函数，分别处理三种不同类型的资源文件，具体做什么业务处理，由实现这个Visitor接口的具体的类来决定，比如Extractor负责抽取文本内容，Compressor负责压缩。当新添加一个业务功能的时候，资源文件类不需要做任何修改，只需要再实现一个Visitor接口的实现类即可。代码如下：
```java

public abstract class ResourceFile {
    protected String filePath;
    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }

    // 原来传入Extractor或者Compressor的方法改成了由Visitor处理
    abstract public void accept(Visitor vistor);
}

public class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    //...
}
//...PPTFile、WordFile跟PdfFile类似，这里就省略了...

// Visitor接口定义了访问PdfFile、PPTFile、WordFile等文件的方法，具体对这些文件执行什么操作由实现类决定
public interface Visitor {
    void visit(PdfFile pdfFile);
    void visit(PPTFile pdfFile);
    void visit(WordFile pdfFile);
}

// 每个Visitor的实现类各自实现自己的逻辑即可
public class Extractor implements Visitor {
    @Override
    public void visit(PPTFile pptFile) {
        //...
        System.out.println("Extract PPT.");
    }

    @Override
    public void visit(PdfFile pdfFile) {
        //...
        System.out.println("Extract PDF.");
    }

    @Override
    public void visit(WordFile wordFile) {
        //...
        System.out.println("Extract WORD.");
    }
}

public class Compressor implements Visitor {
    @Override
    public void visit(PPTFile pptFile) {
        //...
        System.out.println("Compress PPT.");
    }

    @Override
    public void visit(PdfFile pdfFile) {
        //...
        System.out.println("Compress PDF.");
    }

    @Override
    public void visit(WordFile wordFile) {
        //...
        System.out.println("Compress WORD.");
    }
}

public class ToolApplication {
    public static void main(String[] args) {
        // 直接实例化Visitor接口的实现类并传入各个ResourceFile的实现类中
        Extractor extractor = new Extractor();
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for (ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(extractor);
        }

        Compressor compressor = new Compressor();
        for(ResourceFile resourceFile : resourceFiles) {
            resourceFile.accept(compressor);
        }
    }

    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //...根据后缀(pdf/ppt/word)由工厂方法创建不同的类对象(PdfFile/PPTFile/WordFile)
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new WordFile("b.word"));
        resourceFiles.add(new PPTFile("c.ppt"));
        return resourceFiles;
    }
}
```

从上面的这些例子就能够理解访问者模式解决的问题，访问者模式将对象与操作解耦，将对对象的业务操作抽离出来，定义在独立细分的访问者类（Extractor、Compressor）中，不断添加功能只需要实现新的访问者类即可，同时访问者模式也避免了因为单分派机制导致的编译错误（单分派指的是执行哪个对象的方法，根据对象的运行时类型来决定；执行对象的哪个方法，根据方法参数的编译时类型来决定）。

访问者模式的代码不容易理解，除非必要，否则尽量不要使用这个模式。