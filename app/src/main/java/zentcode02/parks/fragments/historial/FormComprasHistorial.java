/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import zentcode02.parks.dbModels.FormularioCompraCabeceraDB;
import zentcode02.parks.dbModels.FormularioCompraCategoriaDB;
import zentcode02.parks.dbModels.FormularioCompraDetalleDB;
import zentcode02.parks.network.PreferenceManager;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormComprasHistorial extends Fragment {

    private ListView listaComprasHistorial;
    private PreferenceManager preferenceManager;
    private FragmentManager fragmentManager;
    private TextView txtNombreUnidad, txtSolicitadoPor, txtEnviadoPor, txtArea, txtEmpresaGrupo, txtPrioridad, txtAprobadoPor, txtAprobadoFecha, txtRecibidoPor, txtRecibidoFecha, txtNroOC;
    private Spinner spinnerComprasTab2;
    private String codigo_sync, tipo_form, rawQuery;
    private int cabecera_id;
    private TabHost tab;
    private FormCompraHistorialAdapter adapter;
    private String textColor="#00000000";
    private List<DetalleCompra> detalleCompraOtros;

    public FormComprasHistorial() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.form_compras_historial_fragment, container, false);

        preferenceManager = new PreferenceManager(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Compras");

        tab = (TabHost) view.findViewById(R.id.tabhostCompras);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Cabecera");
        spec1.setIndicator("Cabecera");
        spec1.setContent(R.id.tabComprasHistorial1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Detalle");
        spec2.setIndicator("Detalle");
        spec2.setContent(R.id.tabComprasHistorial2);
        tab.addTab(spec2);

        fragmentManager = getFragmentManager();

        Button btnOk = (Button) view.findViewById(R.id.includeComprasHistorialTab1).findViewById(R.id.okButton);
        Button btnNext = (Button) view.findViewById(R.id.includeComprasHistorialTab1).findViewById(R.id.nextButton);
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
        Log.d("codigo sync in", codigo_sync);
        tipo_form = getArguments().getString("tipo_form");
        Log.d("tipo form in", tipo_form);

        //Cabecera Compras
        txtNombreUnidad = (TextView) view.findViewById(R.id.txtNombreUnidadCompras);
        txtSolicitadoPor = (TextView) view.findViewById(R.id.txtSolicitadoPorHistorial);
        txtEnviadoPor = (TextView) view.findViewById(R.id.txtEnviadoPorHistorial);
        txtArea = (TextView) view.findViewById(R.id.txtAreaHistorial);
        txtEmpresaGrupo = (TextView) view.findViewById(R.id.txtEmpresaGrupoHistorial);
        txtPrioridad = (TextView) view.findViewById(R.id.txtPrioridadHistorial);
        txtAprobadoPor = (TextView) view.findViewById(R.id.txtAprobadoPor);
        txtAprobadoFecha = (TextView) view.findViewById(R.id.txtAprobadoFecha);
        txtRecibidoPor = (TextView) view.findViewById(R.id.txtRecibidoPor);
        txtRecibidoFecha = (TextView) view.findViewById(R.id.txtRecibidoFecha);
        txtNroOC = (TextView) view.findViewById(R.id.txtNroOc);
        //Ocultar items
        if (Objects.equals(tipo_form, "finalizado")) {
            TextView txt_aprobado_por = (TextView) view.findViewById(R.id.label_aprobado_por);
            TextView txt_aprobado_fecha = (TextView) view.findViewById(R.id.label_aprobado_fecha);
            TextView txt_recibido_por = (TextView) view.findViewById(R.id.label_recibido_por);
            TextView txt_recibido_fecha = (TextView) view.findViewById(R.id.label_recibido_fecha);
            TextView txt_nro_oc = (TextView) view.findViewById(R.id.label_nro_oc);
            txt_aprobado_por.setVisibility(View.GONE);
            txt_aprobado_fecha.setVisibility(View.GONE);
            txt_recibido_por.setVisibility(View.GONE);
            txt_recibido_fecha.setVisibility(View.GONE);
            txt_nro_oc.setVisibility(View.GONE);
        }
        //Campos Detalle Compras
        spinnerComprasTab2 = (Spinner) view.findViewById(R.id.spinnerComprasTab2);

        listaComprasHistorial = (ListView) view.findViewById(R.id.lv_formComprasHistorialTab2);

        populateCabeceraComprasHistorial();

        return view;
    }

    private void populateCabeceraComprasHistorial() {

        String nombre_unidad = preferenceManager.getNombreUnidad();

//        CabeceraCompra cabeceraCompra = CabeceraCompra
//                .findById(CabeceraCompra.class, cabecera_id);
        CabeceraCompra cabeceraCompra = Select.from(CabeceraCompra.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(codigo_sync))
                .first();

        if (Objects.equals(tipo_form, "sincronizado")) {
            cabecera_id = cabeceraCompra.getId_servidor();
        } else if (Objects.equals(tipo_form, "finalizado")) {
            cabecera_id = cabeceraCompra.getId().intValue();
        }

        //Populate Cabecera Compra
        txtNombreUnidad.setText(nombre_unidad);
        txtSolicitadoPor.setText(cabeceraCompra.getSolicitado_por());
        txtEnviadoPor.setText(cabeceraCompra.getEnviado_por());
        txtArea.setText(cabeceraCompra.getArea());
        txtEmpresaGrupo.setText(cabeceraCompra.getEmpresa_del_grupo());
        txtPrioridad.setText(cabeceraCompra.getPrioridad());
        if (Objects.equals(tipo_form, "sincronizado")) {
            txtAprobadoPor.setText(cabeceraCompra.getAprobado_por());
            txtAprobadoFecha.setText(cabeceraCompra.getAprobado_fecha());
            txtRecibidoPor.setText(cabeceraCompra.getRecibido_por());
            txtRecibidoFecha.setText(cabeceraCompra.getRecibido_fecha());
            txtNroOC.setText(cabeceraCompra.getNro_oc());
        }

        populateDetalleComprasHistorial();

    }

    private void populateDetalleComprasHistorial() {

        List<String> listaCategorias = new ArrayList<>();

        //Spinner Categorias
        List<FormularioCompraCategoria> formularioCompraCategoriaDBList = FormularioCompraCategoria
                .find(FormularioCompraCategoria.class, null, null, null, NamingHelper.toSQLNameDefault("descripcion"), null);

        for (int i = 0; i < formularioCompraCategoriaDBList.size(); i++) {
            listaCategorias.add(formularioCompraCategoriaDBList.get(i).getDescripcion());
        }
        listaCategorias.add("Otros");

        final String compraProductos = NamingHelper.toSQLNameDefault("FormularioCompraProducto");
        final String compraDetalle = NamingHelper.toSQLNameDefault("DetalleCompra");
        final String db_id = NamingHelper.toSQLNameDefault("id");
        final String producto_id = NamingHelper.toSQLNameDefault("producto_id");
        final String db_categoria_id = NamingHelper.toSQLNameDefault("categoria_id");
        final String cabecera_compra_id = NamingHelper.toSQLNameDefault("cabecera_compra_id");
        final String codigo_formulario_sync= NamingHelper.toSQLNameDefault("codigo_formulario_sync");

        if (Objects.equals(tipo_form, "sincronizado")) {
            rawQuery = "SELECT * FROM " + compraDetalle
                    + " LEFT JOIN " + compraProductos
                    + " ON " + compraProductos+"."+db_id + " = " + compraDetalle+"."+producto_id
                    + " WHERE " + compraProductos+"."+db_categoria_id + " = " +  formularioCompraCategoriaDBList.get(0).getId_servidor()
                    + " AND " + compraDetalle+"."+cabecera_compra_id + " = " + cabecera_id;
        } else if (Objects.equals(tipo_form, "finalizado")) {
            rawQuery = "SELECT * FROM " + compraDetalle
                    + " LEFT JOIN " + compraProductos
                    + " ON " + compraProductos+"."+db_id + " = " + compraDetalle+"."+producto_id
                    + " WHERE " + compraProductos+"."+db_categoria_id + " = " +  formularioCompraCategoriaDBList.get(0).getId_servidor()
                    + " AND " + compraDetalle+"."+codigo_formulario_sync + " = " + codigo_sync;
        }

        List<DetalleCompra> formularioDetalleList = DetalleCompra.findWithQuery(
                DetalleCompra.class,
                rawQuery, null);

        for (int i = 0; i < formularioDetalleList.size(); i++) {
            Log.d("form cod sync", formularioDetalleList.get(i).getCodigo_formulario_sync());
            Log.d("cod sync ", codigo_sync);
        }

        adapter = new FormCompraHistorialAdapter(getActivity(), formularioDetalleList, 0);
        listaComprasHistorial.setAdapter(adapter);
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

                spinnerComprasTab2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Log.d("item categorias", spinnerComprasTab2.getAdapter().getItem(position).toString());

                        if (!Objects.equals(spinnerComprasTab2.getAdapter().getItem(position).toString(), "Otros")) {

                            FormularioCompraCategoria categoria = Select.from(FormularioCompraCategoria.class)
                                    .where(Condition.prop(NamingHelper.toSQLNameDefault("descripcion"))
                                            .eq(spinnerComprasTab2.getAdapter().getItem(position).toString())).first();

                            //Lista de productos
                            listaComprasHistorial.setAdapter(null);

                            if (Objects.equals(tipo_form, "sincronizado")) {
                                rawQuery = "SELECT * FROM " + compraDetalle
                                        + " LEFT JOIN " + compraProductos
                                        + " ON " + compraProductos+"."+db_id + " = " + compraDetalle+"."+producto_id
                                        + " WHERE " + compraProductos+"."+db_categoria_id + " = " +  categoria.getId_servidor()
                                        + " AND " + compraDetalle+"."+cabecera_compra_id + " = " + cabecera_id;
                            } else if (Objects.equals(tipo_form, "finalizado")) {
                                rawQuery = "SELECT * FROM " + compraDetalle
                                        + " LEFT JOIN " + compraProductos
                                        + " ON " + compraProductos+"."+db_id + " = " + compraDetalle+"."+producto_id
                                        + " WHERE " + compraProductos+"."+db_categoria_id + " = " +  categoria.getId_servidor()
                                        + " AND " + compraDetalle+"."+codigo_formulario_sync + " = " + codigo_sync;
                            }

                            List<DetalleCompra> formularioDetalleList = DetalleCompra.findWithQuery(
                                    DetalleCompra.class,
                                    rawQuery, null);

                            adapter = new FormCompraHistorialAdapter(getActivity(), formularioDetalleList, 0);
                            listaComprasHistorial.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {

                            //Lista de productos
                            listaComprasHistorial.setAdapter(null);

                            if (Objects.equals(tipo_form, "sincronizado")) {
                                detalleCompraOtros = Select.from(DetalleCompra.class)
                                        .where(
                                                Condition.prop(NamingHelper.toSQLNameDefault("producto_id")).eq(0),
                                                Condition.prop(NamingHelper.toSQLNameDefault(cabecera_compra_id)).eq(cabecera_id),
                                                Condition.prop(NamingHelper.toSQLNameDefault("producto_eliminado")).eq(0)
                                        ).list();
                            } else if (Objects.equals(tipo_form, "finalizado")) {
                                detalleCompraOtros = Select.from(DetalleCompra.class)
                                        .where(
                                                Condition.prop(NamingHelper.toSQLNameDefault("producto_id")).eq(0),
                                                Condition.prop(NamingHelper.toSQLNameDefault(codigo_formulario_sync)).eq(codigo_sync),
                                                Condition.prop(NamingHelper.toSQLNameDefault("producto_eliminado")).eq(0)
                                        ).list();
                            }

                            adapter = new FormCompraHistorialAdapter(getActivity(), detalleCompraOtros, 1);
                            listaComprasHistorial.setAdapter(adapter);
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
        spinnerComprasTab2.setAdapter(categoriasAdapter);
        spinnerComprasTab2.setSelection(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Compras");
    }

    private class FormCompraHistorialAdapter extends BaseAdapter {

        private AlertDialog.Builder alert;
        private LayoutInflater inflater;
        private Activity activity;
        private List<DetalleCompra> detalleList;

        private int type_list;
        private int type;
        private static final int TYPE_PRODUCTO = 0;
        private static final int TYPE_PRODUCTO_SIN_ID = 1;

        FormCompraHistorialAdapter(Activity activity, List<DetalleCompra> items, int type_list) {
            this.activity = activity;
            this.detalleList = items;
            this.type_list = type_list;
        }

        @Override
        public int getItemViewType(int position) {
            if (type_list== 0) {
                type = TYPE_PRODUCTO;
            } else if (type_list == 1) {
                type = TYPE_PRODUCTO_SIN_ID;
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
            final DetalleCompra formularioCompraDetalleDB = detalleList.get(position);

            if(inflater==null){
                inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView ==null){
                holder = new ViewHolder();
                alert = new AlertDialog.Builder(activity);
                convertView=inflater.inflate(R.layout.layout_comprashistorialtab2,null);
                holder.descripcionProducto = (TextView) convertView.findViewById(R.id.descripcionCompras);
                holder.btnDetalles = (Button) convertView.findViewById(R.id.btnDetallesProducto);
                holder.btnOk = (Button) convertView.findViewById(R.id.includeComprasHistorialTab2).findViewById(R.id.okButton);
                holder.btnNext_container = (LinearLayout) convertView.findViewById(R.id.includeComprasHistorialTab2).findViewById(R.id.btnNext_container);
                holder.tableRow = (TableRow) convertView.findViewById(R.id.rowComprasHistorialBtnOk);

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

                case TYPE_PRODUCTO:
                    holder.descripcionProducto.setText(formularioCompraDetalleDB.getProducto());
                    break;

                case TYPE_PRODUCTO_SIN_ID:
                    holder.descripcionProducto.setText(formularioCompraDetalleDB.getProductos_sin_id());
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

                    final TextView txtCantidadSolicitada = new TextView(v.getContext());
                    final TextView txtCantidadAprobada = new TextView(v.getContext());
                    final TextView txtCantidadRecibida = new TextView(v.getContext());
                    txtCantidadSolicitada.setTextSize(18);
                    txtCantidadAprobada.setTextSize(18);
                    txtCantidadRecibida.setTextSize(18);
                    txtCantidadSolicitada.setPadding(20, 0, 20, 0);
                    txtCantidadAprobada.setPadding(20, 0, 20, 0);
                    txtCantidadRecibida.setPadding(20, 0, 20, 0);
                    String label_cantidad_solicitada = "Cantidad solicitada: "+ formularioCompraDetalleDB.getCantidad_solicitada();
                    String label_cantidad_aprobada = "Cantidad aprobada: "+ formularioCompraDetalleDB.getCantidad_aprobada();
                    String label_cantidad_recibida = "Cantidad recibida: "+ formularioCompraDetalleDB.getCantidad_recibida();
                    txtCantidadSolicitada.setText(label_cantidad_solicitada);
                    txtCantidadAprobada.setText(label_cantidad_aprobada);
                    txtCantidadRecibida.setText(label_cantidad_recibida);

                    layout.addView(txtCantidadSolicitada);
                    layout.addView(txtCantidadAprobada);
                    layout.addView(txtCantidadRecibida);

                    alert.setView(layout);

                    alert.setTitle("Detalles item: " + formularioCompraDetalleDB.getNombre());

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
            TextView descripcionProducto;
            Button btnDetalles, btnOk;
            TableRow tableRow;
            LinearLayout btnNext_container;
        }

    }

}
*/
