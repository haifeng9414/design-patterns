# 原型模式

## 目的
原型模式要求对象实现一个可以克隆自身的接口。这样一来，通过原型实例创建新的对象，就不需要关心这个实例本身的类型，只需要实现克隆自身的方法，也而无需再去通过new来创建。

## 优点
1. 逃避构造函数的约束。
2. 如果对象的创建过程代价比较大（排序、计算某个值、网络调用等），基于已有对象创建出来的对象不需要这些代价比较大的初始化过程，直接从已有对象复制数据即可。

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

极客时间《设计模式之美》47讲的例子：

假设数据库中存储了大约10万条“搜索关键词”信息，每条信息包含关键词、关键词被搜索的次数、信息最近被更新的时间等。系统A在启动的时候会加载这份数
据到内存中，用于满足其他业务的查询需求。为了方便快速地查找某个关键词对应的信息，给关键词建立一个散列表索引。

假设还有另外一个系统B，专门用来分析搜索日志，定期（比如间隔10分钟）批量地更新数据库中的数据，并且标记为新的数据版本。为了保证系统A中数据的
实时性（不一定非常实时，但数据也不能太旧），系统A需要定期根据数据库中的数据，更新内存中的索引和数据。

下面是简单的实现：
```java

public class Demo {
  private ConcurrentHashMap<String, SearchWord> currentKeywords = new ConcurrentHashMap<>();
  private long lastUpdateTime = -1;

  public void refresh() {
    // 从数据库中取出更新时间>lastUpdateTime的数据，放入到currentKeywords中
    List<SearchWord> toBeUpdatedSearchWords = getSearchWords(lastUpdateTime);
    long maxNewUpdatedTime = lastUpdateTime;
    for (SearchWord searchWord : toBeUpdatedSearchWords) {
      if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
        maxNewUpdatedTime = searchWord.getLastUpdateTime();
      }
      if (currentKeywords.containsKey(searchWord.getKeyword())) {
        currentKeywords.replace(searchWord.getKeyword(), searchWord);
      } else {
        currentKeywords.put(searchWord.getKeyword(), searchWord);
      }
    }

    lastUpdateTime = maxNewUpdatedTime;
  }

  private List<SearchWord> getSearchWords(long lastUpdateTime) {
    // TODO: 从数据库中取出更新时间大于lastUpdateTime的数据
    return null;
  }
}
```

如果有一个特殊的要求：任何时刻，系统A中的所有数据都必须是同一个版本的，要么都是版本a（时间点a的数据），要么都是版本b（时间点b的数据），不
能有的是版本a，有的是版本b。那上面的更新方式就不能满足这个要求了。同时我们还要求在更新内存数据的时候，系统A不能处于不可用状态，也就是不能停
机更新数据。

针对这些问题，只需要每次更新的时候从数据库查出所有数据，重新创建map的记录即可：
```java

public class Demo {
  private HashMap<String, SearchWord> currentKeywords=new HashMap<>();

  public void refresh() {
    HashMap<String, SearchWord> newKeywords = new LinkedHashMap<>();

    // 从数据库中取出所有的数据，放入到newKeywords中
    List<SearchWord> toBeUpdatedSearchWords = getSearchWords();
    for (SearchWord searchWord : toBeUpdatedSearchWords) {
      newKeywords.put(searchWord.getKeyword(), searchWord);
    }

    currentKeywords = newKeywords;
  }

  private List<SearchWord> getSearchWords() {
    // TODO: 从数据库中取出所有的数据
    return null;
  }
}
```

上面实现的问题很明显，newKeywords构建的成本比较高，需要将10万条数据从数据库中读出，然后计算哈希值，构建newKeywords。这个过程显然是比较
耗时。为了提高效率，原型模式就派上用场了。

