/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.FormularioCategoria;
import zentcode02.parks.dbModels.FormularioModelo;
import zentcode02.parks.dbModels.FormularioTravesia;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class FormCategoriasHistorial extends Fragment {

    private ListView listaCategorias;
    private int travesia_id;
    private String colorButton = "#";

    public FormCategoriasHistorial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.categorias_historial_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categorias de Formularios");

        //GET ARGS FRAGMENT
        travesia_id = getArguments().getInt("travesia_id");

        listaCategorias = (ListView) view.findViewById(R.id.listaCategoriasHistorial);

        populateCategoriasFromDB(travesia_id);

        return view;
    }

    private void populateCategoriasFromDB(int travesia_id) {

        List<String> lista_form_travesias_id = new ArrayList<>();

        //Query formularios travesias que correspondan a la travesia actual
        List<FormularioTravesia> formularioTravesiaList = Select.from(FormularioTravesia.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("travesia_id")).eq(travesia_id))
                .list();

        //Traer los ids de los formularios modelo
        for (int i = 0; i < formularioTravesiaList.size(); i++) {
            lista_form_travesias_id.add(String.valueOf(formularioTravesiaList.get(i).getFormulario_modelo_id()));
        }

        StringBuilder queryModelos = new StringBuilder(NamingHelper.toSQLNameDefault("formulariomodelo_id") +" IN (");

        for (int i = 0; i < lista_form_travesias_id.size(); i++) {
            queryModelos.append("?").append(i == lista_form_travesias_id.size() - 1 ? ")" : ",");
        }

        //Query formulario modelos que correspondan a los formularios travesias
        List<FormularioModelo> formularioModelosList = FormularioModelo.find(FormularioModelo.class,
                queryModelos.toString(), lista_form_travesias_id.toArray(new String[lista_form_travesias_id.size()]));

        List<String> categoriasList = new ArrayList<>();

        //Obtener los ids de las categorias disponibles en formularios modelos
        for (int i = 0; i < formularioModelosList.size(); i++) {
            //Comprobar que no se carguen Ids de categorias repetidos
            if (i == 0) {
                categoriasList.add(String.valueOf(formularioModelosList.get(i).getCategoria_id()));
            } else {
                for (int  j = 0; j < categoriasList.size(); j++) {
                    if (!categoriasList.get(j).equals(String.valueOf(formularioModelosList.get(i).getCategoria_id()))) {
                        categoriasList.add(String.valueOf(formularioModelosList.get(i).getCategoria_id()));
                    }
                }
            }
        }

        populateCategories(categoriasList);

    }

    private void populateCategories(List<String> categoriaIds) {

        StringBuilder queryCategorias = new StringBuilder(NamingHelper.toSQLNameDefault("formulariocategorias_id") +" IN (");

        for (int i = 0; i < categoriaIds.size(); i++) {
            queryCategorias.append("?").append(i == categoriaIds.size() - 1 ? ")" : ",");
        }

        List<FormularioCategoria> formularioCategoriaList = FormularioCategoria.find(FormularioCategoria.class,
                queryCategorias.toString(), categoriaIds.toArray(new String[categoriaIds.size()]));

        CategoriasHistorialAdapter adapter = new CategoriasHistorialAdapter(getActivity(), formularioCategoriaList);
        listaCategorias.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categorias de Formularios");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FormularioCategoria categoria = (FormularioCategoria) parent.getAdapter().getItem(position);
                int categoria_id  = categoria.getId_servidor();

                FormModelosHistorial fragment = new FormModelosHistorial();

                Bundle args_id = new Bundle();
                args_id.putInt("categoria_id", categoria_id);
                args_id.putInt("travesia_id", travesia_id);
                fragment.setArguments(args_id);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();

            }
        });
    }

    private class CategoriasHistorialAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Activity activity;
        private List<FormularioCategoria> categoriasList;

        CategoriasHistorialAdapter(Activity activity, List<FormularioCategoria> items) {
            this.activity = activity;
            this.categoriasList = items;
        }

        @Override
        public int getCount() {
            return categoriasList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoriasList.get(position);
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
                convertView=inflater.inflate(R.layout.layout_txtlist,null);
                holder.categoria = (TextView) convertView.findViewById(R.id.txtList);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            FormularioCategoria categoria = categoriasList.get(position);

            holder.categoria.setText(categoria.getNombre());
            holder.categoria.setBackgroundColor(Color.parseColor(colorButton+categoria.getColor()));
            holder.categoria.setTextColor(Color.WHITE);
            holder.categoria.setPadding(5, 50, 5, 50);

            return convertView;
        }

        class ViewHolder {
            TextView categoria;
        }

    }

}
*/
