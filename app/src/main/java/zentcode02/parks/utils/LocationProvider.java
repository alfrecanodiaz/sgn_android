package zentcode02.parks.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationProvider {

    private static LocationManager locationManager;

    public interface LocationCallback {
        void onNewLocationAvailable(GPSCoordinates location);
    }

    @SuppressWarnings("MissingPermission")
    public static void requestUpdate(final Context context, final LocationCallback callback) throws SecurityException {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (isNetworkEnabled()) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override public void onProviderEnabled(String provider) { }
                @Override public void onProviderDisabled(String provider) { }
            }, null);
        } else {
            if (isGPSEnabled()) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                    }
                    @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                    @Override public void onProviderEnabled(String provider) { }
                    @Override public void onProviderDisabled(String provider) { }
                }, null);
            }
        }
    }

    private static boolean isNetworkEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private static boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }
}
