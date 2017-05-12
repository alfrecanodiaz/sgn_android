package zentcode02.parks.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.FormularioCategoria;
import zentcode02.parks.dbModels.FormularioModelo;
import zentcode02.parks.dbModels.FormularioTravesia;
import zentcode02.parks.dbModels.Travesia;
import zentcode02.parks.helpers.db.DBConfig;
import zentcode02.parks.network.PreferenceManager;

public class FormCategoriesFragment extends Fragment {

    private RecyclerView recycler_view_categorias;
    private PreferenceManager preferenceManager;
    private Integer travesia_id;

    public FormCategoriesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getContext());
        travesia_id = preferenceManager.getTravesiaId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.formcategories_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categorias de Formularios");

        recycler_view_categorias = (RecyclerView) view.findViewById(R.id.recycler_view_categorias);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view_categorias.setLayoutManager(layoutManager);

        getFormulariosTravesia();

        return view;
    }

    private void getFormulariosTravesia() {
        Travesia travesia = Select.from(Travesia.class).where(
                Condition.prop(DBConfig.FK_ID).eq(String.valueOf(travesia_id))
        ).first();

        List<FormularioTravesia> formularios_travesia = travesia.getFormulariosTravesia();

        List<String> aux = new ArrayList<>();

        for (FormularioTravesia fm : formularios_travesia) {
            aux.add(String.valueOf(fm.getFormulario_modelo_id()));
        }

        getFormulariosModelo(aux);
    }

    private void getFormulariosModelo(List<String> aux) {
        StringBuilder query = new StringBuilder(DBConfig.FK_ID +" IN (");

        for (int i = 0; i < aux.size(); i++) {
            query.append("?").append(i == aux.size() - 1 ? ")" : ",");
        }

        List<FormularioModelo> formularios_modelo = FormularioModelo.find(FormularioModelo.class,
                query.toString(), aux.toArray(new String[aux.size()]));

        List<String> categorias = new ArrayList<>();

        for (FormularioModelo fm : formularios_modelo) {
            if (!categorias.contains(String.valueOf(fm.getCategoria_id()))) {
                categorias.add(String.valueOf(fm.getCategoria_id()));
            }
        }

        getCategorias(categorias);
    }

    private void getCategorias(List<String> aux) {
        StringBuilder query = new StringBuilder(DBConfig.FK_ID +" IN (");

        for (int i = 0; i < aux.size(); i++) {
            query.append("?").append(i == aux.size() - 1 ? ")" : ",");
        }

        List<FormularioCategoria> fm_categorias = FormularioCategoria.find(FormularioCategoria.class,
                query.toString(), aux.toArray(new String[aux.size()]));

        setAdapter(fm_categorias);
    }

    private void setAdapter(List<FormularioCategoria> categorias) {
        CategoriasRecyclerViewAdapter adapter = new CategoriasRecyclerViewAdapter(getContext(), categorias);
        recycler_view_categorias.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class CategoriasRecyclerViewAdapter extends RecyclerView.Adapter<CategoriasRecyclerViewAdapter.ItemsViewHolder> {

        private List<FormularioCategoria> categorias;
        private Context mContext;

        private CategoriasRecyclerViewAdapter(Context context, List<FormularioCategoria> items) {
            this.mContext = context;
            this.categorias = items;
        }

        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_txtlist, parent, false);
            return new ItemsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {
            FormularioCategoria categoria = categorias.get(position);

            holder.txt_categoria.setText(categoria.getNombre());
            holder.txt_categoria.setBackgroundColor(Color.parseColor(categoria.getColor()));
            holder.txt_categoria.setTextColor(Color.WHITE);
        }

        @Override
        public int getItemCount() {
            return categorias.size();
        }

        class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View view;
            TextView txt_categoria;

            ItemsViewHolder(View view) {
                super(view);
                this.view = view;
                this.txt_categoria = (TextView) view.findViewById(R.id.txtList);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                FormularioCategoria categoria = categorias.get(getAdapterPosition());

                preferenceManager.clearNombreCategoria();
                preferenceManager.saveNombreCategoria(categoria.getNombre());

                FormModelosListFragment fragment = new FormModelosListFragment();

                Bundle args_id = new Bundle();
                args_id.putInt("categoria_id", categoria.getId_servidor());
                fragment.setArguments(args_id);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categorias de Formularios");
    }
}