package com.enet.smartrestaurent;

/**
 * Created by asela on 5/8/17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class SaladFragment extends Fragment{
    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Chef Salad", "Sliver Ring Special Salad", "Chicken Pineapple Salad", "Russian Salad",
            "Mix Salad", "Coleslaw Salad",
    };


    int[] listviewImage = new int[]{
            R.mipmap.ic_icecream, R.mipmap.ic_icecream,  R.mipmap.ic_icecream,  R.mipmap.ic_icecream,
            R.mipmap.ic_icecream, R.mipmap.ic_icecream,
    };

    String[] listviewShortDescription = new String[]{
            "650", "450", "350", "300",
            "300", "300",
    };
    public SaladFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        try {


            Iterator<MenuItem> menuItemIterator = MenuItem.findAll(MenuItem.class);
            MenuItem item;
//        Log.d("add menu item",String.valueOf(MenuItem.findById(MenuItem.class,(long)0).price));
            while (menuItemIterator.hasNext()) {
                item = menuItemIterator.next();
                try {

                    Log.d("add menu item", item.toString());
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("listview_title", item.name);
                    hm.put("listview_discription", String.valueOf(item.price) + " LKR");
                    hm.put("listview_image", Integer.toString(listviewImage[0]));
                    aList.add(hm);
                } catch (NullPointerException ex) {
                    Log.d("add menu item error", ex.toString());
                }
            }

            String[] from = {"listview_image", "listview_title", "listview_discription"};

            int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_activity, from, to);
            ListView androidListView = (ListView) view.findViewById(R.id.list_view);
            androidListView.setAdapter(simpleAdapter);
        }
        catch (android.database.sqlite.SQLiteException ex){
            Toast.makeText(getContext(),"No items in this Category",Toast.LENGTH_SHORT).show();
        }
    }

}