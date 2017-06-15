package com.enet.smartrestaurent;

import com.orm.SugarRecord;

/**
 * Created by asela on 6/15/17.
 */
public class ActiveOrderItem extends SugarRecord<ActiveOrderItem> {
    String itemName;
    String qty;
    String state;
    int tableId;
    int orderId;

    public ActiveOrderItem(String itemName, String qty, String state, int tableId, int orderId) {
        this.itemName = itemName;
        this.qty = qty;
        this.state = state;
        this.tableId = tableId;
        this.orderId = orderId;
    }

    public ActiveOrderItem(){}
}
