package zentcode02.parks.fragments;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.models.Unidades;
import zentcode02.parks.network.Config;
import zentcode02.parks.network.CustomObjectRequest;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.network.ServerRoutes;
import zentcode02.parks.utils.Helper;

public class UnidadesFragment extends Fragment {

    private RecyclerView recycler_view_unidades;
    private static String api_token, status_login;
    public static List<Unidades> list_unidades;
    private String nombre_modulo;
    private Integer modulo_id;

    public UnidadesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        api_token = preferenceManager.getApiToken();
        modulo_id = getArguments().getInt("modulo_id");
        nombre_modulo = getArguments().getString("nombre_modulo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.unidades_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Unidades");

        TextView txtTitleUnidad = (TextView) view.findViewById(R.id.txtTitleUnidad);
        txtTitleUnidad.setText("Modulo " + nombre_modulo + ". " + "Seleccionar Unidad:");

        recycler_view_unidades = (RecyclerView) view.findViewById(R.id.recycler_view_unidades);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view_unidades.setLayoutManager(layoutManager);

        getDataFromServer();

        return view;
    }

    private void getDataFromServer() {
        final ProgressDialog pDialog = Helper.showProgressDialog(getContext(), Config.LOADING_MSG);
        String url = ServerRoutes.getUnidadesUrl(modulo_id, api_token);
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
                    Toast.makeText(getContext(), status_login, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray unidades = response.getJSONArray("unidades");
                        listaUnidades(unidades);
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

    private void listaUnidades(JSONArray jsonArray) {
        list_unidades = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Unidades unidades =new Unidades();
                unidades.setNombre(jsonObject.getString("nombre"));
                unidades.setId(jsonObject.getInt("id"));
                list_unidades.add(unidades);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setAdapter();
    }

    private void setAdapter() {
        UnidadesRecyclerViewAdapter adapter = new UnidadesRecyclerViewAdapter(getContext(), list_unidades);
        recycler_view_unidades.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class UnidadesRecyclerViewAdapter extends RecyclerView.Adapter<UnidadesRecyclerViewAdapter.ItemsViewHolder> {

        private List<Unidades> unidades;
        private Context mContext;

        private UnidadesRecyclerViewAdapter(Context context, List<Unidades> items) {
            this.mContext = context;
            this.unidades = items;
        }

        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_txtlist, parent, false);
            return new ItemsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {
            Unidades unidad = unidades.get(position);
            holder.txt_unidad.setText(unidad.getNombre());
        }

        @Override
        public int getItemCount() {
            return unidades.size();
        }

        class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View view;
            TextView txt_unidad;

            ItemsViewHolder(View view) {
                super(view);
                this.view = view;
                this.txt_unidad = (TextView) view.findViewById(R.id.txtList);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Unidades unidades = list_unidades.get(getAdapterPosition());
                Integer unidad_id = unidades.idUnidad();
                String nombre_unidad = unidades.nombreUnidad();

                TravesiasFragment fragment = new TravesiasFragment();

                Bundle args_id = new Bundle();
                args_id.putInt("unidad_id", unidad_id);
                args_id.putString("nombre_unidad", nombre_unidad);
                args_id.putString("nombre_modulo", nombre_modulo);
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Unidades");
    }
}
