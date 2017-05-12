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

import zentcode02.parks.models.Modulos;
import zentcode02.parks.R;
import zentcode02.parks.network.Config;
import zentcode02.parks.network.CustomObjectRequest;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.network.ServerRoutes;
import zentcode02.parks.utils.Helper;

public class ModulosFragment extends Fragment {

    private RecyclerView recycler_view_modulos;
    public static List<Modulos> list_modulos;
    private String api_token, status_login;

    public ModulosFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        api_token = preferenceManager.getApiToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modulos_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Modulos");

        recycler_view_modulos = (RecyclerView) view.findViewById(R.id.recycler_view_modulos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view_modulos.setLayoutManager(layoutManager);

        getDataFromServer();

        return view;
    }

    private void getDataFromServer() {
        final ProgressDialog pDialog = Helper.showProgressDialog(getContext(), Config.LOADING_MSG);
        String url = ServerRoutes.getModulosUrl(api_token);
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
                        JSONArray modulos = response.getJSONArray("modulos");
                        listModulos(modulos);
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

    private void listModulos(JSONArray jsonArray) {
        list_modulos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Modulos mods =new Modulos();
                mods.setNombre(jsonObject.getString("nombre"));
                mods.setId(jsonObject.getInt("id"));
                list_modulos.add(mods);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setAdapter();
    }

    private void setAdapter() {
        ModulosRecyclerViewAdapter adapter = new ModulosRecyclerViewAdapter(getContext(), list_modulos);
        recycler_view_modulos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class ModulosRecyclerViewAdapter extends RecyclerView.Adapter<ModulosRecyclerViewAdapter.ItemsViewHolder> {

        private List<Modulos> modulos;
        private Context mContext;

        private ModulosRecyclerViewAdapter(Context context, List<Modulos> items) {
            this.mContext = context;
            this.modulos = items;
        }

        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_txtlist, parent, false);
            return new ItemsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {
            Modulos modulo = modulos.get(position);
            holder.txt_modulo.setText(modulo.getNombre());
        }

        @Override
        public int getItemCount() {
            return modulos.size();
        }

        class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View view;
            TextView txt_modulo;

            ItemsViewHolder(View view) {
                super(view);
                this.view = view;
                this.txt_modulo = (TextView) view.findViewById(R.id.txtList);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Modulos modulo = list_modulos.get(getAdapterPosition());
                Integer modulo_id = modulo.idModulo();
                String nombre_modulo = modulo.nombreModulo();

                UnidadesFragment fragment = new UnidadesFragment();

                Bundle args_id = new Bundle();
                args_id.putInt("modulo_id", modulo_id);
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Modulos");
    }
}
