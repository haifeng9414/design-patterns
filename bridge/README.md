# 桥接模式

## 目的
将抽象部分与它的实现部分分离，使它们都可以独立地变化。

## 优点
抽象和实现的分离，提高了扩展能力，实现细节对客户透明

## 缺点
桥接模式的引入会增加系统的理解与设计难度，由于聚合关联关系建立在抽象层，要求开发者针对抽象进行设计与编程。

## 例子
有一个附魔武器的接口，武器能够挥舞，同时武器也能有魔法攻击，如果将魔法动作和武器本身的动作都定义在接口方法中，则针对每种武器的每种魔法，都要有一个具体的实现类，显然是不应该的。为了武器和魔法动作能够分别变化，将魔法动作抽象为一个接口，武器接口只需要能够访问到魔法接口，就能实现武器和魔法动作的解耦，如武器和魔法的接口定义：
```java
/**
 * 武器
 */
public interface Weapon {
    /**
     * 出鞘
     */
    void wield();

    /**
     * 挥舞
     */
    void swing();

    /**
     * 收鞘
     */
    void unWield();

    /**
     * 武器可以有魔法动作
     */
    Enchantment getEnchantment();
}

/**
 * 魔法动作
 */
public interface Enchantment {
    /**
     * 激活魔法
     */
    void onActivate();

    /**
     * 使用魔法
     */
    void apply();

    /**
     * 取消魔法
     */
    void onDeactivate();
}
```

针对武器的接口的实现：
```java
/**
 * 剑
 */
public class Sword implements Weapon {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sword.class);

    private final Enchantment enchantment;

    public Sword(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public void wield() {
        LOGGER.info("剑出鞘");
        enchantment.onActivate();
    }

    @Override
    public void swing() {
        LOGGER.info("挥舞剑");
        enchantment.apply();
    }

    @Override
    public void unWield() {
        LOGGER.info("剑收鞘");
        enchantment.onDeactivate();
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }
}

/**
 * 锤子
 */
public class Hammer implements Weapon {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hammer.class);

    private final Enchantment enchantment;

    public Hammer(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public void wield() {
        LOGGER.info("拿出锤子");
        enchantment.onActivate();
    }

    @Override
    public void swing() {
        LOGGER.info("挥舞锤子");
        enchantment.apply();
    }

    @Override
    public void unWield() {
        LOGGER.info("收起锤子");
        enchantment.onDeactivate();
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }
}
```

针对魔法接口的实现：
```java
/**
 * 飞行魔法
 */
public class FlyingEnchantment implements Enchantment {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlyingEnchantment.class);

    @Override
    public void onActivate() {
        LOGGER.info("物品开始微弱地发光");
    }

    @Override
    public void apply() {
        LOGGER.info("该物品飞走并击中了敌人，最终回到了主人的手中");
    }

    @Override
    public void onDeactivate() {
        LOGGER.info("物品的光晕消失");
    }
}

/**
 * 噬魂魔法
 */
public class SoulEatingEnchantment implements Enchantment {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoulEatingEnchantment.class);

    @Override
    public void onActivate() {
        LOGGER.info("该物品散布嗜血的味道");
    }

    @Override
    public void apply() {
        LOGGER.info("该物品吞噬了敌人的灵魂");
    }

    @Override
    public void onDeactivate() {
        LOGGER.info("嗜血慢慢消失");
    }
}
```

使用：
```java
public static void main(String[] args) {
    LOGGER.info("骑士收到一把噬魂的附魔剑");
    Sword enchantedSword = new Sword(new SoulEatingEnchantment());
    enchantedSword.wield();
    enchantedSword.swing();
    enchantedSword.unWield();

    LOGGER.info("骑士收到一把会飞的附魔剑");
    enchantedSword = new Sword(new FlyingEnchantment());
    enchantedSword.wield();
    enchantedSword.swing();
    enchantedSword.unWield();

    LOGGER.info("女武神得到了一个会飞的附魔锤子");
    Hammer hammer = new Hammer(new FlyingEnchantment());
    hammer.wield();
    hammer.swing();
    hammer.unWield();
}

/*
输出：
15:00:43.644 [main] INFO com.dhf.Application - 骑士收到一把噬魂的附魔剑
15:00:43.649 [main] INFO com.dhf.bridge.Sword - 剑出鞘
15:00:43.649 [main] INFO com.dhf.bridge.SoulEatingEnchantment - 该物品散布嗜血的味道
15:00:43.649 [main] INFO com.dhf.bridge.Sword - 挥舞剑
15:00:43.649 [main] INFO com.dhf.bridge.SoulEatingEnchantment - 该物品吞噬了敌人的灵魂
15:00:43.649 [main] INFO com.dhf.bridge.Sword - 剑收鞘
15:00:43.649 [main] INFO com.dhf.bridge.SoulEatingEnchantment - 嗜血慢慢消失
15:00:43.649 [main] INFO com.dhf.Application - 骑士收到一把会飞的附魔剑
15:00:43.650 [main] INFO com.dhf.bridge.Sword - 剑出鞘
15:00:43.650 [main] INFO com.dhf.bridge.FlyingEnchantment - 物品开始微弱地发光
15:00:43.650 [main] INFO com.dhf.bridge.Sword - 挥舞剑
15:00:43.650 [main] INFO com.dhf.bridge.FlyingEnchantment - 该物品飞走并击中了敌人，最终回到了主人的手中
15:00:43.650 [main] INFO com.dhf.bridge.Sword - 剑收鞘
15:00:43.650 [main] INFO com.dhf.bridge.FlyingEnchantment - 物品的光晕消失
15:00:43.650 [main] INFO com.dhf.Application - 女武神得到了一个会飞的附魔锤子
15:00:43.650 [main] INFO com.dhf.bridge.Hammer - 拿出锤子
15:00:43.650 [main] INFO com.dhf.bridge.FlyingEnchantment - 物品开始微弱地发光
15:00:43.650 [main] INFO com.dhf.bridge.Hammer - 挥舞锤子
15:00:43.650 [main] INFO com.dhf.bridge.FlyingEnchantment - 该物品飞走并击中了敌人，最终回到了主人的手中
15:00:43.650 [main] INFO com.dhf.bridge.Hammer - 收起锤子
15:00:43.650 [main] INFO com.dhf.bridge.FlyingEnchantment - 物品的光晕消失
*/
```

可以看到，将魔法动作提取成接口后，武器的改变和魔法的改变互不影响