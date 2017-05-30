package com.enet.smartrestaurent;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CommunicationService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.enet.smartrestaurent.action.FOO";
    private static final String ACTION_BAZ = "com.enet.smartrestaurent.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.enet.smartrestaurent.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.enet.smartrestaurent.extra.PARAM2";
    private static Socket s = null;


    private BufferedReader inp;

    private static int retries=0;

    public CommunicationService() {
        super("CommunicationService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionReceive(Context context, String param2) {
//        Intent intent = new Intent(context, CommunicationService.class);
//        intent.setAction(ACTION_FOO);
//
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void sendToServer(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CommunicationService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startReceving(Context context, String param1, String param2){
        Intent intent = new Intent(context, CommunicationService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {

                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
               handleActionReceive();
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionSendToServer(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
//    private void handleActionReceive(String param2) {
//
//
//            String serverMsg;
//            BufferedReader inp = null;
//            try {
//                SharedPreferences sharedPref=getSharedPreferences("pref",Context.MODE_PRIVATE);;
//                String ip = sharedPref.getString(getString(R.string.ip_key), "192.168.8.100");
//
//
//                Socket s = new Socket(ip, 8080);
//                inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
//
//                while (true) {
//                    serverMsg = inp.readLine();
//                    Log.d("communication service","Received: "+ serverMsg);
//                }
//            } catch (IOException e) {
//                if(retries<5) {
//                    startActionReceive(this, "");
//                    Log.d("communication service", "Retrying to connect");
//                }
//                retries++;
//                e.printStackTrace();
//            }
//
//    }
    private void handleActionReceive() {
        try {
            if (s == null) {
                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                String ip = sharedPref.getString(getString(R.string.ip_key), "172.24.1.1");
                s = new Socket(ip, 8080);
            }

            inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Receiver receiver = new Receiver("");
            receiver.start();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSendToServer(String data, String action) {
        try {



            if(s==null) {
                SharedPreferences sharedPref=getSharedPreferences("pref",Context.MODE_PRIVATE);
                String ip = sharedPref.getString(getString(R.string.ip_key), "172.24.1.1");
                s = new Socket(ip, 8080);
                CommunicationService.startReceving(this,"","");
                Log.d("communication service", "connecting..");
            }


            PrintWriter outp = new PrintWriter(s.getOutputStream(), true);

            outp.println(data);
            Boolean success = !outp.checkError();
            if(success){
                GlobalState.setConnected(true);
                Log.d("communication service", "sending success: "+data);
            }
            else{
                GlobalState.setConnected(false);
                Log.d("communication service", "Failed: "+data);
                if(s!=null)
                    s.close();
                s = null;
            }




        } catch (IOException e) {
//            Toast toast = Toast.makeText(this, "Communication Error!", Toast.LENGTH_SHORT);
//            toast.show();
            GlobalState.setConnected(false);
            Log.d("communication service","error");
            Intent intent=new Intent();
            intent.setAction(action);
            intent.putExtra(Constants.RESPONSE_KEY,Constants.ERROR_RESPONSE);
            sendBroadcast(intent);
            e.printStackTrace();

        }
    }

    class Receiver extends Thread{


        public Receiver(String action){


        }
        @Override
        public void run(){
            String serverMsg = null;//readout echoed message
            String action = Constants.CHECKUPDATES_ACTION;
            while (true) {
                try {
                    Log.d("communication service", "waiting for messages");
                    serverMsg = inp.readLine();


                    try {
                        if (serverMsg.contains("order_success"))
                            action = Constants.ORDER_ACTION;
                        else if (serverMsg.contains("Ready!"))
                            action = Constants.CHECKUPDATES_ACTION;
                        else
                            action = "";

                        Log.d("communication service", "Received: " + serverMsg);
                    }
                    catch (NullPointerException e){
                        Log.d("communication service", "Null response received. reconnecting");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            s.close();
                            s = null;
                        }
                        catch (NullPointerException ex){}
                        CommunicationService.startReceving(getBaseContext(),"","");
                    }
                    // startActionReceive(this,"");


                    Intent intent = new Intent();
                    intent.setAction(action);
                    intent.putExtra(Constants.RESPONSE_KEY, serverMsg);
                    sendBroadcast(intent);

                } catch (IOException e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    Log.d("communication service", "error in receive thread");
                    Intent intent = new Intent();
                    intent.setAction(Constants.CHECKUPDATES_ACTION);
                    intent.putExtra(Constants.RESPONSE_KEY, Constants.ERROR_RESPONSE);
                    sendBroadcast(intent);
                    e.printStackTrace();
                }
            }
        }
    }
}
