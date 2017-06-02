package com.enet.smartrestaurent;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asela on 5/17/17.
 */
public class Menu {

    public Menu(Context ctx){
        WebServerCommunicationService.sendGetRequest(ctx,Constants.API_BASE_URL+Constants.API_CATEGORIES,Constants.CATEGORIES_UPDATE_ACTION);

        WebServerCommunicationService.sendGetRequest(ctx,Constants.API_BASE_URL+Constants.API_MENU,Constants.MENU_UPDATE_ACTION);
    }


    public void updateMenuItems(Context ctx, JSONObject menuObject) throws JSONException {
        JSONArray menuArray = menuObject.getJSONObject(Constants.API_MENU).getJSONArray(Constants.RECORDS_KEY);

        List<MenuItem> items = new ArrayList<>();

        for(int i=0;i<menuArray.length();i++){
            JSONArray item = menuArray.getJSONArray(i);
            String imageName = item.getString(4);
            Log.d("Menu","imageName: "+imageName);
            try{
                imageName = item.getString(4).split("/")[1].split("\\.")[0] + "-120x120.jpg";
            }
            catch (java.lang.ArrayIndexOutOfBoundsException ex){
                imageName = "salad-120x120.jpg";
            }
            items.add(new MenuItem(item.getInt(0),item.getInt(5),item.getString(1),item.getDouble(3),imageName));
            Log.d("adding menu item:",menuArray.getJSONArray(i).getString(1));


            if(ImageStorage.checkifImageExists(imageName,ctx))
            {
                File file = ImageStorage.getImage("/"+imageName,ctx);
                String path = file.getAbsolutePath();
                if (path != null){
                    Log.d("image found",path);
                   // b = BitmapFactory.decodeFile(path);
                    //imageView.setImageBitmap(b);
                }
            } else {
                String imgurl = "http://192.168.8.107/assets/images/thumbs/"+imageName;
                new GetImages(imgurl, imageName,ctx).execute() ;
            }
        }

        MenuItem.deleteAll(MenuItem.class);
        MenuItem.saveInTx(items);
    }

    public void updateCategories(Context ctx,JSONObject categoriesObject) throws JSONException {
        JSONArray menuArray = categoriesObject.getJSONObject(Constants.API_CATEGORIES).getJSONArray(Constants.RECORDS_KEY);

        List<Category> categoryArrayList = new ArrayList<>();

        for(int i=0;i<menuArray.length();i++){
            JSONArray item = menuArray.getJSONArray(i);
            categoryArrayList.add(new Category(item.getInt(0),item.getString(1)));
            Log.d("adding category",item.getInt(0)+":"+item.getString(1));
        }

        Category.deleteAll(Category.class);
        Category.saveInTx(categoryArrayList);
    }

}
