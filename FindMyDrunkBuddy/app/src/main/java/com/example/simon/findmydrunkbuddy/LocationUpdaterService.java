package com.example.simon.findmydrunkbuddy;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static android.R.attr.duration;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationUpdaterService extends Service {

    private boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;


    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (!isRunning) {
            isRunning = true;


            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    makeUseOfNewLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                private void makeUseOfNewLocation(Location location) {
                    new SendLocationTask().execute(location.getLatitude(), location.getLongitude());

                }

                class SendLocationTask extends AsyncTask<Double, Void, Boolean> {

                    private static final String updateUserPos = "update dbo.users set Lattitude = ?, Longtitude = ? where Id = ?";


                    @Override
                    protected Boolean doInBackground(Double... params) {
                        try {
                            Class.forName("net.sourceforge.jtds.jdbc.Driver");
                            String ConnURL = getResources().getString(R.string.connectionURL);
                            Connection conn = DriverManager.getConnection(ConnURL);
                            String sql = updateUserPos;
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setDouble(1, params[0]);
                            ps.setDouble(2, params[1]);
                            ps.setInt(3, intent.getIntExtra("UserId", 0));
                            ps.execute();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        ;
                        return null;
                    }
                }

            };


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return START_STICKY;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 50, locationListener);
            return Service.START_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
