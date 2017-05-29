package com.example.simon.findmydrunkbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class MyGroupsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        new Connector().execute();
    }

    private class Connector extends AsyncTask<String, Void, List<ListItem>> {

        private ListView lv = (ListView) findViewById(R.id.myGroups);
        private ArrayAdapter<ListItem> itemsAdapter;
        private static final String selectFromUserGroup = "select * from dbo.userToGroup utg join dbo.groups g on utg.groupId = g.Id where userId = ?";

        protected void onPostExecute(List<ListItem> items) {
            itemsAdapter = new ArrayAdapter<ListItem>(MyGroupsActivity.this, android.R.layout.simple_list_item_1, items);
            lv.setAdapter(itemsAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ListItem item = itemsAdapter.getItem(position);
                    Intent intent = new Intent(MyGroupsActivity.this, GroupActivity.class);
                    intent.putExtra("groupId", item.getId());
                    startActivity(intent);
                }
            });
        }


        @Override
        protected List<ListItem> doInBackground(String... searchString) {
            try {
                SQLiteOpenHelper helper = new LocalDatabase(MyGroupsActivity.this);
                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor cursor = db.rawQuery("select * from USERDATA", null);

                int userId = 0;

                if (cursor.moveToFirst()) {
                    userId = cursor.getInt(1);
                }

                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = getResources().getString(R.string.connectionURL);
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = selectFromUserGroup;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                List<ListItem> items = new LinkedList<ListItem>();

                while(rs.next()){
                    ListItem li = new ListItem(rs.getInt("Id"), rs.getString("Name"), rs.getInt("Duration"), rs.getString("Password"));
                    items.add(li);
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
