package zentcode02.parks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import zentcode02.parks.services.GPSTracker;
import zentcode02.parks.utils.LocationProvider;

public class DebugFragment extends Fragment implements View.OnClickListener {

    public DebugFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.debug_fragment, container, false);

        Button btnLocation = (Button) view.findViewById(R.id.btnLocation);

        btnLocation.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLocation:
                LocationProvider.requestUpdate(getContext(), new LocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(LocationProvider.GPSCoordinates location) {
                        Log.d("Location", "my location is " + location.latitude + " - " + location.longitude);
                        Toast.makeText(getContext(), "Latitude " + location.latitude + " - Longitude " + location.longitude, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }
}
