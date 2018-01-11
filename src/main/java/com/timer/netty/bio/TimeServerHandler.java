package com.timer.netty.bio;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-11
 * Time: 15:48
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime = null;
            String body = null;

            while(true){
                body = in.readLine();
                if(StringUtils.isEmpty(body)){
                    break;
                }
                System.out.println("The time server receive order : " + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date().toString() :"BAD ORDER";
                out.println(currentTime);
                out.flush();
            }
            System.out.println("ENDING");

        }catch (Exception e){
            e.printStackTrace();
            if(in != null){
                try {
                    in.close();
                }catch (Exception e2){

                }
                if(out != null){
                    out.close();
                    out = null;
                }
                if(this.socket != null){
                    try{
                        this.socket.close();
                    }catch (Exception e3){

                    }
                    this.socket = null;
                }
            }
        }
    }
}
