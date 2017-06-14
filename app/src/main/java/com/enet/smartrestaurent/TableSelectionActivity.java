package com.enet.smartrestaurent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class TableSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_selection);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_table_selection);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=#8B0000>Silver Ring Village Hotel</font>"));
    }

    public void tableSelected(View v){
        Log.d("Table",((Button)v).getText().toString());
        Intent intent = new Intent(this, menuActivity.class);

//        String message = editText.getText().toString();
        intent.putExtra("TABLE_ID", ((Button)v).getText().toString());
        startActivity(intent);
    }

}
