package com.enet.smartrestaurent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetImages extends AsyncTask<Object, Object, Object> {
    private String requestUrl, imagename_;
//    private ImageView view;
    private Bitmap bitmap ;
    private FileOutputStream fos;
    Context ctx;
    public GetImages(String requestUrl, String _imagename_,Context ctx) {
        this.requestUrl = requestUrl;
//        this.view = view;
        this.imagename_ = _imagename_ ;
        this.ctx = ctx;
    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(!ImageStorage.checkifImageExists(imagename_,ctx))
        {
//            view.setImageBitmap(bitmap);
            try {
                Log.d("image downloaded", bitmap.toString());
                ImageStorage.saveToSdCard(bitmap, imagename_, ctx);
            }
            catch (java.lang.NullPointerException ex){
                Log.d("GetImages","Image not downloaded");
            }
        }
    }
}