package com.example.android.housecut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Implemented by Nick and Jose
 */

public class main_page_activity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_icon:
                //Intent intent = new Intent(main_page_activity.this, settings_activity.class);
                //startActivity(intent);
                return true;

            case R.id.grocery_list_icon:
                //Intent intent2 = new Intent(main_page_activity.this, grocery_list_activity.class);
                //startActivity(intent2);
                return true;

            case R.id.task_list_icon:
                //Intent intent3 = new Intent(main_page_activity.this, task_list_activity.class);
                //startActivity(intent3);
                return true;

            case R.id.home_page_icon:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}