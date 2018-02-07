package com.spi;

import java.util.List;

public class FileSearch implements Search {

    @Override
    public List<Long> search(Long input) {
        System.out.println("now use file system search. keyword:" + input);
        return null;
    }
}
