//package zentcode02.parks.fragments;
//
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TabHost;
//import android.widget.TextView;
//
//import com.orm.query.Condition;
//import com.orm.query.Select;
//import com.orm.util.NamingHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import zentcode02.parks.R;
//import zentcode02.parks.adapters.FormCheckAdapter;
//import zentcode02.parks.dbModels.CabeceraChecksDB;
//import zentcode02.parks.dbModels.ItemsChecksDB;
//import zentcode02.parks.dbModels.SeccionChecksDB;
//import zentcode02.parks.dbModels.SincronizarDB;
//import zentcode02.parks.models.Items;
//import zentcode02.parks.network.PreferenceManager;
//import zentcode02.parks.services.GPSTracker;
//import zentcode02.parks.utils.DatePickerFragment;
//import zentcode02.parks.utils.GlobalMethods;
//
//public class FormCheckFragment extends Fragment {
//
//    private TextView txtNombreUnidad, txtFechaCabecera;
//    private EditText edtVerificadoPor, edtRevisadoPor;
//    private RadioGroup rgCheckViaje;
//    private ListView listaCheck;
//    private PreferenceManager preferenceManager;
//    private Global globalMethods;
//    private int form_type;
//    private FragmentManager fragmentManager;
//    private TabHost tab;
//    private int tab_active = 0;
//    private CabeceraCheck cabeceraChecksDB;
//    public static final int DATEPICKER_FRAGMENT=1; // adding this line
//    private GPSTracker gps;
//    private int gps_check_run;
//    private Sincronizar sincronizarDB;
//
//    public FormCheckFragment() {}
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        gps_check_run = 0;
//        preferenceManager = new PreferenceManager(getActivity());
//        globalMethods = new Global(getActivity().getApplicationContext());
//        gps = new GPSTracker(getActivity());
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.formcheck_fragment, container, false);
//
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Check");
//
//        tab = (TabHost) view.findViewById(R.id.tabhost);
//        tab.setup();
//
//        TabHost.TabSpec spec1 = tab.newTabSpec("Cabecera");
//        spec1.setIndicator("Cabecera");
//        spec1.setContent(R.id.tabCheck1);
//        tab.addTab(spec1);
//
//        TabHost.TabSpec spec2 = tab.newTabSpec("Detalle");
//        spec2.setIndicator("Detalle");
//        spec2.setContent(R.id.tabCheck2);
//        tab.addTab(spec2);
//
//        //Set and get tab active on changed listener
//        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                Log.d("tab active", String.valueOf(tab.getCurrentTabTag()));
//                switch (tab.getCurrentTab()) {
//                    case 0:
//                        tab.setCurrentTab(tab.getCurrentTab());
//                        tab_active = 1;
//                        break;
//                    case 1:
//                        tab.setCurrentTab(tab.getCurrentTab());
//                        tab_active = 2;
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//
//        //On back stack set the tab active by the last changed
//        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            public void onBackStackChanged() {
//                Log.d("entro en el back stack ", "Entro");
//                if (tab_active == 1) {
//                    tab.setCurrentTab(0);
//                } else if (tab_active == 2) {
//                    tab.setCurrentTab(1);
//                }
//            }
//        });
//
//        fragmentManager = getFragmentManager();
//
//        Button btnOk = (Button) view.findViewById(R.id.includeCheckTab1).findViewById(R.id.okButton);
//        Button btnNext = (Button) view.findViewById(R.id.includeCheckTab1).findViewById(R.id.nextButton);
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager.popBackStackImmediate();
//            }
//        });
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tab.setCurrentTab(1);
//            }
//        });
//
//        //GET ARGS FRAGMENT
//        form_type = getArguments().getInt("form_type");
//        Log.d("form type chk", String.valueOf(form_type));
//        int cabecera_id = form_type;
//
//        //Query Cabecera Checks
//        cabeceraChecksDB = CabeceraCheck.findById(CabeceraCheck.class, cabecera_id);
//
//        txtNombreUnidad = (TextView) view.findViewById(R.id.txtNombreUnidad);
//        txtFechaCabecera = (TextView) view.findViewById(R.id.txtFechaCabecera);
//        edtVerificadoPor = (EditText) view.findViewById(R.id.edtVerificadoPor);
//        edtRevisadoPor = (EditText) view.findViewById(R.id.edtRevisadoPor);
//        rgCheckViaje = (RadioGroup) view.findViewById(R.id.rgCabeceraCheck);
//        RadioButton rb_incio_viaje = (RadioButton) view.findViewById(R.id.rb_inicio_viaje);
//        RadioButton rb_fin_viaje = (RadioButton) view.findViewById(R.id.rb_fin_viaje);
//
//        //Validación y guardado de los Edit Text en la Cabecera Compra
//        edtVerificadoPor.addTextChangedListener(new CustomWatcher(edtVerificadoPor));
//        edtRevisadoPor.addTextChangedListener(new CustomWatcher(edtRevisadoPor));
//
//        txtFechaCabecera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment fragment = new DatePickerFragment();
//                fragment.setTargetFragment(FormCheckFragment.this, DATEPICKER_FRAGMENT);
//                fragment.show(getFragmentManager(), "datePicker");
//            }
//        });
//
//        if (!Objects.equals(cabeceraChecksDB.getInicio_viaje(), "") || !Objects.equals(cabeceraChecksDB.getFin_viaje(), "")) {
//            if (Objects.equals(cabeceraChecksDB.getInicio_viaje(), "Si")) {
//                rb_incio_viaje.setChecked(true);
//                rb_fin_viaje.setChecked(false);
//            } else if (Objects.equals(cabeceraChecksDB.getFin_viaje(), "Si")) {
//                rb_incio_viaje.setChecked(false);
//                rb_fin_viaje.setChecked(true);
//            }
//        }
//
//        rgCheckViaje.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_inicio_viaje:
//                        cabeceraChecksDB.setInicio_viaje("Si");
//                        cabeceraChecksDB.setFin_viaje("No");
//                        cabeceraChecksDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
//                        cabeceraChecksDB.setUpdated_by(preferenceManager.getUserId());
//                        cabeceraChecksDB.save();
//                        break;
//                    case R.id.rb_fin_viaje:
//                        cabeceraChecksDB.setInicio_viaje("No");
//                        cabeceraChecksDB.setFin_viaje("Si");
//                        cabeceraChecksDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
//                        cabeceraChecksDB.setUpdated_by(preferenceManager.getUserId());
//                        cabeceraChecksDB.save();
//                        break;
//                }
//            }
//        });
//
//        checkGpsEnabled();
//
//        listaCheck = (ListView) view.findViewById(R.id.lv_formchecktab2);
//lpopulateCabeceraCheck();
//
//        populateFormCheck(cabecera_id);
//
//        return view;
//    }
//
//    private void checkGpsEnabled() {
//        if (!gps.isGpsEnabled()) {
//            if (gps_check_run == 0) {
//                gps_check_run = 1;
//                gpsShowWarning();
//            }
//        } else {
//            getGpsLocation();
//        }
//    }
//
//    private void gpsShowWarning() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//        alertDialog.setTitle("Se esta configurando el GPS");
//        alertDialog.setMessage("El GPS no esta activado. Active el GPS.");
//        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                gps.checkGpsEnabled();
//            }
//        });
//        alertDialog.show();
//    }
//
//    private void getGpsLocation() {
//        gps.getLocation();
//    }
//
//    public void populateFormCheck(int cabecera_id) {
//
//        //Adaptador Check
//        FormCheckAdapter adapter;
//
//        //Populate Cabecera Check
//        String nombre_unidad = preferenceManager.getNombreUnidad();
//        Log.d("nombre de unidad", nombre_unidad);
//        txtNombreUnidad.setText(nombre_unidad);
//        txtFechaCabecera.setText(cabeceraChecksDB.getFecha());
//        edtVerificadoPor.setText(cabeceraChecksDB.getVerificado_por());
//        edtRevisadoPor.setText(cabeceraChecksDB.getRevisado_por());
//
////            List<SeccionCheck> seccionChecksDBList = SeccionCheck.find(SeccionCheck.class, NamingHelper.toSQLNameDefault("codigo_formulario_sync")+" = ?", cabeceraChecksDB.getCodigo_formulario_sync());
//
//        List<SeccionCheck> seccionChecksDBList = Select.from(SeccionCheck.class)
//                .where(Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(cabeceraChecksDB.getCodigo_formulario_sync())).list();
//
//            List<Items> itemsToAdapterList = new ArrayList<>();
//            for (int i = 0; i < seccionChecksDBList.size(); i++) {
//                Items itemsSeccion = new Items();
//                itemsSeccion.setSeccion_check_id(seccionChecksDBList.get(i).getId().intValue());
//                itemsSeccion.setSeccion_check_nombre(seccionChecksDBList.get(i).getNombre());
//                itemsSeccion.setIsHeader(1);
//                itemsToAdapterList.add(itemsSeccion);
////                List<ItemCheck> itemsChecksList = ItemCheck.find(ItemCheck.class, NamingHelper.toSQLNameDefault("seccion_check_id") +" = ?", String.valueOf(seccionChecksDBList.get(i).getId()));
//                List<ItemCheck> itemsChecksList = Select.from(ItemCheck.class).where(Condition.prop(NamingHelper.toSQLNameDefault("seccion_check_id")).eq(String.valueOf(seccionChecksDBList.get(i).getId())))
//                        .where(Condition.prop(NamingHelper.toSQLNameDefault("id_servidor")).eq(0))
//                        .list();
//                for (int x = 0; x < itemsChecksList.size(); x++) {
//                    Items itemsDescripcion = new Items();
//                    itemsDescripcion.setItem_check_id(itemsChecksList.get(x).getId().intValue());
//                    itemsDescripcion.setItem_check_nombre(itemsChecksList.get(x).getNombre());
//                    itemsDescripcion.setSatisfactorio(itemsChecksList.get(x).getSatisfactorio());
//                    itemsDescripcion.setObservacion(itemsChecksList.get(x).getObservacion());
//                    itemsDescripcion.setIsHeader(0);
//                    //Cargar los datos para poder controlar las instancias en el ListView
//                    //Loaded = para la carga de los campoes img, gps, qr
//                    //Enabled = para la habilitacion de los botones img, gps, qr
//                    //Color = para los colores de los botones de img, gps, qr
//                    if (itemsChecksList.get(x).getImagen_requerido() == 1 ||
//                            itemsChecksList.get(x).getQr_requerido() == 1 ||
//                            itemsChecksList.get(x).getGps_requerido() == 1) {
//                        itemsDescripcion.setButton_hide_show_color(Color.RED);
//                        itemsDescripcion.setButton_hide_show_drawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_more_horiz_black_24dp_red));
//                    } else {
//                        itemsDescripcion.setButton_hide_show_color(Color.BLACK);
//                        itemsDescripcion.setButton_hide_show_drawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_more_horiz_black_24dp));
//                    }
//                    if (itemsChecksList.get(x).getImagen_requerido() == 1) {
//                        itemsDescripcion.setImageLoaded("no");
//                        itemsDescripcion.setImageEnabled(true);
//                        itemsDescripcion.setButton_color_img(Color.RED);
//                    } else {
//                        itemsDescripcion.setImageLoaded("unrequired");
//                        if (itemsChecksList.get(x).getImagen_habilitar() == 0) {
//                            itemsDescripcion.setButton_color_img(Color.GRAY);
//                            itemsDescripcion.setImageEnabled(false);
//                        } else {
//                            itemsDescripcion.setButton_color_img(Color.BLACK);
//                            itemsDescripcion.setImageEnabled(true);
//                        }
//                    }
//                    if (itemsChecksList.get(x).getGps_requerido() == 1) {
//                        itemsDescripcion.setGpsLoaded("no");
//                        itemsDescripcion.setGpsEnabled(true);
//                        itemsDescripcion.setButton_color_gps(Color.RED);
//                    } else {
//                        itemsDescripcion.setGpsLoaded("unrequired");
//                        itemsDescripcion.setGpsEnabled(false);
//                        if (itemsChecksList.get(x).getGps_habilitar() == 0) {
//                            itemsDescripcion.setButton_color_gps(Color.GRAY);
//                            itemsDescripcion.setGpsEnabled(false);
//                        } else {
//                            itemsDescripcion.setButton_color_gps(Color.BLACK);
//                            itemsDescripcion.setGpsEnabled(true);
//                        }
//                    }
//                    if (itemsChecksList.get(x).getQr_requerido() == 1) {
//                        itemsDescripcion.setQrLoaded("no");
//                        itemsDescripcion.setQrEnabled(true);
//                        itemsDescripcion.setButton_color_qr(Color.RED);
//                    } else {
//                        itemsDescripcion.setQrLoaded("unrequired");
//                        itemsDescripcion.setQrEnabled(false);
//                        if (itemsChecksList.get(x).getQr_habilitar() == 0) {
//                            itemsDescripcion.setButton_color_qr(Color.GRAY);
//                            itemsDescripcion.setQrEnabled(false);
//                        } else {
//                            itemsDescripcion.setButton_color_qr(Color.BLACK);
//                            itemsDescripcion.setQrEnabled(true);
//                        }
//                    }
//                    itemsToAdapterList.add(itemsDescripcion);
//                }
//            }
//
//            //Populate Detalle Check
//            adapter = new FormCheckAdapter(getActivity(), fragmentManager, itemsToAdapterList, listaCheck);
//            listaCheck.setAdapter(adapter);
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//            case DATEPICKER_FRAGMENT:
//                if (resultCode == Activity.RESULT_OK) {
//                    // here the part where I get my selected date from the saved variable in the intent and the displaying it.
//                    Bundle bundle=data.getExtras();
////                    String resultDate = bundle.getString("selectedDate","error");
//                    String resultDate = bundle.getString("selectedDate");
//                    txtFechaCabecera.setText(resultDate);
//                    Log.d("cabecera", String.valueOf(cabeceraChecksDB.getId()));
//                    cabeceraChecksDB.setFecha(resultDate);
//                    cabeceraChecksDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
//                    cabeceraChecksDB.setUpdated_by(preferenceManager.getUserId());
//                    cabeceraChecksDB.save();
//                    Log.d("cabecera fecha", cabeceraChecksDB.getFecha());
//                    sincronizarDB = new Sincronizar(
//                            "cabecera_checks",
//                            cabeceraChecksDB.getId().intValue(),
//                            "update",
//                            false,
//                            0
//                    );
//                    sincronizarDB.save();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Check");
//    }
//
//    private class CustomWatcher implements TextWatcher {
//
//        private boolean mWasEdited = false;
//        private View view;
//
//        CustomWatcher(View view) {
//            this.view = view;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//            if (mWasEdited){
//                mWasEdited = false;
//                return;
//            }
//
//            switch(view.getId()) {
//                case R.id.edtVerificadoPor:
//                    cabeceraChecksDB.setVerificado_por(editable.toString());
//                    cabeceraChecksDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
//                    cabeceraChecksDB.setUpdated_by(preferenceManager.getUserId());
//                    cabeceraChecksDB.save();
//                    Log.d("solicitado por", cabeceraChecksDB.getVerificado_por());
//                    //Update tabla Sincronizar
//                    sincronizarDB = new Sincronizar(
//                            "cabecera_checks",
//                            cabeceraChecksDB.getId().intValue(),
//                            "update",
//                            false,
//                            0
//                    );
//                    sincronizarDB.save();
//                    break;
//
//                case R.id.edtRevisadoPor:
//                    cabeceraChecksDB.setRevisado_por(editable.toString());
//                    cabeceraChecksDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
//                    cabeceraChecksDB.setUpdated_by(preferenceManager.getUserId());
//                    cabeceraChecksDB.save();
//                    Log.d("enviado por", cabeceraChecksDB.getRevisado_por());
//                    //Update tabla Sincronizar
//                    sincronizarDB = new Sincronizar(
//                            "cabecera_checks",
//                            cabeceraChecksDB.getId().intValue(),
//                            "update",
//                            false,
//                            0
//                    );
//                    sincronizarDB.save();
//                    break;
//            }
//
//        }
//    }
//
//}
