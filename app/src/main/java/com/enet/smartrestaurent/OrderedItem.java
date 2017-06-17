package com.enet.smartrestaurent;

import java.io.Serializable;

/**
 * Created by asela on 6/9/17.
 */
public class OrderedItem implements Serializable {

    int menuId;
    String itemId;
    int category;
    String name;
    double price;
    String imageName;
    int preparedIn;
    int qty;

    int orderId;
    public OrderedItem(int menuId, int category, String name, double price, String imageName, int preparedIn,String itemId) {

        this.menuId = menuId;
        this.category = category;
        this.name = name;
        this.price = price;
        this.imageName = imageName;
        this.preparedIn = preparedIn;
        this.qty = 1;
        this.itemId = itemId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getPreparedIn() {
        return preparedIn;
    }

    public void setPreparedIn(int preparedIn) {
        this.preparedIn = preparedIn;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
    public int addOne(){
        return ++qty;
    }
    public int removeOne(){

        return --qty;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}
