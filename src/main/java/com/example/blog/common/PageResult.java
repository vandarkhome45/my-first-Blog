package com.example.blog.common;

import java.util.List;

public class PageResult<T> {

    private List<T> list;
    private long total;

    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    // getter setter
}