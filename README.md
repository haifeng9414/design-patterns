# 设计模式学习

## 理论
- SOLID原则
  - 单一职责原则（SRP，Single Responsibility Principle）：一个类或者模块只负责完成一个职责（或者功能）。当发生下面的情况时，可能就需要考虑一个类是否满足单一责任原则。当然这些只是主观上的判断，需要结合实际项目分析才能确定，并且项目本身是会变化的，所以这些情况也是不断变化的，实际实现项目时需要持续重构。
     1. 类中的代码行数、函数或者属性过多
     2. 类依赖的其他类过多
     3. 私有方法过多
     4. 比较难给类起一个合适的名字
     5. 类中大量的方法都是集中操作类中的某几个属性
  - 开闭原则（OCP，Open Closed Principle）：对扩展开放、对修改关闭。通俗点说就是添加一个新的功能应该时，在已有代码基础上扩展代码（新增模块、类、方法等），而非修改已有代码（修改模块、类、方法等），即对扩展开放是为了应对需求的变化，对修改关闭是为了保证已有代码的稳定性。满足开闭原则的代码能够提供很好的扩展性。实际写代码时，多花点时间思考未来可能有哪些需求变更、如何设计代码结构，事先留好扩展点，以便在未来需求变更的时候，不需要改动代码整体结构、做到最小代码改动的情况下，新的代码能够很灵活地插入到扩展点上。在识别出代码可变部分和不可变部分之后，将可变部分封装起来，隔离变化，提供抽象化的不可变接口，给上层系统使用。当具体的实现发生变化的时候，只需要基于相同的抽象接口，扩展一个新的实现，替换掉老的实现即可，上游系统的代码几乎不需要修改。
  - 里式替换原则（LSP，Liskov Substitution Principle）：子类对象能够替换程序中父类对象出现的任何地方，并且保证原来程序的逻辑行为不变及正确性不被破坏。里式替换原则和多态的区别是，多态只是面向对象编程的一大特性，对子类的实现没有特殊要求，而里式替换原则要求子类替换父类时不能破坏程序原本的正确性，如某处使用父类的地方改用子类后，子类抛出异常就破坏了程序的正确性，里式替换原则另外一层含义是：按照协议来设计，即子类在设计的时候，要遵守父类的行为约定。`  这里的行为约定包括：函数声明要实现的功能；对输入、输出、异常的约定；甚至包括注释中所罗列的任何特殊说明。
  - 接口隔离原则（ISP，Interface Segregation Principle）：客户端不应该被强迫依赖它不需要的接口。对于接口，可以有下面三种理解：
     1. 一组方法集合，如某个Service的所有方法。如果这个Service被多个系统使用，但是其中某些方法只被部分系统使用，则这部分方法应该隔离出来。如UserService方法有注册、登录和获取信息等方法，这些方法各个模块都会使用，但是如果此时增加一个删除用户方法，由于该方法比较敏感，只允许被后台系统使用，此时应该将删除用户方法隔离到另一个接口，UserServiceImpl同时实现UserService和删除用户接口，普通模块注入UserService，后台系统注入UserService和删除用户接口，以此达到限制普通模块删除用户的能力。
     2. 单个API接口或函数。如果某些客户端只需要函数的部分功能，则应该将函数拆分成粒度更细的多个函数，让调用者只依赖它需要的那个细粒度函数。如count方法返回Statistics对象，该对象包含了max、min、sum、average等统计结果，如果客户端需要所有结果，则可以直接返回Statistics对象，但是如果客户端只是部分需要这些结果，则可以将max、min、sum、average等统计结果分别放到单独的函数中实现。
     3. Java中的接口。对于Java接口，设计要尽量单一，不要让接口的实现类和调用者，依赖不需要的接口函数。如项目中用到了三个外部系统：Redis、MySQL、Kafka。每个系统都对应一系列配置信息，比如地址、端口、访问超时时间等。为了在内存中存储这些配置信息，供项目中的其他模块来使用，分别设计实现了三个 Configuration类：RedisConfig、MysqlConfig、KafkaConfig。现在有一个新的功能需求，希望支持Redis和Kafka配置信息的热更新。所谓热更新就是如果在配置中心中更改了配置信息，则希望在不用重启系统的情况下，能将最新的配置信息加载到内存中（也就是RedisConfig、KafkaConfig类中）。但是，因为某些原因，不希望对MySQL的配置信息进行热更新。为了实现这样一个功能需求，定义一个Updater接口，指定了update()方法，并设计实现了一个ScheduledUpdater类，以固定时间频率来调用RedisConfig、KafkaConfig的update()方法更新配置信息。之后又有了一个新的监控功能需求，希望能有一种方便的方式查看配置信息。为了实现这个需要，在项目中开发一个内嵌的SimpleHttpServer，输出项目的配置信息到一个固定的HTTP地址。出于某些原因，我们只想暴露MySQL和Redis的配置信息，不想暴露Kafka的配置信息，此时同样定义一个Viewer接口，指定了输出信息的方法。现在设计了两个功能非常单一的接口：Updater和Viewer。ScheduledUpdater只依赖Updater这个跟热更新相关的接口，不需要被强迫去依赖不需要的Viewer接口，满足接口隔离原则。同理，SimpleHttpServer只依赖跟查看信息相关的Viewer接口，不依赖不需要的Updater接口，也满足接口隔离原则。这样做的好处是，使得代码更加灵活、易扩展、易复用。比如，现在又有一个新的需求，开发一个Metrics性能统计模块，并且希望将Metrics也通过 SimpleHttpServer显示在网页上，以方便查看。这个时候，尽管Metrics跟RedisConfig等没有任何关系，但仍然可以让Metrics类实现非常通用的Viewer接口，复用SimpleHttpServer的代码实现。另外接口隔离原则也避免了代码做一些无用功。比如如果不满足接口隔离原则，把上面提到的所有方法放到一个公共接口，RedisConfig、MysqlConfig、KafkaConfig都实现了这个接口，这会导致不需要实现热更新的MysqlConfig也需要实现热更新功能，或者写一个空方法，如果使用接口隔离原则就不会有这个问题。
  - 依赖反转原则（DIP，Dependency Inversion Principle）：高层模块不要依赖低层模块。高层模块和低层模块应该通过抽象来互相依赖。除此之外，抽象不要依赖具体实现细节，具体实现细节依赖抽象。以Tomcat这个Servlet容器为例，Tomcat是运行Java Web应用程序的容器。编写的Web应用程序代码只需要部署在Tomcat容器下，便可以被Tomcat容器调用执行。按照之前的划分原则，Tomcat就是高层模块，编写的Web应用程序代码就是低层模块。Tomcat和应用程序代码之间并没有直接的依赖关系，两者都依赖同一个“抽象”，也就是Sevlet规范。Servlet规范不依赖具体的Tomcat容器和应用程序的实现细节，而Tomcat容器和应用程序依赖Servlet规范。
  
- KISS原则（Keep It Simple and Stupid）：尽量保持简单。KISS原则强调的是提高代码可读性。想要写出满足KISS原则的代码，可以遵循：
  - 不要使用同事可能不懂的技术来实现代码
  - 不要重复造轮子，要善于使用已经有的工具类库
  - 不要过度优化
- YAGNI原则（You Aren't Gonna Need It）：不要去设计当前用不到的功能，不要去编写当前用不到的代码。
- DRY原则（Don’t Repeat Yourself）：不要写重复的代码。重复并不是指代码重复，而是指，一个功能点，不要实现两遍，如两个不同的工具类分别实现了不同的IP校验方法，即使用到了不同的校验方法，但是他们做的是同一件事，违背DRY原则；不要做重复的逻辑，如参数校验，Service层如果做了校验了，Repository层就不要再做校验了。
- 迪米特法则（LOD，Law of Demeter）：最小知识原则。不该有直接依赖关系的类之间，不要有依赖；有依赖关系的类之间，尽量只依赖必要的接口（也就是定义中的“有限知识”）。例子：
  - 不该有直接依赖关系的类之间，不要有依赖。这个例子实现了简化版的搜索引擎爬取网页的功能。代码中包含三个主要的类。其中，NetworkTransporter类负责底层网络通信，根据请求获取数据；HtmlDownloader类用来通过URL获取网页；Document表示网页文档，后续的网页内容抽取、分词、索引都是以此为处理对象。
    ```
    
    public class NetworkTransporter {
        // 省略属性和其他方法...
        public Byte[] send(HtmlRequest htmlRequest) {
          //...
        }
    }

    public class HtmlDownloader {
      private NetworkTransporter transporter;//通过构造函数或IOC注入

      public Html downloadHtml(String url) {
        Byte[] rawHtml = transporter.send(new HtmlRequest(url));
        return new Html(rawHtml);
      }
    }

    public class Document {
      private Html html;
      private String url;

      public Document(String url) {
        this.url = url;
        HtmlDownloader downloader = new HtmlDownloader();
        this.html = downloader.downloadHtml(url);
      }
      //...
    }
    ``` 
    上面的实现存在的问题是，首先NetworkTransporter类。作为一个底层网络通信类，我们希望它的功能尽可能通用，而不只是服务于下载HTML，所以，我们不应该直接依赖太具体的发送对象HtmlRequest。从这一点上讲，NetworkTransporter类的设计违背迪米特法则，依赖了不该有直接依赖关系的HtmlRequest类。如何进行重构，让 NetworkTransporter类满足迪米特法则呢？这里有个形象的比喻。假如你现在要去商店买东西，你肯定不会直接把钱包给收银员，让收银员自己从里面拿钱，而是你从钱包里把钱拿出来交给收银员。这里的HtmlRequest对象就相当于钱包，HtmlRequest里的address和content对象就相当于钱。我们应该把address和content交给NetworkTransporter，而非是直接把HtmlRequest交给NetworkTransporter。根据这个思路，NetworkTransporter重构之后的代码如下所示：
    ```
    public class NetworkTransporter {
        // 省略属性和其他方法...
        public Byte[] send(String address, Byte[] data) {
          //...
        }
    }
    ```
    再来看Document类。这个类的问题比较多，主要有三点。第一，构造函数中的downloader.downloadHtml()逻辑复杂，耗时长，不应该放到构造函数中，会影响代码的可测试性。第二，HtmlDownloader对象在构造函数中通过new来创建，违反了基于接口而非实现编程的设计思想，也会影响到代码的可测试性。第三，从业务含义上来讲，Document网页文档没必要依赖HtmlDownloader类，违背了迪米特法则。针对这些问题，修改后的代码是：
    ```
    public class Document {
      private Html html;
      private String url;

      public Document(String url, Html html) {
        this.html = html;
        this.url = url;
      }
      //...
    }

    // 通过一个工厂方法来创建Document
    public class DocumentFactory {
      private HtmlDownloader downloader;

      public DocumentFactory(HtmlDownloader downloader) {
        this.downloader = downloader;
      }

      public Document createDocument(String url) {
        Html html = downloader.downloadHtml(url);
        return new Document(url, html);
      }
    }
    ```
  - 有依赖关系的类之间，尽量只依赖必要的接口。下面这个例子非常简单，Serialization类负责对象的序列化和反序列化。
    ```
    public class Serialization {
      public String serialize(Object object) {
        String serializedResult = ...;
        //...
        return serializedResult;
      }

      public Object deserialize(String str) {
        Object deserializedResult = ...;
        //...
        return deserializedResult;
      }
    }
    ``` 
    假设项目中，有些类只用到了序列化操作，而另一些类只用到反序列化操作。那基于迪米特法则后半部分“有依赖关系的类之间，尽量只依赖必要的接口”，只用到序列化操作的那部分类不应该依赖反序列化接口。同理，只用到反序列化操作的那部分类不应该依赖序列化接口。根据这个思路，应该将Serialization类拆分为两个更小粒度的类，一个只负责序列化（Serializer类），一个只负责反序列化（Deserializer类）。拆分之后，使用序列化操作的类只需要依赖Serializer类，使用反序列化操作的类只需要依赖Deserializer类。拆分之后的代码如下所示：
    ```
    public class Serializer {
      public String serialize(Object object) {
        String serializedResult = ...;
        ...
        return serializedResult;
      }
    }

    public class Deserializer {
      public Object deserialize(String str) {
        Object deserializedResult = ...;
        ...
        return deserializedResult;
      }
    }
    ```
    尽管拆分之后的代码更能满足迪米特法则，但却违背了高内聚的设计思想。高内聚要求相近的功能要放到同一个类中，这样可以方便功能修改的时候，修改的地方不至于过于分散。对于刚刚这个例子来说，如果我们修改了序列化的实现方式，比如从JSON换成了XML，那反序列化的实现逻辑也需要一并修改。在未拆分的情况下，只需要修改一个类即可。在拆分之后，需要修改两个类。显然，这种设计思路的代码改动范围变大了。针对这个问题，通过引入两个接口就能解决，具体的代码如下所示：
    ```
    public interface Serializable {
      String serialize(Object object);
    }

    public interface Deserializable {
      Object deserialize(String text);
    }

    public class Serialization implements Serializable, Deserializable {
      @Override
      public String serialize(Object object) {
        String serializedResult = ...;
        ...
        return serializedResult;
      }

      @Override
      public Object deserialize(String str) {
        Object deserializedResult = ...;
        ...
        return deserializedResult;
      }
    }

    public class DemoClass_1 {
      private Serializable serializer;

      public Demo(Serializable serializer) {
        this.serializer = serializer;
      }
      //...
    }

    public class DemoClass_2 {
      private Deserializable deserializer;

      public Demo(Deserializable deserializer) {
        this.deserializer = deserializer;
      }
      //...
    }
    ```
    整个类只包含序列化和反序列化两个操作，只用到序列化操作的使用者，即便能够感知到仅有的一个反序列化函数，问题也不大。那为了满足迪米特法则，将一个非常简单的类，拆分出两个接口，是否有点过度设计的意思呢？这需要结合项目的实际情况分析。对于刚刚这个Serialization类来说，只包含两个操作，确实没有太大必要拆分成两个接口。但是，如果我们对Serialization类添加更多的功能，实现更多更好用的序列化、反序列化函数，重新考虑一下这个问题。修改之后的具体的代码如下：
    ```
    public class Serializer { // 参看JSON的接口定义
      public String serialize(Object object) { //... }
      public String serializeMap(Map map) { //... }
      public String serializeList(List list) { //... }

      public Object deserialize(String objectString) { //... }
      public Map deserializeMap(String mapString) { //... }
      public List deserializeList(String listString) { //... }
    }
    ```
    在这种场景下，将Serialization类拆成两个接口更好些。因为基于之前的应用场景来说，大部分代码只需要用到序列化的功能。对于这部分使用者，没必要了解反序列化的“知识”，而修改之后的Serialization类，反序列化的“知识”，从一个函数变成了三个。一旦任意一个反序列化操作有代码改动，需要检查、测试所有依赖Serialization类的代码是否还能正常工作。为了减少耦合和测试工作量，应该按照迪米特法则，将反序列化和序列化的功能隔离开来。
- 高内聚、低耦合：高内聚，就是指相近的功能应该放到同一个类中，不相近的功能不要放到同一个类中。相近的功能往往会被同时修改，放到同一个类中，修改会比较集中，代码容易维护。低耦合，就是指在代码中，类与类之间的依赖关系简单清晰。即使两个类有依赖关系，一个类的代码改动不会或者很少导致依赖类的代码改动。

## 重构
- 如何发现代码质量问题：
  - 常规思路：
    - 目录设置是否合理、模块划分是否清晰、代码结构是否满足“高内聚、松耦合“？
    - 是否遵循经典的设计原则和设计思想（SOLID、DRY、KISS、YAGNI、LOD 等）？
    - 设计模式是否应用得当？是否有过度设计？
    - 代码是否容易扩展？如果要添加新功能，是否容易实现？
    - 代码是否可以复用？是否可以复用已有的项目代码或类库？是否有重复造轮子？
    - 代码是否容易测试？单元测试是否全面覆盖了各种正常和异常的情况？
    - 代码是否易读？是否符合编码规范（比如命名和注释是否恰当、代码风格是否一致等）？
  - 业务角度：
    - 代码是否实现了预期的业务需求？
    - 逻辑是否正确？是否处理了各种异常情况？
    - 日志打印是否得当？是否方便 debug 排查问题？
    - 接口是否易用？是否支持幂等、事务等？
    - 代码是否存在并发问题？是否线程安全？
    - 性能是否有优化空间，比如，SQL、算法是否可以优化？
    - 是否有安全漏洞？比如输入输出校验是否全面？

## 例子
- [极客时间《设计模式之美》 - 39讲](https://time.geekbang.org/column/article/193221)