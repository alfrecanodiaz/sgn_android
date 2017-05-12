package zentcode02.parks.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.CabeceraCheck;
import zentcode02.parks.dbModels.CabeceraCheckCopia;
import zentcode02.parks.dbModels.CabeceraCompra;
import zentcode02.parks.dbModels.CabeceraMantenimiento;
import zentcode02.parks.dbModels.DetalleCompra;
import zentcode02.parks.dbModels.DetalleMantenimiento;
import zentcode02.parks.dbModels.FormularioCompraProducto;
import zentcode02.parks.dbModels.FormularioTravesia;
import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.dbModels.ItemCheck;
import zentcode02.parks.dbModels.ItemCheckCopia;
import zentcode02.parks.dbModels.Maquinaria;
import zentcode02.parks.dbModels.SeccionCheck;
import zentcode02.parks.dbModels.SeccionCheckCopia;
import zentcode02.parks.helpers.db.CreateDBHelper;
import zentcode02.parks.helpers.db.DBConfig;
import zentcode02.parks.helpers.db.UpdateDBHelper;
import zentcode02.parks.network.Config;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.DBHelper;
import zentcode02.parks.utils.Helper;

public class FormsListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recycler_view_forms;
    private Integer travesia_id;
    private Integer fm_modelo_id;
    private Integer user_id;
    private String fm_modelo_nombre, tipo_categoria, codigo_sync, codigo_formulario_sync;
    private Long cabecera_ins_id;
    private FormularioTravesia fm_travesia;
    private FragmentManager fragmentManager;

    private static final String TYPE_CHECK = "check";
    private static final String TYPE_COMPRA = "compra";
    private static final String TYPE_MANTENIMIENTO = "mantenimiento";

    private LinearLayout empty_form_container;

    public FormsListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        travesia_id = preferenceManager.getTravesiaId();
        Integer nro_app_sync = preferenceManager.getNroAppSync();
        user_id = preferenceManager.getUserId();
        fm_modelo_id = getArguments().getInt("modelo_id");
        fm_modelo_nombre = getArguments().getString("nombre_modelo");
        tipo_categoria = getArguments().getString("tipo_categoria");
        codigo_sync = travesia_id + "-" + nro_app_sync;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.formslist_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista de Formularios");

        empty_form_container = (LinearLayout) view.findViewById(R.id.empty_form_list_container) ;
        empty_form_container.setVisibility(View.GONE);

        TextView txt_titulo_form = (TextView) view.findViewById(R.id.txt_titulo_form);
        txt_titulo_form.setText(fm_modelo_nombre);

        Button btn_agregar = (Button) view.findViewById(R.id.btn_agregar);
        btn_agregar.setOnClickListener(this);

        recycler_view_forms = (RecyclerView) view.findViewById(R.id.recycler_view_forms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view_forms.setLayoutManager(layoutManager);

        new PopulateFormList().execute();

        return view;
    }

    private class PopulateFormList extends AsyncTask<Void, Void, List<Form>> {

        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = Helper.showProgressDialog(getContext(), Config.DATA_PROCESS_MSG);
            getFormularioTravesia();
        }

        @Override
        protected List<Form> doInBackground(Void... params) {
            return getFormularios();
        }

        @Override
        protected void onPostExecute(List<Form> result) {
            super.onPostExecute(result);
            Helper.hideProgressDialog(loading);
            if (result.size() > 0) {
                setAdapter(result);
            } else {
                empty_form_container.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getFormularioTravesia() {
        fm_travesia = Select.from(FormularioTravesia.class).where(
                Condition.prop(DBConfig.F_MODELO_ID).eq(fm_modelo_id),
                Condition.prop(DBConfig.TRAVESIA_ID).eq(travesia_id)
        ).first();
    }

    private List<Form> getFormularios() {
        List<Formularios> formularios = Formularios.findWithQuery(Formularios.class, getRawQuery(), null);
        Log.d("99 formularios ", formularios.toString());
        return setFormularios(formularios);
    }

    private String getRawQuery() {
        return DBConfig.SELECT + DBConfig.FORMULARIOS+".*" + DBConfig.FROM + DBConfig.FORMULARIOS +
                DBConfig.LEFT_JOIN + DBConfig.FORMULARIO_TRAVESIA +
                DBConfig.ON + DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.FK_ID + DBConfig.EQUAL +
                DBConfig.FORMULARIOS+"."+DBConfig.F_TRAVESIA_ID +
                DBConfig.LEFT_JOIN + DBConfig.FORMULARIO_MODELO +
                DBConfig.ON + DBConfig.FORMULARIO_MODELO+"."+DBConfig.FK_ID + DBConfig.EQUAL +
                DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.F_MODELO_ID +
                DBConfig.WHERE + DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.TRAVESIA_ID + DBConfig.EQUAL + travesia_id +
                DBConfig.AND + DBConfig.FORMULARIO_MODELO+"."+DBConfig.FK_ID + DBConfig.EQUAL + fm_modelo_id +
                DBConfig.AND + DBConfig.FORMULARIO_TRAVESIA+"."+DBConfig.FK_ID + DBConfig.EQUAL + fm_travesia.getId_servidor() +
                DBConfig.AND + DBConfig.FORMULARIOS+"."+DBConfig.F_ESTADO + DBConfig.NOT_EQUAL + "'Eliminado'";
    }

    private List<Form> setFormularios(List<Formularios> formularios) {
        List<Form> forms = new ArrayList<>();
        int index = 1;
        if (formularios.size() > 0) {
            for (Formularios formulario : formularios) {
                Form form = new Form();
                form.setId(formulario.getId());
                form.setCodigo_sync(formulario.getCodigo_sync());
                form.setNombre(formulario.getNombre() + "-" + index++);
                form.setEstado(formulario.getEstado());
                if (Objects.equals(formulario.getEstado(), "Finalizado")) {
                    form.setRow2_visibility(View.GONE);
                    form.setRow3_visibility(View.VISIBLE);
                }
                if (Objects.equals(tipo_categoria, TYPE_CHECK)) {
                    form.setType(0);
                    form.setTotal_items(getTotalItemsCheck(formulario.getCodigo_sync()));
                } else if (Objects.equals(tipo_categoria, TYPE_COMPRA)) {
                    form.setType(1);
                    form.setTotal_items(getTotalItemsCompra(formulario.getCodigo_sync()));
                } else if (Objects.equals(tipo_categoria, TYPE_MANTENIMIENTO)) {
                    form.setType(2);
                    form.setTotal_items(getTotalItemsMantenimiento(formulario.getCodigo_sync()));
                }
                forms.add(form);
            }
        }
        return forms;
    }

    private void setAdapter(List<Form> forms) {
        FormsRecyclerViewAdapter adapter = new FormsRecyclerViewAdapter(getContext(), forms);
        recycler_view_forms.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private Integer getTotalItemsCheck(String cod_form_sync) {
        Integer porcn = 0;
        Integer count = 0;
        float porcentaje = 0;

        SeccionCheck seccion = Select.from(SeccionCheck.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(cod_form_sync)
        ).first();

        List<ItemCheck> items = Select.from(ItemCheck.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("seccion_check_id")).eq(String.valueOf(seccion.getId())),
                Condition.prop(NamingHelper.toSQLNameDefault("id_servidor")).eq(0)
        ).list();

        for (ItemCheck item : items) {
            if (item.getSatisfactorio() != 2) {
                porcn = porcn + 1;
            }
            count = count + 1;
        }
        if (count > 0) {
            porcentaje = ((float) porcn) / ((float) count) * 100;
        }
        return Math.round(porcentaje);
    }

    private Integer getTotalItemsCompra(String cod_form_sync) {
        Long count = Select.from(DetalleCompra.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(cod_form_sync),
                Condition.prop(NamingHelper.toSQLNameDefault("cantidad_solicitada")).notEq(0),
                Condition.prop(NamingHelper.toSQLNameDefault("producto_eliminado")).eq(0)
        ).count();
        return count.intValue();
    }

    private Integer getTotalItemsMantenimiento(String cod_form_sync) {
        Long count = Select.from(DetalleMantenimiento.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(cod_form_sync),
                Condition.prop(NamingHelper.toSQLNameDefault("mantenimiento")).notEq("Ninguno"),
                Condition.prop(NamingHelper.toSQLNameDefault("mantenimiento")).notEq("")
        ).count();
        return count.intValue();
    }

    private class createFormOperation extends AsyncTask<Void, Void, Void> {

        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = Helper.showProgressDialog(getContext(), Config.DATA_PROCESS_MSG);
        }

        @Override
        protected Void doInBackground(Void... params) {
            createFormulario();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Helper.hideProgressDialog(loading);
            redirectForm();
        }
    }

    private void createFormulario() {
        Long f_ins_id = CreateDBHelper.createFormulario(fm_travesia, codigo_sync, user_id);
        Formularios fm_ins = Formularios.findById(Formularios.class, f_ins_id);
        DBHelper.saveSync("formularios", fm_ins.getId(), "insert", false, 0);
        codigo_formulario_sync = CreateDBHelper.setCodigoSyncFormulario(fm_ins.getId());

        checkTipoCategoria();
    }

    private void checkTipoCategoria() {
        switch (tipo_categoria) {
            case TYPE_CHECK:
                saveDataCheck();
                break;
            case TYPE_COMPRA:
                saveDataCompra();
                break;
            case TYPE_MANTENIMIENTO:
                saveDataMantenimiento();
                break;
        }
    }

    private void saveDataCheck() {
        CabeceraCheckCopia cabecera_copia = Select.from(CabeceraCheckCopia.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("id_servidor")).eq(fm_travesia.getCabecera_id())
        ).first();

        StringBuilder query_sec = new StringBuilder(NamingHelper.toSQLNameDefault("id_servidor") +" IN (");
        List<String> secciones = fm_travesia.getSecciones();
        for (int i = 0; i < secciones.size(); i++) {
            query_sec.append("?").append(i == secciones.size() - 1 ? ")" : ",");
        }

        List<SeccionCheckCopia> list_sec_copias = SeccionCheckCopia.find(SeccionCheckCopia.class, query_sec.toString(), secciones.toArray(new String[secciones.size()]));

        cabecera_ins_id = CreateDBHelper.createCabeceraCheck(cabecera_copia, codigo_sync, codigo_formulario_sync, user_id);
        DBHelper.saveSync("cabecera_checks", cabecera_ins_id, "insert", false, 0);

        for (SeccionCheckCopia seccion_copia : list_sec_copias) {
            Long seccion_ins_id = CreateDBHelper.createSeccionCheck(seccion_copia, codigo_sync, codigo_formulario_sync, user_id);
            DBHelper.saveSync("seccion_checks", seccion_ins_id, "insert", false, 0);

            List<ItemCheckCopia> item_copias = Select.from(ItemCheckCopia.class).where(
                    Condition.prop(NamingHelper.toSQLNameDefault("seccion_check_id")).eq(seccion_copia.getId_servidor())
            ).list();
            for (ItemCheckCopia item_copia : item_copias) {
                Long item_ins_id = CreateDBHelper.createItemCheck(item_copia, codigo_sync, seccion_ins_id, user_id);
                DBHelper.saveSync("items_check", item_ins_id, "insert", false, 0);
            }
        }
    }

    private void saveDataCompra() {
        cabecera_ins_id = CreateDBHelper.createCabeceraCompra(codigo_sync, codigo_formulario_sync, user_id);
        DBHelper.saveSync("formulariocompra__cabecera_compras", cabecera_ins_id, "insert", false, 0);

        List<FormularioCompraProducto> productos = FormularioCompraProducto.listAll(FormularioCompraProducto.class);
        for (FormularioCompraProducto producto : productos) {
            Long detalle_ins_id = CreateDBHelper.createDetalleCompra(
                    producto.getId_servidor(), cabecera_ins_id, codigo_sync, codigo_formulario_sync, user_id);
            DBHelper.saveSync("formulariocompra__detalle_compras", detalle_ins_id, "insert", false, 0);
        }
    }

    private void saveDataMantenimiento() {
        cabecera_ins_id = CreateDBHelper.createCabeceraMantenimiento(codigo_sync, codigo_formulario_sync, user_id);
        DBHelper.saveSync("cabecera_mantenimientos", cabecera_ins_id, "insert", false, 0);

        List<Maquinaria> maquinarias = Maquinaria.listAll(Maquinaria.class);
        for (Maquinaria maquinaria : maquinarias) {
            Long detalle_ins_id = CreateDBHelper.createDetalleMantenimiento(
                    maquinaria.getId_servidor(), cabecera_ins_id, codigo_sync, codigo_formulario_sync, user_id);
            DBHelper.saveSync("detalle_mantenimientos", detalle_ins_id, "insert", false, 0);
        }
    }

    private void redirectForm() {
        switch (tipo_categoria) {
            case TYPE_CHECK:
                //use cabecera_ins_id
                break;
            case TYPE_COMPRA:

                break;
            case TYPE_MANTENIMIENTO:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_agregar:
                new createFormOperation().execute();
                break;
        }
    }

    @SuppressWarnings("WrongConstant")
    private class FormsRecyclerViewAdapter extends RecyclerView.Adapter<FormsRecyclerViewAdapter.ItemsViewHolder> {

        private List<Form> forms;
        private Context mContext;

        private String label;
        private Integer type;
        private static final int CHECK = 0;
        private static final int COMPRA = 1;
        private static final int MANTENIMIENTO = 2;

        private FormsRecyclerViewAdapter(Context context, List<Form> items) {
            this.mContext = context;
            this.forms = items;
        }

        @Override
        public int getItemViewType(int position) {
            switch (forms.get(position).getType()) {
                case CHECK:
                    type = CHECK;
                    break;
                case COMPRA:
                    type = COMPRA;
                    break;
                case MANTENIMIENTO:
                    type = MANTENIMIENTO;
                    break;
            }
            return type;
        }

        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.formslist_layout, parent, false);
            return new ItemsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {
            Form form = forms.get(position);

            holder.row2.setVisibility(form.getRow2_visibility());
            holder.row3.setVisibility(form.getRow3_visibility());

            holder.txt_form.setText(form.getNombre());

            switch (holder.getItemViewType()) {
                case CHECK:

                    CabeceraCheck check = Select.from(CabeceraCheck.class).where(
                            Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(form.getCodigo_sync())
                    ).first();

                    holder.txt_area.setVisibility(View.GONE);
                    label = "Verificado por: " + check.getVerificado_por();
                    holder.txt_solicitado_por.setText(label);
                    label = form.getTotal_items() + "%";
                    holder.txt_total_items.setText(label);
                    break;

                case COMPRA:
                    CabeceraCompra compra = Select.from(CabeceraCompra.class).where(
                            Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(form.getCodigo_sync())
                    ).first();

                    label = "Solicitado por: " + compra.getSolicitado_por();
                    holder.txt_solicitado_por.setText(label);
                    label = "Area: " + compra.getArea();
                    holder.txt_area.setText(label);
                    holder.txt_total_items.setText(String.valueOf(form.getTotal_items()));
                    break;

                case MANTENIMIENTO:
                    CabeceraMantenimiento mantenimiento = Select.from(CabeceraMantenimiento.class).where(
                            Condition.prop(NamingHelper.toSQLNameDefault("codigo_formulario_sync")).eq(form.getCodigo_sync())
                    ).first();

                    label = "Solicitado por: " + mantenimiento.getSolicitado_por();
                    holder.txt_solicitado_por.setText(label);
                    label = "Area: " + mantenimiento.getArea();
                    holder.txt_area.setText(label);
                    holder.txt_total_items.setText(String.valueOf(form.getTotal_items()));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return forms.size();
        }

        class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View view;
            TextView txt_form, txt_solicitado_por, txt_area, txt_total_items;
            Button btn_eliminar, btn_finalizar, btn_editar;
            LinearLayout row1, row2, row3;

            ItemsViewHolder(View view) {
                super(view);
                this.view = view;
                this.txt_form = (TextView) view.findViewById(R.id.txtFormList);
                this.txt_solicitado_por = (TextView) view.findViewById(R.id.txtSolicitadoPor);
                this.txt_area = (TextView) view.findViewById(R.id.txtArea);
                this.txt_total_items = (TextView) view.findViewById(R.id.count_items);
                this.btn_eliminar = (Button) view.findViewById(R.id.btnFormEliminar);
                this.btn_finalizar = (Button) view.findViewById(R.id.btnFormFinalizar);
                this.btn_editar = (Button) view.findViewById(R.id.btnFormEditar);
                this.row1 = (LinearLayout) view.findViewById(R.id.rowForm1);
                this.row2 = (LinearLayout) view.findViewById(R.id.rowForm2);
                this.row3 = (LinearLayout) view.findViewById(R.id.rowForm3);
                btn_eliminar.setOnClickListener(this);
                btn_finalizar.setOnClickListener(this);
                btn_editar.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Fragment fragment;
                Form form = forms.get(getAdapterPosition());
                switch (v.getId()) {
                    case R.id.btnFormFinalizar:
                        showFinishAlert(form.getId());
                        break;
                    case R.id.btnFormEliminar:
                        showDeleteAlert(form.getId());
                        break;
                    case R.id.btnFormEditar:
                        switch (form.getType()) {
                            case CHECK:

                                break;
                            case COMPRA:

                                break;
                            case MANTENIMIENTO:

                                break;
                        }
                        break;
                }
            }

            void showFinishAlert(final Long form_id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                AlertDialog alertDialog;
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Esta seguro que desea finalizar este Formulario?");
                alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateDBHelper.updateEstadoFormulario(form_id, "Finalizado", user_id);
                        forms.get(getAdapterPosition()).setRow2_visibility(View.GONE);
                        forms.get(getAdapterPosition()).setRow3_visibility(View.VISIBLE);
                        notifyItemChanged(getAdapterPosition());
                        Toast.makeText(mContext, Config.CORE_UPDATE_MSG, Toast.LENGTH_SHORT).show();
                    }
                });
                alertBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = alertBuilder.create();
                alertDialog.show();
            }

            void showDeleteAlert(final Long form_id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                AlertDialog alertDialog;
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Esta seguro que desea eliminar este Formulario?");
                alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateDBHelper.updateEstadoFormulario(form_id, "Eliminado", user_id);
                        forms.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), forms.size());
                        Toast.makeText(mContext, Config.CORE_DELETE_MSG, Toast.LENGTH_SHORT).show();
                    }
                });
                alertBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista de Formularios");
    }

    private class Form {
        private Long id;
        private String codigo_sync;
        private String nombre;
        private String estado;

        private Integer type;
        private Integer total_items;
        private int row2_visibility = View.VISIBLE;
        private int row3_visibility = View.GONE;

        Form() {}

        Long getId() {
            return id;
        }

        void setId(Long id) {
            this.id = id;
        }

        public String getCodigo_sync() {
            return codigo_sync;
        }

        public void setCodigo_sync(String codigo_sync) {
            this.codigo_sync = codigo_sync;
        }

        String getNombre() {
            return nombre;
        }

        void setNombre(String nombre) {
            this.nombre = nombre;
        }

        String getEstado() {
            return estado;
        }

        void setEstado(String estado) {
            this.estado = estado;
        }

        Integer getType() {
            return type;
        }

        void setType(Integer type) {
            this.type = type;
        }

        Integer getTotal_items() {
            return total_items;
        }

        void setTotal_items(Integer total_items) {
            this.total_items = total_items;
        }

        public int getRow2_visibility() {
            return row2_visibility;
        }

        public void setRow2_visibility(int row2_visibility) {
            this.row2_visibility = row2_visibility;
        }

        public int getRow3_visibility() {
            return row3_visibility;
        }

        public void setRow3_visibility(int row3_visibility) {
            this.row3_visibility = row3_visibility;
        }
    }
}