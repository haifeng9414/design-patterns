# 享元模式

## 目的
通过共享技术实现相同或相似对象的重用，在享元模式中可以共享的相同内容称为内部状态（IntrinsicState），而那些需要外部环境来设置的不能共享的内容称为外部状态（Extrinsic State），由于区分了内部状态和外部状态，因此可以通过设置不同的外部状态使得相同的对象可以具有一些不同的特征，而相同的内部状态是可以共享的。

在享元模式中通常会出现工厂模式，需要创建一个享元工厂来负责维护一个享元池（Flyweight Pool）用于存储具有相同内部状态的享元对象。

在享元模式中共享的是享元对象的内部状态，外部状态需要通过环境来设置。在实际使用中，能够共享的内部状态是有限的，因此享元对象一般都设计为较小的对象，它所包含的内部状态较少，这种对象也称为细粒度对象。享元模式的目的就是使用共享技术来实现大量细粒度对象的复用。

## 优点
1. 可以极大减少内存中对象的数量，使得相同对象或相似对象在内存中只保存一份。
2. 享元模式的外部状态相对独立，而且不会影响其内部状态，从而使得享元对象可以在不同的环境中被共享。

## 缺点
1. 享元模式使得系统更加复杂，需要分离出内部状态和外部状态，这使得程序的逻辑复杂化。
2. 为了使对象可以共享，享元模式需要将享元对象的状态外部化，而读取外部状态使得运行时间变长。

## 例子
假设架子上有很多药水，客户端想要喝掉架子上所有的药水，针对药水对象，就可以用享元模式避免重复定义相同的药水：
```java
public class AlchemistShop {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlchemistShop.class);

    private List<Potion> topShelf;
    private List<Potion> bottomShelf;

    /**
     * Constructor
     */
    public AlchemistShop() {
        topShelf = new ArrayList<>();
        bottomShelf = new ArrayList<>();
        fillShelves();
    }

    private void fillShelves() {
        // PotionFactory用于创建药水，内部用一个map保存了所有已经创建过的药水类型，其createPotion方法根据传入的参数
        // 创建药水，并把创建的新药水保存到map，避免以后再创建，所以下面调用createPotion方法时传入的参数如果相等则获取
        // 到的就是同一个对象
        PotionFactory factory = new PotionFactory();

        topShelf.add(factory.createPotion(PotionType.INVISIBILITY));
        topShelf.add(factory.createPotion(PotionType.INVISIBILITY));
        topShelf.add(factory.createPotion(PotionType.STRENGTH));
        topShelf.add(factory.createPotion(PotionType.HEALING));
        topShelf.add(factory.createPotion(PotionType.INVISIBILITY));
        topShelf.add(factory.createPotion(PotionType.STRENGTH));
        topShelf.add(factory.createPotion(PotionType.HEALING));
        topShelf.add(factory.createPotion(PotionType.HEALING));

        bottomShelf.add(factory.createPotion(PotionType.POISON));
        bottomShelf.add(factory.createPotion(PotionType.POISON));
        bottomShelf.add(factory.createPotion(PotionType.POISON));
        bottomShelf.add(factory.createPotion(PotionType.HOLY_WATER));
        bottomShelf.add(factory.createPotion(PotionType.HOLY_WATER));
    }

    /**
     * Get a read-only list of all the items on the top shelf
     *
     * @return The top shelf potions
     */
    public final List<Potion> getTopShelf() {
        return Collections.unmodifiableList(this.topShelf);
    }

    /**
     * Get a read-only list of all the items on the bottom shelf
     *
     * @return The bottom shelf potions
     */
    public final List<Potion> getBottomShelf() {
        return Collections.unmodifiableList(this.bottomShelf);
    }

    /**
     * Enumerate potions
     */
    public void enumerate() {
        LOGGER.info("Enumerating top shelf potions\n");

        for (Potion p : topShelf) {
            p.drink();
        }

        LOGGER.info("Enumerating bottom shelf potions\n");

        for (Potion p : bottomShelf) {
            p.drink();
        }
    }
}

public class PotionFactory {
    private final Map<PotionType, Potion> potions;

    public PotionFactory() {
        potions = new EnumMap<>(PotionType.class);
    }

    Potion createPotion(PotionType type) {
        Potion potion = potions.get(type);
        if (potion == null) {
            switch (type) {
                case HEALING:
                    potion = new HealingPotion();
                    potions.put(type, potion);
                    break;
                case HOLY_WATER:
                    potion = new HolyWaterPotion();
                    potions.put(type, potion);
                    break;
                case INVISIBILITY:
                    potion = new InvisibilityPotion();
                    potions.put(type, potion);
                    break;
                case POISON:
                    potion = new PoisonPotion();
                    potions.put(type, potion);
                    break;
                case STRENGTH:
                    potion = new StrengthPotion();
                    potions.put(type, potion);
                    break;
                default:
                    break;
            }
        }
        return potion;
    }
}
```

