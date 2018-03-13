package io.nio.concurrent;

import io.netty.util.internal.ConcurrentSet;

import java.nio.channels.SelectionKey;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-13
 * Time: 17:55
 */
public class SelectionKeyHolder {

    private static ConcurrentSet<SelectionKey> keySet = new ConcurrentSet<>();

    public static void put(SelectionKey key){
        keySet.add(key);
    }

    public static boolean isContainKey(SelectionKey key){
        return keySet.contains(key);
    }

    public static void putIfAbsent(SelectionKey key){
        if(keySet.contains(key)){
            return;
        }
        put(key);
    }

    public static void remove(SelectionKey key){
        keySet.remove(key);
    }



}
