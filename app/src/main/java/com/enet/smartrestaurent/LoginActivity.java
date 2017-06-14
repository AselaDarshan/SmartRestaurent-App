package com.enet.smartrestaurent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Spinner  spinner2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addItemsOnSpinner2();
    }

    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("Select your username");
        list.add("Waiter 2");
        list.add("Waiter 3");
        list.add("Waiter 1");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

    }

    public void loginButtonClicked(View v){

        GlobalState.setCurrentUsername(spinner2.getSelectedItem().toString());
        Intent intent = new Intent(this, StartingActivity.class);
        startActivity(intent);

    }
}
