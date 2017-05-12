package zentcode02.parks.utils;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;


import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.ItemCheck;
import zentcode02.parks.dbModels.Sincronizar;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRScanner extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private int item_id;
    private ItemCheck itemCheck;
    private QRCodeReaderView qrCodeReaderView;
    /*private AlertDialog.Builder builder;
    private AlertDialog alertDialog;*/
    private String title, message;

    public QRScanner() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        //GET ARGS FRAGMENT
        item_id = getArguments().getInt("item_id");
        //Query tabla Items Check
        queryItemChecks(item_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qrscanner_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Verificación QR");

        //alertDialog = new AlertDialog.Builder(getActivity());
        /*builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setCancelable(false);*/

        qrCodeReaderView = (QRCodeReaderView) view.findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        initQRCodeReaderView();

        return view;
    }

    private void queryItemChecks(int item_id) {
        itemCheck = ItemCheck.findById(ItemCheck.class, item_id);
        Log.d("item query", itemCheck.getNombre());
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.d("qr decode result", text);
        qrCodeReaderView.stopCamera();
        if (text != null) {
            if (qrCodeValidate(text)) {
                qrCodeSaveDatabase();
                title = "Scaneo de codigo QR correcto!";
                message = "El codigo QR es correcto.";
                redirectBack(title, message);
                /*builder.setTitle("Scaneo de codigo QR correcto!");
                builder.setMessage("El codigo QR es correcto.");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redirectBack();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();*/
            } else {
                title = "Error!";
                message = "El codigo QR no coincide. Intente de nuevo.";
                redirectBack(title, message);
                /*builder.setTitle("Error!");
                builder.setMessage("El codigo QR no coincide. Intente de nuevo.");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redirectBack();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();*/
            }
        } else {
            title = "Error al scanear el codigo QR!";
            message = "No se obtuvieron datos del codigo QR. Intente de nuevo..";
            redirectBack(title, message);
            /*builder.setTitle("Error al scanear el codigo QR!");
            builder.setMessage("No se obtuvieron datos del codigo QR. Intente de nuevo..");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    redirectBack();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    private void initQRCodeReaderView() {
        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);
        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);
        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);
        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();
        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.startCamera();
    }

    private boolean qrCodeValidate(String result_qr) {
        return Objects.equals(itemCheck.getQr(), result_qr);
    }

    private void qrCodeSaveDatabase() {
        Log.d("item checks", itemCheck.getNombre());
        itemCheck.setQr_status(1);
        itemCheck.save();
        Sincronizar sincronizar = new Sincronizar(
                "items_check",
                itemCheck.getId(),
                "update",
                false,
                0
        );
        sincronizar.save();
    }

    private void redirectBack(String title, String message) {
        qrCodeReaderView.stopCamera();
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStackImmediate();
                        //redirectFragment();
                    }
                }).setCancelable(false).show();
        /*alertDialog.dismiss();
        final ProgressDialog prg = new ProgressDialog(getActivity());
        prg.setMessage("Redireccionando...");
        prg.setCancelable(false);
        prg.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                prg.dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        }, 1500);*/
    }

    /*private void redirectFragment() {
        final ProgressDialog prg = new ProgressDialog(getActivity());
        prg.setMessage("Redireccionando...");
        prg.setCancelable(false);
        prg.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                prg.dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        }, 1500);
    }*/

}
