package com.betaonly.transactionviewer.repos;

import android.content.Context;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.Util;
import com.betaonly.transactionviewer.currency.CurrencyPair;
import com.betaonly.transactionviewer.model.Rate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelvinko on 7/11/2016.
 */

public class RateDataSource {

    private static RateDataSource sInstance = new RateDataSource();
    private Gson mGson = new Gson();
    private List<CurrencyPair> mRates;
    private RateDataSource() {
        // empty private constructor
    }

    public static RateDataSource getInstance() {
        return sInstance;
    }

    public List<CurrencyPair> getRates(Context context) {
        if (mRates == null) {
            mRates = new ArrayList<>();
            Rate[] rates = readRateFromFile(context);
            for(Rate rate : rates) {
                mRates.add(new CurrencyPair(rate.getFrom(), rate.getTo(), rate.getRate()));
            }
        }
        return mRates;
    }

    private Rate[] readRateFromFile(Context context) {
        String content = Util.loadAssetFile(context, AppConst.DEFAULT_RATE_FILE);
        Rate[] rates = mGson.fromJson(content, Rate[].class);
        return rates;
    }
}
