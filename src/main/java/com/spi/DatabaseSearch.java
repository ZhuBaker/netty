package com.spi;

import java.util.List;

public class DatabaseSearch implements Search {

    @Override
    public List<Long> search(Long input) {
        System.out.println("now use database search. keyword:" + input);
        return null;
    }
}
