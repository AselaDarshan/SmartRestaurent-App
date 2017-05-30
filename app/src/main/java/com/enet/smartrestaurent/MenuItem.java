package com.enet.smartrestaurent;

import com.orm.SugarRecord;

/**
 * Created by asela on 5/17/17.
 */
public class MenuItem extends SugarRecord<MenuItem> {
    String name;
    double price;

    public MenuItem()
    {
        this.name = "name";
        this.price = 0.0;
    }

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
