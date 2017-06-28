package com.enet.smartrestaurent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asela on 6/2/17.
 */
public class ItemListAdapter extends SimpleAdapter{

    private Activity mContext;
    public ImageLoader imageLoader;
    public LayoutInflater inflater=null;

    private HashMap<Integer,String> optionPriceMap;
    private TextView priceText;

    public ItemListAdapter(Activity context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        optionPriceMap =  new HashMap<>();
       // imageLoader=new ImageLoader(mContext.getApplicationContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listview_activity, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView nameText = (TextView)vi.findViewById(R.id.listview_item_title);
        priceText = (TextView)vi.findViewById(R.id.listview_item_short_description);

        Spinner options = (Spinner) vi.findViewById(R.id.listview_item_options);

        String name = (String) data.get("listview_title");
        String price = (String) data.get("listview_price");
        String imageName = (String) data.get("listview_image");

        String optionsString = (String) data.get("listview_options");
        nameText.setText(name);
        priceText.setText(price);

        if(optionsString!=null) {

            String[] optionArray = optionsString.split(",");// new ArrayList<String>();
//            String[] spinnerArray = new String[optionArray.length];
            List<StringWithTag> list = new ArrayList<StringWithTag>();


            int itemCount= 0;
            for(itemCount=0;itemCount<optionArray.length;itemCount++){
//                spinnerArray[itemCount] = optionArray[itemCount].split(":")[0];
                list.add(new StringWithTag(optionArray[itemCount].split(":")[0], optionArray[itemCount].split(":")[1]));
//                optionPriceMap.put(i,optionArray[i].split(":")[1]);
            }
//        spinnerArray.add("item1");
//        spinnerArray.add("item2");

            ArrayAdapter<StringWithTag> adapter = new ArrayAdapter<StringWithTag>(mContext, android.R.layout.simple_spinner_item, list);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            options.setAdapter(adapter);
            options.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) mContext);
//            for(int i=0;i<itemCount;i++){
//                options.getChildAt(i).setTag(optionArray[i].split(":")[1]);
//            }

            Log.d("Item_list_Adapter", "options: " + options);
        }
        else{
            options.setVisibility(View.GONE);
        }

        ImageView image=(ImageView)vi.findViewById(R.id.listview_image);

        File path = ImageStorage.getImage(imageName, mContext.getApplicationContext());
        Bitmap b = BitmapFactory.decodeFile(path.getAbsolutePath());
        image.setImageBitmap(b);
        return vi;
    }


}
