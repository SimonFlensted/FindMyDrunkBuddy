package com.example.simon.findmydrunkbuddy;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    EditText password;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_COARSE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.loginUsername);
        password = (EditText) findViewById(R.id.loginPassword);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_COARSE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSIONS_REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void login(View view) {
        new LoginTask().execute(userName.getText().toString(), password.getText().toString());
    }

    public void register(View view){
        new RegisterTask().execute(userName.getText().toString(), password.getText().toString());
    }


    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        private String username;
        private String password;
        private int id;

        @Override
        protected void onPostExecute(Boolean b){
            if(b){
                SQLiteOpenHelper databaseHelper = new LocalDatabase(LoginActivity.this);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                LocalDatabase.deletePrevious(db);

                LocalDatabase.insertUser(db, username, password, id);

                Intent locationIntent = new Intent(LoginActivity.this, LocationUpdaterService.class);
                locationIntent.putExtra("UserId", id);


                startService(locationIntent);

                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }

            if(!b){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setMessage("Wrong password or username");

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
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                Connection conn = DriverManager.getConnection(ConnURL);
                String sql = "select * from dbo.users where Username = ? AND Password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, params[0]);
                ps.setString(2, params[1]);

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    username = rs.getString("Username");
                    password = rs.getString("Password");
                    id = rs.getInt("Id");
                    return true;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean b){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setMessage("Registration succesful!");

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
                String sql = "insert into dbo.users (Username, Password) values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, params[0]);
                ps.setString(2, params[1]);
                ps.execute();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }
    }
}