我们拷贝currentKeywords数据到newKeywords中，然后从数据库中只捞出新增或者有更新的关键词，更新到newKeywords中。而相对于10万条数据来说，
每次新增或者更新的关键词个数是比较少的，所以，这种策略大大提高了数据更新的效率。
```java
public class Demo {
  private HashMap<String, SearchWord> currentKeywords=new HashMap<>();
  private long lastUpdateTime = -1;

  public void refresh() {
    // 原型模式，拷贝已有对象的数据，更新少量差值
    HashMap<String, SearchWord> newKeywords = (HashMap<String, SearchWord>) currentKeywords.clone();

    // 从数据库中取出更新时间>lastUpdateTime的数据，放入到newKeywords中
    List<SearchWord> toBeUpdatedSearchWords = getSearchWords(lastUpdateTime);
    long maxNewUpdatedTime = lastUpdateTime;
    for (SearchWord searchWord : toBeUpdatedSearchWords) {
      if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
        maxNewUpdatedTime = searchWord.getLastUpdateTime();
      }
      if (newKeywords.containsKey(searchWord.getKeyword())) {
        SearchWord oldSearchWord = newKeywords.get(searchWord.getKeyword());
        oldSearchWord.setCount(searchWord.getCount());
        oldSearchWord.setLastUpdateTime(searchWord.getLastUpdateTime());
      } else {
        newKeywords.put(searchWord.getKeyword(), searchWord);
      }
    }

    lastUpdateTime = maxNewUpdatedTime;
    currentKeywords = newKeywords;
  }

  private List<SearchWord> getSearchWords(long lastUpdateTime) {
    // TODO: 从数据库中取出更新时间>lastUpdateTime的数据
    return null;
  }
}
```

上面的代码实际上还有问题，我们通过调用HashMap的clone()浅拷贝方法来实现原型模式。当通过newKeywords更新SearchWord对象的时候，newKeywords
和currentKeywords因为指向相同的一组SearchWord对象，就会导致currentKeywords中指向的SearchWord，有的是老版本的，有的是新版本的，就没法
满足之前的需求：currentKeywords中的数据在任何时刻都是同一个版本的，不存在介于老版本与新版本之间的中间状态。

为了解决上面的问题，需要将拷贝改成深拷贝：
```java

public class Demo {
  private HashMap<String, SearchWord> currentKeywords=new HashMap<>();
  private long lastUpdateTime = -1;

  public void refresh() {
    // Deep copy，下面是循环创建的方式，也可以通过序列化实现
    HashMap<String, SearchWord> newKeywords = new HashMap<>();
    for (HashMap.Entry<String, SearchWord> e : currentKeywords.entrySet()) {
      SearchWord searchWord = e.getValue();
      SearchWord newSearchWord = new SearchWord(
              searchWord.getKeyword(), searchWord.getCount(), searchWord.getLastUpdateTime());
      newKeywords.put(e.getKey(), newSearchWord);
    }

    // 从数据库中取出更新时间>lastUpdateTime的数据，放入到newKeywords中
    List<SearchWord> toBeUpdatedSearchWords = getSearchWords(lastUpdateTime);
    long maxNewUpdatedTime = lastUpdateTime;
    for (SearchWord searchWord : toBeUpdatedSearchWords) {
      if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
        maxNewUpdatedTime = searchWord.getLastUpdateTime();
      }
      if (newKeywords.containsKey(searchWord.getKeyword())) {
        SearchWord oldSearchWord = newKeywords.get(searchWord.getKeyword());
        oldSearchWord.setCount(searchWord.getCount());
        oldSearchWord.setLastUpdateTime(searchWord.getLastUpdateTime());
      } else {
        newKeywords.put(searchWord.getKeyword(), searchWord);
      }
    }

    lastUpdateTime = maxNewUpdatedTime;
    currentKeywords = newKeywords;
  }

  private List<SearchWord> getSearchWords(long lastUpdateTime) {
    // TODO: 从数据库中取出更新时间>lastUpdateTime的数据
    return null;
  }
}
```

不过也可以基于浅拷贝实现版本一致，只需要在更新对象的时候创建一个新的SearchWord对象而不是修改原来的SearchWord对象：
```java

public class Demo {
  private HashMap<String, SearchWord> currentKeywords=new HashMap<>();
  private long lastUpdateTime = -1;

  public void refresh() {
    // Shallow copy
    HashMap<String, SearchWord> newKeywords = (HashMap<String, SearchWord>) currentKeywords.clone();

    // 从数据库中取出更新时间>lastUpdateTime的数据，放入到newKeywords中
    List<SearchWord> toBeUpdatedSearchWords = getSearchWords(lastUpdateTime);
    long maxNewUpdatedTime = lastUpdateTime;
    for (SearchWord searchWord : toBeUpdatedSearchWords) {
      if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
        maxNewUpdatedTime = searchWord.getLastUpdateTime();
      }
        
      // 直接替换旧的SearchWord对象
      newKeywords.put(searchWord.getKeyword(), searchWord);
    }

    lastUpdateTime = maxNewUpdatedTime;
    currentKeywords = newKeywords;
  }

  private List<SearchWord> getSearchWords(long lastUpdateTime) {
    // TODO: 从数据库中取出更新时间>lastUpdateTime的数据
    return null;
  }
}
```