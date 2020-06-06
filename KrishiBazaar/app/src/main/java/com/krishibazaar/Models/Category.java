package com.krishibazaar.Models;


import java.util.ArrayList;
import java.util.List;

public class Category implements CategoryInterface {
    int id;
    String name;
    List<SubCategory> subCategories;

    public Category(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void addSubcategory(int id, String name) {
        if (subCategories == null)
            subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(id, name));
    }

    public List<SubCategory> getSubcategoryList() {
        return subCategories;
    }

    public boolean hasSubcategory() {
        return subCategories != null;
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
