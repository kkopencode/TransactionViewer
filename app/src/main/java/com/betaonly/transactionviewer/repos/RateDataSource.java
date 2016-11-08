package com.betaonly.transactionviewer.repos;

import android.content.Context;

import com.betaonly.transactionviewer.AppConst;
import com.betaonly.transactionviewer.utils.Util;
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
    private boolean inited = false;
    private RateDataSource() {
        // empty private constructor
    }

    public static RateDataSource getInstance() {
        return sInstance;
    }

    public void init(Context context) throws Exception {
        if (!inited) {
            mRates = new ArrayList<>();
            Rate[] rates = readRateFromFile(context);
            for(Rate rate : rates) {
                mRates.add(new CurrencyPair(rate.getFrom().toUpperCase(),
                        rate.getTo().toUpperCase(),
                        rate.getRate()));
            }
            inited = true;
        }
    }

    public List<CurrencyPair> getRates() {
        return mRates;
    }

    // TODO: better exception handling, currently just report unable to load the data on any error
    private Rate[] readRateFromFile(Context context) throws Exception {
        String content = Util.loadAssetFile(context, AppConst.DEFAULT_RATE_FILE);
        Rate[] rates;
        rates = mGson.fromJson(content, Rate[].class);

        return rates;
    }
}
