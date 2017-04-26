package com.lanna.android.portfolioschart.domain;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lanna.android.portfolioschart.model.PcPortfolio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by lanna on 4/26/17.
 */

public class DataService {

    private Context context;

    public DataService(Context context) {
        this.context = context;
    }

    public Observable<List<PcPortfolio>> getListPortfolio() {

        return Observable.defer(new Callable<ObservableSource<List<PcPortfolio>>>() {
            @Override
            public ObservableSource<List<PcPortfolio>> call() throws Exception {
                StringBuilder buf = new StringBuilder();
                BufferedReader in = null;
                try {
                    InputStream is = context.getAssets().open("data_json.json");
                    in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String str;

                    while ((str = in.readLine()) != null) {
                        buf.append(str);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (in != null) {
                        in.close();
                    }
                    return Observable.error(new IOException("Error: " + e.getMessage()));
                }

                if (buf.length() > 0) {
                    Type listType = new TypeToken<List<PcPortfolio>>(){}.getType();
                    List<PcPortfolio> portfolios = new Gson().fromJson(buf.toString(), listType);
                    return Observable.just(portfolios);
                }

                return Observable.error(new IOException("Could not read data"));
            }
        });
    }
}
