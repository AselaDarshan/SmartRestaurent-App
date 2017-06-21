package com.enet.smartrestaurent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ActiveOrdersActivity extends AppCompatActivity {
    private RVAdapter adapter;
    private List<ActiveOrder> activeOrders;
    private ArrayList<ActiveOrder> activeOrderList;

    private Receiver receiver = new Receiver();
    private int printOrderId = -1;

    protected Button printButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Active Orders</font>"));



//        activeOrders = ActiveOrder.findWithQuery(ActiveOrder.class, "SELECT * from ACTIVE_ORDER where IS_COMPLETED=0");
//        activeOrderList = new ArrayList<>();
//
//
//
//        for (ActiveOrder order:activeOrders){
//
//            activeOrderList.add(order);
//
//            List<ActiveOrderItem> activeOrderItems = ActiveOrderItem.findWithQuery(ActiveOrderItem.class, "SELECT * from ACTIVE_ORDER_ITEM where TABLE_ID=" +order.tableId);
//            order.setItemList(activeOrderItems);
//        }




//        adapter = new RVAdapter(this, activeOrderList);
//        rv.setAdapter(adapter);
        retrieveOrderList();


        IntentFilter mqttIntentFilter = new IntentFilter(Constants.MQTT_NEW_MESSAGE_ACTION);

        IntentFilter mqttConnectionIntentFilter = new IntentFilter(Constants.MQTT_CONNECTION_STATE_ACTION);
        IntentFilter orderUpdateIntentFilter = new IntentFilter(Constants.ORDERS_UPDATE_ACTION);
        this.registerReceiver(receiver,mqttIntentFilter);
        this.registerReceiver(receiver,orderUpdateIntentFilter);
        this.registerReceiver(receiver, mqttConnectionIntentFilter);
        IntentFilter publishFilter = new IntentFilter(Constants.MQTT_PUBLISH_STATE_ACTION);

        this.registerReceiver(receiver, publishFilter);
        printOrderId = -1;


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
        WebServerCommunicationService.sendGetRequest(this,Constants.API_BASE_URL+Constants.API_ORDER_MENUS+"?filter[]=comment,sw,"+GlobalState.getCurrrentUserId()+".&filter[]=option_values,neq,"+Constants.ITEM_STATE_COMPLETED+"&satisfy=all",Constants.ORDERS_UPDATE_ACTION);
    }

    public void printBillButtonClick(View v){
        if(printOrderId == -1) {
            printButton = (Button)v;
            v.setEnabled(false);
            int position = (Integer) (((CardView) v.getParent().getParent()).getTag());
            ActiveOrder order = activeOrderList.get(position);
            JSONObject dataToSendToPrinter = new JSONObject();
            printOrderId = position;
            try {
//            dataToSendToPrinter.put("TABLE",tableIdText.getText());
            dataToSendToPrinter.put("ORDER_ID",order.getItemList().get(0).orderId);
            dataToSendToPrinter.put("WAITER",GlobalState.getCurrentUsername());

//            dataToSendToPrinter.put("WAITER",GlobalState.getCurrentUsername());
                for (ActiveOrderItem item : order.getItemList()) {
                    if(item.state.equals(Constants.ITEM_STATE_PREPARED)) {
                        JSONObject menuItem = new JSONObject();
                        menuItem.put(Constants.ITEM_QTY_KEY, item.qty);
                        menuItem.put(Constants.ITEM_NAME_KEY, item.itemName);
                        menuItem.put(Constants.ITEM_PRICE_KEY, item.price);
                        menuItem.put(Constants.ITEM_ID_KEY, item.itemId);
                        dataToSendToPrinter.put(item.itemName, menuItem);
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Can't print the bill until all item has been prepared", Toast.LENGTH_SHORT);
                        toast.show();
                        printOrderId = -1;
                        printButton.setEnabled(true);
                        return;
                    }

                }
                MQTTClient mqttClient = new MQTTClient();
                Log.d("avtive_order", "sending to printer");
                mqttClient.initializeMQTTClient(this.getBaseContext(), "tcp://iot.eclipse.org:1883", "app:waiter:printer" + GlobalState.getCurrentUsername(), false, false, null, null);
                mqttClient.publish("print_bill", 2, dataToSendToPrinter.toString().getBytes());
            } catch (JSONException e) {
                printOrderId = -1;
                printButton.setEnabled(true);
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

            } catch (MqttException e) {
                printOrderId = -1;
                printButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Can't print the bill due to connection error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Throwable throwable) {
                printOrderId = -1;
                printButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                throwable.printStackTrace();
            }

//        activeOrderList.remove(position);
//        adapter.notifyDataSetChanged();
//        adapter.notifyItemRemoved(position);
            Log.d("Active_orders", "print bill clicked:" + position);
//        activeOrderList.get(0).itemList.get(0).state="testt";
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Can't print the bill now! Bill print is in process", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    protected void printSuccess(){
        ActiveOrder order = activeOrderList.get(printOrderId);
        List<ActiveOrderItem> itemList = order.getItemList();
//        for(ActiveOrderItem item:itemList)
//            UpdateBackendIntentService.startSyncronizeRequest(getBaseContext(),item.itemId,Constants.ITEM_STATE_COMPLETED);
        activeOrderList.remove(printOrderId);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(printOrderId);
        Log.d("Active_orders", "bill sent to print" + printOrderId);
        printOrderId = -1;

    }
    protected void markItemAsReady(String item,String itemId){
        String itemName = item.split(" for table ")[0];

        Log.d("Active_orders","item ready: "+itemName+" "+itemId);
        try {
            for (ActiveOrder order : activeOrderList) {
                order.changeState(itemId, Constants.ITEM_STATE_PREPARED);

            }

        }catch (java.lang.NullPointerException ex){
            Log.e("Error_ActiveOrders",ex.toString());
        }

        recreate();

    }

    public void updateOrderList(JSONObject ordersObjectArray) {
        JSONArray orderArray = null;
        HashMap<Integer,ActiveOrder> activeOrderMap = new HashMap<>();
        try {
            orderArray = ordersObjectArray.getJSONObject(Constants.API_ORDER_MENUS).getJSONArray(Constants.RECORDS_KEY);
            List<MenuItem> items = new ArrayList<>();

            for (int i = 0; i < orderArray.length(); i++) {
                JSONArray item = orderArray.getJSONArray(i);
                String itemData = item.getString(8);
                int tableId = Integer.parseInt(itemData.split("\\.")[1]);

                ActiveOrderItem activeOrderItem = new ActiveOrderItem(item.getString(3),String.valueOf(item.getInt(4)),item.getString(7),tableId,item.getInt(1),itemData,item.getDouble(5));

                if(activeOrderMap.containsKey(tableId)){

                    activeOrderMap.get(tableId).itemList.add(activeOrderItem);
                }
                else{
                    ActiveOrder activeOrder =null;
                    if(tableId>99){
                        activeOrder = new ActiveOrder("Room "+itemData.split("\\.")[1]);
                    }
                    else {
                        activeOrder = new ActiveOrder("Table "+itemData.split("\\.")[1]);
                    }
                    activeOrder.itemList.add(activeOrderItem);
                    activeOrderMap.put(tableId,activeOrder);
                }

            }
            Set<Integer> keys = activeOrderMap.keySet();
            activeOrderList = new ArrayList<>();
            for(int key:keys){
                activeOrderList.add(activeOrderMap.get(key));
                Log.d("active_order_activity",activeOrderMap.get(key).tableId);
            }
            //show in list
            RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
            rv.setLayoutManager(llm);
            adapter = new RVAdapter(this, activeOrderList);
            rv.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

                        showMesage(response.split("~")[1].split("`")[0]);

                        String itemId = response.split("~")[1].split("`")[1];
                        ActiveOrder.executeQuery("UPDATE ACTIVE_ORDER_ITEM SET STATE = '"+Constants.ITEM_STATE_PREPARED+"' WHERE ITEM_ID='"+itemId+"'");
                        markItemAsReady(response.split("~")[1].split("`")[0],response.split("~")[1].split("`")[1]);
//                        UpdateBackendIntentService.startSyncronizeRequest(getBaseContext(),itemId,Constants.ITEM_STATE_PREPARED);
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
            else if(action.equals(Constants.ORDERS_UPDATE_ACTION)){
               try {
                   JSONObject jObject = new JSONObject(response);
                   updateOrderList(jObject);


               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }else if(arg1.getAction().equals(Constants.MQTT_PUBLISH_STATE_ACTION)) {
//                unregisterReceiver(receiver);
              //  String response = arg1.getExtras().getString(Constants.RESPONSE_KEY);
                Log.d("mqtt", "Received to activeOrderActivity: " + response);
                if (response != null && response.equals(Constants.MQTT_DELIVER_SUCCESS)) {
                    Log.d("mqtt", "Received to activeOrderActivity: error ");
                    Toast toast = Toast.makeText(getApplicationContext(), "Bill is printing..", Toast.LENGTH_SHORT);
                    toast.show();
                    printSuccess();
//                    orderSucceed();
                }
                else if(response != null && response.equals(Constants.MQTT_PUBLISH_FAILED)){
                    Log.d("mqtt", "Received to activeOrderActivity: error ");
                    printButton.setEnabled(true);
                    Toast toast = Toast.makeText(getApplicationContext(), "Can't print the bill due to connection error!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    printButton.setEnabled(true);
                    Log.d("mqtt", "Received to activeOrderActivity: error ");
                    Toast toast = Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        }
    }

    public void showMesage(String message){
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Order Ready!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
