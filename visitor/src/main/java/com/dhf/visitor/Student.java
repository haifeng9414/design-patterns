package com.dhf.visitor;

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