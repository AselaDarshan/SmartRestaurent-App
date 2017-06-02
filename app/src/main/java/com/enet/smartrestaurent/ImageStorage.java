package com.enet.smartrestaurent;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageStorage {


    public static String saveToSdCard(Bitmap bitmap, String filename,Context ctx) {

        String stored = null;
        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,filename);
       // File sdcard = Environment.getDataDirectory() ;

       // File folder = new File(sdcard.getAbsoluteFile(), "your_specific_directory");//the dot makes this directory hidden to the user
       // folder.mkdir();
       // File file = new File(folder.getAbsoluteFile(), filename) ;
        Log.d("ImageStorage","Saving Image..");

        if (mypath.exists()) {
            Log.d("ImageStorage", "Path Exists");
            //return stored;
        }

        try {
            FileOutputStream out = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
            Log.d("ImageStorage","Image saved");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ImageStorage","Image saving failed");
        }
        return stored;
    }

    public static File getImage(String imagename,Context ctx) {

        File mediaImage = null;
        try {
            ContextWrapper cw = new ContextWrapper(ctx);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            mediaImage=new File(directory,imagename);
//            String root = Environment.getDataDirectory().toString();
//            File myDir = new File(root);
//            if (!myDir.exists())
//                return null;
            Log.d("ImageStorage","Image retrieved: "+imagename);
           // mediaImage = new File(myDir.getPath() + "/your_specific_directory/"+imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("ImageStorage","Image received failed");
            e.printStackTrace();
        }
        return mediaImage;
    }
    public static boolean checkifImageExists(String imagename,Context ctx)
    {
        Bitmap b = null ;
       // File file = ImageStorage.getImage("/"+imagename);
     //   String path = file.getAbsolutePath();
        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
       File mediaImage=new File(directory,imagename);
        if (mediaImage != null)
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(mediaImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        if(b == null ||  b.equals(""))
        {
            Log.d("ImageStorage","Image not found");
            return false ;
        }
        Log.d("ImageStorage","Image exists");
        return true ;
    }
}