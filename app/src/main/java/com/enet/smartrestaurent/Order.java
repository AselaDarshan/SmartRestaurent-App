package com.enet.smartrestaurent;

import java.util.HashMap;

/**
 * Created by asela on 5/17/17.
 */
public class Order {
//    private static Order instance;

    private HashMap<String,Integer> orderedItems;



    private String tableId;

    public Order(){
        orderedItems = new HashMap<>();
    }
    public int addItem(String item){
        if(orderedItems.containsKey(item)){
            orderedItems.put(item,orderedItems.get(item)+1);
        }
        else
            orderedItems.put(item,1);
        return orderedItems.get(item);
    }

    public int removeItem(String item){
        if(orderedItems.containsKey(item)){
            int newCount = orderedItems.get(item)-1;
            if(newCount == 0){
                orderedItems.remove(item);
            }
            else {
                orderedItems.put(item, newCount);
            }
            return newCount;
        }
        return -1;
    }

    public HashMap<String,Integer> getOrder(){
        return orderedItems;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
