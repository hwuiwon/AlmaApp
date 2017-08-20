package com.hwuiwon.alma;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class DataIO {

    private Context context;
    private String filename;

    DataIO(Context context) {
        this.context = context;
    }

    String readFile(String filename) {
        String ret = "";
        this.filename = filename;

        try {
            FileInputStream fileInputStream = new FileInputStream(createFile());

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
                stringBuilder.append(System.lineSeparator());
            }

            fileInputStream.close();
            ret = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private File createFile() {
        File directory = context.getCacheDir();
        return new File(directory, filename);
    }

    void writeFile(String filename, String content) {
        this.filename = filename;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(createFile());
            fileOutputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
