# 适配器模式 (包装器模式)

## 目的
将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。

适配器模式可以看作一种补偿模式，用来补救设计上的缺陷。应用这种模式算是无奈之举。如果在设计初期，能协调规避接口不兼容的问题，那这种模式就没有应用的机会了。

## 优点
可以让任何两个没有关联的类一起运行。

## 缺点
过多地使用适配器，会让系统非常零乱，不易整体进行把握。比如，明明看到调用的是A接口，其实内部被适配成了B接口的实现，一个系统如果太多出现这种情况，无异于一场灾难。因此如果不是很有必要，可以不使用适配器，而是直接对系统进行重构。

## 例子
鸭子和火鸡是两个没有直接关联的类型，但是他们能做相似的事，可以通过适配器将火鸡的行为适配成鸭子的行为，使得操作火鸡能够和操作鸭子一样：
```java
public interface Duck {
    /**
     * 鸭子会飞
     */
    void fly();

    /**
     * 鸭子会嘎嘎叫
     */
    void quack();
}

public interface Turkey {
    /**
     * 火鸡会飞
     */
    void fly();

    /**
     * 火鸡会咯咯叫
     */
    void gobble();
}

/**
 * 火鸡适配器，将火鸡转为鸭子
 */
public class TurkeyAdapter implements Duck {
    Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    /**
     * 火鸡要飞3次才能到鸭子的距离，这里要表达的意思是火鸡执行鸭子的动作可能需要些变化
     */
    @Override
    public void fly() {
        this.turkey.fly();
        this.turkey.fly();
        this.turkey.fly();
    }

    @Override
    public void quack() {
        this.turkey.gobble();
    }
}

public static void main(String[] args) {
    Duck duck = new ADuck();
    Turkey turkey = new ATurkey();
    TurkeyAdapter turkeyAdapter = new TurkeyAdapter(turkey);
    duck.fly();
    turkeyAdapter.fly();
    duck.quack();
    turkeyAdapter.quack();
}
```

上面是通过组合的形式实现适配器（构造函数传入被适配的对象），可以认为是对象适配器，另外还可以通过继承实现适配器模式，可以认为是类适配器，下面是类适配器和对象适配器的简单例子：
```java
// 类适配器: 基于继承
public interface ITarget {
    void f1();
    void f2();
    void fc();
}

public class Adaptee {
    public void fa() { //... }
    public void fb() { //... }
    public void fc() { //... }
}

// 适配器继承被适配类，实现接口
public class Adaptor extends Adaptee implements ITarget {
    public void f1() {
        super.fa();
    }
    
    public void f2() {
        //...重新实现f2()...
    }
    
    // 这里fc()不需要实现，直接继承自Adaptee，这是跟对象适配器最大的不同点
}

// 对象适配器，基于组合
public interface ITarget {
    void f1();
    void f2();
    void fc();
}

public class Adaptee {
    public void fa() { //... }
    public void fb() { //... }
    public void fc() { //... }
}

// 适配器组合被适配对象，实现接口
public class Adaptor implements ITarget {
    private Adaptee adaptee;
    
    public Adaptor(Adaptee adaptee) {
        this.adaptee = adaptee;
    }
    
    public void f1() {
        adaptee.fa(); //委托给Adaptee
    }
    
    public void f2() {
        //...重新实现f2()...
    }
    
    public void fc() {
        adaptee.fc();
    }
}
```

对象适配器和类适配器分别有自己的应用场景，判断使用哪种形式的标准主要有两个，一个是被适配的类的方法个数，另一个是被适配类和适配器接口（上面的ITarget）的契合程度：
- 如果被适配的类的方法接口并不多，那两种实现方式都可以
- 如果被适配的类的方法很多，而且被适配的类和适配器接口的方法定义大部分都相同，那推荐使用类适配器，因为适配器实现类可以复用父类的方法
- 如果被适配的类的方法很多，而且被适配的类和适配器接口定义大部分都不相同，那推荐使用对象适配器，因为组合结构相对于继承更加灵活

