/*
package zentcode02.parks.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.CabeceraChecksDB;
import zentcode02.parks.dbModels.CabeceraMantenimientoDB;
import zentcode02.parks.dbModels.FormularioCompraCabeceraDB;
import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.dbModels.SincronizarDB;
import zentcode02.parks.fragments.FormCheckFragment;
import zentcode02.parks.fragments.FormComprasFragment;
import zentcode02.parks.fragments.FormMantenimientosFragment;
import zentcode02.parks.fragments.FormsListFragment;
import zentcode02.parks.fragments.historial.FormCheckHistorial;
import zentcode02.parks.fragments.historial.FormComprasHistorial;
import zentcode02.parks.fragments.historial.FormMantenimientoHistorial;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.GlobalMethods;

*/
/**
 * Created by zentcode02 on 08/02/17.
 *//*


@SuppressWarnings("WrongConstant")
public class FormsListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private List<FormsListFragment.Form> formsList;
    private FragmentManager fragmentManager;
    private int type;
    private static final int TYPE_CHECK = 0;
    private static final int TYPE_COMPRA = 1;
    private static final int TYPE_MANTENIMIENTO = 2;
    private AlertDialog.Builder alert;
    private Sincronizar sincronizarDB;
    private int user_id;
    private PreferenceManager preferenceManager;
    private Global globalMethods;
    private static final  String solicitadoPor = "Solicitado por: ";
    private static final String area = "Área: ";
    private static final String verificadoPor = "Verificado por: ";
    private String label_text, label_area;

    public FormsListAdapter(Activity activity, List<FormsListFragment.Form> items, FragmentManager fragmentManager) {
        this.activity = activity;
        this.formsList = items;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (formsList.get(position).getType() == 0) {
            type = TYPE_CHECK;
        } else if (formsList.get(position).getType() == 1) {
            type = TYPE_COMPRA;
        } else if (formsList.get(position).getType() == 2) {
            type = TYPE_MANTENIMIENTO;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        //Correspondiente a la cantidad de tipos en getItemViewType
        return 3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final FormsListFragment.Form formItems = formsList.get(position);
        final Formularios formulario = Formularios.findById(Formularios.class, formItems.getFormulario_id());
        Log.d("form adapter", formulario.getNombre());

        if(inflater==null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView ==null){
            holder = new ViewHolder();
            alert = new AlertDialog.Builder(activity);
            globalMethods = new Global(activity.getApplicationContext());
            preferenceManager = new PreferenceManager(activity);
            user_id = preferenceManager.getUserId();
            convertView=inflater.inflate(R.layout.formslist_layout,null);

            holder.txtForm = (TextView) convertView.findViewById(R.id.txtFormList);
            holder.txtSolicitadoPor = (TextView) convertView.findViewById(R.id.txtSolicitadoPor);
            holder.txtArea = (TextView) convertView.findViewById(R.id.txtArea);
            holder.txt_total_items = (TextView) convertView.findViewById(R.id.count_items);
            holder.btnEliminar = (Button) convertView.findViewById(R.id.btnFormEliminar);
            holder.btnFinalizar = (Button) convertView.findViewById(R.id.btnFormFinalizar);
            holder.btnEditar = (Button) convertView.findViewById(R.id.btnFormEditar);
            holder.row1 = (LinearLayout) convertView.findViewById(R.id.rowForm1);
            holder.row2 = (LinearLayout) convertView.findViewById(R.id.rowForm2);
            holder.row3 = (LinearLayout) convertView.findViewById(R.id.rowForm3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        alert.setTitle("Atención!!");
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        holder.btnEliminar.setClickable(formItems.isModificable());
        holder.btnFinalizar.setClickable(formItems.isModificable());
        holder.btnEditar.setClickable(formItems.isModificable());
        holder.row2.setVisibility(formItems.getRow2_enabled());
        holder.row3.setVisibility(formItems.getRow3_enabled());

        switch (type) {

            case TYPE_CHECK:

                final CabeceraCheck cabeceraCheck = Select.from(CabeceraCheck.class)
                        .where(Condition.prop(NamingHelper.toSQLNameDefault("id")).eq(formItems.getCabecera_id())).first();

                holder.txtForm.setText(formItems.getNombre());
                label_text = verificadoPor + cabeceraCheck.getVerificado_por();
                holder.txtSolicitadoPor.setText(label_text);
                holder.txtArea.setVisibility(View.GONE);
                String label_total = formItems.getTotal_items() + "%";
                holder.txt_total_items.setText(label_total);

                holder.txtForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args_check = new Bundle();
                        if (!Objects.equals(formulario.getEstado(), "Finalizado")) {
                            FormCheckFragment fragment = new FormCheckFragment();

                            args_check.putInt("form_type", cabeceraCheck.getId().intValue());
                            fragment.setArguments(args_check);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null).commit();
                        } else {
                            FormCheckHistorial fragmentCheck = new FormCheckHistorial();

                            args_check.putString("codigo_sync", String.valueOf(formulario.getId()));
                            args_check.putString("tipo_form", "finalizado");
                            fragmentCheck.setArguments(args_check);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragmentCheck)
                                    .addToBackStack(null).commit();
                        }
                    }
                });

                if (holder.btnEditar.isClickable()) {
                    holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args_check;
                            if (!Objects.equals(formulario.getEstado(), "Finalizado")) {
                                FormCheckFragment fragment = new FormCheckFragment();

                                args_check = new Bundle();
                                args_check.putInt("form_type", cabeceraCheck.getId().intValue());
                                fragment.setArguments(args_check);

                                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, fragment)
                                        .addToBackStack(null).commit();
                            } */
