package com.timer.netty.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-13
 * Time: 12:10
 */
public class FileCopy {

    private final static Path copy_from = Paths.get("D:/千图网_年终盛典双十二提前购海报设计_图片编号27880563.zip");
    private final static Path copy_to = Paths.get("D:/abc.zip");
    private static long startTime , elapsedTime;
    private static int bufferSizeKB = 4;
    private static int bufferSize = bufferSizeKB * 1024;

    public static void main(String[] args) throws Exception{
        //transferFrom();
        transferTo();


    }

    private static void transferFrom(){
        try{
            FileChannel fileChannel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
            FileChannel fileChannel_to = FileChannel.open(copy_to,EnumSet.of(StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE));
            startTime = System.nanoTime();
            fileChannel_to.transferFrom(fileChannel_from,0L,(int)fileChannel_from.size());
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elasped Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void transferTo(){
        try{
            FileChannel fileChannel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
            FileChannel fileChannel_to = FileChannel.open(copy_to,EnumSet.of(StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE));
            startTime = System.nanoTime();
            fileChannel_from.transferTo(0L,fileChannel_from.size(),fileChannel_to);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elasped Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void nonDirectBuffer(){
        try{
            FileChannel fileChannel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
            FileChannel fileChannel_to = FileChannel.open(copy_to,EnumSet.of(StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE));
            startTime = System.nanoTime();
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            int bytesCount;
            while((bytesCount = fileChannel_from.read(buffer))> 0) {
                buffer.flip();
                fileChannel_to.write(buffer);
                buffer.clear();
            }
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elasped Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void directBuffer(){
        try{
            FileChannel fileChannel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
            FileChannel fileChannel_to = FileChannel.open(copy_to,EnumSet.of(StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE));
            startTime = System.nanoTime();
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
            int bytesCount;
            while((bytesCount = fileChannel_from.read(buffer))> 0) {
                buffer.flip();
                fileChannel_to.write(buffer);
                buffer.clear();
            }
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elasped Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void mapperedBuffer() throws Exception{
        try{
            FileChannel fileChannel_from = FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ));
            FileChannel fileChannel_to = FileChannel.open(copy_to,EnumSet.of(StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE));
            startTime = System.nanoTime();


        }catch (Exception e){

        }
    }


    public static void ioBufferedStream(){
        File inFileStr = copy_from.toFile();
        File outFileStr = copy_to.toFile();
        try{
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFileStr));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFileStr));
            startTime = System.nanoTime();
            byte[] byteArray = new byte[bufferSize];
            int bytesCount ;
            while((bytesCount = in.read(byteArray)) != -1){
                out.write(byteArray,0,bytesCount);
            }

            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elasped Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void ioUnBufferedStream(){
        File inFileStr = copy_from.toFile();
        File outFileStr = copy_to.toFile();
        try{
            FileInputStream in = new FileInputStream(inFileStr);
            FileOutputStream out = new FileOutputStream(outFileStr);
            startTime = System.nanoTime();
            byte[] byteArray = new byte[bufferSize];
            int bytesCount ;
            while((bytesCount = in.read(byteArray)) != -1){
                out.write(byteArray,0,bytesCount);
            }

            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elasped Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void copyPath2Path(){
        try{
            startTime = System.nanoTime();
            Files.copy(copy_from,copy_to,LinkOption.NOFOLLOW_LINKS );
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void copyInputStream2Path(){
        try{
            startTime = System.nanoTime();
            InputStream in = new FileInputStream(copy_from.toFile());
            Files.copy(in,copy_to);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void copyPath2OutputStream() {
        try{
            OutputStream out = new FileOutputStream(copy_to.toFile());
            startTime = System.nanoTime();
            Files.copy(copy_from,out);
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void randomReadFile(){
        try{
            RandomAccessFile read = new RandomAccessFile("","r");
            RandomAccessFile writer = new RandomAccessFile("","rw");
            startTime = System.nanoTime();
            byte[] b = new byte[200*1024*1024];
            while(read.read(b)!=-1){
                writer.write(b);
            }
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
