package com.enet.smartrestaurent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ActiveOrdersActivity extends AppCompatActivity {
    private RVAdapter adapter;
    private List<ActiveOrder> activeOrders;
    private ArrayList<ActiveOrder> activeOrderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Active Orders</font>"));



        activeOrders = ActiveOrder.findWithQuery(ActiveOrder.class, "SELECT * from ACTIVE_ORDER where IS_COMPLETED=0");
        activeOrderList = new ArrayList<>();



        for (ActiveOrder order:activeOrders){

            activeOrderList.add(order);

            List<ActiveOrderItem> activeOrderItems = ActiveOrderItem.findWithQuery(ActiveOrderItem.class, "SELECT * from ACTIVE_ORDER_ITEM where TABLE_ID=" +order.tableId);
            order.setItemList(activeOrderItems);
        }


        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(this, activeOrderList);
        rv.setAdapter(adapter);


    }

    public void printBillButtonClick(View v){
        int position = (Integer) (((CardView)v.getParent().getParent()).getTag());
        activeOrderList.remove(position);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(position);
        Log.d("Active_orders","print bill clicked:"+position);
    }
}
