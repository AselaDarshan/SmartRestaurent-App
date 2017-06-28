package com.enet.smartrestaurent;

import com.orm.SugarRecord;

/**
 * Created by asela on 5/17/17.
 */
public class MenuItem extends SugarRecord<MenuItem> {

    int id;
    int category;
    String name;
    double price;
    String imageName;
    String options;//"option:value,option:value.."
    int prepearedIn;

    public MenuItem()
    {
        this.category = 0;
        this.id = 0;
        this.name = "name";
        this.price = 0.0;
        this.imageName ="";
        this.prepearedIn = 0;
    }

    public MenuItem(int id,int category,String name, double price,String imageName,int prepearedIn) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.category = category;
        this.imageName = imageName;
        this.prepearedIn = prepearedIn;
    }
}
