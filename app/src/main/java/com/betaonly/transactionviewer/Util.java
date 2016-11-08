package com.betaonly.transactionviewer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * Created by kelvinko on 6/11/2016.
 */

public class Util {
    public static String loadAssetFile(Context context, String filename) throws IOException {
        String content = null;

        InputStream is = context.getAssets().open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        content = new String(buffer, "UTF-8");

        return content;
    }


    public static String formatCurrencyString(BigDecimal amount, String currencyCode) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        Currency currency = Currency.getInstance(currencyCode);

        if (currency == null) {
            return currencyCode + " " + amount;
        } else {
            format.setCurrency(currency);
            format.setMaximumFractionDigits(AppConst.CURRENCY_DECIMAL_PLACE);
            return format.format(amount);
        }
    }
}
