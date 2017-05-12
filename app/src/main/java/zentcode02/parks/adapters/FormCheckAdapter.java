/*
package zentcode02.parks.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.ItemsChecksDB;
import zentcode02.parks.dbModels.SincronizarDB;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.GlobalMethods;
import zentcode02.parks.utils.ImageUpload;
import zentcode02.parks.models.Items;
import zentcode02.parks.services.GPSTracker;
import zentcode02.parks.utils.QRScanner;

*/
/**
 * Created by zentcode02 on 06/01/17.
 *//*


public class FormCheckAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private List<Items> formCheckList;
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    private GPSTracker gps;
    private ArrayList<String> gps_location;
    private ProgressDialog prg;
    private FragmentManager fragmentManager;
    private ListView listView;
    private int required_errors;
    private String item_required_error;
    private AlertDialog.Builder alert, alertRequired;
    private Sincronizar sincronizarDB;
    private int loading_time;
    private PreferenceManager preferenceManager;
    private Global globalMethods;
//    private Drawable pointsDrawable;

    private int type;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    public FormCheckAdapter(Activity activity, FragmentManager fragmentManager, List<Items> items, ListView listView) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.formCheckList = items;
        this.listView = listView;
    }

    @Override
    public int getItemViewType(int position) {
        if (formCheckList.get(position).getIsHeader() == 0) {
            type = TYPE_ITEM;
        } else if (formCheckList.get(position).getIsHeader() == 1) {
            type = TYPE_SEPARATOR;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {

        return formCheckList.size();
    }

    @Override
    public Object getItem(int position) {

        return formCheckList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        final Items itemsDB = formCheckList.get(position);
//        Calendar c = Calendar.getInstance();
//        DateFormat df = DateFormat.getDateTimeInstance();
        //final String fecha_app = MySqlDateTimeFormat();
        Log.d("items en la lista", itemsDB.toString());
        Log.d("id item", String.valueOf( itemsDB.getItem_check_id()));
        final ItemCheck itemCheck = ItemCheck.findById(ItemCheck.class, itemsDB.getItem_check_id());

        System.out.print("position listview " + position);

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            */
/*pointsDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_more_horiz_black_24dp);
            pointsDrawable = DrawableCompat.wrap(pointsDrawable);*//*

            holder = new ViewHolder();
            gps = new GPSTracker(activity);
            preferenceManager = new PreferenceManager(activity);
            globalMethods = new Global(activity.getApplicationContext());
            //Alert Dialog con el Edit Text de Observacion
            alert = new AlertDialog.Builder(activity);
            //AlertBuilder Requeridos
            alertRequired = new AlertDialog.Builder(activity);
            //Progress Dialog GPs
            prg = new ProgressDialog(activity);

            switch (type) {
                case TYPE_SEPARATOR:
                    //inflate the new layout
                    convertView = inflater.inflate(R.layout.layout_txtlist, null);
                    holder.tituloCheck = (TextView) convertView.findViewById(R.id.txtList);
                    break;
                case TYPE_ITEM:
                    //inflate the new layout
                    convertView = inflater.inflate(R.layout.layout_checktab2, null);
                    holder.descripcionCheck = (TextView) convertView.findViewById(R.id.descripcionCheck);
                    holder.rgCheck = (RadioGroup) convertView.findViewById(R.id.rgCheck);
                    holder.rbSi = (RadioButton) convertView.findViewById(R.id.rbSi);
                    holder.rbNo = (RadioButton) convertView.findViewById(R.id.rbNo);
                    holder.btnObservacion = (Button) convertView.findViewById(R.id.btnObservacion);
                    */
