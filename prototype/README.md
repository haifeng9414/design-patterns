# 原型模式

## 目的
原型模式要求对象实现一个可以克隆自身的接口。这样一来，通过原型实例创建新的对象，就不需要关心这个实例本身的类型，只需要实现克隆自身的方法，也而无需再去通过new来创建。

## 优点
1. 逃避构造函数的约束。

## 缺点
1. 原型模式最主要的缺点是每一个类都必须要配备一个克隆方法。配备克隆方法需要对类的功能进行通盘考虑，这对于全新的类来说并不是很难，但是对于已有的类来说并不容易。

## 例子
原型模式简单来说就是对象实现了clone方法或者定义了类似功能的接口，客户端直接通过对象就能创建一个新的对象，或通过工厂创建，而不用自己new一个：
```java
public class HeroFactoryImpl implements HeroFactory {
    private Mage mage;
    private Warlord warlord;
    private Beast beast;

    /**
     * Constructor
     */
    public HeroFactoryImpl(Mage mage, Warlord warlord, Beast beast) {
        this.mage = mage;
        this.warlord = warlord;
        this.beast = beast;
    }

    /**
     * Create mage
     */
    public Mage createMage() {
        try {
            // 简单起见，这里直接用clone方法表示对象根据自己创建对象的过程
            return mage.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Create warlord
     */
    public Warlord createWarlord() {
        try {
            return warlord.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Create beast
     */
    public Beast createBeast() {
        try {
            return beast.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
```

使用：
```java
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        HeroFactory factory;
        Mage mage;
        Warlord warlord;
        Beast beast;

        factory = new HeroFactoryImpl(new ElfMage(), new ElfWarlord(), new ElfBeast());
        mage = factory.createMage();
        warlord = factory.createWarlord();
        beast = factory.createBeast();
        LOGGER.info(mage.toString());
        LOGGER.info(warlord.toString());
        LOGGER.info(beast.toString());

        factory = new HeroFactoryImpl(new OrcMage(), new OrcWarlord(), new OrcBeast());
        mage = factory.createMage();
        warlord = factory.createWarlord();
        beast = factory.createBeast();
        LOGGER.info(mage.toString());
        LOGGER.info(warlord.toString());
        LOGGER.info(beast.toString());
    }
}
/*
输出：
16:23:17.890 [main] INFO com.dhf.Application - Elven mage
16:23:17.893 [main] INFO com.dhf.Application - Elven warlord
16:23:17.893 [main] INFO com.dhf.Application - Elven eagle
16:23:17.893 [main] INFO com.dhf.Application - Orcish mage
16:23:17.893 [main] INFO com.dhf.Application - Orcish warlord
16:23:17.893 [main] INFO com.dhf.Application - Orcish wolf
*/
```