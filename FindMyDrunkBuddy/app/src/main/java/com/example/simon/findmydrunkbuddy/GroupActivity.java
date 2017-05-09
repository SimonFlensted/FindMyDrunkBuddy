package com.example.simon.findmydrunkbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();

        new Connector().execute(intent.getIntExtra("groupId", 0));
    }

    public void findAllMembers(View view){
        new GetAllUserLocations().execute(getIntent().getIntExtra("groupId", 0));
    }

    private class Connector extends AsyncTask<Integer, Void, List<User>> {

        private ListView lv = (ListView) findViewById(R.id.groupMembers);
        private ArrayAdapter<User> itemsAdapter;

        protected void onPostExecute(List<User> users) {
            itemsAdapter = new ArrayAdapter<User>(GroupActivity.this, android.R.layout.simple_list_item_1, users);
            lv.setAdapter(itemsAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final User user = itemsAdapter.getItem(position);
                    new GetUserLocation().execute(user.getId());
                }
            });
        }


        @Override
        protected List<User> doInBackground(Integer... GroupId) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "select * from dbo.userToGroup utg join dbo.users u on utg.userId = u.Id where groupId = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, GroupId[0]);
                ResultSet rs = ps.executeQuery();

                List<User> users = new LinkedList<User>();

                while(rs.next()){
                    User user = new User(rs.getInt("Id"), rs.getString("Username"), rs.getFloat("Lattitude"), rs.getFloat("Longtitude"));
                    users.add(user);
                }

                return users;


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private class GetAllUserLocations extends AsyncTask<Integer, Void, ArrayList<User>> {

        @Override
        protected void onPostExecute(ArrayList<User> users) {
                Intent intent = new Intent(GroupActivity.this, MapsActivity.class);
                intent.putExtra("Users", users);
                startActivity(intent);
        }

        @Override
        protected ArrayList<User> doInBackground(Integer... GroupId) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "select * from dbo.userToGroup join dbo.users on UserId = Id where GroupId = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, GroupId[0]);
                ResultSet rs = ps.executeQuery();

                ArrayList<User> users = new ArrayList<>();

                while(rs.next()){
                    users.add(new User(rs.getInt("Id"), rs.getString("Username"), rs.getFloat("Lattitude"), rs.getFloat("Longtitude")));
                }
                return users;


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private class GetUserLocation extends AsyncTask<Integer, Void, ArrayList<User>> {

        private static final String selectFromUsers = "select * from dbo.users where Id = ?";

        @Override
        protected void onPostExecute(ArrayList<User> user) {
            Intent intent = new Intent(GroupActivity.this, MapsActivity.class);
            intent.putExtra("Users", user);
            startActivity(intent);
        }

        @Override
        protected ArrayList<User> doInBackground(Integer... UserId) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = getResources().getString(R.string.connectionURL);
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = selectFromUsers;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, UserId[0]);
                ResultSet rs = ps.executeQuery();

                ArrayList<User> user = new ArrayList<>();

                while(rs.next()){
                    user.add(new User(rs.getInt("Id"), rs.getString("Username"), rs.getFloat("Lattitude"), rs.getFloat("Longtitude")));
                }
                return user;

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
