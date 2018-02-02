package com.phei.netty;

import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-30
 * Time: 14:45
 */
public class HashMapTest {

    public static void main(String[] args) {
        Map<String,String> map = new HashMap();
        map.put("abc","aa");
        map.put("abc","bb");
        String a = map.get("abc");
        String b = map.get("abc");
        System.out.println(a + " : " + b);

    }
}
