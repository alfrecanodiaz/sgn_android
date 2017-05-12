/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.FormularioModelo;
import zentcode02.parks.dbModels.FormularioTravesia;
import zentcode02.parks.dbModels.Formularios;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormModelosHistorial extends Fragment {

    private ListView listaModelos;
    private int travesia_id_arg;
    private List<FormularioModelo> formularioModeloList;

    public FormModelosHistorial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.modelos_historial_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Formularios");

        //GET ARGS FRAGMENT
        int categoria_id = getArguments().getInt("categoria_id");
        travesia_id_arg = getArguments().getInt("travesia_id");

        listaModelos = (ListView) view.findViewById(R.id.lvModelosHistorial);

        populateModelosFromDB(categoria_id, travesia_id_arg);

        return view;
    }

    private void populateModelosFromDB(int categoria_id, int travesia_id) {

        List<String> lista_form_travesias_id = new ArrayList<>();

        //Query formularios travesias que correspondan a la travesia actual
        List<FormularioTravesia> formularioTravesiaList = Select.from(FormularioTravesia.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("travesia_id"))
                        .eq(travesia_id)).list();

        Log.d("list travesias", formularioTravesiaList.toString());

        for (int i = 0; i < formularioTravesiaList.size(); i++) {
            lista_form_travesias_id.add(String.valueOf(formularioTravesiaList.get(i).getFormulario_modelo_id()));
        }

        //Filtrar Formularios Modelos por el ID de la Categoria

        StringBuilder queryModelos = new StringBuilder(NamingHelper.toSQLNameDefault("formulariomodelo_id") +" IN (");

        for (int i = 0; i < lista_form_travesias_id.size(); i++) {
            queryModelos.append("?").append(i == lista_form_travesias_id.size() - 1 ? ")" : ",");
        }

        formularioModeloList = FormularioModelo.find(FormularioModelo.class,
                queryModelos.toString(), lista_form_travesias_id.toArray(new String[lista_form_travesias_id.size()]));

        Log.d("list modelos", formularioModeloList.toString());

        Log.d("list modelos size", String.valueOf(formularioModeloList.size()));

        //Remover el formulario modelo que no pertenece a la categoria seleccionada
        List<FormularioModelo> toRemoveModelos = new ArrayList<>();
        for (int i = 0; i < formularioModeloList.size(); i++) {
            Log.d("form model categoria id", String.valueOf(formularioModeloList.get(i).getCategoria_id()));
            Log.d("id_formulario_categoria", String.valueOf(categoria_id));
            if (formularioModeloList.get(i).getCategoria_id() != categoria_id) {
                //formularioModeloList.remove(i);
                toRemoveModelos.add(formularioModeloList.get(i));
            }
        }
        formularioModeloList.removeAll(toRemoveModelos);

        countFormsModelosDB();

        */
