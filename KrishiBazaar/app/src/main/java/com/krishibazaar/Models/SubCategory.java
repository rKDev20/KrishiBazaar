package com.krishibazaar.Models;

public class SubCategory implements CategoryInterface {
    int id;
    String name;

    public SubCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }
}
