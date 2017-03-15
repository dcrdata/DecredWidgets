package com.dcrdata.dcrdroidwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dcrdata.dcrdroidwidgets.R;

public class DrawErrorHandler extends IntentHandler {

    public DrawErrorHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle(Context context) {
        showInAllStats("");

        views.setTextViewText(R.id.update_status, context.getString(R.string.offline));
        showProgressBar(false);
    }
}
