package com.dcrdata.dcrdroidwidgets.intenthandlers;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dcrdata.dcrdroidwidgets.R;
import com.dcrdata.dcrdroidwidgets.intents.IntentExtras;
import com.dcrdata.dcrdroidwidgets.service.DcrData;
import com.dcrdata.dcrdroidwidgets.widget.TimeStamp;

public class DrawStatsHandler extends IntentHandler {

    public DrawStatsHandler(Intent intent, RemoteViews views) {
        super(intent, views);
    }

    @Override
    public void handle(Context context) {
        DcrData stats = (DcrData) intent.getExtras().get(IntentExtras.DCR_STATS);

        drawPriceStats(stats);
        drawStakeStats(stats);
        drawWorkStats(stats);

        views.setTextViewText(R.id.update_status, new TimeStamp().toString());
        showProgressBar(false);
    }

    private void drawPriceStats(DcrData stats) {
        views.setTextViewText(R.id.text_usd_price, stats.getUsdPrice());
        views.setTextViewText(R.id.text_btc_price, stats.getBtcPrice());
    }

    private void drawStakeStats(DcrData stats) {
        views.setTextViewText(R.id.text_ticket_price, stats.getTicketPrice());

        double ticketChange = stats.getPriceChangeInSeconds();

        views.setTextViewText(R.id.text_price_change, new ChangeTime(ticketChange).format());

        views.setTextViewText(R.id.text_est_new_price, stats.getEstNextPrice());
    }

    private void drawWorkStats(DcrData stats) {
        views.setTextViewText(R.id.text_difficulty, stats.getDifficulty());

        double networkHash = stats.getNetworkHash();

        views.setTextViewText(R.id.text_network_hash,new HashRate(networkHash).format() + "h/s" );
    }
}
