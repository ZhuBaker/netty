package com.timer.NETTY4;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-19
 * Time: 16:39
 */
public class BIOTimeServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                // 采用默认值
            }
        }
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            while(true) {
                socket = server.accept();
                new Thread().start();
            }

        }catch (Exception e){

        }finally {
            if(server != null){
                System.out.println("The time server close ");
                server.close();
                server = null;
            }
        }

    }
}
