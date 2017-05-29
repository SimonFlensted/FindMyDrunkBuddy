package com.example.simon.findmydrunkbuddy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.util.LogWriter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.ExecutionException;

import static android.R.attr.duration;

public class FindGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
    }

    public void searchGroup(View view){

        EditText searchString = (EditText) findViewById(R.id.searchField);
        //GroupAdapter itemsAdapter = null;
        new Connector().execute(searchString.getText().toString());
    }


    private class Connector extends AsyncTask<String, Void, List<ListItem>> {

        private ListView lv = (ListView) findViewById(R.id.groupList);
        private GroupAdapter itemsAdapter;
        private static final String selectFromUserData = "select * from USERDATA";
        private static final String selectFromGroup = "select * from dbo.groups where Name like ?";

        protected void onPostExecute(List<ListItem> items) {
            itemsAdapter = new GroupAdapter(items);
            lv.setAdapter(itemsAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ListItem item = (ListItem) itemsAdapter.getItem(position);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FindGroupActivity.this);

                    final EditText et = new EditText(FindGroupActivity.this);
                    et.setHint("Insert group password");

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(et);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(item.getPassword().replace(" ", "").equals(et.getText().toString())){
                                LocalDatabase helper = new LocalDatabase(FindGroupActivity.this);
                                SQLiteDatabase db = helper.getReadableDatabase();

                                Cursor cursor = db.rawQuery(selectFromUserData, null);

                                if (cursor.moveToFirst()) {
                                    new insertUserToGroup().execute(cursor.getInt(1), item.getId());
                                }
                            }
                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });
        }


        @Override
        protected List<ListItem> doInBackground(String... searchString) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = getResources().getString(R.string.connectionURL);
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = selectFromGroup;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + searchString[0] + "%");
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

        private class insertUserToGroup extends AsyncTask<Integer, Void, Void>{

            private static final String insertIntoUserGroup = "insert into dbo.userToGroup values (?, ?)";

            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    String ConnURL = getResources().getString(R.string.connectionURL);
                    Connection conn = DriverManager.getConnection(ConnURL);
                    String sql = insertIntoUserGroup;
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, params[0]);
                    ps.setInt(2, params[1]);
                    ps.execute();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }
    }
}
