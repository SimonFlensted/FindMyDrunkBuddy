package com.example.simon.findmydrunkbuddy;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
public class LocationUpdaterService extends IntentService {

    public LocationUpdaterService() {
        super("LocationUpdaterService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d("service", "knep");
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
            Connection conn = DriverManager.getConnection(ConnURL);
            String sql = "update dbo.users set Lattitude = ?, Longtitude = ? where userId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, 2);
            ps.setDouble(2, 3);
            ps.setInt(3, intent.getIntExtra("UserId", 0));
            ps.execute();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            private void makeUseOfNewLocation(Location location) {
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    String ConnURL = "jdbc:jtds:sqlserver://findmymate.can4eqtlkgly.eu-central-1.rds.amazonaws.com:1433/findMyMate;user=lasif;password=findMyProj";
                    Connection conn = DriverManager.getConnection(ConnURL);
                    String sql = "update dbo.users set Lattitude = ?, Longtitude = ? where userId = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setDouble(1, location.getLatitude());
                    ps.setDouble(2, location.getLongitude());
                    ps.setInt(3, intent.getIntExtra("UserId", 0));
                    ps.execute();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);



    }
}
