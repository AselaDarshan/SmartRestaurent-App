package com.enet.smartrestaurent;

import com.orm.SugarRecord;

/**
 * Created by asela on 6/1/17.
 */
public class Category extends SugarRecord<MenuItem> {
    int categoryId;
    String name;
    String imageName;

    public Category(){}

    public Category(int categoryId, String name,String imageName){
        this.categoryId = categoryId;
        this.name = name;
        this.imageName = imageName;

    }
}
