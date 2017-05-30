package com.enet.smartrestaurent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WebServerCommunicationService extends IntentService {



    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POST = "action.POST";
    private static final String ACTION_GET = "action.GET";
    private static final String ACTION_JSON_POST= "JSON_POST";
    private static final String ACTION_BAZ = "action.BAZ";

    // TODO: Rename parameters
    private static final String PARAM_DATA = "extra.DATA";
    private static final String PARAM_URL = "extra.URL";
    private static final String EXTRA_PARAM1 = "extra.PARAM1";
    private static final String EXTRA_PARAM2 = "extra.PARAM2";


    public WebServerCommunicationService() {
        super("ServerCommunicationIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void sendPostRequest(Context context, HashMap<String, String> data, String url) {
        Log.d("communication_service","send post request: "+data+" url: "+url);
        Intent intent = new Intent(context, WebServerCommunicationService.class);
        intent.setAction(ACTION_POST);
        intent.putExtra(PARAM_DATA, data);
        intent.putExtra(PARAM_URL,url);
        context.startService(intent);
    }
    public static void sendGetRequest(Context context, String url) {
        Log.d("communication_service","sendGetRequest");
        Intent intent = new Intent(context, WebServerCommunicationService.class);
        intent.setAction(ACTION_GET);
        intent.putExtra(PARAM_URL,url);
        context.startService(intent);
    }

    public static void sendJsonPostRequest(Context context, String data, String url) {
        Log.d("communication_service","send json post request: "+data+" url: "+url);
        Intent intent = new Intent(context, WebServerCommunicationService.class);
        intent.setAction(ACTION_JSON_POST);
        intent.putExtra(PARAM_DATA, data);
        intent.putExtra(PARAM_URL,url);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("communication_service","onHandelCalled");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST.equals(action)) {
                handleActionHttpPostRequest((HashMap<String, String>)intent.getSerializableExtra(PARAM_DATA),intent.getStringExtra(PARAM_URL));
            }
            else if(ACTION_GET.equals(action)) {
                handleGetRequest(intent.getStringExtra(PARAM_URL));
            }
            if (ACTION_JSON_POST.equals(action)) {
                try {
                    JSONObject jsonData = new JSONObject(intent.getStringExtra(PARAM_DATA));
                    handleActionJsonPostRequest(jsonData, intent.getStringExtra(PARAM_URL));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }
    private void handleActionHttpPostRequest(final Map<String, String> MyData, String url) {
        // Instantiate the RequestQueue.
        SecurityHandler.handleSSLHandshake();
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("communication_service","http response: "+response);
                broadcast(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.d("communication_service","http error: "+error.toString());
                broadcast(Constants.COMMUNICATION_ERROR);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
//                Map<String, String> MyData = new HashMap<String, String>();
//                if(Parameters.currentUser!=null) {
//                    MyData.put("user", Parameters.currentUser);//sender's username
//                }
//                MyData.put("data", data); //Post data.
                Map<String,String> params = MyData;
                Log.d("communication_service","sending string request: "+params);
//
//                HashMap<String, String> data = new HashMap<>();
//
//
////            data.put(Constants.PASSWORD_KEY,password);
////            data.put(Constants.USERNAME_KEY,nic);
//                data.put("grant_type","password");
//                data.put("username","484848");
//                data.put("password","484848");
//                data.put("scope","openid");
//                return params;
                return MyData;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
              //  params.put("Content-Type", "application/x-www-form-urlencoded");
              //  String key =clientkey+":"+secretkey;
              //  params.put("Authorization", "Basic "+ Base64.encodeToString(key.getBytes(), Base64.DEFAULT));
                return params;
            }
        };



    // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void handleActionJsonPostRequest(final JSONObject data, String url) {
        // Instantiate the RequestQueue.
        SecurityHandler.handleSSLHandshake();
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("communication_service","http json response: "+response);
                broadcast(response.toString());
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.d("communication_service","http error: "+error.toString());
                broadcast(Constants.COMMUNICATION_ERROR);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("data", data.toString()); //Post data.
                return MyData;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
              //  params.put("Accept", "application/json");
//                params.put("Authorization", "Bearer "+Parameters.token);
                return params;
            }
        };



        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private void handleGetRequest(final String url){
        Log.d("communication_service","sending get request: "+url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        broadcast(response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getStackTrace().toString());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void broadcast(String status ){
         /*
     * Creates a new Intent containing a Uri object
     * BROADCAST_ACTION is a custom Intent action
     */
        Intent localIntent =
                new Intent(Constants.BROADCAST_ACTION)
                        // Puts the status into the Intent
                        .putExtra(Constants.EXTENDED_DATA_STATUS, status);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }

}

