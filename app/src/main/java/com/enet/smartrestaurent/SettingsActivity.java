package com.enet.smartrestaurent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Settings</font>"));

        sharedPref = getSharedPreferences("pref",Context.MODE_PRIVATE);
        ((EditText)findViewById(R.id.ip_text)).setText(sharedPref.getString(getString(R.string.ip_key), "192.168.8.100"));

    }

    public void okClicked(View v){
        String ip = ((EditText)findViewById(R.id.ip_text)).getText().toString();

        sharedPref = getSharedPreferences("pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.ip_key), ip);
        editor.commit();
        editor.apply();

        finish();

    }


}
