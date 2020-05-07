package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ReadAndWrite {
    public static void write(String fileName, String jsonString, Context context) {
        try {
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            ows.write(jsonString);
            ows.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        }
    }
    public static String read(String fileName, Context context) {
        try {
            InputStream ins = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String s = "";
            String fullText= "";
            while((s=br.readLine()) != null) {
                fullText = fullText + s;
            }
            ins.close();
            return fullText;
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        }
        return "";
    }
}
