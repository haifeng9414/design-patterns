# 责任链模式

## 简介
在责任链模式里，每一个对象持有其下家的引用形成一条链。请求在这个链上传递，直到链上的某一个对象决定处理此请求。发出这个请求的客户端并不知道链上的哪一个对象最终处理这个请求，这使得系统可以在不影响客户端的情况下动态地重新组织和分配责任，如指挥官可以发出不同的命令，炮手、枪炮长、舰长分别能够处理不同的命令：
```java
/**
 * 指挥官发出的命令
 */
public class Request {
    private final String description;
    private final RequestType type;
    private boolean isHandled;

    public Request(String description, RequestType type) {
        this.description = description;
        this.type = type;
    }

    public boolean isHandled() {
        return isHandled;
    }

    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    public String getDescription() {
        return description;
    }

    public RequestType getType() {
        return type;
    }

    public void markRequest() {
        setHandled(true);
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public enum RequestType {
        // 射击
        SHOOTING,
        // 航行
        SAILING,
        // 待命
        AWAIT_ORDERS
    }
}

/**
 * 指挥官
 */
public class Commander {
    private RequestHandler chain;

    public Commander() {
        createChain();
    }

    // 指挥官持有责任链，发出命令时交给责任链处理，他不知道最终谁会执行这个命令
    private void createChain() {
        chain = new Captain(new Gunny(new Gunner(null)));
    }

    public void handleRequest(Request request) {
        chain.handleRequest(request);
    }
}

/**
 * 消息处理者，所有能够处理命令的类都继承该抽象类
 */
public abstract class RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    private RequestHandler next;

    public RequestHandler(RequestHandler next) {
        this.next = next;
    }

    public void handleRequest(Request request) {
        if (null != next) {
            next.handleRequest(request);
        }
    }

    @Override
    public abstract String toString();

    protected void printHandleMessage(Request request) {
        LOGGER.info("{}处理消息中，消息内容为：{}", this, request);
    }
}
```

针对RequestHandler的具体实现：
```java
/**
 * 舰长
 */
public class Captain extends RequestHandler {
    public Captain(RequestHandler next) {
        super(next);
    }

    @Override
    public String toString() {
        return "舰长";
    }

    @Override
    public void handleRequest(Request request) {
        if (request.getType() == Request.RequestType.SAILING) {
            // 处理命令
            printHandleMessage(request);
            // 设置命令已被处理，非必须步骤
            request.markRequest();
        } else {
            // 处理不了就交给下一个处理
            super.handleRequest(request);
        }
    }
}

/**
 * 枪炮长
 */
public class Gunny extends RequestHandler {

    public Gunny(RequestHandler next) {
        super(next);
    }

    @Override
    public String toString() {
        return "枪炮长";
    }

    @Override
    public void handleRequest(Request request) {
        if (request.getType() == Request.RequestType.AWAIT_ORDERS) {
            printHandleMessage(request);
            request.markRequest();
        } else {
            super.handleRequest(request);
        }
    }
}

/**
 * 炮手
 */
public class Gunner extends RequestHandler {
    public Gunner(RequestHandler next) {
        super(next);
    }

    @Override
    public String toString() {
        return "炮手";
    }

    @Override
    public void handleRequest(Request request) {
        if (request.getType() == Request.RequestType.SHOOTING) {
            printHandleMessage(request);
            request.markRequest();
        } else {
            super.handleRequest(request);
        }
    }
}
```

使用：
```java
public static void main(String[] args) {
    Commander commander = new Commander();

    commander.handleRequest(new Request("正常航行", Request.RequestType.SAILING));
    commander.handleRequest(new Request("原地待命", Request.RequestType.AWAIT_ORDERS));
    commander.handleRequest(new Request("发射驱逐导弹", Request.RequestType.SHOOTING));
}

/*
输出：
15:30:50.376 [main] INFO com.dhf.chain.RequestHandler - 舰长处理消息中，消息内容为：正常航行
15:30:50.381 [main] INFO com.dhf.chain.RequestHandler - 枪炮长处理消息中，消息内容为：原地待命
15:30:50.381 [main] INFO com.dhf.chain.RequestHandler - 炮手处理消息中，消息内容为：发射驱逐导弹
*/
```