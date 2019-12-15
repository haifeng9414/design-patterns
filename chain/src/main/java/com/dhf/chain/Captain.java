package com.dhf.chain;

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
            printHandleMessage(request);
            request.markRequest();
        } else {
            super.handleRequest(request);
        }
    }
}