/*holder.btnGps = (Button) convertView.findViewById(R.id.btnGps);*//*

                    holder.btnImg = (Button) convertView.findViewById(R.id.btnImg);
                    holder.btnQr = (Button) convertView.findViewById(R.id.btnQr);
                    holder.btnHideShowRow = (Button) convertView.findViewById(R.id.btnHideShowCheck);
                    holder.btnOk = (Button) convertView.findViewById(R.id.includeCheckTab2).findViewById(R.id.okButton);
                    holder.btnNext = (Button) convertView.findViewById(R.id.includeCheckTab2).findViewById(R.id.nextButton);
                    holder.btnNext_container = (LinearLayout) convertView.findViewById(R.id.includeCheckTab2).findViewById(R.id.btnNext_container);
                    holder.tableRow = (TableRow) convertView.findViewById(R.id.rowCheck2);
                    holder.tableRow3 = (TableRow) convertView.findViewById(R.id.rowCheck3);

                    break;
            }

            assert convertView != null;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (type) {
            case TYPE_SEPARATOR:

                holder.tituloCheck.setText(itemsDB.getSeccion_check_nombre());
                holder.tituloCheck.setPadding(20, 10, 20, 10);

                break;
            case TYPE_ITEM:

                //holder.btnNext.setVisibility(View.GONE);
                holder.btnNext_container.setVisibility(View.GONE);

                String descripcionPreCheck = itemsDB.getItem_check_nombre();
                holder.descripcionCheck.setText(descripcionPreCheck.replace("[", "").replace("]", ""));
                //Ocultar fila de Botones
                holder.tableRow.setVisibility(itemsDB.visibility);

                holder.tableRow3.setVisibility(View.GONE);
                if (position == formCheckList.size()-1) {
                    holder.tableRow3.setVisibility(View.VISIBLE);
                } else {
                    holder.tableRow3.setVisibility(View.GONE);
                }

                //Check Imagen habilitada, imagen requerida
                holder.btnImg.setTextColor(itemsDB.getButton_color_img());
                holder.btnImg.setClickable(itemsDB.isImageEnabled());
                */
/*holder.btnGps.setTextColor(itemsDB.getButton_color_gps());
                holder.btnGps.setClickable(itemsDB.isGpsEnabled());*//*

                holder.btnQr.setTextColor(itemsDB.getButton_color_qr());
                holder.btnQr.setClickable(itemsDB.isQrEnabled());

                holder.btnHideShowRow.setTextColor(itemsDB.getButton_hide_show_color());
                holder.btnHideShowRow.setCompoundDrawablesWithIntrinsicBounds(null, itemsDB.getButton_hide_show_drawable(), null, null);
                //DrawableCompat.setTint(pointsDrawable, itemsDB.getButton_hide_show_color())

                holder.rgCheck.setOnCheckedChangeListener(null);
                holder.rgCheck.clearCheck();

                if (mSpCheckedState.indexOfKey(position) > -1) {
                    holder.rgCheck.check(mSpCheckedState.get(position));
                } else {
                    holder.rgCheck.clearCheck();

                    if (itemsDB.getSatisfactorio() == 1) {
                        holder.rbSi.setChecked(true);
                        holder.rbNo.setChecked(false);
                    } else if (itemsDB.getSatisfactorio() == 0) {
                        holder.rbSi.setChecked(false);
                        holder.rbNo.setChecked(true);
                    } else {
                        holder.rbSi.setChecked(false);
                        holder.rbNo.setChecked(false);
                    }
                }

                //AlertBuilder Requeridos
                alertRequired.setTitle("Atención!");
                alertRequired.setIcon(android.R.drawable.ic_dialog_alert);
                alertRequired.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertRequired.setCancelable(false);

                holder.rgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        */
