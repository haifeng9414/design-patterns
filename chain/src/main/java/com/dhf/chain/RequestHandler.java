package com.dhf.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理者
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