适配器模式常用的场景有：
- 封装有缺陷的接口设计，假设依赖的外部系统在接口设计方面有缺陷（比如包含大量静态方法），引入之后会影响到自身代码的可测试性。为了隔离设计上的缺陷，对外部系统提供的接口进行二次封装，抽象出更好的接口设计，这个时候就可以使用适配器模式：
```java

public class CD { //这个类来自外部sdk，我们无权修改它的代码
    public static void staticFunction1() { //... }
    
    public void uglyNamingFunction2() { //... }

    public void tooManyParamsFunction3(int paramA, int paramB, ...) { //... }
    
    public void lowPerformanceFunction4() { //... }
}

// 使用适配器模式进行重构
public class ITarget {
    void function1();
    void function2();
    void fucntion3(ParamsWrapperDefinition paramsWrapper);
    void function4();
    //...
}
// 适配器实现类
public class CDAdaptor extends CD implements ITarget {
    //...
    public void function1() {
        super.staticFunction1();
    }
    
    public void function2() {
        super.uglyNamingFucntion2();
    }
    
    public void function3(ParamsWrapperDefinition paramsWrapper) {
        super.tooManyParamsFunction3(paramsWrapper.getParamA(), ...);
    }
    
    public void function4() {
        //...reimplement it...
    }
}
```
- 统一多个类的接口设计，某个功能的实现依赖多个外部系统（或者说类）。通过适配器模式，将它们的接口适配为统一的接口定义，然后就可以使用多态的特性来复用代码逻辑。

    假设我们的系统要对用户输入的文本内容做敏感词过滤，为了提高过滤的召回率，引入了多款第三方敏感词过滤系统，依次对用户输入的内容进行过滤，过滤掉尽可能多的敏感词。但是，每个系统提供的过滤接口都是不同的。这就意味着没法复用一套逻辑来调用各个系统。这个时候，就可以使用适配器模式，将所有系统的接口适配为统一的接口定义，这样可以复用调用敏感词过滤的代码。下面是例子：
```java
public class ASensitiveWordsFilter { // A敏感词过滤系统提供了两个过滤方法
    public String filterSexyWords(String text) {
        // ...
    }
    
    public String filterPoliticalWords(String text) {
        // ...
    } 
}

public class BSensitiveWordsFilter  { // B敏感词过滤系统提供了一个过滤方法
    public String filter(String text) {
        //...
    }
}

public class CSensitiveWordsFilter { // C敏感词过滤系统提供了一个过滤方法
    public String filter(String text, String mask) {
        //...
    }
}

// 未使用适配器模式之前的代码：代码的可测试性、扩展性不好
public class RiskManagement {
    // 需要组合多个过滤系统
    private ASensitiveWordsFilter aFilter = new ASensitiveWordsFilter();
    private BSensitiveWordsFilter bFilter = new BSensitiveWordsFilter();
    private CSensitiveWordsFilter cFilter = new CSensitiveWordsFilter();
    
    // 过滤方法的实现中需要调用多个过滤系统的不同方法
    public String filterSensitiveWords(String text) {
        String maskedText = aFilter.filterSexyWords(text);
        maskedText = aFilter.filterPoliticalWords(maskedText);
        maskedText = bFilter.filter(maskedText);
        maskedText = cFilter.filter(maskedText, "***");
        return maskedText;
    }
}

// 使用适配器模式进行改造，统一接口定义，定义一个适配器接口
public interface ISensitiveWordsFilter {
    String filter(String text);
}

// 为每个过滤系统创建一个适配器，各自实现自己的过滤逻辑
public class ASensitiveWordsFilterAdaptor implements ISensitiveWordsFilter {
    private ASensitiveWordsFilter aFilter;
    public String filter(String text) {
        String maskedText = aFilter.filterSexyWords(text);
        maskedText = aFilter.filterPoliticalWords(maskedText);
        return maskedText;
    }
}
// 省略BSensitiveWordsFilterAdaptor、CSensitiveWordsFilterAdaptor，B、C过滤系统的适配器也是分别通过组合实现ISensitiveWordsFilter接口

// 扩展性更好，更加符合开闭原则，如果添加一个新的敏感词过滤系统，
// 这个类完全不需要改动；而且基于接口而非实现编程，代码的可测试性更好。
public class RiskManagement { 
    private List<ISensitiveWordsFilter> filters = new ArrayList<>();
    
    public void addSensitiveWordsFilter(ISensitiveWordsFilter filter) {
        filters.add(filter);
    }
    
    public String filterSensitiveWords(String text) {
        String maskedText = text;
        for (ISensitiveWordsFilter filter : filters) {
        maskedText = filter.filter(maskedText);
        }
        return maskedText;
    }
}
```
- 替换依赖的外部系统，把项目中依赖的一个外部系统替换为另一个外部系统的时候，利用适配器模式，可以减少对代码的改动：
```java

// 外部系统A
public interface IA {
    //...
    void fa();
}
public class A implements IA {
    //...
    public void fa() { //... }
}
// 在我们的项目中，外部系统A的使用示例
public class Demo {
    private IA a;
    public Demo(IA a) {
        this.a = a;
    }
    //...
}
Demo d = new Demo(new A());

// 将外部系统A替换成外部系统B
public class BAdaptor implemnts IA {
    private B b;
    public BAdaptor(B b) {
        this.b= b;
    }
    public void fa() {
        //...
        b.fb();
    }
}
// 借助BAdaptor，Demo的代码中，调用IA接口的地方都无需改动，
// 只需要将BAdaptor如下注入到Demo即可。
Demo d = new Demo(new BAdaptor(new B()));
```

