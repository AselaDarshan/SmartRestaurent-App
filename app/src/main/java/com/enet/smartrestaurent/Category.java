package com.enet.smartrestaurent;

import com.orm.SugarRecord;

/**
 * Created by asela on 6/1/17.
 */
public class Category extends SugarRecord<MenuItem> {
    int categoryId;
    String name;

    public Category(){}

    public Category(int categoryId, String name){
        this.categoryId = categoryId;
        this.name = name;
    }
}
