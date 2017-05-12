package zentcode02.parks.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import zentcode02.parks.dbModels.ListAreaCompra;
import zentcode02.parks.dbModels.ListAreaMantenimiento;
import zentcode02.parks.models.StaticItems;

public class Global {

    private Context context;

    public Global(Context context) {
        this.context = context;
    }

    public String getCurrentVersion() {
        return StaticItems.Versions[StaticItems.Versions.length-1];
    }

    /*public String MySqlDateTimeFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(c.getTime());
    }

    public String MySqlDateFormat() {
        Calendar cc = Calendar.getInstance();
        int year=cc.get(Calendar.YEAR);
        int month=cc.get(Calendar.MONTH);
        int day = cc.get(Calendar.DAY_OF_MONTH);
        return formatDate(year, month, day);
    }

    public String formatDate(int year, int month, int day) {
        String anho = String.valueOf(year);
        String dia;
        int month_calc = month+1;
        String mes = month_calc/10==0?("0"+month_calc):String.valueOf(month_calc);
        //Los meses cuentan del 0 al 11
        if(day < 10){
            dia  = "0" + String.valueOf(day);
        } else {
            dia = String.valueOf(day);
        }
        return dia + "-"+ mes + "-" + anho;
    }*/

    public String[] getListAreaCompra() {
        List<ListAreaCompra> list_area = ListAreaCompra.listAll(ListAreaCompra.class);
        List<String> list_string = new ArrayList<>();
        list_string.add("Seleccione el area");
        for (int i = 0; i < list_area.size(); i++) {
            list_string.add(list_area.get(i).getArea());
        }
        return list_string.toArray(new String[0]);
    }

    public String[] getListAreaMantenimiento() {
        List<ListAreaMantenimiento> list_area = ListAreaMantenimiento.listAll(ListAreaMantenimiento.class);
        List<String> list_string = new ArrayList<>();
        list_string.add("Seleccione el area");
        for (int i = 0; i < list_area.size(); i++) {
            list_string.add(list_area.get(i).getArea());
        }
        return list_string.toArray(new String[0]);
    }

}
