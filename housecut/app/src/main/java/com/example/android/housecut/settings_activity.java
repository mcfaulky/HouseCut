package com.example.android.housecut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by nickjohnson on 11/18/16.
 */

public class settings_activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
                Intent intent = new Intent(settings_activity.this, settings_activity.class);
                startActivity(intent);
                return true;

            case R.id.grocery_list_icon:
                Intent intent2 = new Intent(settings_activity.this, grocery_list_activity.class);
                startActivity(intent2);
                return true;

            case R.id.task_list_icon:
                Intent intent3 = new Intent(settings_activity.this, task_list_activity.class);
                startActivity(intent3);
                return true;

            case R.id.home_page_icon:
                Intent intent4 = new Intent(settings_activity.this, task_list_activity.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
