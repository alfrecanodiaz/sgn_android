package zentcode02.parks.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zentcode02 on 23/01/17.
 */

public class GPSTracker extends Service implements LocationListener {

    private Activity activity;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private Location location;

    private double latitude;
    private double longitude;

//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
//    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;

    private int count = 0;

    protected LocationManager locationManager;

    private AlertDialog.Builder alertDialog;

    public GPSTracker() {}

    public GPSTracker(Activity activity) {
        this.activity = activity;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                this.canGetLocation = false;

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }

                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this);
                    }

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS() {
        locationManager.removeUpdates(GPSTracker.this);
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setTitle("Se esta configurando el GPS");
        alertDialog.setMessage("El GPS no esta activado. Active el GPS.");

        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }

    public void checkGpsEnabled() {
        if (!isGpsEnabled()) {
            runGpsFinder();
        } else {
            loopServiceGps();
            Log.d("gps activado", "buscando la localizacion");
        }
    }

    public void runGpsFinder() {
        if (!isGPSEnabled) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("Buscando gps", "servicio gps finder");
                    checkGpsEnabled();
                }
            }, 1000);
        } else {
            loopServiceGps();
        }
    }

    private void loopServiceGps() {
        count = count +1;
        if (count < 10) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocation();
                    System.out.println("Localización GPS " + getLatitude() + " " + getLongitude());
                    checkGpsEnabled();
                }
            }, 1000);
        }
    }

//    public void checkGpsEnabled() {
//        if (!isGpsEnabled()) {
//            alertRunGpsDisabled();
//        }
//    }
//
//    public void alertRunGpsDisabled() {
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                alertDialog = new AlertDialog.Builder(activity);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//                alertDialog.setTitle("Se esta configurando el GPS");
//                alertDialog.setMessage("El GPS no esta activado. Active el GPS.");
//                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        checkGpsEnabled();
//                    }
//                });
//                alertDialog.show();
//            }
//        }, 7000);
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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

    public boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE );
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
