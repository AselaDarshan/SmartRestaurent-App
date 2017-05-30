package com.enet.smartrestaurent;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asela on 5/17/17.
 */
public class Menu {

    public void initialize(){
        List<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem("Salad",650);
//        items.add(new MenuItem("Salad",650));
//        items.add(new MenuItem("Salad 2",450));

        item.save();
//        MenuItem.saveInTx(items);
    }
}
