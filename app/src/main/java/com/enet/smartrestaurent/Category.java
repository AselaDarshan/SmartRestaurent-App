package com.enet.smartrestaurent;

import com.orm.SugarRecord;

/**
 * Created by asela on 6/1/17.
 */
public class Category extends SugarRecord<Category> {
    int categoryId;
    String name;
    String imageName;
    int parentId;

    public Category(){
        this.categoryId = 0;
        this.name = "";
        this.imageName ="";
        this.parentId = 0;
    }
    public Category(int categoryId, String name,String imageName,int parentId){
        this.categoryId = categoryId;
        this.name = name;
        this.imageName = imageName;
        this.parentId =parentId;
    }
}
