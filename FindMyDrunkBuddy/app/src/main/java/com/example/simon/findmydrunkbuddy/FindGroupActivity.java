package com.example.simon.findmydrunkbuddy;

import android.os.AsyncTask;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        ArrayAdapter<ListItem> itemsAdapter = null;
        new Connector("%" + searchString.toString() + "%").execute();
    }


    private class Connector extends AsyncTask {

        String searchString;

        public Connector(String searchString){
            this.searchString=searchString;
        }

        protected void onPostExecute(List<ListItem> items) {
            ArrayAdapter<ListItem> itemsAdapter = new ArrayAdapter<ListItem>(FindGroupActivity.this, android.R.layout.simple_list_item_1, items);
            ListView lv = (ListView) findViewById(R.id.groupList);
            lv.setAdapter(itemsAdapter);
        }

        @Override
        protected List<ListItem> doInBackground(Object[] params) {
            try {
                Log.d("hej", "nej");

                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "select * from dbo.groups where Name like ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, searchString);
                ResultSet rs = ps.executeQuery();

                List<ListItem> items = new LinkedList<ListItem>();

                while(rs.next()){
                    ListItem li = new ListItem(rs.getInt("Id"), rs.getString("Name"), rs.getInt("Duration"), rs.getString("Password"));
                    items.add(li);
                    Log.d(li.getName(), li.getPassword());
                }

                return items;


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
