package com.dcrdata.dcrdroidwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dcrdata.dcrdroidwidgets.R;

public class ButtonPressedHandler extends IntentHandler {

    public ButtonPressedHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle(Context context) {
        showInAllStats(context.getString(R.string.dots));

        views.setTextViewText(R.id.update_status, "");
        showProgressBar(true);
    }
}
