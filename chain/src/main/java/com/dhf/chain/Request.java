package com.dhf.chain;

/**
 * 请求
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
