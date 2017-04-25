package com.example.simon.findmydrunkbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void createGroup(View view){
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    public void findGroup(View view){
        Intent intent = new Intent(this, FindGroupActivity.class);
        startActivity(intent);
    }

    public void myGroups(View view){
        Intent intent = new Intent(this, MyGroupsActivity.class);
        startActivity(intent);
    }


}
