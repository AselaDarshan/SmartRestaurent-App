package com.enet.smartrestaurent;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asela on 5/17/17.
 */
public class Menu {

    public Menu(Context ctx){
        WebServerCommunicationService.sendGetRequest(ctx,Constants.API_BASE_URL+Constants.API_MENU);
    }


    public void updateMenu(Context ctx,JSONObject menuObject) throws JSONException {
        JSONArray menuArray = menuObject.getJSONObject(Constants.API_MENU).getJSONArray(Constants.RECORDS_KEY);

        List<MenuItem> items = new ArrayList<>();

        for(int i=0;i<menuArray.length();i++){
            JSONArray item = menuArray.getJSONArray(i);
            items.add(new MenuItem(item.getInt(0),item.getInt(5),item.getString(1),item.getDouble(3)));
            Log.d("adding menu item:",menuArray.getJSONArray(i).getString(1));
        }


        //MenuItem item = new MenuItem("Salad",650);


        //item.save();
        MenuItem.deleteAll(MenuItem.class);
        MenuItem.saveInTx(items);
//        Log.d("menu item:",menuArray.getJSONArray(0).getString(1));
    }



}
