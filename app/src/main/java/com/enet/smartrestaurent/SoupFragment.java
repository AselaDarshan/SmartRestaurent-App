package com.enet.smartrestaurent;

/**
 * Created by asela on 5/8/17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SoupFragment extends Fragment{
    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Silver Ring Special Soup", "Sea Food Chowder", "Minestrone Soup", "Lemon Grass with Prawn Flavor Soup",
            "Chicken Soup", "Beef Soup", "Sweet corn Soup", "Mushroom Soup","Tomato Soup","Vegetable Soup",
    };


    int[] listviewImage = new int[]{
            R.mipmap.ic_icecream, R.mipmap.ic_icecream,  R.mipmap.ic_icecream,  R.mipmap.ic_icecream,
            R.mipmap.ic_icecream, R.mipmap.ic_icecream,  R.mipmap.ic_icecream, R.mipmap.ic_icecream, R.mipmap.ic_icecream, R.mipmap.ic_icecream,
    };

    String[] listviewShortDescription = new String[]{
            "400", "300", "300", "300",
            "275", "250", "250", "250","250","250",
    };
    public SoupFragment() {
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

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]+" LKR");
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};

        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) view.findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
    }


}