package com.dhf;

import com.dhf.chain.Commander;
import com.dhf.chain.Request;

public class Application {
    public static void main(String[] args) {
        Commander commander = new Commander();

        commander.handleRequest(new Request("正常航行", Request.RequestType.SAILING));
        commander.handleRequest(new Request("原地待命", Request.RequestType.AWAIT_ORDERS));
        commander.handleRequest(new Request("发射驱逐导弹", Request.RequestType.SHOOTING));
    }
}
