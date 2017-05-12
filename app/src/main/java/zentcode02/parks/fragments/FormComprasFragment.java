/*
package zentcode02.parks.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.adapters.FormComprasAdapter;
import zentcode02.parks.dbModels.FormularioCompraCabeceraDB;
import zentcode02.parks.dbModels.FormularioCompraCategoriaDB;
import zentcode02.parks.dbModels.FormularioCompraDetalleDB;
import zentcode02.parks.dbModels.ListAreaCompraDB;
import zentcode02.parks.dbModels.SincronizarDB;
import zentcode02.parks.models.StaticItems;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.GlobalMethods;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormComprasFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ListView listaCompras;
    private PreferenceManager preferenceManager;
    private Global globalMethods;
    private int form_type;
    private TextView txtNombreUnidad;
    private int cabecera_compra_id;
    private int check_area=0;
    private int check_prioridad=0;
    private int check_empresa=0;
    private Spinner spinnerComprasTab2, spinnerArea, spinnerPrioridad, spinnerEmpresaGrupo;
    private FormComprasAdapter adapter;
    private List<DetalleCompra> formularioCompraDetalleList;
    private Button btnAgregarProducto;
    private FragmentManager fragmentManager;
    private CabeceraCompra formularioCompraCabeceraDB;
    private EditText solicitadoPor, enviadoPor;
    private TabHost tab;

    public FormComprasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getActivity());
        globalMethods = new Global(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.form_compras_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Compras");

        tab = (TabHost) view.findViewById(R.id.tabhostCompras);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Cabecera");
        spec1.setIndicator("Cabecera");
        spec1.setContent(R.id.tabCompras1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Detalle");
        spec2.setIndicator("Detalle");
        spec2.setContent(R.id.tabCompras2);
        tab.addTab(spec2);

        fragmentManager = getFragmentManager();

        Button btnOk = (Button) view.findViewById(R.id.includeComprasTab1).findViewById(R.id.okButton);
        Button btnNext = (Button) view.findViewById(R.id.includeComprasTab1).findViewById(R.id.nextButton);
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
        form_type = getArguments().getInt("form_type");
        Log.d("form type", String.valueOf(form_type));
        cabecera_compra_id = form_type;

        //Query en la Tabla Formulario Compra Cabecera con el ultimo id para actualizar sus campos
        formularioCompraCabeceraDB = CabeceraCompra.findById(CabeceraCompra.class, cabecera_compra_id);

        txtNombreUnidad = (TextView) view.findViewById(R.id.txtNombreUnidadCompras);
        //Campos Detalle Compras
        spinnerComprasTab2 = (Spinner) view.findViewById(R.id.spinnerComprasTab2);
        btnAgregarProducto = (Button) view.findViewById(R.id.btnAgregarProducto);
        btnAgregarProducto.setVisibility(View.GONE);
        //Campos de la Cabecera Compras
        solicitadoPor = (EditText) view.findViewById(R.id.edtSolicitadoPor);
        enviadoPor = (EditText) view.findViewById(R.id.edtEnviadoPor);
        spinnerArea = (Spinner) view.findViewById(R.id.spinnerArea);
        spinnerPrioridad = (Spinner) view.findViewById(R.id.spinnerPrioridad);
        spinnerEmpresaGrupo = (Spinner) view.findViewById(R.id.spinnerEmpresaGrupo);

        //Adaptadores Selects Area
        SpinnerAdapter spinnerAdapterArea = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
                globalMethods.getListAreaCompra());
        spinnerAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(spinnerAdapterArea);
        if (!Objects.equals(formularioCompraCabeceraDB.getArea(), "")) {
            spinnerSetSelectedItem(spinnerArea, spinnerAdapterArea, formularioCompraCabeceraDB.getArea());
        }

        //Adaptadores Select Prioridad
        SpinnerAdapter spinnerAdapterPrioridad = new SpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, StaticItems.prioridadLista);
        spinnerAdapterPrioridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrioridad.setAdapter(spinnerAdapterPrioridad);
        if (!Objects.equals(formularioCompraCabeceraDB.getPrioridad(), "")) {
            spinnerSetSelectedItem(spinnerPrioridad, spinnerAdapterPrioridad, formularioCompraCabeceraDB.getPrioridad());
        }

        SpinnerAdapter spinnerAdapterEmpresaGrupo = new SpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item, StaticItems.empresaGrupoLista);
        spinnerAdapterEmpresaGrupo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmpresaGrupo.setAdapter(spinnerAdapterEmpresaGrupo);
        if (!Objects.equals(formularioCompraCabeceraDB.getEmpresa_del_grupo(), "")) {
            spinnerSetSelectedItem(spinnerEmpresaGrupo, spinnerAdapterEmpresaGrupo, formularioCompraCabeceraDB.getEmpresa_del_grupo());
        }

        Log.d("cabecera compra id", String.valueOf(cabecera_compra_id));

        spinnerArea.setOnItemSelectedListener(this);
        spinnerPrioridad.setOnItemSelectedListener(this);
        spinnerEmpresaGrupo.setOnItemSelectedListener(this);

        //Validación y guardado de los Edit Text en la Cabecera Compra
        solicitadoPor.addTextChangedListener(new CustomWatcher(solicitadoPor));
        enviadoPor.addTextChangedListener(new CustomWatcher(enviadoPor));

        //Agregar producto sin ID en categoria Otros - Popup Alert
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                Context context = view.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText inputProducto = new EditText(getActivity());
                final EditText inputCantidadSolicitada = new EditText(getActivity());
                inputProducto.setHint("Ingrese el nombre del producto");
                inputCantidadSolicitada.setHint("Ingrese la cantidad solicitada");
                inputProducto.setInputType(InputType.TYPE_CLASS_TEXT);
                inputCantidadSolicitada.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputCantidadSolicitada.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 1000000)});
                layout.addView(inputProducto);
                layout.addView(inputCantidadSolicitada);

                alert.setTitle("Agregar producto");
                //alert.setMessage("Ingrese el producto");

                alert.setView(layout);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.d("input producto", inputProducto.getText().toString());
                        Log.d("input cantidad", inputCantidadSolicitada.getText().toString());
                        String cantidad_solicitada;
                        if (!Objects.equals(inputCantidadSolicitada.getText().toString(), "")) {
                            cantidad_solicitada = inputCantidadSolicitada.getText().toString();
                        } else {
                            cantidad_solicitada = String.valueOf(0);
                        }

                        DetalleCompra formCompraDetalleAdd = new DetalleCompra(
                                0,
                                Integer.valueOf(cantidad_solicitada),
                                "",
                                "",
                                formularioCompraCabeceraDB.getId().intValue(),
                                0,
                                formularioCompraCabeceraDB.getCodigo_sync(),
                                formularioCompraCabeceraDB.getCodigo_formulario_sync(),
                                inputProducto.getText().toString(),
                                0,
                                inputProducto.getText().toString(),
                                0,
                                "",
                                globalMethods.MySqlDateTimeFormat(),
                                globalMethods.MySqlDateTimeFormat(),
                                preferenceManager.getUserId(),
                                preferenceManager.getUserId()
                        );
                        long producto_insert_id =  formCompraDetalleAdd.save();

                        //GET Last Formulario Compra Detalle ID
                        DetalleCompra formularioCompraDetalle = DetalleCompra.findById(DetalleCompra.class, producto_insert_id);

                        //Save Detalle Compra en la tabla Sincronizar
                        Sincronizar sincronizarDetalleCompraDB = new Sincronizar(
                                "formulariocompra__detalle_compras",
                                formularioCompraDetalle.getId().intValue(),
                                "insert",
                                false,
                                0
                        );
                        sincronizarDetalleCompraDB.save();

                        Toast.makeText(getActivity(), "Producto agregado correctamente", Toast.LENGTH_LONG).show();

                        populateItemsAgregados();

                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
//                inputProducto.requestFocus();
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });

        listaCompras = (ListView) view.findViewById(R.id.lv_formComprasTab2);
        populateCabeceraCompras();
        populateDetalleCompras();

        return view;
    }

    private void spinnerSetSelectedItem(Spinner spinner, SpinnerAdapter adapter, String value) {
        int spinnerPosition = adapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }

    private void populateCabeceraCompras() {

        //Populate Cabecera Compras
        String nombre_unidad = preferenceManager.getNombreUnidad();
        Log.d("nombre de unidad", nombre_unidad);
        txtNombreUnidad.setText(nombre_unidad);

        solicitadoPor.setText(formularioCompraCabeceraDB.getSolicitado_por());
        enviadoPor.setText(formularioCompraCabeceraDB.getEnviado_por());

    }

    private void  populateDetalleCompras() {

        List<String> listaCategorias = new ArrayList<>();

        //Spinner Categorias
        List<FormularioCompraCategoria> formularioCompraCategoriaDBList = FormularioCompraCategoria
                .find(FormularioCompraCategoria.class, null, null, null, NamingHelper.toSQLNameDefault("descripcion"), null);

        Log.d("formulario categorias", formularioCompraCategoriaDBList.toString());

        for (int i = 0; i < formularioCompraCategoriaDBList.size(); i++) {
            listaCategorias.add(formularioCompraCategoriaDBList.get(i).getDescripcion());
            Log.d("lista categorias", listaCategorias.toString());
        }
        listaCategorias.add("Otros");
        Log.d("lista categorias new", listaCategorias.toString());

        //Query de los Productos con la Categoria en la posicion 0
        formularioCompraDetalleList = Select.from(DetalleCompra.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("cabecera_compra_id"))
                                .eq(String.valueOf(cabecera_compra_id)),
                        Condition.prop(NamingHelper.toSQLNameDefault("categoria_id"))
                                .eq(String.valueOf(String.valueOf(formularioCompraCategoriaDBList.get(0).getId_servidor()))))
                .list();

        Log.d("Productos list", formularioCompraDetalleList.toString());

        adapter = new FormComprasAdapter(getActivity(), fragmentManager, formularioCompraDetalleList, 0);
        listaCompras.setAdapter(adapter);
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

                spinnerComprasTab2.setOnItemSelectedListener(FormComprasFragment.this);

                return v;
            }
        };
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComprasTab2.setAdapter(categoriasAdapter);
        spinnerComprasTab2.setSelection(0);

    }

    private void populateItemsAgregados() {

        //Lista de productos
        listaCompras.setAdapter(null);

        formularioCompraDetalleList = Select.from(DetalleCompra.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("cabecera_compra_id"))
                                .eq(String.valueOf(formularioCompraCabeceraDB.getId().intValue())),
                        Condition.prop(NamingHelper.toSQLNameDefault("producto_id"))
                                .eq(String.valueOf(0)),
                        Condition.prop(NamingHelper.toSQLNameDefault("producto_eliminado"))
                                .notEq(1),
                        Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync"))
                                .eq(formularioCompraCabeceraDB.getCodigo_formulario_sync()))
                .list();

        Log.d("Productos list", formularioCompraDetalleList.toString());

        adapter = new FormComprasAdapter(getActivity(), fragmentManager, formularioCompraDetalleList, 1);
        listaCompras.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinnerArea:
                check_area=check_area+1;
                Log.d("item area", spinnerArea.getAdapter().getItem(position).toString());
                if (check_area > 1) {
                    formularioCompraCabeceraDB.setArea(spinnerArea.getAdapter().getItem(position).toString());
                    formularioCompraCabeceraDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    formularioCompraCabeceraDB.setUpdated_by(preferenceManager.getUserId());
                    formularioCompraCabeceraDB.save();
                    //Update tabla sincronizar
                    Sincronizar sincronizarCabeceraCompraDB = new Sincronizar(
                            "formulariocompra__cabecera_compras",
                            formularioCompraCabeceraDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarCabeceraCompraDB.save();
                }
                Log.d("formulario area", formularioCompraCabeceraDB.getArea());
                break;
            case R.id.spinnerPrioridad:
                check_prioridad=check_prioridad+1;
                Log.d("item prioridad", spinnerPrioridad.getAdapter().getItem(position).toString());
                if (check_prioridad > 1) {
                    formularioCompraCabeceraDB.setPrioridad(spinnerPrioridad.getAdapter().getItem(position).toString());
                    formularioCompraCabeceraDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    formularioCompraCabeceraDB.setUpdated_by(preferenceManager.getUserId());
                    formularioCompraCabeceraDB.save();
                    //Update tabla sincronizar
                    Sincronizar sincronizarCabeceraCompraDB = new Sincronizar(
                            "formulariocompra__cabecera_compras",
                            formularioCompraCabeceraDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarCabeceraCompraDB.save();
                }
                Log.d("formulario prioridad", formularioCompraCabeceraDB.getPrioridad());
                break;

            case R.id.spinnerEmpresaGrupo:
                check_empresa=check_empresa+1;
                Log.d("item empresa", spinnerEmpresaGrupo.getAdapter().getItem(position).toString());
                if (check_empresa > 1) {
                    formularioCompraCabeceraDB.setEmpresa_del_grupo(spinnerEmpresaGrupo.getAdapter().getItem(position).toString());
                    formularioCompraCabeceraDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    formularioCompraCabeceraDB.setUpdated_by(preferenceManager.getUserId());
                    formularioCompraCabeceraDB.save();
                    //Update tabla sincronizar
                    Sincronizar sincronizarCabeceraCompraDB = new Sincronizar(
                            "formulariocompra__cabecera_compras",
                            formularioCompraCabeceraDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarCabeceraCompraDB.save();
                }
                Log.d("formulario empresa", formularioCompraCabeceraDB.getEmpresa_del_grupo());
                break;

            case R.id.spinnerComprasTab2:
                Log.d("item categorias", spinnerComprasTab2.getAdapter().getItem(position).toString());

                if (!Objects.equals(spinnerComprasTab2.getAdapter().getItem(position).toString(), "Otros")) {

                    btnAgregarProducto.setVisibility(View.GONE);

                    FormularioCompraCategoria categoriaCompra = Select.from(FormularioCompraCategoria.class)
                            .where(Condition.prop(NamingHelper.toSQLNameDefault("descripcion"))
                                    .eq(spinnerComprasTab2.getAdapter().getItem(position).toString())).first();

                    int categoria_id = categoriaCompra.getId_servidor();

                    Log.d("categoria pos id", String.valueOf(categoria_id));

                    //Lista de productos
                    listaCompras.setAdapter(null);

                    formularioCompraDetalleList = Select.from(DetalleCompra.class)
                            .where(
                                    Condition.prop(NamingHelper.toSQLNameDefault("cabecera_compra_id"))
                                            .eq(String.valueOf(cabecera_compra_id)),
                                    Condition.prop(NamingHelper.toSQLNameDefault("categoria_id"))
                                            .eq(String.valueOf(categoria_id)))
                            .list();

                    Log.d("Productos list", formularioCompraDetalleList.toString());

                    adapter = new FormComprasAdapter(getActivity(), fragmentManager, formularioCompraDetalleList, 0);
                    listaCompras.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {

                    btnAgregarProducto.setVisibility(View.VISIBLE);

                    populateItemsAgregados();

                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Compras");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private class InputFilterMinMax implements InputFilter {

        private int min, max;

        InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

//        InputFilterMinMax(String min, String max) {
//            this.min = Integer.parseInt(min);
//            this.max = Integer.parseInt(max);
//        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
                Log.d("numer format ex", nfe.toString());
            }
            return "";
        }
    }

    private class CustomWatcher implements TextWatcher {

        private boolean mWasEdited = false;
        private View view;

        CustomWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable editable) {

            if (mWasEdited){
                mWasEdited = false;
                return;
            }

            switch(view.getId()) {
                case R.id.edtSolicitadoPor:
                    formularioCompraCabeceraDB.setSolicitado_por(editable.toString());
                    formularioCompraCabeceraDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    formularioCompraCabeceraDB.setUpdated_by(preferenceManager.getUserId());
                    formularioCompraCabeceraDB.save();
                    Log.d("solicitado por", formularioCompraCabeceraDB.getSolicitado_por());
                    //Update tabla Sincronizar
                    Sincronizar sincronizarCabeceraSolicitadoDB = new Sincronizar(
                            "formulariocompra__cabecera_compras",
                            formularioCompraCabeceraDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarCabeceraSolicitadoDB.save();
                    break;

                case R.id.edtEnviadoPor:
                    formularioCompraCabeceraDB.setEnviado_por(editable.toString());
                    formularioCompraCabeceraDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    formularioCompraCabeceraDB.setUpdated_by(preferenceManager.getUserId());
                    formularioCompraCabeceraDB.save();
                    //Update tabla Sincronizar
                    Sincronizar sincronizarCabeceraEnviadoDB = new Sincronizar(
                            "formulariocompra__cabecera_compras",
                            formularioCompraCabeceraDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarCabeceraEnviadoDB.save();
                    Log.d("enviado por", formularioCompraCabeceraDB.getEnviado_por());
                    break;
            }

        }
    }

    private class SpinnerAdapter extends ArrayAdapter<String> {

        SpinnerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
        {
            View v;

            // If this is the initial dummy entry, make it hidden
            if (position == 0) {
                TextView tv = new TextView(getContext());
                tv.setHeight(0);
                tv.setVisibility(View.GONE);
                v = tv;
            }
            else {
                // Pass convertView as null to prevent reuse of special case views
                v = super.getDropDownView(position, null, parent);
            }

            // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
            parent.setVerticalScrollBarEnabled(false);
            return v;
        }
    }

}
*/
