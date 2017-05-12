/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.FormularioTravesia;
import zentcode02.parks.dbModels.Formularios;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormListHistorial extends Fragment {

    private ListView listForms;
    private static final String TYPE_CHECK = "check";
    private static final String TYPE_COMPRA = "compra";
    private static final String TYPE_MANTENIMIENTO = "mantenimiento";
    private FragmentManager fragmentManager;

    public FormListHistorial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.form_list_historial_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista de Formularios");

        fragmentManager = getFragmentManager();

        //GET ARGS FRAGMENT
        int travesia_id = getArguments().getInt("travesia_id");
        int id_formulario_modelo = getArguments().getInt("id_formulario_modelo");
        String nombre_formulario_modelo = getArguments().getString("nombre_formulario_modelo");
        String categoria_nombre = getArguments().getString("categoria_nombre");

        TextView txtFormModeloNombre = (TextView) view.findViewById(R.id.txtFormsListHistorial);
        txtFormModeloNombre.setText(nombre_formulario_modelo);

        listForms = (ListView) view.findViewById(R.id.lvFormListHistorial);
        populateFormsListFromDB(categoria_nombre, id_formulario_modelo, travesia_id);

        return view;
    }

    private void populateFormsListFromDB(String categoria_nombre, int id_formulario_modelo, int travesia_id) {

        JSONObject cabecera;
        JSONArray cabeceraArr;
        JSONObject cabeceraObj;
        List<Formularios> toRemoveFormularios = new ArrayList<>();

        FormularioTravesia formularioTravesia = Select.from(FormularioTravesia.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("formulario_modelo_id")).eq(id_formulario_modelo))
                .where(Condition.prop(NamingHelper.toSQLNameDefault("travesia_id")).eq(travesia_id))
                .first();

        int form_travesia_id = formularioTravesia.getId_servidor();

        List<Formularios> formulariosList = Select.from(Formularios.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("formulario_travesia_id")).eq(form_travesia_id))
                .list();

        //Obtener el Formulario que pertenece a la categoria, remover los que no
        for (int i = 0; i < formulariosList.size(); i++) {
            try {
                cabecera = new JSONObject(formulariosList.get(i).getCabecera());
                cabeceraArr = cabecera.getJSONArray("cabecera");
                cabeceraObj = new JSONObject();
                for (int j = 0; j < cabeceraArr.length(); j++) {
                    cabeceraObj = cabeceraArr.getJSONObject(j);
                }
                if (!Objects.equals(cabeceraObj.getString("nombre"), categoria_nombre)) {
                    toRemoveFormularios.add(formulariosList.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        formulariosList.removeAll(toRemoveFormularios);

        FormsListHistorialAdapter adapter = new FormsListHistorialAdapter(getActivity(), formulariosList);
        listForms.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listForms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int cabecera_id = 0;
                String cabecera_tipo = "";
                Bundle args_id = new Bundle();

                Formularios formulario = (Formularios) parent.getAdapter().getItem(position);

                try {
                    JSONObject cabecera = new JSONObject(formulario.getCabecera());
                    JSONArray cabeceraArr = cabecera.getJSONArray("cabecera");
                    for (int i = 0; i < cabeceraArr.length(); i ++) {
                        JSONObject cabeceraObj = cabeceraArr.getJSONObject(i);
                        cabecera_id = cabeceraObj.getInt("id");
                        cabecera_tipo = cabeceraObj.getString("nombre");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                switch (cabecera_tipo) {
                    case TYPE_CHECK:

                        FormCheckHistorial fragmentCheck = new FormCheckHistorial();

                        args_id.putString("codigo_sync", formulario.getCodigo_sync());
                        args_id.putString("tipo_form", "sincronizado");
                        fragmentCheck.setArguments(args_id);

                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.fragment_container, fragmentCheck)
                                .addToBackStack(null).commit();

                        break;

                    case TYPE_COMPRA:

                        FormComprasHistorial fragmentCompras = new FormComprasHistorial();

//                        args_id.putInt("cabecera_id", cabecera_id);
                        args_id.putString("codigo_sync", formulario.getCodigo_sync());
                        args_id.putString("tipo_form", "sincronizado");
                        fragmentCompras.setArguments(args_id);

                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.fragment_container, fragmentCompras)
                                .addToBackStack(null).commit();

                        break;

                    case TYPE_MANTENIMIENTO:

                        FormMantenimientoHistorial fragmentMantenimiento = new FormMantenimientoHistorial();

                        args_id.putString("codigo_sync", formulario.getCodigo_sync());
                        args_id.putString("tipo_form", "sincronizado");
                        fragmentMantenimiento.setArguments(args_id);

                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.fragment_container, fragmentMantenimiento)
                                .addToBackStack(null).commit();

                        break;
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista de Formularios");
    }

    private class FormsListHistorialAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Activity activity;
        private List<Formularios> formsList;

        FormsListHistorialAdapter(Activity activity, List<Formularios> items) {
            this.activity = activity;
            this.formsList = items;
        }

        @Override
        public int getCount() {
            return formsList.size();
        }

        @Override
        public Object getItem(int position) {
            return formsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if(inflater==null){
                inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView ==null){
                holder = new ViewHolder();
                convertView=inflater.inflate(android.R.layout.simple_list_item_1,null);
                holder.formulario = (TextView) convertView.findViewById(android.R.id.text1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Formularios formulario = formsList.get(position);

            holder.formulario.setText(formulario.getNombre() + "-" + String.valueOf(position+1));
            holder.formulario.setGravity(Gravity.CENTER_HORIZONTAL);
            holder.formulario.setPadding(0, 20, 0, 0);

            return convertView;
        }

        class ViewHolder {
            TextView formulario;
        }

    }

}
*/
