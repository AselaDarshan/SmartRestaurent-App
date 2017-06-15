package com.enet.smartrestaurent;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by asela on 6/15/17.
 */
public class ActiveOrder extends SugarRecord<ActiveOrder>  {
    String tableId;

    public ActiveOrder(){}
    public ActiveOrder(String tableId) {
        this.tableId = tableId;


    }


}
