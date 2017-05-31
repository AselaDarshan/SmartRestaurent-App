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

    public MenuItem()
    {
        this.category = 0;
        this.id = 0;
        this.name = "name";
        this.price = 0.0;
    }

    public MenuItem(int id,int category,String name, double price) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.category = category;
    }
}
