package com.timer.netty.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-11
 * Time: 16:02
 */
public class TimeClient {

    public static void main(String[] args) {

        int port = 8080;
        if(args!= null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch (Exception e){

            }
        }
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try{
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.print("QUERY TIME ORDER\n");
            out.flush();
            System.out.println("Send order 2 server succeed.");
            String resp  = in.readLine();
            System.out.println("Now is" + resp);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                    out = null;
                }
                if(in != null){
                    in.close();
                    in = null;
                }
                if(socket != null){
                    socket.close();
                    socket = null;
                }

            }catch (Exception e){

            }
        }




    }
}