/*****
                                *Check Required
                                *//*

                        //Get required fields
                        //Imagen requerida
                        if (itemCheck.getImagen_requerido() == 1) {
                            if (!Objects.equals(itemCheck.getImagen(), "")) {
                                itemsDB.setImageLoaded("si");
                            }
                        }
                        //GPS requerido
                        if (itemCheck.getGps_requerido() == 1) {
                            if (gps.isGpsEnabled()) {
                                itemsDB.setGpsLoaded("si");
                            }
                        }
                        //QR requerido
                        if (itemCheck.getQr_requerido() == 1) {
                            if (itemCheck.getQr_status() == 1) {
                                itemsDB.setQrLoaded("si");
                            }
                        }

                        required_errors = 0;
                        item_required_error = "";
                        //Imagen requerida
                        if (itemCheck.getImagen_requerido() == 1) {
                            if (!Objects.equals(itemsDB.getImageLoaded(), "si")) {
                                required_errors = 1;
                                item_required_error = "imagen no cargada";
                            }
                        }
                        //GPS requerido
                        if (itemCheck.getGps_requerido() == 1) {
                            if (!Objects.equals(itemsDB.getGpsLoaded(), "si")) {
                                required_errors = 1;
                                if (!Objects.equals(item_required_error, "")) {
                                    item_required_error = item_required_error + ", el gps no esta activado";
                                } else {
                                    item_required_error =  "el gps no esta activado";
                                }
                            }
                        }
                        //QR requerido
                        if (itemCheck.getQr_requerido() == 1) {
                            if (!Objects.equals(itemsDB.getQrLoaded(), "si")) {
                                required_errors = 1;
                                if (!Objects.equals(item_required_error, "")) {
                                    item_required_error = item_required_error + ", qr no cargado";
                                } else {
                                    item_required_error =  "qr no cargado";
                                }
                            }
                        }
                        if (itemCheck.getGps_requerido() == 1) {
                            loading_time = 3000;
                        } else {
                            loading_time = 500;
                        }
                        */
/*****
                                *Check Required
                                *//*


                        if (checkedId > -1) {
                            mSpCheckedState.put(position, checkedId);
                            switch (checkedId) {
                                //RADIO BUTTON SI
                                case R.id.rbSi:
                                    System.out.println("position " + position);

                                    if (required_errors == 1) {
                                        alertRequired.setMessage("Error al cargar el formulario. Verifique los siguientes campos requeridos: " + item_required_error + ".");
                                        alertRequired.show();
                                        holder.rgCheck.clearCheck();
                                    } else {
                                        */
/*itemCheck.setSatisfactorio(1);
                                        itemCheck.setFecha_app(MySqlDateTimeFormat());
                                        itemCheck.save();
                                        //Update tabla Sincronizar
                                        sincronizarDB = new Sincronizar(
                                                "items_check",
                                                itemCheck.getId().intValue(),
                                                "update",
                                                false,
                                                0
                                        );
                                        sincronizarDB.save();*//*

                                            */
/*gps.getLocation();
                                            double latitude = gps.getLatitude();
                                            double longitude = gps.getLongitude();
                                            gps_location = new ArrayList<>();
                                            gps_location.add(String.valueOf(latitude));
                                            gps_location.add(String.valueOf(longitude));*//*

                                            System.out.println("position " + position);
                                            prg.setMessage("Obteniendo información...");
                                            prg.setCancelable(false);
                                            prg.show();
                                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    gps.getLocation();
                                                    double latitude = gps.getLatitude();
                                                    double longitude = gps.getLongitude();
                                                    gps_location = new ArrayList<>();
                                                    gps_location.add(String.valueOf(latitude));
                                                    gps_location.add(String.valueOf(longitude));
                                                    if (itemCheck.getGps_habilitar() == 1) {
                                                        itemCheck.setGps(String.valueOf(gps_location));
                                                    }
                                                    itemCheck.setSatisfactorio(1);
                                                    itemCheck.setFecha_app(globalMethods.MySqlDateTimeFormat());
                                                    itemCheck.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                                                    itemCheck.setUpdated_by(preferenceManager.getUserId());
                                                    itemCheck.save();
                                                    prg.dismiss();
                                                    System.out.println("satisfactorio: " + itemCheck.getSatisfactorio());
                                                    System.out.println("item seleccionado " + itemCheck.toString());
                                                    System.out.println("gps " + itemCheck.getGps());
                                                    //Update tabla Sincronizar
                                                    sincronizarDB = new Sincronizar(
                                                            "items_check",
                                                            itemCheck.getId().intValue(),
                                                            "update",
                                                            false,
                                                            0
                                                    );
                                                    sincronizarDB.save();
                                                }
                                            }, loading_time);
                                    }

                                    System.out.println("item fecha actual " + itemCheck.getFecha_app());
                                    System.out.println("item seleccionado " + itemCheck.toString());
                                    System.out.println("item update si " + itemCheck.getSatisfactorio());
                                    break;

                                //RADIO BUTTON NO
                                case R.id.rbNo:
                                    System.out.println("position " + position);

                                    */
