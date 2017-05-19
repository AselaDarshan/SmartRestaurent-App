package com.enet.smartrestaurent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ConfirmOrderActivity extends AppCompatActivity {


    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private JSONObject dataToSend;
    private SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_confirm_order);

        EditText tableIdText = (EditText)ConfirmOrderActivity.this.findViewById(R.id.table_id_text);
        tableIdText.setText(getIntent().getStringExtra("TABLE_ID"));

        HashMap<String,Integer> orderList = (HashMap<String, Integer>) getIntent().getSerializableExtra("ORDER");

        dataToSend = new JSONObject();
        try {
            dataToSend.put("TABLE",tableIdText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TableLayout table = (TableLayout)ConfirmOrderActivity.this.findViewById(R.id.order_table);
        for(String key : orderList.keySet())
        {
            // Inflate your row "template" and fill out the fields.
            TableRow row = (TableRow) LayoutInflater.from(ConfirmOrderActivity.this).inflate(R.layout.order_row, null);
            ((TextView)row.findViewById(R.id.attrib_name)).setText(key);
            ((TextView)row.findViewById(R.id.attrib_value)).setText(String.valueOf(orderList.get(key)));
            table.addView(row);

            try {
                dataToSend.put(key,orderList.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        table.requestLayout();     // Not sure if this is needed.

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Order Confirmation</font>"));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        sharedPref = getSharedPreferences("pref",Context.MODE_PRIVATE);
//        Button confirm = (Button) findViewById(R.id.confirmButton);
//        try {
//            confirm.setOnClickListener(new View.OnClickListener() {
//
//                Socket s = new Socket("192.168.1.11", 8080);
//
//
//                @Override
//                public void onClick(View v) {
//                   // status.setText("...");
//                    PrintWriter outp = null;
//                    BufferedReader inp = null;
//                   // status.setText("Established connection..");
//                    String serverMsg = null;
//
//                    try {
//                        outp = new PrintWriter(s.getOutputStream(), true);
//                        inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                        serverMsg = inp.readLine();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //convo.append(serverMsg + "\n");
//
//
//                            try {
//
//                              //  convo.append(message + "\n");
//                                outp.println("order");
//                                serverMsg = inp.readLine();
//                                Log.d("communication",serverMsg);
//                              //  convo.append(serverMsg + "\n");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }

    public void confirmOrder(View v){
        try {


            String  ip = sharedPref.getString(getString(R.string.ip_key), "192.168.8.100");

//            Socket s = new Socket("192.168.8.108", 8080);
            Socket s = new Socket(ip, 8080);

            PrintWriter outp = new PrintWriter(s.getOutputStream(), true);
            BufferedReader inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String serverMsg;
//            JSONObject dataObj = new JSONObject(dataToSend);
            Log.d("communication",dataToSend.toString());
            outp.println(dataToSend.toString());
            serverMsg = inp.readLine();
            Log.d("communication",serverMsg);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this,"Communication Error!",Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
            return;
        }

        final ConfirmOrderActivity obj= this;
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(obj, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(obj);
        }
        builder.setTitle("Order has been placed")
                .setMessage("You will receive a notification after the order has been prepared")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(obj, StartingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }

}
