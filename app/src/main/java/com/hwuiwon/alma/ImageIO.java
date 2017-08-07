package com.hwuiwon.alma;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageIO {

    private String filename;
    private String filepath;
    private Context context;

    public ImageIO (Context context) {
        this.context = context;
    }

    public ImageIO setFileName (String filename) {
        this.filename = filename;
        return this;
    }

    public ImageIO setFilepath (String filepath) {
        this.filepath = filepath;
        return this;
    }

    public void save (Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(createFile());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d("debug", "image saved : "+filepath+"/"+filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFile() {
        File directory = context.getDir(filepath, Context.MODE_PRIVATE);
        return new File(directory, filename);
    }

    public Bitmap load() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(createFile());
            Log.d("debug", "image loaded : "+filepath+"/"+filename);
            return BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