/*required_errors = 0;
                                    item_required_error = "";
                                    //Imagen requerida
                                    if (itemCheck.getImagen_requerido() == 1) {
                                        if (!Objects.equals(itemsDB.getImageLoaded(), "si")) {
                                            required_errors = 1;
                                            item_required_error = "imagen";
                                        }
                                    }
                                    //GPS requerido
                                    if (itemCheck.getGps_requerido() == 1) {
                                        if (!Objects.equals(itemsDB.getGpsLoaded(), "si")) {
                                            required_errors = 1;
                                            item_required_error = item_required_error + ", gps";
                                        }
                                    }
                                    //QR requerido
                                    if (itemCheck.getQr_requerido() == 1) {
                                        if (!Objects.equals(itemsDB.getQrLoaded(), "si")) {
                                            required_errors = 1;
                                            item_required_error = item_required_error + ", qr";
                                        }
                                    }*//*


                                    if (required_errors == 1) {
                                        alertRequired.setMessage("Error al cargar el formulario. Verifique los siguientes campos requeridos: " + item_required_error + ".");
                                        alertRequired.show();
                                        holder.rgCheck.clearCheck();
                                    } else {
                                        */
/*itemCheck.setSatisfactorio(0);
                                        itemCheck.setFecha_app(MySqlDateTimeFormat());
                                        itemCheck.save();
                                        //Update tabla Sincronizar
                                        sincronizarDB = new Sincronizar(
                                                "items_check",
                                                itemCheck.getId().intValue(),
                                                "update",
                                                false,
                                                0
                                        );
                                        sincronizarDB.save();*//*

                                            */
/*gps.getLocation();
                                            double latitude = gps.getLatitude();
                                            double longitude = gps.getLongitude();
                                            gps_location = new ArrayList<>();
                                            gps_location.add(String.valueOf(latitude));
                                            gps_location.add(String.valueOf(longitude));
                                            System.out.println("gps location " + String.valueOf(gps_location));*//*

                                            System.out.println("position " + position);
                                            prg.setMessage("Obteniendo información..");
                                            prg.setCancelable(false);
                                            prg.show();
                                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    gps.getLocation();
                                                    double latitude = gps.getLatitude();
                                                    double longitude = gps.getLongitude();
                                                    gps_location = new ArrayList<>();
                                                    gps_location.add(String.valueOf(latitude));
                                                    gps_location.add(String.valueOf(longitude));
                                                    System.out.println("gps location " + String.valueOf(gps_location));
                                                    if (itemCheck.getGps_habilitar() == 1) {
                                                        itemCheck.setGps(String.valueOf(gps_location));
                                                    }
                                                    itemCheck.setSatisfactorio(0);
                                                    itemCheck.setFecha_app(globalMethods.MySqlDateTimeFormat());
                                                    itemCheck.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                                                    itemCheck.setUpdated_by(preferenceManager.getUserId());
                                                    itemCheck.save();
                                                    prg.dismiss();
                                                    System.out.println("satisfactorio: " + itemCheck.getSatisfactorio());
                                                    System.out.println("item seleccionado " + itemCheck.toString());
                                                    System.out.println("gps " + itemCheck.getGps());
                                                    //Update tabla Sincronizar
                                                    sincronizarDB = new Sincronizar(
                                                            "items_check",
                                                            itemCheck.getId().intValue(),
                                                            "update",
                                                            false,
                                                            0
                                                    );
                                                    sincronizarDB.save();
                                                }
                                            }, loading_time);
                                    }

                                    System.out.println("item fecha actual " + itemCheck.getFecha_app());
                                    System.out.println("item seleccionado " + itemCheck.toString());
                                    System.out.println("item update no " + itemCheck.getSatisfactorio());
                                    break;
                            }
                        } else {
                            if (mSpCheckedState.indexOfKey(position) > -1) {
                                mSpCheckedState.removeAt(mSpCheckedState.indexOfKey(position));
                            }
                        }

                    }
                });

                holder.btnObservacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final EditText inputText = new EditText(activity);
                        inputText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                        inputText.setSelectAllOnFocus(true);
                        inputText.selectAll();

                        alert.setTitle("Observación item: " + itemCheck.getNombre());
                        alert.setMessage("Ingrese la observación");

                        inputText.setText(itemCheck.getObservacion());

                        // Set an EditText view to get user input
                        alert.setView(inputText);

                        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Log.d("input text", inputText.getText().toString());
                                System.out.println("position " + position);
                                itemCheck.setObservacion(inputText.getText().toString());
                                itemCheck.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                                itemCheck.setUpdated_by(preferenceManager.getUserId());
                                itemCheck.save();
                                System.out.println("item seleccionado " + itemCheck.toString());
                                System.out.println("observacion " + itemCheck.getObservacion());
                                //Update tabla Sincronizar
                                sincronizarDB = new Sincronizar(
                                        "items_check",
                                        itemCheck.getId().intValue(),
                                        "update",
                                        false,
                                        0
                                );
                                sincronizarDB.save();
                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                            }
                        });

                        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                            }
                        });

                        alert.show();
                        inputText.requestFocus();
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });

                */