- 兼容老版本接口，在做版本升级的时候，对于一些要废弃的接口，不直接将其删除，而是暂时保留，并且标注为deprecated，并将内部实现逻辑委托为新的接口实现。这样做的好处是，让使用它的项目有个过渡期，而不是强制进行代码修改。
- Java中有很多日志框架，在项目开发中，常常用它们来打印日志信息。其中，比较常用的有log4j、logback，以及JDK提供的JUL(java.util.logging) 和Apache的JCL(Jakarta Commons Logging) 等。如果某个项目使用了一种日志框架，引入的组件使用了另一种日志框架，这样项目相当于使用了两种日志框架，每种日志框架都有自己特有的配置方式，需要针对每种日志框架编写不同的配置文件（比如，日志存储的文件地址、打印日志的格式）。如果引入多个组件，每个组件使用的日志框架都不一样，那日志本身的管理工作就变得非常复杂。为了解决这个问题，我们需要统一日志打印框架。slf4j日志框架就是为了解决这个问题，slf4j相当于JDBC规范，提供了一套打印日志的统一接口规范。不过，它只定义了接口，并没有提供具体的实现，需要配合其他日志框架（log4j、logback等）来使用。但是slf4j的出现晚于JUL、JCL、log4j等日志框架，所以slf4j提供了针对不同日志框架的适配器。对不同日志框架的接口进行二次封装，适配成统一的slf4j接口定义。

## 代理、桥接、装饰器、适配器 4 种设计模式的区别
代理、桥接、装饰器、适配器，这4种模式是比较常用的结构型设计模式。它们的代码结构非常相似。笼统来说，它们都可以称为Wrapper模式，也就是通过Wrapper类二次封装原始类。尽管代码结构相似，但这4种设计模式的用意完全不同，也就是说要解决的问题、应用场景不同，下面是这几种设计模式的区别：
- 代理模式：代理模式在不改变原始类接口的条件下，为原始类定义一个代理类，主要目的是控制访问，而非加强功能，这是它跟装饰器模式最大的不同。
- 桥接模式：桥接模式的目的是将接口部分和实现部分分离，从而让它们可以较为容易、也相对独立地加以改变。
- 装饰器模式：装饰者模式在不改变原始类接口的情况下，对原始类功能进行增强，并且支持多个装饰器的嵌套使用。
- 适配器模式：适配器模式是一种事后的补救策略。适配器提供跟原始类不同的接口，而代理模式、装饰器模式提供的都是跟原始类相同的接口。