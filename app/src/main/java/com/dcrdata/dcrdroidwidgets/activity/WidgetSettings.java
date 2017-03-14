package com.jamieholdstock.dcrwidgets.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.jamieholdstock.dcrwidgets.R;
import com.jamieholdstock.dcrwidgets.intents.MyIntents;
import com.jamieholdstock.dcrwidgets.widget.DcrWidget;
import com.jamieholdstock.dcrwidgets.widget.WidgetType;

import static com.jamieholdstock.dcrwidgets.widget.WidgetType.*;

public class WidgetSettings extends AppCompatActivity {

    private static final String PREFS_NAME = "com.jamieholdstock.dcrwidgets.activity.WidgetSettings";
    private static final String PREF_PREFIX_KEY = "widget_type";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public WidgetSettings() {
        super();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.dcr_settings_layout);

        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetSettings.this;

            RadioButton allRadio = (RadioButton)findViewById(R.id.allRadioBtn);
            RadioButton priceRadio = (RadioButton)findViewById(R.id.salePriceBtn);
            RadioButton stakeRadio = (RadioButton)findViewById(R.id.stakeInfoBtn);
            WidgetType widgetType;
            if (allRadio.isChecked()) {
                widgetType = ALL;
            }
            else if (priceRadio.isChecked()) {
                widgetType = PRICE;
            }
            else if (stakeRadio.isChecked()) {
                widgetType = STAKE;
            }
            else {
                widgetType = WORK;
            }

            saveWidgetType(context, mAppWidgetId, widgetType);

            Intent intent = new Intent(context, DcrWidget.class);
            intent.setAction(MyIntents.BUTTON_PRESSED);
            context.sendBroadcast(intent);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public static void saveWidgetType(Context context, int appWidgetId, WidgetType widgetType) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, widgetType.ordinal());
        prefs.commit();
    }

    public static WidgetType loadWidgetType(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int ordinal = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        return WidgetType.values()[ordinal];
    }
}
