package com.enet.smartrestaurent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asela on 6/2/17.
 */
public class ItemListAdapter extends SimpleAdapter {

    private Context mContext;
    public ImageLoader imageLoader;
    public LayoutInflater inflater=null;
    public ItemListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // imageLoader=new ImageLoader(mContext.getApplicationContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listview_activity, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView nameText = (TextView)vi.findViewById(R.id.listview_item_title);
        TextView priceText = (TextView)vi.findViewById(R.id.listview_item_short_description);
        String name = (String) data.get("listview_title");
        String price = (String) data.get("listview_price");
        String imageName = (String) data.get("listview_image");
        nameText.setText(name);
        priceText.setText(price);

        ImageView image=(ImageView)vi.findViewById(R.id.listview_image);

        File path = ImageStorage.getImage(imageName, mContext.getApplicationContext());
        Bitmap b = BitmapFactory.decodeFile(path.getAbsolutePath());
        image.setImageBitmap(b);
        return vi;
    }
}
