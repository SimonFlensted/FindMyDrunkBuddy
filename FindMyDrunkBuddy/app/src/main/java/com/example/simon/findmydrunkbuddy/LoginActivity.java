package com.example.simon.findmydrunkbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.loginUsername);
        password = (EditText) findViewById(R.id.loginPassword);
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

                LocalDatabase.insertUser(db, username, password, id);

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

            return true;
        }
    }
}
