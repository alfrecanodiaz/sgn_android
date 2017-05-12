/*
package zentcode02.parks.fragments.historial;


import android.app.Activity;
import android.content.Context;
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

import java.util.List;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.TravesiasDB;
import zentcode02.parks.network.PreferenceManager;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class TravesiasHistorial extends Fragment {

    private ListView listaTravesias;
    private PreferenceManager preferenceManager;

    public TravesiasHistorial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.travesias_historial_fragment, container, false);

        preferenceManager = new PreferenceManager(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista Travesias");

        listaTravesias = (ListView) view.findViewById(R.id.lvTravesiasHistorial);

        populateTravesiasFromDB();

        return view;
    }

    private void populateTravesiasFromDB() {

        int travesia_actual_id = preferenceManager.getTravesiaId();

        List<Travesia> travesias = Select.from(Travesia.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("travesia_actual")).eq(0))
                .where(Condition.prop(NamingHelper.toSQLNameDefault("travesia_id")).notEq(travesia_actual_id))
                .list();

        TravesiasHistorialAdapter adapter = new TravesiasHistorialAdapter(getActivity(), travesias);
        listaTravesias.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lista Travesias");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listaTravesias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Travesia travesia = (Travesia) parent.getAdapter().getItem(position);
                int travesia_id = travesia.getId_servidor();

                FormCategoriasHistorial fragment = new FormCategoriasHistorial();

                Bundle args_id = new Bundle();
                args_id.putInt("travesia_id", travesia_id);
                fragment.setArguments(args_id);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();

            }
        });

    }

    private class TravesiasHistorialAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Activity activity;
        private List<Travesia> travesiasList;

        TravesiasHistorialAdapter(Activity activity, List<Travesia> items) {
            this.activity = activity;
            this.travesiasList = items;
        }

        @Override
        public int getCount() {
            return travesiasList.size();
        }

        @Override
        public Object getItem(int position) {
            return travesiasList.get(position);
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
                holder.travesia = (TextView) convertView.findViewById(R.id.txtList);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Travesia travesia = travesiasList.get(position);

            holder.travesia.setText(travesia.getDestino());

            return convertView;
        }

        class ViewHolder {
            TextView travesia;
        }

    }

}
*/
