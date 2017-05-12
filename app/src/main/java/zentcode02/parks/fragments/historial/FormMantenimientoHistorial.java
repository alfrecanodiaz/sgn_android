/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.CabeceraMantenimientoDB;
import zentcode02.parks.dbModels.CategoriaMaquinariaDB;
import zentcode02.parks.dbModels.DetalleMantenimientoDB;
import zentcode02.parks.network.PreferenceManager;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormMantenimientoHistorial extends Fragment {

    private PreferenceManager preferenceManager;
    private FragmentManager fragmentManager;
    private TextView txtNombreUnidad, txtSolicitadoPor, txtFecha, txtArea;
    private String codigo_sync, tipo_form;
    private int cabecera_id;
    private Spinner spinnerMantenimientoTab2;
    private TabHost tab;
    private ListView listaMantenimientoHistorial;
    private FormMantenimientoHistorialAdapter adapter;
    private String rawQuery;
    private List<DetalleMantenimiento> detallesOtros;

    public FormMantenimientoHistorial() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.form_mantenimiento_historial_fragment, container, false);

        preferenceManager = new PreferenceManager(getActivity());
        fragmentManager = getFragmentManager();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Mantenimiento");

        tab = (TabHost) view.findViewById(R.id.tabhostMantenimientoHistorial);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Cabecera");
        spec1.setIndicator("Cabecera");
        spec1.setContent(R.id.tabMantenimientoHistorial1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Detalle");
        spec2.setIndicator("Detalle");
        spec2.setContent(R.id.tabMantenimientoHistorial2);
        tab.addTab(spec2);

        Button btnOk = (Button) view.findViewById(R.id.includeMantenimientoTab1).findViewById(R.id.okButton);
        Button btnNext = (Button) view.findViewById(R.id.includeMantenimientoTab1).findViewById(R.id.nextButton);
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

        //GET ARGS FRAGMENT
        codigo_sync = getArguments().getString("codigo_sync");
        tipo_form = getArguments().getString("tipo_form");

        //Cabecera Mantenimiento
        txtNombreUnidad = (TextView) view.findViewById(R.id.txtNombreUnidadMantenimiento);
        txtSolicitadoPor = (TextView) view.findViewById(R.id.txtSolicitadoPor);
        txtFecha = (TextView) view.findViewById(R.id.txtFechaCabecera);
        txtArea = (TextView) view.findViewById(R.id.txtArea);
        //Campos Detalle Mantenimiento
        spinnerMantenimientoTab2 = (Spinner) view.findViewById(R.id.spinnerMantenimientoTab2);

        listaMantenimientoHistorial = (ListView) view.findViewById(R.id.lv_formMantenimientoTabHistorial2);

        populateCabeceraMantenimientoHistorial();

        return view;
    }

    private void populateCabeceraMantenimientoHistorial() {
        String nombre_unidad = preferenceManager.getNombreUnidad();

        CabeceraMantenimiento cabeceraMantenimiento = Select.from(CabeceraMantenimiento.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(codigo_sync))
                .first();

        if (Objects.equals(tipo_form, "sincronizado")) {
            cabecera_id = cabeceraMantenimiento.getId_servidor();
        } else if (Objects.equals(tipo_form, "finalizado")) {
            cabecera_id = cabeceraMantenimiento.getId().intValue();
        }

        //Populate Cabecera Mantenimiento
        txtNombreUnidad.setText(nombre_unidad);
        txtSolicitadoPor.setText(cabeceraMantenimiento.getSolicitado_por());
        txtFecha.setText(cabeceraMantenimiento.getFecha());
        txtArea.setText(cabeceraMantenimiento.getArea());

        populateDetalleMatenimientoHistorial();

    }

    private void populateDetalleMatenimientoHistorial() {
        List<String> listaCategorias = new ArrayList<>();

        List<CategoriaMaquinaria> categoriaMaquinariaDBList = Select.from(CategoriaMaquinaria.class)
                .orderBy(NamingHelper.toSQLNameDefault("descripcion")).list();

        for (int i = 0; i < categoriaMaquinariaDBList.size(); i++) {
            listaCategorias.add(categoriaMaquinariaDBList.get(i).getDescripcion());
        }
        listaCategorias.add("Otros");

        final String maquinarias = NamingHelper.toSQLNameDefault("Maquinaria");
        final String detalle_mantenimientos = NamingHelper.toSQLNameDefault("DetalleMantenimiento");
        final String db_id = NamingHelper.toSQLNameDefault("id");
        final String maquinaria_id = NamingHelper.toSQLNameDefault("maquinaria_id");
        final String categoria_id = NamingHelper.toSQLNameDefault("categoria_id");
        final String cabecera_mantenimiento_id = NamingHelper.toSQLNameDefault("cabecera_mantenimiento_id");
        final String codigo_formulario_sync= NamingHelper.toSQLNameDefault("codigo_formulario_sync");

        if (Objects.equals(tipo_form, "sincronizado")) {
            rawQuery = "SELECT * FROM " + detalle_mantenimientos
                    + " LEFT JOIN " + maquinarias
                    + " ON " + maquinarias+"."+db_id + " = " + detalle_mantenimientos+"."+maquinaria_id
                    + " WHERE " + maquinarias+"."+categoria_id + " = " +  categoriaMaquinariaDBList.get(0).getId_servidor()
                    + " AND " + detalle_mantenimientos+"."+cabecera_mantenimiento_id + " = " + cabecera_id;
        } else if (Objects.equals(tipo_form, "finalizado")) {
            rawQuery = "SELECT * FROM " + detalle_mantenimientos
                    + " LEFT JOIN " + maquinarias
                    + " ON " + maquinarias+"."+db_id + " = " + detalle_mantenimientos+"."+maquinaria_id
                    + " WHERE " + maquinarias+"."+categoria_id + " = " +  categoriaMaquinariaDBList.get(0).getId_servidor()
                    + " AND " + detalle_mantenimientos+"."+codigo_formulario_sync + " = " + codigo_sync;
        }


        List<DetalleMantenimiento> detallesMantenimientoList = DetalleMantenimiento.findWithQuery(
                DetalleMantenimiento.class,
                rawQuery, null);

        adapter = new FormMantenimientoHistorialAdapter(getActivity(), detallesMantenimientoList, 0);
        listaMantenimientoHistorial.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Adaptadores Selects Categorias Compras
        // Populate the spinner using a customized ArrayAdapter that hides the first (dummy) entry
        ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_bg, listaCategorias) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View v;

                v = super.getDropDownView(position, null, parent);

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);

                spinnerMantenimientoTab2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (!Objects.equals(spinnerMantenimientoTab2.getAdapter().getItem(position).toString(), "Otros")) {

                            CategoriaMaquinaria categoria = Select.from(CategoriaMaquinaria.class)
                                    .where(Condition.prop(NamingHelper.toSQLNameDefault("descripcion"))
                                            .eq(spinnerMantenimientoTab2.getAdapter().getItem(position).toString())).first();

                            //Lista de productos
                            listaMantenimientoHistorial.setAdapter(null);

                            if (Objects.equals(tipo_form, "sincronizado")) {
                                rawQuery = "SELECT * FROM " + detalle_mantenimientos
                                        + " LEFT JOIN " + maquinarias
                                        + " ON " + maquinarias+"."+db_id + " = " + detalle_mantenimientos+"."+maquinaria_id
                                        + " WHERE " + maquinarias+"."+categoria_id + " = " +  categoria.getId_servidor()
                                        + " AND " + detalle_mantenimientos+"."+cabecera_mantenimiento_id + " = " + cabecera_id;
                            } else if (Objects.equals(tipo_form, "finalizado")) {
                                rawQuery = "SELECT * FROM " + detalle_mantenimientos
                                        + " LEFT JOIN " + maquinarias
                                        + " ON " + maquinarias+"."+db_id + " = " + detalle_mantenimientos+"."+maquinaria_id
                                        + " WHERE " + maquinarias+"."+categoria_id + " = " +  categoria.getId_servidor()
                                        + " AND " + detalle_mantenimientos+"."+codigo_formulario_sync + " = " + codigo_sync;
                            }

                            List<DetalleMantenimiento> detallesMantenimientoList = DetalleMantenimiento.findWithQuery(
                                    DetalleMantenimiento.class,
                                    rawQuery, null);

                            adapter = new FormMantenimientoHistorialAdapter(getActivity(), detallesMantenimientoList, 0);
                            listaMantenimientoHistorial.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {

                            //Lista de productos
                            listaMantenimientoHistorial.setAdapter(null);

                            if (Objects.equals(tipo_form, "sincronizado")) {
                                detallesOtros = Select.from(DetalleMantenimiento.class)
                                        .where(
                                                Condition.prop(NamingHelper.toSQLNameDefault("maquinaria_id")).eq(0),
                                                Condition.prop(NamingHelper.toSQLNameDefault(cabecera_mantenimiento_id)).eq(cabecera_id),
                                                Condition.prop(NamingHelper.toSQLNameDefault("maquinaria_eliminada")).eq(0)
                                        ).list();
                            } else if (Objects.equals(tipo_form, "finalizado")) {
                                detallesOtros = Select.from(DetalleMantenimiento.class)
                                        .where(
                                                Condition.prop(NamingHelper.toSQLNameDefault("maquinaria_id")).eq(0),
                                                Condition.prop(NamingHelper.toSQLNameDefault(codigo_formulario_sync)).eq(codigo_sync),
                                                Condition.prop(NamingHelper.toSQLNameDefault("maquinaria_eliminada")).eq(0)
                                        ).list();
                            }

                            adapter = new FormMantenimientoHistorialAdapter(getActivity(), detallesOtros, 1);
                            listaMantenimientoHistorial.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                return v;
            }
        };
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMantenimientoTab2.setAdapter(categoriasAdapter);
        spinnerMantenimientoTab2.setSelection(0);

    }

    private class FormMantenimientoHistorialAdapter extends BaseAdapter {

        private AlertDialog.Builder alert;
        private LayoutInflater inflater;
        private Activity activity;
        private List<DetalleMantenimiento> detalleList;

        private int type_list;
        private int type;
        private static final int TYPE_MAQUINARIA = 0;
        private static final int TYPE_MAQUINARIA_SIN_ID = 1;

        FormMantenimientoHistorialAdapter(Activity activity, List<DetalleMantenimiento> items, int type_list) {
            this.activity = activity;
            this.detalleList = items;
            this.type_list = type_list;
        }

        @Override
        public int getItemViewType(int position) {
            if (type_list== 0) {
                type = TYPE_MAQUINARIA;
            } else if (type_list == 1) {
                type = TYPE_MAQUINARIA_SIN_ID;
            }
            return type;
        }

        @Override
        public int getCount() {
            return detalleList.size();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return detalleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final DetalleMantenimiento detalleMantenimiento = detalleList.get(position);
            if(inflater==null){
                inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView ==null){
                holder = new ViewHolder();
                alert = new AlertDialog.Builder(activity);
                convertView = inflater.inflate(R.layout.layout_mantenimiento_historial_tab2,null);
                holder.maquinaria = (TextView) convertView.findViewById(R.id.nombreMaquinaria);
                holder.btnDetalles = (Button) convertView.findViewById(R.id.btnDetallesMaquinaria);
                holder.btnOk = (Button) convertView.findViewById(R.id.includeMantenimientoHistorialTab2).findViewById(R.id.okButton);
                holder.btnNext_container = (LinearLayout) convertView.findViewById(R.id.includeMantenimientoHistorialTab2).findViewById(R.id.btnNext_container);
                holder.tableRow = (TableRow) convertView.findViewById(R.id.rowMantenimientoHistorial2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btnNext_container.setVisibility(View.GONE);

            holder.tableRow.setVisibility(View.GONE);
            if (position == detalleList.size()-1) {
                holder.tableRow.setVisibility(View.VISIBLE);
            } else {
                holder.tableRow.setVisibility(View.GONE);
            }

            switch (type) {

                case TYPE_MAQUINARIA:
                    holder.maquinaria.setText(detalleMantenimiento.getMaquinaria());
                    break;

                case TYPE_MAQUINARIA_SIN_ID:
                    holder.maquinaria.setText(detalleMantenimiento.getMaquinaria_sin_id());
                    break;

            }

            holder.btnDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Create LinearLayout Dynamically
                    final LinearLayout layout = new LinearLayout(v.getContext());
                    //Setup Layout Attributes
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout.setLayoutParams(params);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final TextView txtMantenimiento = new TextView(v.getContext());
                    final TextView txtTipo = new TextView(v.getContext());
                    final TextView txtConforme = new TextView(v.getContext());
                    final TextView txtObservacion = new TextView(v.getContext());
                    txtMantenimiento.setTextSize(18);
                    txtTipo.setTextSize(18);
                    txtConforme.setTextSize(18);
                    txtObservacion.setTextSize(18);
                    txtMantenimiento.setPadding(30, 0, 30, 0);
                    txtTipo.setPadding(30, 0, 30, 0);
                    txtConforme.setPadding(30, 0, 30, 0);
                    txtObservacion.setPadding(30, 0, 30, 0);
                    String label_mantenimiento = "Mantenimiento: "+ detalleMantenimiento.getMantenimiento();
                    String label_tipo = "Tipo: "+ detalleMantenimiento.getTipo();
                    String label_conforme = "Conforme: "+ detalleMantenimiento.getConforme();
                    String label_observacion = "Observación: "+ detalleMantenimiento.getObservacion();
                    txtMantenimiento.setText(label_mantenimiento);
                    txtTipo.setText(label_tipo);
                    txtConforme.setText(label_conforme);
                    txtObservacion.setText(label_observacion);

                    layout.addView(txtMantenimiento);
                    layout.addView(txtTipo);
                    layout.addView(txtConforme);
                    layout.addView(txtObservacion);

                    alert.setView(layout);

                    alert.setTitle("Detalles item: " + detalleMantenimiento.getNombre_maquinaria());

                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
                }
            });

            //Ok Button
            holder.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.popBackStackImmediate();
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView maquinaria;
            Button btnOk, btnDetalles;
            TableRow tableRow;
            LinearLayout btnNext_container;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Mantenimiento");
    }

}
*/
