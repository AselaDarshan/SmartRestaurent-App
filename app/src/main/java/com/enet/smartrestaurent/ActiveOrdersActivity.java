package com.enet.smartrestaurent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActiveOrdersActivity extends AppCompatActivity {
    private RVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Active Orders</font>"));


        Iterator<ActiveOrder> activeOrders = ActiveOrder.findAll(ActiveOrder.class);
        ArrayList<ActiveOrder> activeOrderList = new ArrayList<>();
        while (activeOrders.hasNext()){
            activeOrderList.add(activeOrders.next());
        }

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(this, activeOrderList);
        rv.setAdapter(adapter);


        // Setting RecyclerView
        rv.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        rv.setLayoutManager(llm);
// nuggetsList is an ArrayList of Custom Objects, in this case  Nugget.class
        adapter = new RVAdapter(this, activeOrderList);
     //   coursesRecyclerView.setAdapter(adapter);
    }
}
