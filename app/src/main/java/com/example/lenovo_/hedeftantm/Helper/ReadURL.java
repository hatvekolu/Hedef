package com.example.lenovo_.hedeftantm.Helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by Lenovo- on 16.12.2017.
 */

public class ReadURL {
    public String readURL(String link) {
        URL u = null;
        try {
            String new_link = link.replace(" ", "");
            Log.e("URL", new_link);
            u = new URL(new_link);
            URLConnection conn = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer buffer = new StringBuffer();

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                buffer.append(inputLine);

            in.close();

            return buffer.toString();
        }
        catch (Exception e) {
            Log.e("Hata",
                    "İnternet sorunu" +
                            "\n" + e.toString());
            return null;
        }
    }
    private String convertToUTF8(String s) {
        Charset.forName("UTF-8").encode(s);
        return s;
    }
}
