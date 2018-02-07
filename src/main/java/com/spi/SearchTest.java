package com.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SearchTest {

    public static void main(String[] args) {
        ServiceLoader<Search> load = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = load.iterator();
        while(iterator.hasNext()){
            Search s = iterator.next();
            s.search(100L);
        }
    }
}
