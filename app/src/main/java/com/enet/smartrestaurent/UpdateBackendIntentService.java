package com.enet.smartrestaurent;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateBackendIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ORDER = "com.enet.smartrestaurent.action.order";
    private static final String ACTION_ORDER_MENUS = "com.enet.smartrestaurent.action.order_menus";
    private static final String ACTION_UPDATE_ITEM = "com.enet.smartrestaurent.action.update_item";

    private Receiver receiver = new Receiver();

//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.enet.smartrestaurent.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.enet.smartrestaurent.extra.PARAM2";

    public UpdateBackendIntentService() {
        super("UpdateBackendIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSendOrderToBackend(Context context,HashMap<String,OrderedItem> order,int tableId) {
        Intent intent = new Intent(context, UpdateBackendIntentService.class);
        intent.setAction(ACTION_ORDER);
        intent.putExtra("ORDER", order);
        intent.putExtra("TABLE_ID", tableId);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }
    public static void startSyncronizeRequest(Context context,String itemId) {
        Intent intent = new Intent(context, UpdateBackendIntentService.class);
        intent.setAction(ACTION_UPDATE_ITEM);

        intent.putExtra("ITEM_ID", itemId);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }
    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSendOrderMenusToBackend(Context context,int orderId,HashMap<String,OrderedItem> order,int tableId) {
        Intent intent = new Intent(context, UpdateBackendIntentService.class);
        intent.setAction(ACTION_ORDER_MENUS);
        intent.putExtra("ORDER", order);
        intent.putExtra("ORDER_ID", orderId);
        intent.putExtra("TABLE_ID", tableId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ORDER.equals(action)) {
                final HashMap<String, OrderedItem> orderList = (HashMap<String, OrderedItem>)intent.getSerializableExtra("ORDER");
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                final int tableId = intent.getIntExtra("TABLE_ID",0);
                handleActionSendOrderToBackend(orderList,tableId);
            } else if (ACTION_ORDER_MENUS.equals(action)) {
                final HashMap<String, OrderedItem> orderList = (HashMap<String, OrderedItem>)intent.getSerializableExtra("ORDER");
//
                final int orderId = intent.getIntExtra("ORDER_ID",0);
                final int tableId = intent.getIntExtra("TABLE_ID",0);
                handleActionSendOrderMenusToBackend(orderId,orderList,tableId);
            }
            else if (ACTION_UPDATE_ITEM.equals(action)) {

                final String itemId = intent.getStringExtra("ITEM_ID");
                handleSyncronizeRequest(itemId);
            }
        }
    }
    private HashMap<String,OrderedItem> orderdItems;
    private int tableID;
    private boolean responseRecieved = false;
    public void handleSyncronizePutRequest(int itemId,String comment){
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        int REQUEST_TIMEOUT = 10;

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("option_values",Constants.ITEM_STATE_PREPARED);
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,"http://resmng.enetlk.com/api/api.php/forsj3vth_order_menus/"+itemId,dataObject, future, future);
            requestQueue.add(request);
//            try {
////                JSONObject responses = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS); // this will block (forever)
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//...



    }
    public void handleSyncronizeRequest(String itemId){
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        int REQUEST_TIMEOUT = 10;

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"http://resmng.enetlk.com/api/api.php/forsj3vth_order_menus/?filter=comment,eq,"+itemId, null, future, future);
        requestQueue.add(request);

//...

        try {
            JSONObject responses = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS); // this will block (forever)


            Log.d("startingActivity","order Object: "+responses);

            JSONArray menuArray = responses.getJSONObject(Constants.API_ORDER_MENUS).getJSONArray(Constants.RECORDS_KEY);
            JSONArray item = menuArray.getJSONArray(0);
            int itemIdDB = item.getInt(0);
            handleSyncronizePutRequest(itemIdDB,itemId);
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void handleActionSendOrderToBackend(HashMap<String,OrderedItem> orderList,int tableId){
        orderdItems = orderList;
        IntentFilter filter = new IntentFilter(Constants.ADD_ORDER_ACTION);
        this.registerReceiver(receiver, filter);

        Set<String> keys = orderList.keySet();
        double total=0;
        for(String key:keys) {
                OrderedItem item = orderList.get(key);
                total += item.getPrice()*item.getQty();
        }


        JSONObject orderObject = new JSONObject();
        try {
            Date date = new Date();
            orderObject.put("total_items", orderList.size());
            orderObject.put("date_added", new java.sql.Timestamp(date.getTime()));
            orderObject.put("order_time", new java.sql.Time(date.getTime()));
            orderObject.put("name","testitem");
            orderObject.put("order_date", new java.sql.Timestamp(date.getTime()));
            orderObject.put("order_total", total);
            orderObject.put("first_name", "");
            orderObject.put("last_name", "");
            orderObject.put("assignee_id", GlobalState.getCurrrentUserId());
            orderObject.put("customer_id", 11);
            orderObject.put("status_id", 11);
            WebServerCommunicationService.sendJsonPostRequest(this,orderObject.toString(),"http://resmng.enetlk.com/api/api.php/forsj3vth_orders",Constants.ADD_ORDER_ACTION);
            tableID = tableId;
            while (!responseRecieved){

            }
            //orderId = Integer.parseInt(SendJOSNPOST("http://resmng.enetlk.com/api/api.php/forsj3vth_orders",orderObject));
            //order.setOrderId(orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void handleActionSendOrderMenusToBackend(int orderId,HashMap<String,OrderedItem> orderedItems,int tableId){
        IntentFilter filter = new IntentFilter(Constants.ADD_ORDER_MENUS_ACTION);
        this.registerReceiver(receiver, filter);
        Set<String> keys = orderedItems.keySet();
        for(String key:keys) {
            JSONObject orderItem = new JSONObject();
            try {
                OrderedItem item = orderedItems.get(key);

                orderItem.put("order_id", orderId);
                orderItem.put("menu_id", item.getMenuId());
                orderItem.put("name", item.getName());
                orderItem.put("quantity", item.getQty());
                orderItem.put("price", item.getPrice());
                orderItem.put("subtotal", item.getPrice()*item.getQty());
                orderItem.put("option_values",Constants.ITEM_STATE_SENT);
                orderItem.put("comment",item.itemId);
                WebServerCommunicationService.sendJsonPostRequest(this,orderItem.toString(),"http://resmng.enetlk.com/api/api.php/forsj3vth_order_menus",Constants.ADD_ORDER_MENUS_ACTION);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDestroy()  {
        try {


            unregisterReceiver(receiver);
            super.onDestroy();
        }
        catch (java.lang.IllegalArgumentException e){}
    }
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(Constants.ADD_ORDER_ACTION)) {
                int orderId = Integer.parseInt(arg1.getExtras().getString(Constants.RESPONSE_KEY));
              //  Toast.makeText(getApplicationContext(), arg1.getExtras().getString(Constants.RESPONSE_KEY), Toast.LENGTH_SHORT).show();
                startActionSendOrderMenusToBackend(getApplicationContext(),orderId,orderdItems,tableID);
                responseRecieved = true;
            }
        }
    }
}
