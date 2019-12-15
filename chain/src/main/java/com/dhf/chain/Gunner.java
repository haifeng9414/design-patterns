package com.dhf.chain;

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
