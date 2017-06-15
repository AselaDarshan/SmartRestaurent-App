package com.enet.smartrestaurent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Spinner  spinner2;
    private Receiver receiver = new Receiver();
    private List<String> list;
    private ArrayList<Integer> idList;
    private ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addItemsOnSpinner2();
    }

    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        list = new ArrayList<String>();


//        list.add("Waiter 2");
//        list.add("Waiter 3");
//        list.add("Waiter 1");
        dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);


        IntentFilter filter = new IntentFilter(Constants.WAITER_LIST_RETRIVE_ACTION);
        this.registerReceiver(receiver,filter);

        retrieveOrderList();

    }
    public void retrieveOrderList(){
        WebServerCommunicationService.sendGetRequest(this,Constants.API_BASE_URL+Constants.API_STAFF+"?filter=staff_group_id,eq,"+Constants.WAITER_GROUP_ID,Constants.WAITER_LIST_RETRIVE_ACTION);
    }

    public void updateUserList(String response){
        list.add("Select your username");
        idList = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(response);
            JSONArray menuArray = jObject.getJSONObject(Constants.API_STAFF).getJSONArray(Constants.RECORDS_KEY);



            for(int i=0;i<menuArray.length();i++){
                JSONArray item = menuArray.getJSONArray(i);
                list.add(item.getString(1));
                idList.add(item.getInt(0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataAdapter.notifyDataSetChanged();
    }

    public void loginButtonClicked(View v){
        if(spinner2.getSelectedItem()==null){
            Toast.makeText(getApplicationContext(), "Please select your username", Toast.LENGTH_LONG).show();
            retrieveOrderList();
            return;
        }
        String username = spinner2.getSelectedItem().toString();
        if(username.equals("Select your username")){
            Toast.makeText(getApplicationContext(), "Please select your username", Toast.LENGTH_LONG).show();
        }

        else {
            GlobalState.setCurrentUsername(username);
            GlobalState.setCurrrentUserId(idList.get(spinner2.getSelectedItemPosition()));
            Intent intent = new Intent(this, StartingActivity.class);
            startActivity(intent);

        }
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

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String response = arg1.getExtras().getString(Constants.RESPONSE_KEY);
            String action = arg1.getAction();
            Log.d("broadcast_received_logi", arg1.getAction());

            if (action.equals(Constants.WAITER_LIST_RETRIVE_ACTION)) {
                if (response != null && !response.contains(Constants.ERROR_RESPONSE)) {
                    Log.d("update Received", "Received to loginactivity: " + response);

                   updateUserList(response);


                } else {
                    Log.d("communication", "Received to activeOrderActivity: error ");
                    Toast toast = Toast.makeText(getApplicationContext(), "Please check your wifi connection", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }
}
