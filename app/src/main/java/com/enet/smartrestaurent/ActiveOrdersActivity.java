package com.enet.smartrestaurent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActiveOrdersActivity extends AppCompatActivity {
    private RVAdapter adapter;
    private List<ActiveOrder> activeOrders;
    private ArrayList<ActiveOrder> activeOrderList;

    private Receiver receiver = new Receiver();
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



        IntentFilter mqttIntentFilter = new IntentFilter(Constants.MQTT_NEW_MESSAGE_ACTION);

        IntentFilter mqttConnectionIntentFilter = new IntentFilter(Constants.MQTT_CONNECTION_STATE_ACTION);
        this.registerReceiver(receiver,mqttIntentFilter);

        this.registerReceiver(receiver, mqttConnectionIntentFilter);

        retrieveOrderList();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            this.unregisterReceiver(receiver);
        }
        catch (java.lang.IllegalArgumentException e){

        }

    }
    public void retrieveOrderList(){
        WebServerCommunicationService.sendGetRequest(this,Constants.API_BASE_URL+Constants.API_ORDER_MENUS,Constants.ORDERS_UPDATE_ACTION);
    }

    public void printBillButtonClick(View v){
        int position = (Integer) (((CardView)v.getParent().getParent()).getTag());
        activeOrderList.remove(position);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(position);
        Log.d("Active_orders","print bill clicked:"+position);
    }

    protected void markItemAsReady(String item){
        String itemName = item.split(" for table ")[0];
        Log.d("Active_orders","item ready: "+itemName);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String response = arg1.getExtras().getString(Constants.RESPONSE_KEY);
            String action = arg1.getAction();
            Log.d("broadcast_received act",arg1.getAction());

           if(action.equals(Constants.MQTT_NEW_MESSAGE_ACTION)){
                if (response != null && !response.contains(Constants.ERROR_RESPONSE)) {
                    Log.d("update Received", "Received to activeOrderActivity: " + response);
                    String topic = response.split("~")[0];
                    if(topic.contains(Constants.ORDER_COMPLETED_TOPIC)) {
                        markItemAsReady(response.split("~")[1]);
                    }

                } else {

                    Log.d("communication", "Received to activeOrderActivity: error ");
                    Toast toast = Toast.makeText(getApplicationContext(), "Connection failed!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else if(action.equals(Constants.MQTT_CONNECTION_STATE_ACTION)){
                if (response != null && response.equals(Constants.MQTT_CONNECTION_SUCCESS)) {
                    GlobalState.setConnected(true);
                }
                else if (response != null && response.equals(Constants.MQTT_CONNECTION_FAILED)){
                    GlobalState.setConnected(false);
                    CheckUpdatesService.stopActionUpdateCheck(getBaseContext());
                    CheckUpdatesService.startActionUpdateCheck(getBaseContext());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("mqtt", "Connection failed! reconnecting.. ");
//                    Toast.makeText(getApplicationContext(), "Connection failed! Reconnecting..", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}