使用：
```java
public class Application {
    public static void main(String[] args) {
        AlchemistShop alchemistShop = new AlchemistShop();
        alchemistShop.enumerate();
    }
}

/*
21:05:22.451 [main] INFO com.dhf.flyweight.AlchemistShop - Enumerating top shelf potions

21:05:22.455 [main] INFO com.dhf.flyweight.InvisibilityPotion - You become invisible. (Potion=777874839)
21:05:22.457 [main] INFO com.dhf.flyweight.InvisibilityPotion - You become invisible. (Potion=777874839)
21:05:22.457 [main] INFO com.dhf.flyweight.StrengthPotion - You feel strong. (Potion=565760380)
21:05:22.457 [main] INFO com.dhf.flyweight.HealingPotion - You feel healed. (Potion=6566818)
21:05:22.457 [main] INFO com.dhf.flyweight.InvisibilityPotion - You become invisible. (Potion=777874839)
21:05:22.457 [main] INFO com.dhf.flyweight.StrengthPotion - You feel strong. (Potion=565760380)
21:05:22.457 [main] INFO com.dhf.flyweight.HealingPotion - You feel healed. (Potion=6566818)
21:05:22.457 [main] INFO com.dhf.flyweight.HealingPotion - You feel healed. (Potion=6566818)
21:05:22.458 [main] INFO com.dhf.flyweight.AlchemistShop - Enumerating bottom shelf potions

21:05:22.458 [main] INFO com.dhf.flyweight.PoisonPotion - Urgh! This is poisonous. (Potion=1494279232)
21:05:22.458 [main] INFO com.dhf.flyweight.PoisonPotion - Urgh! This is poisonous. (Potion=1494279232)
21:05:22.458 [main] INFO com.dhf.flyweight.PoisonPotion - Urgh! This is poisonous. (Potion=1494279232)
21:05:22.458 [main] INFO com.dhf.flyweight.HolyWaterPotion - You feel blessed. (Potion=1650967483)
21:05:22.458 [main] INFO com.dhf.flyweight.HolyWaterPotion - You feel blessed. (Potion=1650967483)
*/
```

还有一个例子，假设我们在开发一个棋牌游戏（比如象棋）。一个游戏厅中有成千上万个房间，每个房间对应一个棋局。棋局要保存每个棋子的数据，比如：棋子类型（将、相、士、炮等）、棋子颜色（红方、黑方）、棋子在棋局中的位置。利用这些数据，就能显示一个完整的棋盘给玩家。具体的代码如下：
```java

public class ChessPiece {//棋子
    private int id;
    private String text;
    private Color color;
    private int positionX;
    private int positionY;

    public ChessPiece(int id, String text, Color color, int positionX, int positionY) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionX;
    }

    public static enum Color {
        RED, BLACK
    }

    // ...省略其他属性和getter/setter方法...
}

public class ChessBoard {//棋局
    private Map<Integer, ChessPiece> chessPieces = new HashMap<>();

    public ChessBoard() {
        init();
    }

    private void init() {
        chessPieces.put(1, new ChessPiece(1, "車", ChessPiece.Color.BLACK, 0, 0));
        chessPieces.put(2, new ChessPiece(2,"馬", ChessPiece.Color.BLACK, 0, 1));
        //...省略摆放其他棋子的代码...
    }

    public void move(int chessPieceId, int toPositionX, int toPositionY) {
        //...省略...
    }
}
```

如果游戏大厅中有成千上万的房间，那保存这么多棋局对象就会消耗大量的内存。上面的实现在内存中会有大量的相似对象。这些相似对象的id、text、color都是相同的，唯独positionX、positionY不同。可以通过享元模式将棋子的id、text、color属性拆分出来，设计成独立的类，并且作为享元供多个棋盘复用。这样，棋盘只需要记录每个棋子的位置信息就可以了：
```java

// 享元类，将棋子的id、text、color作为共享的属性
public class ChessPieceUnit {
    private int id;
    private String text;
    private Color color;

    public ChessPieceUnit(int id, String text, Color color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    public static enum Color {
        RED, BLACK
    }

    // ...省略其他属性和getter方法...
}

// 享元模式中的关键实现，享元对象的工厂
public class ChessPieceUnitFactory {
    private static final Map<Integer, ChessPieceUnit> pieces = new HashMap<>();

    // 通过静态代码块初始化享元对象
    static {
        pieces.put(1, new ChessPieceUnit(1, "車", ChessPieceUnit.Color.BLACK));
        pieces.put(2, new ChessPieceUnit(2,"馬", ChessPieceUnit.Color.BLACK));
        //...省略摆放其他棋子的代码...
    }

    public static ChessPieceUnit getChessPiece(int chessPieceId) {
        return pieces.get(chessPieceId);
    }
}

// 棋子类，有一个享元属性
public class ChessPiece {
    private ChessPieceUnit chessPieceUnit;
    private int positionX;
    private int positionY;

    public ChessPiece(ChessPieceUnit unit, int positionX, int positionY) {
        this.chessPieceUnit = unit;
        this.positionX = positionX;
        this.positionY = positionY;
    }
    // 省略getter、setter方法
}

public class ChessBoard {
    private Map<Integer, ChessPiece> chessPieces = new HashMap<>();

    public ChessBoard() {
        init();
    }

    private void init() {
        // 为棋盘创建棋子对象，通过ChessPieceUnitFactory.getChessPiece方法实现id、text、color属性的共享
        chessPieces.put(1, new ChessPiece(
                ChessPieceUnitFactory.getChessPiece(1), 0,0));
        chessPieces.put(1, new ChessPiece(
                ChessPieceUnitFactory.getChessPiece(2), 1,0));
        //...省略摆放其他棋子的代码...
    }

    public void move(int chessPieceId, int toPositionX, int toPositionY) {
        //...省略...
    }
}
```