/*else {
                                FormCheckHistorial fragmentCheck = new FormCheckHistorial();

                                args_check = new Bundle();
                                args_check.putInt("cabecera_id", formulario.getCabecera_id());
                                args_check.putString("tipo_form", "finalizado");
                                fragmentCheck.setArguments(args_check);

                                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, fragmentCheck)
                                        .addToBackStack(null).commit();
                            }*//*

                        }
                    });
                }

                break;

            case TYPE_COMPRA:

                final CabeceraCompra cabeceraCompra = Select.from(CabeceraCompra.class)
                        .where(Condition.prop(NamingHelper.toSQLNameDefault("id")).eq(formItems.getCabecera_id())).first();

                holder.txtForm.setText(formItems.getNombre());
                label_text = solicitadoPor + cabeceraCompra.getSolicitado_por();
                label_area = area + cabeceraCompra.getArea();
                holder.txtSolicitadoPor.setText(label_text);
                holder.txtArea.setText(label_area);
                holder.txt_total_items.setText(String.valueOf(formItems.getTotal_items()));

                holder.txtForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args_compra = new Bundle();
                        if (!Objects.equals(formulario.getEstado(), "Finalizado")) {
                            FormComprasFragment fragment = new FormComprasFragment();

                            args_compra.putInt("form_type", cabeceraCompra.getId().intValue());
                            fragment.setArguments(args_compra);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null).commit();
                        } else {
                            FormComprasHistorial fragmentCompras = new FormComprasHistorial();

                            Log.d("codigo sync", formulario.getCodigo_sync());
                            //En la app el Codigo sync en las cabeceras es el ID del Formulario, pero al sincronizar el historial desde
                            //la Web el codigo sync ya es un conjunto de ids
                            args_compra.putString("codigo_sync", String.valueOf(formulario.getId()));
                            args_compra.putString("tipo_form", "finalizado");
                            fragmentCompras.setArguments(args_compra);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragmentCompras)
                                    .addToBackStack(null).commit();
                        }
                    }
                });

                if (holder.btnEditar.isClickable()) {
                    holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args_compra = new Bundle();
                            if (!Objects.equals(formulario.getEstado(), "Finalizado")) {
                                FormComprasFragment fragment = new FormComprasFragment();

                                args_compra.putInt("form_type", cabeceraCompra.getId().intValue());
                                fragment.setArguments(args_compra);

                                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, fragment)
                                        .addToBackStack(null).commit();
                            } */
