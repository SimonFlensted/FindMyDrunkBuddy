package com.example.simon.findmydrunkbuddy;

import android.os.AsyncTask;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static android.R.attr.duration;

public class FindGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
    }

    public void searchGroup(View view){
        Log.d("go'daw", "jaja");
        EditText searchString = (EditText) findViewById(R.id.searchField);
        ResultSet rs = (ResultSet) new Connector(searchString.toString()).execute();

        ListView lv = (ListView) findViewById(R.id.groupList);
    }


    private class Connector extends AsyncTask {

        String searchString;

        public Connector(String searchString){
            this.searchString=searchString;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Log.d("hej", "nej");

                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "select * from dbo.groups where Name like %?%";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, searchString);
                ResultSet rs = ps.executeQuery();

                return rs;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
