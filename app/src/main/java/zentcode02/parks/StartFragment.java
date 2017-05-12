package zentcode02.parks;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.LocationProvider;

public class StartFragment extends Fragment implements View.OnClickListener {

    private int travesia_id;
    private AlertDialog.Builder alertBuilder;

    public StartFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertBuilder = new AlertDialog.Builder(getContext());
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        travesia_id = preferenceManager.getTravesiaId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_fragment, container, false);

        String travesia_actual = "TRAVESIA ACTUAL";
        String historial = "HISTORIAL";

        TextView txtTravesia = (TextView) view.findViewById(R.id.txtStartTravesiaActual);
        TextView txtHistorial = (TextView) view.findViewById(R.id.txtStartHistorial);

        txtTravesia.setText(travesia_actual);
        txtHistorial.setText(historial);

        txtTravesia.setOnClickListener(this);
        txtHistorial.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getFragmentManager();
        alertBuilder.setTitle("Atención!");
        alertBuilder.setMessage("No se sincronizo ninguna travesia.");
        alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();

        switch (v.getId()) {
            case R.id.txtStartTravesiaActual:
                /*if (checkStatusOptions()) {
                    FormCategoriesFragment fragmentCategories = new FormCategoriesFragment();
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container, fragmentCategories)
                            .addToBackStack(null).commit();
                } else {
                  alertDialog.show();
                }*/
                break;

            /*case R.id.txtStartHistorial:
                if (checkStatusOptions()) {
                    TravesiasHistorial fragmentHistorial = new TravesiasHistorial();
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container, fragmentHistorial)
                            .addToBackStack(null).commit();
                } else {
                    alertDialog.show();
                }
                break;*/
        }
    }

    private boolean checkStatusOptions() {
        return travesia_id != 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Inicio");
    }
}
