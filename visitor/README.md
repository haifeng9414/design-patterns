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

在这个例子中，老师和学生就是Element，他们的数据结构稳定不变，也就是拥有固定的属性，可以被访问者访问。从上面的描述中，可以

发现，对数据结构的操作是多变的，一会儿评选成绩，一会儿评选科研，这样就适合使用访问者模式来分离数据结构和操作。
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