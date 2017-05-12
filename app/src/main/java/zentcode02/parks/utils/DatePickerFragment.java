package zentcode02.parks.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import zentcode02.parks.R;

/**
 * Created by zentcode02 on 08/02/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d("year ", String.valueOf(year) + " / month "+ String.valueOf(month) + " / day "+ String.valueOf(day) );
        String anho = String.valueOf(year);
        int month_calc = month+1;
        String mes = month_calc/10==0?("0"+month_calc):String.valueOf(month_calc);
        String dia;
        //Los meses cuentan del 0 al 11
//        if(month < 9){
//            if (month != 1) {
//                mes = "0" + String.valueOf(month)+1;
//            } else {
//                mes = "0" + String.valueOf(month);
//            }
//        } else {
//            mes = String.valueOf(month);
//        }
        if(day < 10){
            dia  = "0" + String.valueOf(day);
        } else {
            dia = String.valueOf(day);
        }

        String fechaCabecera = dia + "-"+ mes + "-" + anho;
//        TextView textView = (TextView) getActivity().findViewById(R.id.txtFechaCabecera);
//        textView.setText(view.getDayOfMonth() + "/"+ view.getMonth() + "/" + view.getYear());
//        textView.setText(fechaCabecera);

//        String fechaCabecera = anho + "-"+ mes + "-" + dia;
        // in this part I stored the selected date into the intent
        Intent intent = new Intent();
        intent.putExtra("selectedDate",fechaCabecera);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

}
