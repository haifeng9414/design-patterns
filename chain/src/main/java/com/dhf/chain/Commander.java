package com.dhf.chain;

/**
 * 指挥官
 */
public class Commander {
    private RequestHandler chain;

    public Commander() {
        createChain();
    }

    private void createChain() {
        chain = new Captain(new Gunny(new Gunner(null)));
    }

    public void handleRequest(Request request) {
        chain.handleRequest(request);
    }
}
