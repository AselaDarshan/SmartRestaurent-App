package com.enet.smartrestaurent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Silver Ring Village Hotel</font>"));

        IntentFilter filter = new IntentFilter(Constants.CHECKUPDATES_ACTION);
        this.registerReceiver(new Receiver(), filter);
        CommunicationService.startReceving(this,"","");
        CheckUpdatesService.startActionUpdateCheck(this,"","");

    }

    public void newOrderButtonClicked(View v){
        Intent intent = new Intent(this, TableSelectionActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void settingsButtonClicked(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void showMesage(String message){
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
            // Log.d("communication","Received to confirmOrderActivity: "+url);
            if(response!=Constants.ERROR_RESPONSE) {
                Log.d("update Received", "Received to startingActivity: "+response);

                    showMesage(response);


            }
            else{
                Log.d("communication","Received to startingActivity: error ");
                Toast toast = Toast.makeText(getApplicationContext(), "Communication Error!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
