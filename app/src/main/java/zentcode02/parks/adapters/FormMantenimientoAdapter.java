/*
package zentcode02.parks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.DetalleMantenimientoDB;
import zentcode02.parks.dbModels.SincronizarDB;
import zentcode02.parks.models.StaticItems;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.GlobalMethods;

*/
/**
 * Created by zentcode02 on 16/02/17.
 *//*


public class FormMantenimientoAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private FragmentManager fragmentManager;
    private List<DetalleMantenimiento> detalleList;
    private AlertDialog.Builder alert;
    private Sincronizar sincronizarDB;
    private int isCategoriaOtros = 0;
    private PreferenceManager preferenceManager;
    private Global globalMethods;

    public FormMantenimientoAdapter(Activity activity, List<DetalleMantenimiento> detalleList, FragmentManager fragmentManager, int isCategoriaOtros) {
        this.activity = activity;
        this.detalleList = detalleList;
        this.fragmentManager = fragmentManager;
        this.isCategoriaOtros=isCategoriaOtros;
    }

    @Override
    public int getCount() {
        return detalleList.size();
    }

    @Override
    public Object getItem(int position) {
        return detalleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final DetalleMantenimiento detalleMantenimiento = detalleList.get(position);

        if(inflater==null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView ==null){
            holder = new ViewHolder();
            alert = new AlertDialog.Builder(activity);
            preferenceManager = new PreferenceManager(activity);
            globalMethods = new Global(activity.getApplicationContext());
            convertView=inflater.inflate(R.layout.layout_mantenimientotab2,null);
            holder.maquinaria = (TextView) convertView.findViewById(R.id.nombreMaquinaria);
            holder.btnMantenimiento = (Button) convertView.findViewById(R.id.btnMantenimiento);
            holder.btnOk = (Button) convertView.findViewById(R.id.includeMantenimientoTab2).findViewById(R.id.okButton);
            holder.btnNext = (Button) convertView.findViewById(R.id.includeMantenimientoTab2).findViewById(R.id.nextButton);
            holder.btnNext_container = (LinearLayout) convertView.findViewById(R.id.includeMantenimientoTab2).findViewById(R.id.btnNext_container);
            holder.btnEliminarMaquinaria = (Button) convertView.findViewById(R.id.btnEliminarMaquinaria);
            holder.tableRow = (TableRow) convertView.findViewById(R.id.rowMantenimiento2);
            holder.rowEliminarMaquinaria = (LinearLayout) convertView.findViewById(R.id.rowEliminarMaquinaria);

            if (isCategoriaOtros == 1) {
                holder.btnEliminarMaquinaria.setVisibility(View.VISIBLE);
                holder.rowEliminarMaquinaria.setVisibility(View.VISIBLE);
            } else {
                holder.btnEliminarMaquinaria.setVisibility(View.GONE);
                holder.rowEliminarMaquinaria.setVisibility(View.GONE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.btnNext.setVisibility(View.GONE);
        holder.btnNext_container.setVisibility(View.GONE);

        holder.tableRow.setVisibility(View.GONE);
        if (position == detalleList.size()-1) {
            holder.tableRow.setVisibility(View.VISIBLE);
        } else {
            holder.tableRow.setVisibility(View.GONE);
        }

        holder.maquinaria.setText(detalleMantenimiento.getNombre_maquinaria());
        holder.btnMantenimiento.setText(detalleMantenimiento.getMantenimiento());

        holder.btnMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                System.out.println("position " + position);

                //Create LinearLayout Dynamically
                final LinearLayout layout = new LinearLayout(v.getContext());
                //Setup Layout Attributes
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.VERTICAL);

                //Radio Group
                final RadioGroup radioGrp = new RadioGroup(v.getContext()); //create the RadioGroup
                radioGrp.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                //radioGrp.setPadding(0, 10, 0, 0);
                radioGrp.setPadding(10, 10, 10, 10);

                for (int i = 0; i < StaticItems.mantenimientoLista.length; i++) {
                    RadioButton radioButton = new RadioButton(v.getContext());
                    radioButton.setText(StaticItems.mantenimientoLista[i]);
                    radioButton.setId(i);
                    radioGrp.addView(radioButton);
                    if (Objects.equals(detalleMantenimiento.getMantenimiento(), StaticItems.mantenimientoLista[i])) {
                        radioButton.setChecked(true);
                    }
                }

                //set listener to radio button group
                radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int checkedRadioButtonId = radioGrp.getCheckedRadioButtonId();
                        RadioButton radioBtn = (RadioButton) layout.findViewById(checkedRadioButtonId);
                        radioBtn.setPadding(5, 5, 5, 5);
                        String value = StaticItems.mantenimientoLista[radioBtn.getId()];
                        Log.d("valor seleccionado", value);
                        detalleMantenimiento.setMantenimiento(value);
                        detalleMantenimiento.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                        detalleMantenimiento.setUpdated_by(preferenceManager.getUserId());
                        detalleMantenimiento.save();
                        sincronizarDB = new Sincronizar(
                                "detalle_mantenimientos",
                                detalleMantenimiento.getId().intValue(),
                                "update",
                                false,
                                0
                        );
                        sincronizarDB.save();
                        notifyDataSetChanged();
                    }
                });

                //Editext
                final EditText inputText = new EditText(v.getContext());
                inputText.setText(detalleMantenimiento.getObservacion());
                inputText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                inputText.clearFocus();
//                inputText.setPadding(0, 10, 0, 0);

                inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        inputText.selectAll();
                    }
                });

                layout.addView(radioGrp);
                layout.addView(inputText);

                alert.setTitle("Mantenimiento item: " + detalleMantenimiento.getNombre_maquinaria());
                alert.setMessage("Complete los siguientes campos");

                alert.setView(layout);

                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("input text", inputText.getText().toString());
                        detalleMantenimiento.setObservacion(inputText.getText().toString());
                        detalleMantenimiento.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                        detalleMantenimiento.setUpdated_by(preferenceManager.getUserId());
                        detalleMantenimiento.save();
                        System.out.println("item seleccionado " + detalleMantenimiento.getNombre_maquinaria());
                        System.out.println("item obsesrvacion " + detalleMantenimiento.getObservacion());
                        sincronizarDB = new Sincronizar(
                                "detalle_mantenimientos",
                                detalleMantenimiento.getId().intValue(),
                                "update",
                                false,
                                0
                        );
                        sincronizarDB.save();
                        notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();

            }
        });

        holder.btnEliminarMaquinaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("position " + position);

                new AlertDialog.Builder(activity)
                        .setTitle("Atención!")
                        .setMessage("Esta seguro que desea eliminar la maquinaria?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                detalleMantenimiento.setMaquinaria_eliminada(1);
                                detalleMantenimiento.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                                detalleMantenimiento.setUpdated_by(preferenceManager.getUserId());
                                detalleMantenimiento.save();
                                //Update tabla Sincronizar
                                sincronizarDB = new Sincronizar(
                                        "detalle_mantenimientos",
                                        detalleMantenimiento.getId().intValue(),
                                        "update",
                                        false,
                                        0
                                );
                                sincronizarDB.save();
//                                formularioCompraDetalleDB.delete();
                                detalleList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(activity, "Maquinaria eliminada satisfactoriamente", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        //Ok Button
        holder.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStackImmediate();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView maquinaria;
        Button btnMantenimiento, btnOk, btnNext, btnEliminarMaquinaria;
        LinearLayout btnNext_container, rowEliminarMaquinaria;
        TableRow tableRow;
    }

}
*/
