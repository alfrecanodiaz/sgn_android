/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.CabeceraChecksDB;
import zentcode02.parks.dbModels.ItemsChecksDB;
import zentcode02.parks.dbModels.SeccionChecksDB;
import zentcode02.parks.models.Items;
import zentcode02.parks.network.PreferenceManager;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormCheckHistorial extends Fragment {

    private FragmentManager fragmentManager;
    private TabHost tab;
    private int tab_active = 0;
    private ListView listaCheckHistorial;
    private TextView txtNombreUnidad, txtFechaCabecera, txtVerificadoPor, txtRevisadoPor;
    private PreferenceManager preferenceManager;
    private List<Items> itemsToAdapterList;
    private CabeceraCheck cabeceraChecksDB;
    private RadioGroup rgCheckViaje;

    public FormCheckHistorial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.form_check_historial_fragment, container, false);

        preferenceManager = new PreferenceManager(getActivity());

        //GET ARGS FRAGMENT
        String codigo_sync = getArguments().getString("codigo_sync");
        String tipo_form = getArguments().getString("tipo_form");

        Log.d("codigo_sync", codigo_sync);
        cabeceraChecksDB = Select.from(CabeceraCheck.class).where(Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(codigo_sync)).first();

        tab = (TabHost) view.findViewById(R.id.tabhost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Cabecera");
        spec1.setIndicator("Cabecera");
        spec1.setContent(R.id.tabCheckHistorial1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Detalle");
        spec2.setIndicator("Detalle");
        spec2.setContent(R.id.tabCheckHistorial2);
        tab.addTab(spec2);

        //Set and get tab active on changed listener
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tab.getCurrentTab()) {
                    case 0:
                        tab.setCurrentTab(tab.getCurrentTab());
                        tab_active = 1;
                        break;
                    case 1:
                        tab.setCurrentTab(tab.getCurrentTab());
                        tab_active = 2;
                        break;
                    default:
                        break;
                }
            }
        });

        fragmentManager = getFragmentManager();

        //On back stack set the tab active by the last changed
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                Log.d("entro en el back stack ", "Entro");
                if (tab_active == 1) {
                    tab.setCurrentTab(0);
                } else if (tab_active == 2) {
                    tab.setCurrentTab(1);
                }
            }
        });

        Button btnOk = (Button) view.findViewById(R.id.includeCheckHistorialTab1).findViewById(R.id.okButton);
        Button btnNext = (Button) view.findViewById(R.id.includeCheckHistorialTab1).findViewById(R.id.nextButton);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStackImmediate();
            }
        });
        
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab.setCurrentTab(1);
            }
        });

        txtNombreUnidad = (TextView) view.findViewById(R.id.txtNombreUnidad);
        txtFechaCabecera = (TextView) view.findViewById(R.id.txtFechaCabecera);
        txtVerificadoPor = (TextView) view.findViewById(R.id.txtVerificadoPor);
        txtRevisadoPor = (TextView) view.findViewById(R.id.txtRevisadoPor);
        rgCheckViaje = (RadioGroup) view.findViewById(R.id.rgCabeceraCheck);
        RadioButton rb_incio_viaje = (RadioButton) view.findViewById(R.id.rb_inicio_viaje);
        RadioButton rb_fin_viaje = (RadioButton) view.findViewById(R.id.rb_fin_viaje);
        rb_incio_viaje.setClickable(false);
        rb_fin_viaje.setClickable(false);

        if (!Objects.equals(cabeceraChecksDB.getInicio_viaje(), "") || !Objects.equals(cabeceraChecksDB.getFin_viaje(), "")) {
            if (Objects.equals(cabeceraChecksDB.getInicio_viaje(), "Si")) {
                rb_incio_viaje.setChecked(true);
                rb_fin_viaje.setChecked(false);
            } else if (Objects.equals(cabeceraChecksDB.getFin_viaje(), "Si")) {
                rb_incio_viaje.setChecked(false);
                rb_fin_viaje.setChecked(true);
            }
        }

        listaCheckHistorial = (ListView) view.findViewById(R.id.lv_formcheckHistorialtab2);
        populateFormCheckHistorial(tipo_form);

        return view;
    }

    private void populateFormCheckHistorial(String tipo_form) {

        String nombre_unidad = preferenceManager.getNombreUnidad();

        Log.d("cabecera", cabeceraChecksDB.getCodigo_formulario_sync());

        //Populate Cabecera Check
        if (Objects.equals(tipo_form, "sincronizado")) {
            txtFechaCabecera.setText(dateFormatFromMySQL(cabeceraChecksDB.getFecha()));
        } else if (Objects.equals(tipo_form, "finalizado")) {
            txtFechaCabecera.setText(cabeceraChecksDB.getFecha());
        }
        txtNombreUnidad.setText(nombre_unidad);
        txtVerificadoPor.setText(cabeceraChecksDB.getVerificado_por());
        txtRevisadoPor.setText(cabeceraChecksDB.getRevisado_por());

        List<SeccionCheck> seccionChecksDBList = Select.from(SeccionCheck.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync"))
                        .eq(cabeceraChecksDB.getCodigo_formulario_sync()))
                .list();

        Log.d("seccion check", seccionChecksDBList.toString());

        if (Objects.equals(tipo_form, "sincronizado")) {
            itemsToAdapterList = new ArrayList<>();
            for (int i = 0; i < seccionChecksDBList.size(); i++) {
                Items itemsSeccion = new Items();
                itemsSeccion.setSeccion_check_id(seccionChecksDBList.get(i).getId_servidor());
                itemsSeccion.setSeccion_check_nombre(seccionChecksDBList.get(i).getNombre());
                itemsSeccion.setIsHeader(1);
                itemsToAdapterList.add(itemsSeccion);
                List<ItemCheck> itemsChecksList = Select.from(ItemCheck.class)
                        .where(Condition.prop(NamingHelper.toSQLNameDefault("seccion_check_id"))
                                .eq(String.valueOf(seccionChecksDBList.get(i).getId_servidor())))
                        .orderBy(NamingHelper.toSQLNameDefault("id_servidor") + " ASC").list();
                for (int x = 0; x < itemsChecksList.size(); x++) {
                    Items itemsDescripcion = new Items();
                    itemsDescripcion.setItem_check_id(itemsChecksList.get(x).getId_servidor());
                    itemsDescripcion.setItem_check_nombre(itemsChecksList.get(x).getNombre());
                    itemsDescripcion.setSatisfactorio(itemsChecksList.get(x).getSatisfactorio());
                    itemsDescripcion.setObservacion(itemsChecksList.get(x).getObservacion());
                    itemsDescripcion.setIsHeader(0);
                    itemsToAdapterList.add(itemsDescripcion);
                }
            }
        } else if (Objects.equals(tipo_form, "finalizado")) {
            itemsToAdapterList = new ArrayList<>();
            for (int i = 0; i < seccionChecksDBList.size(); i++) {
                Items itemsSeccion = new Items();
                itemsSeccion.setSeccion_check_id(seccionChecksDBList.get(i).getId().intValue());
                itemsSeccion.setSeccion_check_nombre(seccionChecksDBList.get(i).getNombre());
                itemsSeccion.setIsHeader(1);
                itemsToAdapterList.add(itemsSeccion);
                List<ItemCheck> itemsChecksList = Select.from(ItemCheck.class)
                        .where(Condition.prop(NamingHelper.toSQLNameDefault("seccion_check_id"))
                                .eq(String.valueOf(seccionChecksDBList.get(i).getId())))
                        .orderBy(NamingHelper.toSQLNameDefault("id") + " ASC").list();
                for (int x = 0; x < itemsChecksList.size(); x++) {
                    Items itemsDescripcion = new Items();
                    itemsDescripcion.setItem_check_id(itemsChecksList.get(x).getId().intValue());
                    itemsDescripcion.setItem_check_nombre(itemsChecksList.get(x).getNombre());
                    itemsDescripcion.setSatisfactorio(itemsChecksList.get(x).getSatisfactorio());
                    itemsDescripcion.setObservacion(itemsChecksList.get(x).getObservacion());
                    itemsDescripcion.setIsHeader(0);
                    itemsToAdapterList.add(itemsDescripcion);
                }
            }
        }

        FormCheckHistorialAdapter adapter = new FormCheckHistorialAdapter(getActivity(), itemsToAdapterList);
        listaCheckHistorial.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private static String dateFormatFromMySQL(String fecha_cabecera) {
//        DateFormat inputFormatter = new SimpleDateFormat("dd-mm-yyyy");
        DateFormat inputFormatter = new SimpleDateFormat("yyyy-mm-dd");
        Date dateCabecera = null;
        try {
            dateCabecera = inputFormatter.parse(fecha_cabecera);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat outputFormatter = new SimpleDateFormat("dd-mm-yyyy");

        return outputFormatter.format(dateCabecera);
    }

    private class FormCheckHistorialAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Activity activity;
        private List<Items> formCheckList;
        private SparseIntArray mSpCheckedState = new SparseIntArray();
        private AlertDialog.Builder alert;

        private int type;
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        FormCheckHistorialAdapter(Activity activity, List<Items> items) {
            this.activity = activity;
            this.formCheckList = items;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final Items itemsDB = formCheckList.get(position);
//            final ItemCheck itemCheck = ItemCheck.findById(ItemCheck.class, itemsDB.getItem_check_id());

            if(inflater==null){
                inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView ==null){
                holder = new ViewHolder();
                //Alert Dialog con el Text de Observacion
                alert = new AlertDialog.Builder(activity);

                switch (type) {
                    case TYPE_SEPARATOR:
                        //inflate the new layout
                        convertView = inflater.inflate(R.layout.layout_txtlist, null);
                        holder.tituloCheck = (TextView) convertView.findViewById(R.id.txtList);
                        break;

                    case TYPE_ITEM:
                        //inflate the new layout
                        convertView = inflater.inflate(R.layout.layout_checkhistorialtab2, null);
                        holder.descripcionCheck = (TextView) convertView.findViewById(R.id.descripcionCheck);
                        holder.rgCheck = (RadioGroup) convertView.findViewById(R.id.rgCheck);
                        holder.rbSi = (RadioButton) convertView.findViewById(R.id.rbSi);
                        holder.rbNo = (RadioButton) convertView.findViewById(R.id.rbNo);
                        holder.btnObservacion = (Button) convertView.findViewById(R.id.btnObsHistorial);
                        holder.btnOk = (Button) convertView.findViewById(R.id.includeCheckHistorialTab2).findViewById(R.id.okButton);
                        holder.btnNext_container = (LinearLayout) convertView.findViewById(R.id.includeCheckHistorialTab2).findViewById(R.id.btnNext_container);
                        holder.tableRow = (TableRow) convertView.findViewById(R.id.rowCheck2);
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

                    holder.btnNext_container.setVisibility(View.GONE);

                    holder.tableRow.setVisibility(View.GONE);
                    if (position == formCheckList.size()-1) {
                        holder.tableRow.setVisibility(View.VISIBLE);
                    } else {
                        holder.tableRow.setVisibility(View.GONE);
                    }

                    String descripcionPreCheck = itemsDB.getItem_check_nombre();
                    holder.descripcionCheck.setText(descripcionPreCheck.replace("[", "").replace("]", ""));

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

                    holder.btnObservacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final TextView inputText = new TextView(activity);
                            inputText.setPadding(40, 20, 40, 20);
                            inputText.setTextSize(18);

                            inputText.setText(itemsDB.getObservacion());
                            alert.setTitle("Observación item: " + itemsDB.getItem_check_nombre());

                            // Set an EditText view to get user input
                            alert.setView(inputText);

                            alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                            alert.show();
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

        class ViewHolder {
            TextView tituloCheck, descripcionCheck;
            Button btnObservacion, btnOk;
            RadioGroup rgCheck;
            RadioButton rbSi, rbNo;
            TableRow tableRow;
            LinearLayout btnNext_container;
        }

    }

}
*/
