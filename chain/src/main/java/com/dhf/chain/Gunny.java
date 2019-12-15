package com.dhf.chain;

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
