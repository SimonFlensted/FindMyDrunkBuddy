package com.example.simon.findmydrunkbuddy;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void createGroup(View view) throws SQLException, ClassNotFoundException {
        EditText groupName = (EditText) findViewById(R.id.groupNameField);
        EditText groupPassword = (EditText) findViewById(R.id.groupPasswordField);
        EditText duration = (EditText) findViewById(R.id.durationField);

        new Connector(groupName.getText().toString(), groupPassword.getText().toString(), Integer.parseInt(duration.getText().toString())).execute();
    }

    private class Connector extends AsyncTask{

        String groupName;
        String groupPassword;
        int duration;

        public Connector(String groupName, String groupPassword, int duration){
            this.groupName=groupName;
            this.groupPassword=groupPassword;
            this.duration=duration;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "insert into dbo.groups values (20, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, groupName);
                ps.setInt(2, duration);
                ps.setString(3, groupPassword);
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
