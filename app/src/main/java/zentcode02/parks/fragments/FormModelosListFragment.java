package zentcode02.parks.fragments;

import android.content.Context;
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
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.FormularioModelo;
import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.helpers.db.DBConfig;
import zentcode02.parks.network.PreferenceManager;

public class FormModelosListFragment extends Fragment {

    private RecyclerView recycler_view_modelos;
    private Integer categoria_id;
    private Integer travesia_id;

    public FormModelosListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        travesia_id = preferenceManager.getTravesiaId();
        categoria_id = getArguments().getInt("categoria_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.formmodeloslist_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Formularios");

        recycler_view_modelos = (RecyclerView) view.findViewById(R.id.recycler_view_modelos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view_modelos.setLayoutManager(layoutManager);

        getFormularioModelo();

        return view;
    }

    private void getFormularioModelo() {
        List<FormularioModelo> fm_modelo = Select.from(FormularioModelo.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("categoria_id")).eq(String.valueOf(categoria_id))
        ).list();

        setModelo(fm_modelo);
    }

    private void setModelo(List<FormularioModelo> f_modelos) {
        List<Modelo> list_modelos = new ArrayList<>();
        for (FormularioModelo f_modelo : f_modelos) {
            List<Formularios> fm = Formularios.findWithQuery(Formularios.class, getRawQuery(f_modelo.getId_servidor()), null);
            Modelo modelo = new Modelo();
            modelo.setId(f_modelo.getId_servidor());
            modelo.setNombre(f_modelo.getNombre());
            modelo.setCount(fm.size());
            list_modelos.add(modelo);
        }

        setAdapter(list_modelos);
    }

    private String getRawQuery(Integer modelo_id) {
        return DBConfig.SELECT_ALL + DBConfig.FORMULARIOS +
                DBConfig.LEFT_JOIN + DBConfig.FORMULARIO_TRAVESIA +
                DBConfig.ON + DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.FK_ID + DBConfig.EQUAL +
                DBConfig.FORMULARIOS+"."+DBConfig.F_TRAVESIA_ID +
                DBConfig.LEFT_JOIN + DBConfig.FORMULARIO_MODELO +
                DBConfig.ON + DBConfig.FORMULARIO_MODELO+"."+DBConfig.FK_ID + DBConfig.EQUAL +
                DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.F_MODELO_ID +
                DBConfig.WHERE + DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.TRAVESIA_ID + DBConfig.EQUAL + travesia_id +
                DBConfig.AND + DBConfig.FORMULARIO_MODELO+"."+DBConfig.FK_ID + DBConfig.EQUAL + modelo_id +
                DBConfig.AND + DBConfig.FORMULARIOS+"."+DBConfig.F_ESTADO + DBConfig.NOT_EQUAL + "'Eliminado'";
    }

    private void setAdapter(List<Modelo> modelos) {
        ModelosRecyclerViewAdapter adapter = new ModelosRecyclerViewAdapter(getContext(), modelos);
        recycler_view_modelos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class ModelosRecyclerViewAdapter extends RecyclerView.Adapter<ModelosRecyclerViewAdapter.ItemsViewHolder> {

        private List<Modelo> modelos;
        private Context mContext;

        private ModelosRecyclerViewAdapter(Context context, List<Modelo> items) {
            this.mContext = context;
            this.modelos = items;
        }

        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_txtlist, parent, false);
            return new ItemsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {
            Modelo modelo = modelos.get(position);
            String label = modelo.getNombre() + " (" + modelo.getCount() + ") ";
            holder.txt_modelo.setText(label);
        }

        @Override
        public int getItemCount() {
            return modelos.size();
        }

        class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View view;
            TextView txt_modelo;

            ItemsViewHolder(View view) {
                super(view);
                this.view = view;
                this.txt_modelo = (TextView) view.findViewById(R.id.txtList);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Modelo modelo = modelos.get(getAdapterPosition());
                FormularioModelo fm_modelo = Select.from(FormularioModelo.class).where(
                        Condition.prop(DBConfig.FK_ID).eq(modelo.getId())
                ).first();

                FormsListFragment fragment = new FormsListFragment();

                Bundle args_id = new Bundle();
                args_id.putInt("modelo_id", fm_modelo.getId_servidor());
                args_id.putString("nombre_modelo", fm_modelo.getNombre());
                args_id.putString("tipo_categoria", fm_modelo.getCabeceraTipo());
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formularios");
    }

    private class Modelo {

        private long id;
        private String nombre;
        private long count;

        Modelo() {}

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}