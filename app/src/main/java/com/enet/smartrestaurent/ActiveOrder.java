package com.enet.smartrestaurent;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asela on 6/15/17.
 */
public class ActiveOrder extends SugarRecord<ActiveOrder>  {
    String tableId;
    boolean isCompleted;
    @Ignore
    List<ActiveOrderItem> itemList;


    public ActiveOrder(){}
    public ActiveOrder(String tableId) {
        this.tableId = tableId;
        this.isCompleted = false;

    }
    public List<ActiveOrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ActiveOrderItem> itemList) {
        this.itemList = itemList;
    }

}