/*else {
                                FormComprasHistorial fragmentCompras = new FormComprasHistorial();

                                Log.d("codigo sync", formulario.getCodigo_sync());
                                //En la app el Codigo sync en las cabeceras es el ID del Formulario, pero al sincronizar el historial desde
                                //la Web el codigo sync ya es un conjunto de ids
                                args_compra.putString("codigo_sync", String.valueOf(formulario.getId()));
                                args_compra.putString("tipo_form", "finalizado");
                                fragmentCompras.setArguments(args_compra);

                                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, fragmentCompras)
                                        .addToBackStack(null).commit();
                            }*//*

                        }
                    });
                }

                break;

            case TYPE_MANTENIMIENTO:

                final CabeceraMantenimiento cabeceraMantenimiento = Select.from(CabeceraMantenimiento.class)
                        .where(Condition.prop(NamingHelper.toSQLNameDefault("id")).eq(formItems.getCabecera_id())).first();

                holder.txtForm.setText(formItems.getNombre());
                label_text = solicitadoPor + cabeceraMantenimiento.getSolicitado_por();
                label_area = area + cabeceraMantenimiento.getArea();
                holder.txtSolicitadoPor.setText(label_text);
                holder.txtArea.setText(label_area);
                holder.txt_total_items.setText(String.valueOf(formItems.getTotal_items()));

                holder.txtForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args_mantenimiento = new Bundle();
                        if (!Objects.equals(formulario.getEstado(), "Finalizado")) {
                            FormMantenimientosFragment fragment = new FormMantenimientosFragment();

                            args_mantenimiento.putInt("form_type", cabeceraMantenimiento.getId().intValue());
                            fragment.setArguments(args_mantenimiento);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null).commit();
                        } else {
                            FormMantenimientoHistorial fragmentMantenimiento = new FormMantenimientoHistorial();

                            args_mantenimiento.putString("codigo_sync", String.valueOf(formulario.getId()));
                            args_mantenimiento.putString("tipo_form", "finalizado");
                            //En la app el Codigo sync en las cabeceras es el ID del Formulario, pero al sincronizar el historial desde
                            //la Web el codigo sync ya es un conjunto de ids
                            fragmentMantenimiento.setArguments(args_mantenimiento);

                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.fragment_container, fragmentMantenimiento)
                                    .addToBackStack(null).commit();
                        }
                    }
                });

                if (holder.btnEditar.isClickable()) {
                    holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args_mantenimiento = new Bundle();
                            if (!Objects.equals(formulario.getEstado(), "Finalizado")) {
                                FormMantenimientosFragment fragment = new FormMantenimientosFragment();

                                args_mantenimiento.putInt("form_type", cabeceraMantenimiento.getId().intValue());
                                fragment.setArguments(args_mantenimiento);

                                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, fragment)
                                        .addToBackStack(null).commit();
                            }
                        }
                    });
                }

                break;
        }

        if (holder.btnFinalizar.isClickable()) {
            holder.btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("formulario estado", formulario.getEstado());
                    alert.setMessage("Esta seguro que desea finalizar este Formulario?. Al asignar este estado, no podra editarlo.");
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            formulario.setEstado("Finalizado");
                            formulario.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                            formulario.setUpdated_by(user_id);
                            formulario.save();
                            sincronizarDB = new Sincronizar(
                                    "formularios",
                                    formulario.getId().intValue(),
                                    "update",
                                    false,
                                    0
                            );
                            sincronizarDB.save();
                            Log.d("formulario estado", formulario.getEstado() + " user_id " + String.valueOf(formulario.getUpdated_by()) + " updated at " + formulario.getUpdated_at());
                            Toast.makeText(activity, "El formulario ha sido finalizado.", Toast.LENGTH_LONG).show();
                            formItems.setModificable(false);
                            formItems.setRow2_enabled(View.GONE);
                            formItems.setRow3_enabled(View.VISIBLE);
                            notifyDataSetChanged();
                        }
                    });
                    alert.show();
                }
            });
        }

        if (holder.btnEliminar.isClickable()) {
            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("formulario estado", formulario.getEstado());
                    alert.setMessage("Esta seguro que desea eliminar este Formulario?.");
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            formulario.setEstado("Eliminado");
                            formulario.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                            formulario.setUpdated_by(user_id);
                            formulario.save();
                            sincronizarDB = new Sincronizar(
                                    "formularios",
                                    formulario.getId().intValue(),
                                    "update",
                                    false,
                                    0
                            );
                            sincronizarDB.save();
                            Log.d("formulario estado", formulario.getEstado() + " user_id " + String.valueOf(formulario.getUpdated_by()) + " updated at " + formulario.getUpdated_at());
                            formsList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(activity, "El formulario ha sido eliminado.", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.show();
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtForm, txtSolicitadoPor, txtArea, txt_total_items;
        Button btnEliminar, btnFinalizar, btnEditar;
        LinearLayout row1, row2, row3;
    }

}
*/
