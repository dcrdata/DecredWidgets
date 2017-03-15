package com.dcrdata.dcrdroidwidgets.service;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dcrdata.dcrdroidwidgets.L;
import com.dcrdata.dcrdroidwidgets.intents.IntentExtras;
import com.dcrdata.dcrdroidwidgets.intents.MyIntents;
import com.dcrdata.dcrdroidwidgets.widget.DcrWidget;

public class DcrDataService extends IntentService {
    private final static DcrDataUrl DCR_STATS_URL = new DcrDataUrl("https://dcrdata.org");
    //private final static DcrDataUrl DCR_STATS_URL = new DcrDataUrl("http://10.0.2.2:8090");

    private final static int TIMEOUT_MS = 3000;
    private final static int MAX_RETRIES = 0;

    public DcrDataService() {
        super("DcrDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(MyIntents.GET_STATS))
        {
            getStats();
        }
    }

    public void sendErrorToWidget() {
        Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
        i.setAction(MyIntents.DRAW_ERROR);
        this.getApplicationContext().sendBroadcast(i);
    }

    public void sendStatsToWidget(DcrData stats) {
        Intent i = new Intent(this.getApplicationContext(), DcrWidget.class);
        i.setAction(MyIntents.DRAW_STATS);
        i.putExtra(IntentExtras.DCR_STATS, stats);

        this.getApplicationContext().sendBroadcast(i);
    }

    public void getStats() {
        RequestQueue queue = Volley.newRequestQueue(this);

        L.l("Service sending GET to " + DCR_STATS_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DCR_STATS_URL.toString(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    L.l("Service received non-error response\n" + response);
                    DcrData stats = new DcrData(response);
                    sendStatsToWidget(stats);
                }
            },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorToWidget();
                L.l("Service received error response");
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}
