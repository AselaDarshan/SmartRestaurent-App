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
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

//import com.orm.SugarContext;

import org.json.JSONException;
import org.json.JSONObject;

public class StartingActivity extends AppCompatActivity {

    private Receiver receiver = new Receiver();

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Silver Ring Village Hotel</font>"));

        IntentFilter mqttIntentFilter = new IntentFilter(Constants.MQTT_NEW_MESSAGE_ACTION);
        IntentFilter menuUpdateIntentFilter = new IntentFilter(Constants.MENU_UPDATE_ACTION);
        IntentFilter categoriesUpdateIntentFilter = new IntentFilter(Constants.CATEGORIES_UPDATE_ACTION);
        IntentFilter mqttConnectionIntentFilter = new IntentFilter(Constants.MQTT_CONNECTION_STATE_ACTION);
        this.registerReceiver(receiver,mqttIntentFilter);
        this.registerReceiver(receiver, menuUpdateIntentFilter);
        this.registerReceiver(receiver, categoriesUpdateIntentFilter);
        this.registerReceiver(receiver, mqttConnectionIntentFilter);

        

        menu = new Menu(this);


      CheckUpdatesService.startActionUpdateCheck(this);


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    public void newOrderButtonClicked(View v){
        if(GlobalState.isConnected()) {
            Intent intent = new Intent(this, TableSelectionActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            showError("Not Connected!","Please check the WiFi connection");
        }
    }
    public void activeOrdersButtonClicked(View v){

        Intent intent = new Intent(this, ActiveOrdersActivity.class);
        startActivity(intent);

    }
    public void settingsButtonClicked(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String response = arg1.getExtras().getString(Constants.RESPONSE_KEY);
            String action = arg1.getAction();
            Log.d("broadcast_received:",arg1.getAction());

            if(action.equals(Constants.MENU_UPDATE_ACTION)){
                try {
                    JSONObject jObject = new JSONObject(response);
                    menu.updateMenuItems(getApplicationContext(),jObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if(action.equals(Constants.CATEGORIES_UPDATE_ACTION)){
                try {
                    JSONObject jObject = new JSONObject(response);
                    menu.updateCategories(getApplicationContext(),jObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if(action.equals(Constants.MQTT_NEW_MESSAGE_ACTION)){
                if (response != null && !response.contains(Constants.ERROR_RESPONSE)) {
                    Log.d("update Received", "Received to startingActivity: " + response);
                    String topic = response.split("~")[0];
                    if(topic.contains(Constants.ORDER_COMPLETED_TOPIC))
                        showMesage(response.split("~")[1]);

                } else {

                    Log.d("communication", "Received to startingActivity: error ");
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

    public void showError(String title, String message){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }





}