/*FormModelosHistorialAdapter adapter = new FormModelosHistorialAdapter(getActivity(), formularioModeloList);
        listaModelos.setAdapter(adapter);
        adapter.notifyDataSetChanged();*//*


    }

    private void countFormsModelosDB() {
        final String formularios = NamingHelper.toSQLNameDefault("Formularios");
        final String formulario__travesia = NamingHelper.toSQLNameDefault("FormularioTravesia");
        final String formulario__formulariomodelos = NamingHelper.toSQLNameDefault("FormularioModelo");
        //final String id = NamingHelper.toSQLNameDefault("id");
        final String formulario_travesia_id = NamingHelper.toSQLNameDefault("formulario_travesia_id");
        final String formulario_modelo_id = NamingHelper.toSQLNameDefault("formulario_modelo_id");
        final String travesia_id = NamingHelper.toSQLNameDefault("travesia_id");

        List<Modelo> formulariosModelosToAdapter = new ArrayList<>();
        for (int i = 0; i < formularioModeloList.size(); i++) {
            String rawQuery = "SELECT * FROM " + formularios +
                    " LEFT JOIN " + formulario__travesia +
                    " ON " + formulario__travesia+"."+NamingHelper.toSQLNameDefault("formulario_travesia_id") + " = " + formularios+"."+formulario_travesia_id +
                    " LEFT JOIN " + formulario__formulariomodelos +
                    " ON " + formulario__formulariomodelos+"."+NamingHelper.toSQLNameDefault("formulariomodelo_id") + " = " + formulario__travesia+"."+formulario_modelo_id +
                    " WHERE " + formulario__travesia+"."+travesia_id + " = " + travesia_id_arg +
                    " AND " + formulario__formulariomodelos+"."+NamingHelper.toSQLNameDefault("formulariomodelo_id") + " = " + formularioModeloList.get(i).getId_servidor();

            List<Formularios> formulariosQueryList = Formularios.findWithQuery(Formularios.class, rawQuery, null);
            Log.d("formularios query ", formulariosQueryList.toString());
            Log.d("form modelo id ", String.valueOf(formularioModeloList.get(i).getId_servidor()));
            Modelo formModelos = new Modelo();
            formModelos.setForm_modelo_id(formularioModeloList.get(i).getId_servidor());
            formModelos.setNombre(formularioModeloList.get(i).getNombre());
            formModelos.setCabecera(formularioModeloList.get(i).getCabecera());
            formModelos.setDetalle(formularioModeloList.get(i).getDetalles());
            formModelos.setCant_items(formulariosQueryList.size());
            formulariosModelosToAdapter.add(formModelos);
        }

        FormModelosHistorialAdapter adapter = new FormModelosHistorialAdapter(getActivity(), formulariosModelosToAdapter);
        listaModelos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listaModelos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JSONObject cabeceraObj;
                String tipoForm = "";

                Modelo formModelos = (Modelo) parent.getAdapter().getItem(position);
                //FormularioModelo formularioModelo = (FormularioModelo) parent.getAdapter().getItem(position);
                FormularioModelo formularioModelo = Select.from(FormularioModelo.class).where(Condition.prop(NamingHelper.toSQLNameDefault("formulariomodelo_id")).eq(formModelos.getForm_modelo_id())).first();

                int id_formulario_modelo = formularioModelo.idFormModelo();
                String nombre_formulario_modelo = formularioModelo.getNombre();

                try {
                    JSONObject cabecera = new JSONObject(formularioModelo.getCabecera());
                    Log.d("cabecera obj", cabecera.toString());
                    JSONArray cabeceraArr = cabecera.getJSONArray("cabecera");
                    Log.d("cabecera", cabeceraArr.toString());
                    for (int i = 0; i < cabeceraArr.length(); i ++) {
                        cabeceraObj = cabeceraArr.getJSONObject(i);
                        tipoForm = cabeceraObj.getString("nombre");
                    }
                    Log.d("tipo form", tipoForm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FormListHistorial fragment = new FormListHistorial();

                Bundle args_id = new Bundle();
                args_id.putInt("id_formulario_modelo", id_formulario_modelo);
                args_id.putInt("travesia_id", travesia_id_arg);
                args_id.putString("nombre_formulario_modelo", nombre_formulario_modelo);
                args_id.putString("categoria_nombre", tipoForm);
                fragment.setArguments(args_id);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();

            }
        });

    }

    private class FormModelosHistorialAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Activity activity;
        private List<Modelo> modelosList;

        FormModelosHistorialAdapter(Activity activity, List<Modelo> items) {
            this.activity = activity;
            this.modelosList = items;
        }

        @Override
        public int getCount() {
            return modelosList.size();
        }

        @Override
        public Object getItem(int position) {
            return modelosList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final Modelo formModelo = modelosList.get(position);

            if(inflater==null){
                inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView ==null){
                holder = new ViewHolder();
                convertView=inflater.inflate(R.layout.layout_txtlist,null);
                holder.modelo = (TextView) convertView.findViewById(R.id.txtList);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            FormularioModelo modelo = FormularioModelo.findById(FormularioModelo.class, formModelo.getForm_modelo_id());
            FormularioModelo modelo = Select.from(FormularioModelo.class)
                                                                .where(Condition.prop(NamingHelper.toSQLNameDefault("formulariomodelo_id"))
                                                                        .eq(formModelo.getForm_modelo_id()))
                                                                .first();

            String label_form = modelo.getNombre() + " (" + formModelo.getCant_items() + ")";
            holder.modelo.setText(label_form);

            return convertView;
        }

        class ViewHolder {
            TextView modelo;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Formularios");
    }

    private class Modelo {
        private int form_modelo_id;
        private String nombre;
        private String cabecera;
        private String detalle;
        private int cant_items;

        public Modelo() {

        }

        public Modelo(int form_modelo_id, String nombre, String cabecera, String detalle, int cant_items) {
            this.form_modelo_id = form_modelo_id;
            this.nombre = nombre;
            this.cabecera = cabecera;
            this.detalle = detalle;
            this.cant_items = cant_items;
        }

        public int getForm_modelo_id() {
            return form_modelo_id;
        }

        public void setForm_modelo_id(int form_modelo_id) {
            this.form_modelo_id = form_modelo_id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCabecera() {
            return cabecera;
        }

        public void setCabecera(String cabecera) {
            this.cabecera = cabecera;
        }

        public String getDetalle() {
            return detalle;
        }

        public void setDetalle(String detalle) {
            this.detalle = detalle;
        }

        public int getCant_items() {
            return cant_items;
        }

        public void setCant_items(int cant_items) {
            this.cant_items = cant_items;
        }
    }

}
*/
