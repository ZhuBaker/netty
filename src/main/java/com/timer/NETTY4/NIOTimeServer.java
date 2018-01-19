package com.timer.NETTY4;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-19
 * Time: 17:04
 */
public class NIOTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        NIOMultiplexerTimeServer timeServer = new NIOMultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
