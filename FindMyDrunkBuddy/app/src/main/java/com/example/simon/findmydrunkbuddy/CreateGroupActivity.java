package com.example.simon.findmydrunkbuddy;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

    private class Connector extends AsyncTask<String, Void, Boolean>{

        String groupName;
        String groupPassword;
        int duration;

        public Connector(String groupName, String groupPassword, int duration){
            this.groupName=groupName;
            this.groupPassword=groupPassword;
            this.duration=duration;
        }

        @Override
        protected void onPostExecute(Boolean b){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateGroupActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setMessage("Group created succesfully!");

            // set dialog message
            alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "insert into dbo.groups (Name, Duration, Password) values(?, ?, ?)";
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