/*if (holder.btnGps.isClickable()) {
                    holder.btnGps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (gps.isGpsEnabled()) {
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();
                                gps_location = new ArrayList<>();
                                gps_location.add(String.valueOf(latitude));
                                gps_location.add(String.valueOf(longitude));
                                System.out.println("position " + position);
                                prg.show();
                                prg.setMessage("Obteniendo información del GPS...");
                                prg.setCancelable(false);
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemCheck.setGps(String.valueOf(gps_location));
                                        itemCheck.save();
                                        prg.dismiss();
                                        Toast.makeText(activity, "GPS obtenido correctamente", Toast.LENGTH_LONG).show();
                                        System.out.println("item seleccionado " + itemCheck.toString());
                                        System.out.println("gps " + itemCheck.getGps());
                                        //Update tabla Sincronizar
                                        sincronizarDB = new Sincronizar(
                                                "items_check",
                                                itemCheck.getId().intValue(),
                                                "update",
                                                false,
                                                0
                                        );
                                        sincronizarDB.save();
                                    }
                                }, 3000);
                            } else {
                                gps.showSettingsAlert();
                            }
                        }
                    });
                }*//*


                if (holder.btnImg.isClickable()) {
                    holder.btnImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Redirect a la vista para subir Imagen
                            ImageUpload fragment = new ImageUpload();
                            Bundle args_id = new Bundle();
                            args_id.putInt("item_id", itemCheck.getId().intValue());
                            fragment.setArguments(args_id);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null).commit();
                        }
                    });
                }

                if (holder.btnQr.isClickable()) {
                    holder.btnQr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Redirect a la vista para scannear QR
                            QRScanner fragment = new QRScanner();
                            Bundle args_id = new Bundle();
                            args_id.putInt("item_id", itemCheck.getId().intValue());
                            fragment.setArguments(args_id);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null).commit();
                        }
                    });
                }

                //Mostrar y ocultar fila de Botones
                holder.btnHideShowRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemsDB.toggleVisibility();
                        holder.tableRow.setVisibility(itemsDB.visibility);
                        if (position == formCheckList.size()-1) {
                            listView.setSelection(position);
                        }
                    }
                });

                holder.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentManager.popBackStackImmediate();
                    }
                });

                break;
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tituloCheck, descripcionCheck;
        Button btnObservacion, */
/*btnGps,*//*
 btnImg, btnQr, btnHideShowRow, btnOk, btnNext;
        LinearLayout btnNext_container;
        RadioGroup rgCheck;
        RadioButton rbSi, rbNo;
        TableRow tableRow, tableRow3;
    }

}*/
