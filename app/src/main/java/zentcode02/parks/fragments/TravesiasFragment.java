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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zentcode02.parks.MainActivity;
import zentcode02.parks.R;
import zentcode02.parks.dbModels.Travesia;
import zentcode02.parks.models.Travesias;
import zentcode02.parks.network.Config;
import zentcode02.parks.network.CustomObjectRequest;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.network.ServerRoutes;
import zentcode02.parks.utils.DBHelper;
import zentcode02.parks.utils.Helper;
import zentcode02.parks.helpers.db.SyncDBHelper;

public class TravesiasFragment extends Fragment {

    private RecyclerView recycler_view_travesias;
    private static List<Travesias> list_travesias;
    private String api_token, status_login, nombre_unidad, nombre_modulo;
    private PreferenceManager preferenceManager;
    private Integer unidad_id;
    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;

    public TravesiasFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getContext());
        api_token = preferenceManager.getApiToken();
        unidad_id = getArguments().getInt("unidad_id");
        nombre_unidad = getArguments().getString("nombre_unidad");
        nombre_modulo = getArguments().getString("nombre_modulo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.travesias_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sincronizar Travesia");

        TextView txtTitleTravesias = (TextView) view.findViewById(R.id.txtTitleTravesias);
        txtTitleTravesias.setText("Unidad " + nombre_unidad + ". Seleccionar Travesia:");

        recycler_view_travesias = (RecyclerView) view.findViewById(R.id.recycler_view_travesias);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view_travesias.setLayoutManager(layoutManager);

        getDataFromServer();

        return view;
    }

    private void getDataFromServer() {
        final ProgressDialog pDialog = Helper.showProgressDialog(getContext(), Config.LOADING_MSG);
        String url = ServerRoutes.getTravesiasUrl(unidad_id, api_token);
        CustomObjectRequest request = new CustomObjectRequest(getContext());
        request.SendRequest(Request.Method.GET, url, null, new CustomObjectRequest.ServerCallback() {
            @Override
            public void onResponse(JSONObject response) {
                Helper.hideProgressDialog(pDialog);
                if (response.has("status")) {
                    try {
                        status_login = response.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), status_login, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray travesias = response.getJSONArray("travesias");
                        listaTravesias(travesias);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse() {
                Helper.hideProgressDialog(pDialog);
                Toast.makeText(getContext(), Config.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listaTravesias(JSONArray jsonArray) {
        list_travesias = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Travesias travesias = new Travesias();
                travesias.setDestino(jsonObject.getString("destino"));
                travesias.setResponsable1(jsonObject.getString("responsable1"));
                travesias.setResponsable2(jsonObject.getString("responsable2"));
                travesias.setFecha_inicio(jsonObject.getString("fecha_inicio"));
                travesias.setId(jsonObject.getInt("id"));
                list_travesias.add(travesias);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setAdapter();
    }

    private void setAdapter() {
        TravesiasRecyclerViewAdapter adapter = new TravesiasRecyclerViewAdapter(getContext(), list_travesias);
        recycler_view_travesias.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class TravesiasRecyclerViewAdapter extends RecyclerView.Adapter<TravesiasRecyclerViewAdapter.ItemsViewHolder> {

        private List<Travesias> travesias;
        private Context mContext;

        private TravesiasRecyclerViewAdapter(Context context, List<Travesias> items) {
            this.mContext = context;
            this.travesias = items;
        }

        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_travesialist, parent, false);
            return new ItemsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {
            Travesias travesia = travesias.get(position);

            holder.txt_travesia.setText(travesia.getDestino());
            holder.txt_responsable_uno.setText(travesia.getResponsable1());
            holder.txt_responsable_dos.setText(travesia.getResponsable2());
            holder.txt_fecha_inicio.setText(travesia.getFecha_inicio());
        }

        @Override
        public int getItemCount() {
            return travesias.size();
        }

        class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View view;
            TextView txt_travesia, txt_responsable_uno, txt_responsable_dos, txt_fecha_inicio;

            ItemsViewHolder(View view) {
                super(view);
                this.view = view;
                this.txt_travesia = (TextView) view.findViewById(R.id.nombreTravesia);
                this.txt_responsable_uno = (TextView) view.findViewById(R.id.responsableUnoTravesia);
                this.txt_responsable_dos = (TextView) view.findViewById(R.id.responsableDosTravesia);
                this.txt_fecha_inicio = (TextView) view.findViewById(R.id.fechaInicioTravesia);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                final Travesias travesia = list_travesias.get(getAdapterPosition());

                alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Atención!");
                alertBuilder.setMessage("Esta seguro que desea sincronizar la travesia " + travesia.nombreTravesia() + " de la unidad " + nombre_unidad);
                alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.enableDisableView(getView(), false);
                        sincronizarTravesia(travesia);
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

    private void sincronizarTravesia(final Travesias travesia) {
        final ProgressDialog pDialog = Helper.showProgressDialog(getContext(), Config.LOADING_MSG);
        String url = ServerRoutes.getSyncUrl(travesia.idTravesia(), api_token);
        CustomObjectRequest request = new CustomObjectRequest(getContext());
        request.SendRequest(Request.Method.GET, url, null, new CustomObjectRequest.ServerCallback() {
            @Override
            public void onResponse(JSONObject response) {
                Helper.hideProgressDialog(pDialog);
                if (response.has("status")) {
                    try {
                        status_login = response.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), status_login, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject data = response.getJSONObject("datos");
                        savePreferencesTravesia(travesia.idTravesia(), travesia.getDestino());
                        new SyncDataOperation(data).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onErrorResponse() {
                Helper.hideProgressDialog(pDialog);
                Toast.makeText(getContext(), Config.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePreferencesTravesia(Integer travesia_id, String destino_travesia) {
        preferenceManager.clearTravesiaId();
        preferenceManager.saveTravesiaId(travesia_id);
        preferenceManager.clearNombreModulo();
        preferenceManager.saveNombreModulo(nombre_modulo);
        preferenceManager.clearNombreUnidad();
        preferenceManager.saveNombreUnidad(nombre_unidad);
        preferenceManager.clearDestinoTravesia();
        preferenceManager.saveDestinoTravesia(destino_travesia);
        setHeaderSiderBar();
    }

    private class SyncDataOperation extends AsyncTask<Void, Void, Boolean> {

        private JSONObject object;
        private ProgressDialog loading;

        SyncDataOperation(JSONObject object) {
            this.object = object;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = Helper.showProgressDialog(getContext(), Config.DATA_PROCESS_MSG);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (checkAppDataStatus()) {
                saveSyncData(object);
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Helper.hideProgressDialog(loading);
            if (result) {
                redirectCategoriasTravesia();
            } else {
                showSyncAlert();
            }
        }
    }

    private boolean checkAppDataStatus() {
        if (DBHelper.checkDatabase(getContext())) {
            if (!DBHelper.checkPendingSync()) {
                DBHelper.dropDatabase(getContext().getApplicationContext());
            }
            return true;
        } else {
            return false;
        }
    }

    private void showSyncAlert() {
        alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Error!");
        alertBuilder.setMessage("Error, existen tablas a sincronizar.");
        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public void redirectCategoriasTravesia() {
        alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Exito!");
        alertBuilder.setMessage("Travesia sincronizada éxitosamente.");
        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FormCategoriesFragment fragment = new FormCategoriesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void saveSyncData(JSONObject object) {
        try {
            for (int i = 0; i < object.names().length(); i++) {
                JSONObject iteObject;
                JSONArray iteArray;
                switch (object.names().getString(i)) {
                    case "travesia__travesias":
                        iteObject = object.getJSONObject(object.names().getString(i));
                        Long travesia_ins_id = SyncDBHelper.saveTravesia(iteObject);
                        Travesia travesia_ins = Travesia.findById(Travesia.class, travesia_ins_id);
                        saveNroAppSync(travesia_ins.getNro_app_sync());
                        break;
                    case "travesia__travesias__historial":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveTravesia(iteObject);
                            }
                        }
                        break;
                    case "formulario__travesia":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveFormularioTravesia(iteObject);
                            }
                        }
                        break;
                    case "formularios":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveFormulario(iteObject);
                            }
                        }
                        break;
                    case "formulario__formulariomodelos":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveFormularioModelo(iteObject);
                            }
                        }
                        break;
                    case "formulario__formulariocategorias":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveFormularioCategoria(iteObject);
                            }
                        }
                        break;
                    case "formulariocompra__cabecera_compras":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveCabeceraCompra(iteObject);
                            }
                        }
                        break;
                    case "formulariocompra__detalle_compras":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveDetalleCompra(iteObject);
                            }
                        }
                        break;
                    case "formulariocompra__productos":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveFormularioCompraProducto(iteObject);
                            }
                        }
                        break;
                    case "formulariocompra__categoria":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveFormularioCompraCategoria(iteObject);
                            }
                        }
                        break;
                    case "cabecera_check_copias":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveCabeceraCheckCopia(iteObject);
                            }
                        }
                        break;
                    case "seccion_check_copias":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveSeccionCheckCopia(iteObject);
                            }
                        }
                        break;
                    case "item_check_copias":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveItemCheckCopia(iteObject);
                            }
                        }
                        break;
                    case "cabecera_checks":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveCabeceraCheck(iteObject);
                            }
                        }
                        break;
                    case "seccion_checks":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveSeccionCheck(iteObject);
                            }
                        }
                        break;
                    case "items_check":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveItemCheck(iteObject);
                            }
                        }
                        break;
                    case "categoria_maquinaria":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveCategoriaMaquinaria(iteObject);
                            }
                        }
                        break;
                    case "maquinarias":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveMaquinaria(iteObject);
                            }
                        }
                        break;
                    case "cabecera_mantenimientos":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveCabeceraMantenimiento(iteObject);
                            }
                        }
                        break;
                    case "detalle_mantenimientos":
                        iteArray = object.getJSONArray(object.names().getString(i));
                        if (SyncDBHelper.isValidArray(iteArray)) {
                            for (int k = 0; k < iteArray.length(); k++) {
                                iteObject = iteArray.getJSONObject(k);
                                SyncDBHelper.saveDetalleMantenimiento(iteObject);
                            }
                        }
                        break;
                    case "lista_area_compra":
                        iteObject = object.getJSONObject(object.names().getString(i));
                        for (int k = 0; k < iteObject.length(); k++) {
                            String index = String.valueOf(k);
                            SyncDBHelper.saveListAreaCompra(iteObject, index);
                        }
                        break;
                    case "lista_area_mantenimiento":
                        iteObject = object.getJSONObject(object.names().getString(i));
                        for (int k = 0; k < iteObject.length(); k++) {
                            String index = String.valueOf(k);
                            SyncDBHelper.saveListAreaMantenimiento(iteObject, index);
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveNroAppSync(Integer nro_app_sync) {
        preferenceManager.clearNroAppSync();
        preferenceManager.saveNroAppSync(nro_app_sync);
    }

    private void setHeaderSiderBar() {
        ((MainActivity)getActivity()).setHeaderSideBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sincronizar Travesia");
    }
}