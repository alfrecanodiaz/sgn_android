/*
package zentcode02.parks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import zentcode02.parks.R;
import zentcode02.parks.dbModels.FormularioCompraDetalleDB;
import zentcode02.parks.dbModels.SincronizarDB;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.utils.GlobalMethods;

*/
/**
 * Created by zentcode02 on 18/01/17.
 *//*


public class FormComprasAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private FragmentManager fragmentManager;
    private List<DetalleCompra> formComprasList;
    private int isCategoriaOtros = 0;
    private AlertDialog.Builder alert;
    private Sincronizar sincronizarDB;
    private PreferenceManager preferenceManager;
    private Global globalMethods;

    public FormComprasAdapter(Activity activity, FragmentManager fragmentManager, List<DetalleCompra> items, int isCategoriaOtros){
        this.activity=activity;
        this.fragmentManager = fragmentManager;
        this.formComprasList=items;
        this.isCategoriaOtros=isCategoriaOtros;
    }
    @Override
    public int getCount() {

        return formComprasList.size();
    }

    @Override
    public Object getItem(int position) {

        return formComprasList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //ViewHolder holder = null;
        final ViewHolder holder;
        if(inflater==null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView ==null){
            holder = new ViewHolder();
            alert = new AlertDialog.Builder(activity);
            preferenceManager = new PreferenceManager(activity);
            globalMethods = new Global(activity.getApplicationContext());
            convertView=inflater.inflate(R.layout.layout_comprastab2,null);
            holder.descripcionProducto = (TextView) convertView.findViewById(R.id.descripcionCompras);
            holder.btnEliminar = (Button) convertView.findViewById(R.id.btnEliminarProducto);
            holder.btnCantidadSolicitada = (Button) convertView.findViewById(R.id.btnCantidadSolicitada);
            holder.btnOk = (Button) convertView.findViewById(R.id.includeComprasTab2).findViewById(R.id.okButton);
            holder.btnNext = (Button) convertView.findViewById(R.id.includeComprasTab2).findViewById(R.id.nextButton);
            holder.btnNext_container = (LinearLayout) convertView.findViewById(R.id.includeComprasTab2).findViewById(R.id.btnNext_container);
            holder.tableRow = (TableRow) convertView.findViewById(R.id.rowCompras2);
            holder.rowEliminarProducto = (LinearLayout) convertView.findViewById(R.id.rowEliminarProducto);

            if (isCategoriaOtros == 1) {
                holder.btnEliminar.setVisibility(View.VISIBLE);
                holder.rowEliminarProducto.setVisibility(View.VISIBLE);
            } else {
                holder.btnEliminar.setVisibility(View.GONE);
                holder.rowEliminarProducto.setVisibility(View.GONE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.btnNext.setVisibility(View.GONE);
        holder.btnNext_container.setVisibility(View.GONE);

        holder.ref = position;

        final DetalleCompra formularioCompraDetalleDB = formComprasList.get(position);

        Log.d("formulario detalle", String.valueOf(formularioCompraDetalleDB.getProducto_id()));

        if (formularioCompraDetalleDB.getProducto_id() != 0) {
            String label_producto = formularioCompraDetalleDB.getNombre() + " (" + formularioCompraDetalleDB.getUnidad_medida() + ")";
            holder.descripcionProducto.setText(label_producto);
        } else {
            holder.descripcionProducto.setText(formularioCompraDetalleDB.getNombre());
        }
        holder.btnCantidadSolicitada.setText(String.valueOf(formularioCompraDetalleDB.getCantidad_solicitada()));

        holder.tableRow.setVisibility(View.GONE);
        if (position == formComprasList.size()-1) {
            holder.tableRow.setVisibility(View.VISIBLE);
        } else {
            holder.tableRow.setVisibility(View.GONE);
        }

        holder.btnCantidadSolicitada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("position " + position);

                final EditText inputText = new EditText(activity);
                inputText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                inputText.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 1000000)});
                inputText.setSelectAllOnFocus(true);
                inputText.selectAll();

                alert.setTitle("Cantidad solicitada item: " + formularioCompraDetalleDB.getNombre());
                alert.setMessage("Ingrese la cantidad solicitada");

                inputText.setText(String.valueOf(formularioCompraDetalleDB.getCantidad_solicitada()));

                // Set an EditText view to get user input
                alert.setView(inputText);

                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.d("input text", inputText.getText().toString());
                        System.out.println("position " + position);
                        if (!Objects.equals(inputText.getText().toString(), "")) {
                            formularioCompraDetalleDB.setCantidad_solicitada(Integer.valueOf(inputText.getText().toString()));
                            formularioCompraDetalleDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                            formularioCompraDetalleDB.setUpdated_by(preferenceManager.getUserId());
                            formularioCompraDetalleDB.save();
                            System.out.println("item seleccionado " + formularioCompraDetalleDB.toString());
                            System.out.println("cantidad solicitada " + String.valueOf(formularioCompraDetalleDB.getCantidad_solicitada()));
                            //Update tabla Sincronizar
                            sincronizarDB = new Sincronizar(
                                    "formulariocompra__detalle_compras",
                                    formularioCompraDetalleDB.getId().intValue(),
                                    "update",
                                    false,
                                    0
                            );
                            sincronizarDB.save();
                            notifyDataSetChanged();
                        } else {
                            formularioCompraDetalleDB.setCantidad_solicitada(0);
                            formularioCompraDetalleDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                            formularioCompraDetalleDB.setUpdated_by(preferenceManager.getUserId());
                            formularioCompraDetalleDB.save();
                            System.out.println("item seleccionado " + formularioCompraDetalleDB.toString());
                            System.out.println("cantidad solicitada " + String.valueOf(formularioCompraDetalleDB.getCantidad_solicitada()));
                            //Update tabla Sincronizar
                            sincronizarDB = new Sincronizar(
                                    "formulariocompra__detalle_compras",
                                    formularioCompraDetalleDB.getId().intValue(),
                                    "update",
                                    false,
                                    0
                            );
                            sincronizarDB.save();
                        }
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                    }
                });

                alert.show();
                inputText.requestFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("position " + position);

                new AlertDialog.Builder(activity)
                        .setTitle("Atención!")
                        .setMessage("Esta seguro que desea eliminar el producto?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                formularioCompraDetalleDB.setProducto_eliminado(1);
                                formularioCompraDetalleDB.setUpdated_at(globalMethods.MySqlDateTimeFormat());
                                formularioCompraDetalleDB.setUpdated_by(preferenceManager.getUserId());
                                formularioCompraDetalleDB.save();
                                //Update tabla Sincronizar
                                sincronizarDB = new Sincronizar(
                                        "formulariocompra__detalle_compras",
                                        formularioCompraDetalleDB.getId().intValue(),
                                        "update",
                                        false,
                                        0
                                );
                                sincronizarDB.save();
//                                formularioCompraDetalleDB.delete();
                                formComprasList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(activity, "Producto eliminado satisfactoriamente", Toast.LENGTH_LONG).show();
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

    private class ViewHolder {
        TextView descripcionProducto;
        Button btnEliminar, btnCantidadSolicitada, btnOk, btnNext;
        TableRow tableRow;
        LinearLayout rowEliminarProducto, btnNext_container;
        int ref;
    }

    private class InputFilterMinMax implements InputFilter {

        private int min, max;

        private InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        private InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
                Log.d("Number Format Exception", nfe.toString());
            }
            return "";
        }
    }

}
*/
