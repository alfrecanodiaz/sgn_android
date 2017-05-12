/*
package zentcode02.parks.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.adapters.FormMantenimientoAdapter;
import zentcode02.parks.dbModels.CabeceraMantenimientoDB;
import zentcode02.parks.dbModels.CategoriaMaquinariaDB;
import zentcode02.parks.dbModels.DetalleMantenimientoDB;
import zentcode02.parks.dbModels.SincronizarDB;
import zentcode02.parks.models.StaticItems;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.DatePickerFragment;
import zentcode02.parks.utils.GlobalMethods;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormMantenimientosFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private PreferenceManager preferenceManager;
    private Global globalMethods;
    private ListView listaMantenimiento;
    private int form_type;
    private int cabecera_mantenimiento_id;
    private TextView txtNombreUnidad, txtFechaCabecera;
    private Button btnAgregarMaquinaria;
    private EditText solicitadoPor;
    private Spinner spinnerArea, spinnerCategoriaMantenimiento;
    private CabeceraMantenimiento cabeceraMantenimientoDB;
    public static final int DATEPICKER_FRAGMENT=1; // adding this line
    private int check_area=0;
    private Sincronizar sincronizarDB;
    private FragmentManager fragmentManager;
    private FormMantenimientoAdapter adapter;
    private TabHost tab;
    private List<DetalleMantenimiento> detalleMantenimientoDBList;

    public FormMantenimientosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getActivity());
        fragmentManager = getFragmentManager();
        globalMethods = new Global(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.form_mantenimientos_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Mantenimiento");

        tab = (TabHost) view.findViewById(R.id.tabhostMantenimiento);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Cabecera");
        spec1.setIndicator("Cabecera");
        spec1.setContent(R.id.tabMantenimiento1);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Detalle");
        spec2.setIndicator("Detalle");
        spec2.setContent(R.id.tabMantenimiento2);
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
        form_type = getArguments().getInt("form_type");
        Log.d("form type", String.valueOf(form_type));

        //Al crear un formulario
        //ID obtenido como parametro del fragmento Formlist
        cabecera_mantenimiento_id = form_type;

        cabeceraMantenimientoDB = CabeceraMantenimiento.findById(CabeceraMantenimiento.class, cabecera_mantenimiento_id);

        txtNombreUnidad = (TextView) view.findViewById(R.id.txtNombreUnidadMantenimiento);
        txtFechaCabecera = (TextView) view.findViewById(R.id.txtFechaCabecera);
        solicitadoPor = (EditText) view.findViewById(R.id.edtSolicitadoPor);
        spinnerArea = (Spinner) view.findViewById(R.id.spinnerArea);
        spinnerCategoriaMantenimiento = (Spinner) view.findViewById(R.id.spinnerMantenimientoTab2);
        btnAgregarMaquinaria = (Button) view.findViewById(R.id.btnAgregarMaquinaria);
        btnAgregarMaquinaria.setOnClickListener(onClickListener);
        btnAgregarMaquinaria.setVisibility(View.GONE);

        //Adaptadores Selects Area
        SpinnerAdapter spinnerAdapterArea = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
                globalMethods.getListAreaMantenimiento());
        spinnerAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(spinnerAdapterArea);

        if (!Objects.equals(cabeceraMantenimientoDB.getArea(), "")) {
            spinnerSetSelectedItem(spinnerArea, spinnerAdapterArea, cabeceraMantenimientoDB.getArea());
        }

        //Validación y guardado de los Edit Text en la Cabecera Mantenimiento
        solicitadoPor.addTextChangedListener(new CustomWatcher(solicitadoPor));

        spinnerArea.setOnItemSelectedListener(this);

        txtFechaCabecera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.setTargetFragment(FormMantenimientosFragment.this, DATEPICKER_FRAGMENT);
                fragment.show(getFragmentManager(), "datePicker");
            }
        });

        listaMantenimiento = (ListView) view.findViewById(R.id.lv_formMantenimientoTab2);
        populateCabeceraMantenimiento();
        populateDetalleMantenimiento();

        return view;
    }

    private void spinnerSetSelectedItem(Spinner spinner, SpinnerAdapter adapter, String value) {
        int spinnerPosition = adapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }

    private void populateCabeceraMantenimiento() {

        //Populate Cabecera Compras
        String nombre_unidad = preferenceManager.getNombreUnidad();
        Log.d("nombre de unidad", nombre_unidad);
        txtNombreUnidad.setText(nombre_unidad);
        solicitadoPor.setText(cabeceraMantenimientoDB.getSolicitado_por());
        txtFechaCabecera.setText(cabeceraMantenimientoDB.getFecha());

    }

    private void populateDetalleMantenimiento() {

        List<String> listaCategorias = new ArrayList<>();

        List<CategoriaMaquinaria> categoriaMaquinariaDBList = Select.from(CategoriaMaquinaria.class)
                .orderBy(NamingHelper.toSQLNameDefault("descripcion")).list();

        Log.d("categoria maquinaria", categoriaMaquinariaDBList.toString());

        for (int i = 0; i < categoriaMaquinariaDBList.size(); i++) {
            listaCategorias.add(categoriaMaquinariaDBList.get(i).getDescripcion());
            Log.d("lista categorias", listaCategorias.toString());
        }
        listaCategorias.add("Otros");
        Log.d("lista categorias new", listaCategorias.toString());

        detalleMantenimientoDBList = Select.from(DetalleMantenimiento.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("cabecera_mantenimiento_id"))
                                .eq(cabecera_mantenimiento_id),
                        Condition.prop(NamingHelper.toSQLNameDefault("categoria_id"))
                                .eq(String.valueOf(String.valueOf(categoriaMaquinariaDBList.get(0).getId_servidor()))))
                .list();

        Log.d("lista maquinarias", detalleMantenimientoDBList.toString());

        adapter = new FormMantenimientoAdapter(getActivity(), detalleMantenimientoDBList, fragmentManager, 0);
        listaMantenimiento.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Adaptadores Selects Categorias Mantenimiento
        // Populate the spinner using a customized ArrayAdapter that hides the first (dummy) entry
        ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_bg, listaCategorias) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View v;

                v = super.getDropDownView(position, null, parent);

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);

                spinnerCategoriaMantenimiento.setOnItemSelectedListener(FormMantenimientosFragment.this);

                return v;
            }
        };
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaMantenimiento.setAdapter(categoriasAdapter);
        spinnerCategoriaMantenimiento.setSelection(0);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            switch(view.getId()){
                case R.id.btnAgregarMaquinaria:
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    Context context = view.getContext();
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText inputMaquinaria = new EditText(getActivity());
                    inputMaquinaria.setHint("Ingrese el nombre de la maquinaria");
                    inputMaquinaria.setInputType(InputType.TYPE_CLASS_TEXT);

                    layout.addView(inputMaquinaria);

                    alert.setTitle("Agregar maquinaria");
                    alert.setView(layout);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Log.d("input maquinaria", inputMaquinaria.getText().toString());

                            DetalleMantenimiento detalleMantenimientoAdd = new DetalleMantenimiento(
                                    0,
                                    "",
                                    "",
                                    cabeceraMantenimientoDB.getId().intValue(),
                                    0,
                                    cabeceraMantenimientoDB.getCodigo_sync(),
                                    cabeceraMantenimientoDB.getCodigo_formulario_sync(),
                                    "Ninguno",
                                    0,
                                    inputMaquinaria.getText().toString(),
                                    "",
                                    globalMethods.MySqlDateTimeFormat(),
                                    globalMethods.MySqlDateTimeFormat(),
                                    preferenceManager.getUserId(),
                                    preferenceManager.getUserId(),
                                    inputMaquinaria.getText().toString(),
                                    0
                            );
                            long detalle_mantenimiento_insert_id = detalleMantenimientoAdd.save();

                            //GET Last Formulario Compra Detalle ID
                            DetalleMantenimiento detalleMantenimiento = DetalleMantenimiento.findById(DetalleMantenimiento.class, detalle_mantenimiento_insert_id);

                            Log.d("detalle nuevo", detalleMantenimiento.toString());

                            //Save Detalle Compra en la tabla Sincronizar
                            sincronizarDB = new Sincronizar(
                                    "detalle_mantenimientos",
                                    detalleMantenimiento.getId().intValue(),
                                    "insert",
                                    false,
                                    0
                            );
                            sincronizarDB.save();

                            Toast.makeText(getActivity(), "Maquinaria agregada correctamente", Toast.LENGTH_LONG).show();

                            populateItemsAgregados();

                        }
                    });

                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();

                    break;
            }
        }
    };

    private void populateItemsAgregados() {
        //Lista de productos
        listaMantenimiento.setAdapter(null);

        detalleMantenimientoDBList = Select.from(DetalleMantenimiento.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("cabecera_mantenimiento_id"))
                                .eq(String.valueOf(cabeceraMantenimientoDB.getId().intValue())),
                        Condition.prop(NamingHelper.toSQLNameDefault("maquinaria_id"))
                                .eq(String.valueOf(0)),
                        Condition.prop(NamingHelper.toSQLNameDefault("maquinaria_eliminada"))
                                .notEq(1),
                        Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync"))
                                .eq(cabeceraMantenimientoDB.getCodigo_formulario_sync()))
                .list();

        Log.d("Maquinarias list", detalleMantenimientoDBList.toString());

        adapter = new FormMantenimientoAdapter(getActivity(), detalleMantenimientoDBList, fragmentManager, 1);
        listaMantenimiento.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formulario Mantenimiento");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerArea:
                check_area = check_area + 1;
                Log.d("item area", spinnerArea.getAdapter().getItem(position).toString());
                if (check_area > 1) {
                    cabeceraMantenimientoDB.setArea(spinnerArea.getAdapter().getItem(position).toString());
                    cabeceraMantenimientoDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    cabeceraMantenimientoDB.setUpdated_by(preferenceManager.getUserId());
                    cabeceraMantenimientoDB.save();
                    //Update tabla sincronizar
                    sincronizarDB = new Sincronizar(
                            "cabecera_mantenimientos",
                            cabeceraMantenimientoDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarDB.save();
                }
                Log.d("formulario area", cabeceraMantenimientoDB.getArea());
                break;

            case R.id.spinnerMantenimientoTab2:
                if (!Objects.equals(spinnerCategoriaMantenimiento.getAdapter().getItem(position).toString(), "Otros")) {

                    Log.d("item categorias", spinnerCategoriaMantenimiento.getAdapter().getItem(position).toString());

                    btnAgregarMaquinaria.setVisibility(View.GONE);

                    //listaMantenimiento.setAdapter(null);

                    CategoriaMaquinaria categoriaMaquinaria = Select.from(CategoriaMaquinaria.class)
                            .where(Condition.prop(NamingHelper.toSQLNameDefault("descripcion"))
                                    .eq(spinnerCategoriaMantenimiento.getAdapter().getItem(position).toString())).first();

                    int categoria_id = categoriaMaquinaria.getId_servidor();

                    List<DetalleMantenimiento> detalleMantenimientoList = Select.from(DetalleMantenimiento.class)
                            .where(
                                    Condition.prop(NamingHelper.toSQLNameDefault("cabecera_mantenimiento_id"))
                                            .eq(cabecera_mantenimiento_id),
                                    Condition.prop(NamingHelper.toSQLNameDefault("categoria_id"))
                                            .eq(categoria_id))
                            .list();

                    Log.d("detalle list filter", detalleMantenimientoList.toString());

                    adapter = new FormMantenimientoAdapter(getActivity(), detalleMantenimientoList, fragmentManager, 0);
                    listaMantenimiento.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {

                    btnAgregarMaquinaria.setVisibility(View.VISIBLE);
                    populateItemsAgregados();

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case DATEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    // here the part where I get my selected date from the saved variable in the intent and the displaying it.
                    Bundle bundle=data.getExtras();
                    String resultDate = bundle.getString("selectedDate");
                    txtFechaCabecera.setText(resultDate);
                    Log.d("cabecera", String.valueOf(cabeceraMantenimientoDB.getId()));
                    cabeceraMantenimientoDB.setFecha(resultDate);
                    cabeceraMantenimientoDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    cabeceraMantenimientoDB.setUpdated_by(preferenceManager.getUserId());
                    cabeceraMantenimientoDB.save();
                    Log.d("cabecera fecha", cabeceraMantenimientoDB.getFecha());
                    sincronizarDB = new Sincronizar(
                            "cabecera_mantenimientos",
                            cabeceraMantenimientoDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    sincronizarDB.save();
                }
                break;
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
                    cabeceraMantenimientoDB.setSolicitado_por(editable.toString());
                    cabeceraMantenimientoDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                    cabeceraMantenimientoDB.setUpdated_by(preferenceManager.getUserId());
                    cabeceraMantenimientoDB.save();
                    Log.d("solicitado por", cabeceraMantenimientoDB.getSolicitado_por());
                    //Update tabla Sincronizar
                    sincronizarDB = new Sincronizar(
                            "cabecera_mantenimientos",
                            cabeceraMantenimientoDB.getId().intValue(),
                            "update",
                            false,
                            0
                    );
                    cabeceraMantenimientoDB.save();
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
