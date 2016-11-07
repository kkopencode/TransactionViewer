package com.betaonly.transactionviewer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class Util {
    public static String loadAssetFile(Context context, String filename) {
        String content = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
