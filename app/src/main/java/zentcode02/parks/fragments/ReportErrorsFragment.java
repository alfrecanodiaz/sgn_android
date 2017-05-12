/*
package zentcode02.parks.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.ErrorsDB;
import zentcode02.parks.dbModels.SincronizarDB;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class ReportErrorsFragment extends Fragment implements View.OnClickListener {

    private TextView statusSync, regSyncOk, regSyncPending, txtCountErrors;
    private LinearLayout statusContainer, reportErrorsContainer;
    private Button btnReportErrors;
    private static final String OK_STATUS = "OK";
    private static final String ERROR_STATUS = "Error";
    private static final String email = "contacto@zentcode.com";
    private static final String subject = "Reporte SGN: Errores en la Sincronización";

    public ReportErrorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.report_errors_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Estado de Sincronización");

        statusSync = (TextView) view.findViewById(R.id.statusSync);
        regSyncOk = (TextView) view.findViewById(R.id.regSyncOk);
        regSyncPending = (TextView) view.findViewById(R.id.regSyncPending);
        txtCountErrors = (TextView) view.findViewById(R.id.txtCountErrors);
        statusContainer = (LinearLayout) view.findViewById(R.id.syncStatusContainer);
        reportErrorsContainer = (LinearLayout) view.findViewById(R.id.reportErrorsContainer);
        btnReportErrors = (Button) view.findViewById(R.id.btnReportErrors);

        btnReportErrors.setOnClickListener(this);

        fillStatusData();

        return view;
    }

    private void fillStatusData() {
        if (checkErrorsStatus() == 0 || checkSyncPendingData() == 0) {
            statusSync.setText(OK_STATUS);
            statusContainer.setBackgroundResource(R.color.btnFinalizar);
            reportErrorsContainer.setVisibility(View.GONE);
        } else {
            statusSync.setText(ERROR_STATUS);
            statusContainer.setBackgroundResource(R.color.btnEliminar);
            String label_errors = "Errores para reportar: " + String.valueOf(checkErrorsStatus());
            txtCountErrors.setText(label_errors);
            reportErrorsContainer.setVisibility(View.VISIBLE);
        }
        regSyncOk.setText(String.valueOf(checkSyncOkData()));
        regSyncPending.setText(String.valueOf(checkSyncPendingData()));
    }

    private long checkSyncPendingData() {
        return Select.from(Sincronizar.class).where(Condition.prop(NamingHelper.toSQLNameDefault("enviado")).eq("0")).count();
    }

    private long checkSyncOkData() {
        return Select.from(Sincronizar.class).where(Condition.prop(NamingHelper.toSQLNameDefault("enviado")).eq("1")).count();
    }

    private long checkErrorsStatus() {
        return Select.from(ErrorsDB.class).count();
    }

    private String errorBodyEmail() {
        List<ErrorsDB> errorsDB = ErrorsDB.listAll(ErrorsDB.class);
        return errorsDB.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReportErrors:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, errorBodyEmail());
                startActivity(Intent.createChooser(emailIntent, "Enviar Correo"));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Estado de Sincronización");
    }

}
*/
