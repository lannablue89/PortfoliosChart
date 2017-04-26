package com.lanna.android.portfolioschart.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanna.android.portfolioschart.R;

/**
 * Created by lanna on 4/26/17.
 */

public class UiUtils {


    public static Snackbar showSnackMessage(Activity activity, String message, boolean isError) {
        return showSnackMessage(
                ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0),
                message, isError);
    }

    public static Snackbar showSnackMessage(View view, String message, boolean isError) {
        return showSnackMessage(view, message, isError, null, null);
    }

    public static Snackbar showSnackMessage(View view, String message, boolean isError,
                                            String action, View.OnClickListener listener)
    {
        Log.i("app", "showSnackMessage: " + message);
        
        Snackbar snack;
        if (action == null || listener == null) {
            snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        }
        else {
            snack = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, listener);
        }

        View snackView= snack.getView();
        snackView.setBackgroundResource(isError ? android.R.color.holo_red_dark : R.color.colorPrimaryDark);
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
        return snack;
    }
}
