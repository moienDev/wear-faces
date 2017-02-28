package com.forGit.watchElements;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forGit.watchElements.models.ScopesM;
import com.forGit.watchElements.models.TimeCast;

import java.util.Calendar;

import firstwatch.com.nineelements.nineelementsone.R;

public class WatchWearActivity extends Activity {
    private final static IntentFilter intentFilter;
    private boolean isDimmed = false;
    private TimeCast timeToWords;
    private ScopesM[][] scopesMArray;
    private TextView[][] textViews;

    static {
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("9elements One", "onCreate();");

        createLayout();

        timeInfoReceiver.onReceive(this, registerReceiver(null, intentFilter));
        registerReceiver(timeInfoReceiver, intentFilter);

    }

    public BroadcastReceiver timeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            Log.v("WatchFace", "timeChanged();");
            updateLayout();
        }
    };

    public void createLayout() {
        timeToWords = new TimeCast();
        timeToWords.setModelTime(Calendar.getInstance());
        scopesMArray = timeToWords.getScopesMArray();

        setContentView(R.layout.activity_nine_watch_wear);

        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        LinearLayout verticalLinearLayout = (LinearLayout)findViewById(R.id.verticalLinearLayout);

        textViews = new TextView[timeToWords.getWidth()][timeToWords.getHeight()];

        for (int i = 0; i < timeToWords.getHeight(); i++) {
            LinearLayout horizontalLinearLayout = new LinearLayout(this);

            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams hparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            hparams.weight = 1;
            horizontalLinearLayout.setLayoutParams(hparams);
            verticalLinearLayout.addView(horizontalLinearLayout);

            for (int j = 0; j < timeToWords.getWidth(); j++) {

                TextView textView = new TextView(this);
                ScopesM scopesM = scopesMArray[i][j];
                textView.setTextSize(pixels);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 1;

                textView.setLayoutParams(params);
                updateTextViewForCharacter(textView, scopesM);

                horizontalLinearLayout.addView(textView);
                textViews[j][i] = textView;

            }
        }
    }

    public void updateLayout() {
        timeToWords.setModelTime(Calendar.getInstance());
        scopesMArray = timeToWords.getScopesMArray();

        for (int i = 0; i < timeToWords.getHeight(); i++) {
            for (int j = 0; j < timeToWords.getWidth(); j++) {
                ScopesM scopesM = scopesMArray[i][j];
                TextView textView = textViews[j][i];

                updateTextViewForCharacter(textView, scopesM);
            }
        }
    }

    public void updateTextViewForCharacter(TextView textView, ScopesM scopesM) {
        textView.setText("" + scopesM.getCharacter());

        if(scopesM.isOn()) {
            textView.setTextColor(Color.parseColor("#ff0000"));
        } else {
            if(!isDimmed) {
                textView.setTextColor(Color.parseColor("#3a3a3a"));
            } else {
                textView.setTextColor(Color.parseColor("#000000"));
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v("9elements One", "onPause();");
        isDimmed = true;
        updateLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("9elements One", "onResume();");
        isDimmed = false;
        updateLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("9elements One", "onDestroy();");
        unregisterReceiver(timeInfoReceiver);
    }
}
