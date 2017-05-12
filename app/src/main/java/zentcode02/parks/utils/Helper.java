package zentcode02.parks.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.show();
        return pDialog;
    }

    public static void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static String getMySqlDateTimeFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(c.getTime());
    }

    public static String getMySqlDateFormat() {
        Calendar cc = Calendar.getInstance();
        int year=cc.get(Calendar.YEAR);
        int month=cc.get(Calendar.MONTH);
        int day = cc.get(Calendar.DAY_OF_MONTH);
        return formatDate(year, month, day);
    }

    private static String formatDate(int year, int month, int day) {
        String anho = String.valueOf(year);
        String dia;
        int month_calc = month+1;
        String mes = month_calc/10==0?("0"+month_calc):String.valueOf(month_calc);
        if(day < 10){
            dia  = "0" + String.valueOf(day);
        } else {
            dia = String.valueOf(day);
        }
        return dia + "-"+ mes + "-" + anho;
    }
}