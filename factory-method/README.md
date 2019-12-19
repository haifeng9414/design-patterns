# 工厂方法模式

## 目的
定义一个用于创建对象的接口，让子类决定实例化哪一个类。如假定现在有两个厨师，一个只会做中餐，另一个只会做西餐，餐品分为熟食和生食两类。顾客需要顾客需要根据自己的口味来选择对应的厨师并告知其需要熟食还是生食，厨师根据顾客的口味来进行烹制。

## 优点
1. 更符合开-闭原则，新增一种产品时，只需要增加相应的具体产品类和相应的工厂子类即可。
2. 符合单一职责原则，每个具体工厂类只负责创建对应的产品。

## 缺点
1. 添加新产品时，除了增加新产品类外，还要提供与之对应的具体工厂类，系统类的个数将成对增加，在一定程度上增加了系统的复杂度。
2. 虽然保证了工厂方法内的对修改关闭，但对于使用工厂方法的类，如果要更换另外一种产品，仍然需要修改实例化的具体工厂类。

## 例子
Blacksmith接口定义了创建武器的方法，子类实现该方法创建具体的武器，客户端只需要实例化具体的子类就能获取到对应的产品：
```java
public interface Blacksmith {
    Weapon manufactureWeapon(WeaponType weaponType);
}

public class ElfBlacksmith implements Blacksmith {

    // 这里参数为weaponType，这个参数应该是给具体的产品用的，和工厂创建哪个产品和如何创建产品没有关系
    // 即客户端需要一个带指定的weaponType属性的weapon，但是不知道具体的weapon类型，所以委托给某个客户端
    // 指定的Blacksmith铁匠来创建这个weapon，同时客户端将weaponType作为参数传给铁匠以达到将参数应用到
    // 最后返回的weapon中
    public Weapon manufactureWeapon(WeaponType weaponType) {
        return new ElfWeapon(weaponType);
    }
}

public class OrcBlacksmith implements Blacksmith {
    public Weapon manufactureWeapon(WeaponType weaponType) {
        return new OrcWeapon(weaponType);
    }
}
```

使用：
```java
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final Blacksmith blacksmith;

    private Application(Blacksmith blacksmith) {
        this.blacksmith = blacksmith;
    }

    public static void main(String[] args) {
        Application app = new Application(new OrcBlacksmith());
        app.manufactureWeapons();

        app = new Application(new ElfBlacksmith());
        app.manufactureWeapons();
    }

    private void manufactureWeapons() {
        Weapon weapon;
        weapon = blacksmith.manufactureWeapon(WeaponType.SPEAR);
        LOGGER.info(weapon.toString());
        weapon = blacksmith.manufactureWeapon(WeaponType.AXE);
        LOGGER.info(weapon.toString());
    }
}

/*
输出：
21:00:51.578 [main] INFO com.dhf.Application - Orcish spear
21:00:51.581 [main] INFO com.dhf.Application - Orcish axe
21:00:51.582 [main] INFO com.dhf.Application - Elven spear
21:00:51.582 [main] INFO com.dhf.Application - Elven axe
*/
```