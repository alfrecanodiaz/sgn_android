package zentcode02.parks.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import zentcode02.parks.dbModels.CabeceraCheck;
import zentcode02.parks.dbModels.CabeceraCompra;
import zentcode02.parks.dbModels.CabeceraMantenimiento;
import zentcode02.parks.dbModels.DetalleCompra;
import zentcode02.parks.dbModels.DetalleMantenimiento;
import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.dbModels.ItemCheck;
import zentcode02.parks.dbModels.SeccionCheck;
import zentcode02.parks.dbModels.Sincronizar;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DBHelper {

    private static int sincronizar_id;

    public static boolean checkPendingSync(){
        List<Sincronizar> sync_list = Select.from(Sincronizar.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("enviado")).eq("0")
        ).list();
        return !sync_list.isEmpty();
    }

    public static boolean checkDatabase(Context context)  {
        SugarDb db = new SugarDb(context);
        String database = db.getDatabaseName();
        return database != null;
    }

    public static void dropDatabase(Context context) {
        SchemaGenerator schemaGenerator = new SchemaGenerator(context);
        schemaGenerator.deleteTables(new SugarDb(context).getDB());
        SugarContext.init(context);
        schemaGenerator.createDatabase(new SugarDb(context).getDB());
    }

    public static void saveSync(String tabla, Long id, String accion, Boolean enviado, Integer tipo) {
        Sincronizar sync = new Sincronizar();
        sync.setTabla_a_sincronizar(tabla);
        sync.setId_sqlite(id);
        sync.setAccion(accion);
        sync.setEnviado(enviado);
        sync.setTipo(tipo);
        sync.save();
    }

    public static void updateSync(Long id, String accion, Boolean enviado) {
        Sincronizar sync = Select.from(Sincronizar.class).where(
                Condition.prop(NamingHelper.toSQLNameDefault("id_sqlite")).eq(id)
        ).first();
        sync.setAccion(accion);
        sync.setEnviado(enviado);
        sync.save();
    }




    public static String GetDataFromDatabase() {
        JSONObject respuestaJSON = new JSONObject();

        String query = NamingHelper.toSQLNameDefault("enviado");
        String orderBy = NamingHelper.toSQLNameDefault("tipo") + " " + "ASC";
        Sincronizar sincronizar = Select.from(Sincronizar.class).where(Condition.prop(query).eq(0)).orderBy(orderBy).first();

        String sync_table = sincronizar.getTabla_a_sincronizar();
        int id_sqlite = sincronizar.getId_sqlite().intValue();
        Log.d("id sqlite", String.valueOf(id_sqlite));
        String tabla_sincronizar = sincronizar.getTabla_a_sincronizar();
        //Sincronizar ID
        setSincronizar_id(sincronizar.getId().intValue());
//        sincronizar_id =  sincronizar.getId().intValue();

        Log.d("count ", String.valueOf(countUpdatesDatabase(id_sqlite, tabla_sincronizar)));

        //Si es un insert
        if (Objects.equals(sincronizar.getAccion(), "insert")) {

            try {
                respuestaJSON.put("operacion", sincronizar.getAccion());
                respuestaJSON.put("tabla", sincronizar.getTabla_a_sincronizar());
                respuestaJSON.put("datos", jsonBuilderMethod(id_sqlite, sync_table));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("respuesta json", respuestaJSON.toString());
            Log.d("sincronizar id", String.valueOf(sincronizar_id));

            return String.valueOf(respuestaJSON);

            //Si es un update
        } else {

            //Si el campo tipo no es igual a 1 (no contiene imagen)
            if (sincronizar.getTipo() != 1) {

                //Si el count no es mayor a 1 (count = false)
                if (!countUpdatesDatabase(id_sqlite, tabla_sincronizar)) {

                    try {
                        respuestaJSON.put("operacion", sincronizar.getAccion());
                        respuestaJSON.put("tabla", sincronizar.getTabla_a_sincronizar());
                        respuestaJSON.put("datos", jsonBuilderMethod(id_sqlite, sync_table));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("respuesta json", respuestaJSON.toString());
                    Log.d("sincronizar id", String.valueOf(sincronizar_id));

                    return String.valueOf(respuestaJSON);

                    //Si el count es mayor a 1 (count = true)
                } else {
                    Sincronizar sincronizarDB = Sincronizar.findById(Sincronizar.class, sincronizar.getId());
                    sincronizarDB.setEnviado(true);
                    sincronizarDB.save();

                    return "next";
                }
                //Si el campo tipo es igual a 1 (contiene imagen)
            } else {

                //Si el count no es mayor a 1 (count = false)
                if (!countUpdatesImgsDatabase(id_sqlite, tabla_sincronizar)) {

                    try {
                        respuestaJSON.put("operacion", sincronizar.getAccion());
                        respuestaJSON.put("tabla", sincronizar.getTabla_a_sincronizar());
                        respuestaJSON.put("datos", sendImageJson(id_sqlite));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("respuesta json", respuestaJSON.toString());
                    Log.d("sincronizar id", String.valueOf(sincronizar_id));

                    return String.valueOf(respuestaJSON);

                    //Si el count es mayor a 1 (count = true)
                } else {
                    Sincronizar sincronizarDB = Sincronizar.findById(Sincronizar.class, sincronizar.getId());
                    sincronizarDB.setEnviado(true);
                    sincronizarDB.save();

                    return "next";
                }

            }

        }
    }

    public static int getSincronizar_id() {
        return sincronizar_id;
    }

    private static void setSincronizar_id(int sincronizar_id) {
        DBHelper.sincronizar_id = sincronizar_id;
    }

    private static boolean countUpdatesDatabase(int id, String tabla) {
        Long count = Select.from(Sincronizar.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("tabla_a_sincronizar")).eq(tabla),
                        Condition.prop(NamingHelper.toSQLNameDefault("id_sqlite")).eq(id),
                        Condition.prop(NamingHelper.toSQLNameDefault("tipo")).eq(0),
                        Condition.prop(NamingHelper.toSQLNameDefault("enviado")).eq(0))
                .count();

        Log.d("count long", String.valueOf(count));

        return count > 1;
    }

    private static boolean countUpdatesImgsDatabase(int id, String tabla) {
        Long count = Select.from(Sincronizar.class)
                .where(Condition.prop(NamingHelper.toSQLNameDefault("tabla_a_sincronizar")).eq(tabla),
                        Condition.prop(NamingHelper.toSQLNameDefault("id_sqlite")).eq(id),
                        Condition.prop(NamingHelper.toSQLNameDefault("tipo")).eq(1),
                        Condition.prop(NamingHelper.toSQLNameDefault("enviado")).eq(0))
                .count();

        Log.d("count long imgs", String.valueOf(count));

        return count > 1;
    }

    private static JSONObject jsonBuilderMethod(int id_sqlite, String sync_table) {
        JSONObject jsonObjBuilder;
        JSONArray jsonArrayBuilder = new JSONArray();
        JSONObject respuestaObj = new JSONObject();

        switch (sync_table)
        {
            case "formularios":
                Formularios formularios = Formularios.findById(Formularios.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", formularios.getId());
                    jsonObjBuilder.put("nombre", formularios.getNombre());
                    jsonObjBuilder.put("version", formularios.getVersion());
                    jsonObjBuilder.put("codigo", formularios.getCodigo());
                    jsonObjBuilder.put("cabecera", formularios.getCabecera());
                    jsonObjBuilder.put("detalles", formularios.getDetalles());
                    jsonObjBuilder.put("formulario_travesia_id", formularios.getFormulario_travesia_id());
                    jsonObjBuilder.put("estado", formularios.getEstado());
                    jsonObjBuilder.put("nro_checkeo", formularios.getNro_checkeo());
                    jsonObjBuilder.put("codigo_sync", formularios.getCodigo_sync());
                    jsonObjBuilder.put("created_at_app", formularios.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", formularios.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", formularios.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", formularios.getUpdated_by());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("formularios", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "cabecera_checks":
                CabeceraCheck cabeceraCheck = CabeceraCheck.findById(CabeceraCheck.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", cabeceraCheck.getId());
                    jsonObjBuilder.put("fecha", dateFormatMySQL(cabeceraCheck.getFecha()));
                    jsonObjBuilder.put("verificado_por", cabeceraCheck.getVerificado_por());
                    jsonObjBuilder.put("revisado_por", cabeceraCheck.getRevisado_por());
                    jsonObjBuilder.put("codigo_sync", cabeceraCheck.getCodigo_sync());
                    jsonObjBuilder.put("codigo_formulario_sync", cabeceraCheck.getCodigo_formulario_sync());
                    jsonObjBuilder.put("created_at_app", cabeceraCheck.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", cabeceraCheck.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", cabeceraCheck.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", cabeceraCheck.getUpdated_by());
                    jsonObjBuilder.put("inicio_viaje", cabeceraCheck.getInicio_viaje());
                    jsonObjBuilder.put("fin_viaje", cabeceraCheck.getFin_viaje());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("cabecera_checks", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "seccion_checks":
                SeccionCheck seccionCheck = SeccionCheck.findById(SeccionCheck.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", seccionCheck.getId());
                    jsonObjBuilder.put("nombre", seccionCheck.getNombre());
                    jsonObjBuilder.put("nombre_campo", seccionCheck.getNombre_campo());
                    jsonObjBuilder.put("observacion", seccionCheck.getObservacion());
                    jsonObjBuilder.put("codigo_sync", seccionCheck.getCodigo_sync());
                    jsonObjBuilder.put("codigo_formulario_sync", seccionCheck.getCodigo_formulario_sync());
                    jsonObjBuilder.put("created_at_app", seccionCheck.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", seccionCheck.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", seccionCheck.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", seccionCheck.getUpdated_by());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("seccion_checks", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "items_check":
                ItemCheck itemCheck = ItemCheck.findById(ItemCheck.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", itemCheck.getId());
                    jsonObjBuilder.put("nombre", itemCheck.getNombre());
                    jsonObjBuilder.put("imagen_habilitar", itemCheck.getImagen_habilitar());
                    jsonObjBuilder.put("imagen_requerido", itemCheck.getImagen_requerido());
                    jsonObjBuilder.put("gps_habilitar", itemCheck.getGps_habilitar());
                    jsonObjBuilder.put("gps_requerido", itemCheck.getGps_requerido());
                    jsonObjBuilder.put("qr_requerido", itemCheck.getQr_requerido());
                    jsonObjBuilder.put("qr_habilitar", itemCheck.getQr_habilitar());
                    jsonObjBuilder.put("gps", itemCheck.getGps());
                    jsonObjBuilder.put("qr", itemCheck.getQr());
                    jsonObjBuilder.put("seccion_check_id", itemCheck.getSeccion_check_id());
                    jsonObjBuilder.put("fecha_app", itemCheck.getFecha_app());
                    jsonObjBuilder.put("codigo_sync", itemCheck.getCodigo_sync());
                    jsonObjBuilder.put("critico", itemCheck.getCritico());
                    jsonObjBuilder.put("satisfactorio", itemCheck.getSatisfactorio());
                    jsonObjBuilder.put("observacion", itemCheck.getObservacion());
                    jsonObjBuilder.put("created_at_app", itemCheck.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", itemCheck.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", itemCheck.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", itemCheck.getUpdated_by());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("items_check", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "formulariocompra__cabecera_compras":
                CabeceraCompra cabeceraCompra = CabeceraCompra.findById(CabeceraCompra.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", cabeceraCompra.getId());
                    jsonObjBuilder.put("area", cabeceraCompra.getArea());
                    jsonObjBuilder.put("empresa_del_grupo", cabeceraCompra.getEmpresa_del_grupo());
                    jsonObjBuilder.put("prioridad", cabeceraCompra.getPrioridad());
                    jsonObjBuilder.put("solicitado_por", cabeceraCompra.getSolicitado_por());
                    jsonObjBuilder.put("solicitado_fecha", cabeceraCompra.getSolicitado_fecha());
                    jsonObjBuilder.put("enviado_por", cabeceraCompra.getEnviado_por());
                    jsonObjBuilder.put("enviado_fecha", cabeceraCompra.getEnviado_fecha());
                    jsonObjBuilder.put("aprobado_por", cabeceraCompra.getAprobado_por());
                    jsonObjBuilder.put("aprobado_fecha", cabeceraCompra.getAprobado_fecha());
                    jsonObjBuilder.put("recibido_por", cabeceraCompra.getRecibido_por());
                    jsonObjBuilder.put("recibido_fecha", cabeceraCompra.getRecibido_fecha());
                    jsonObjBuilder.put("nro_oc", cabeceraCompra.getNro_oc());
                    jsonObjBuilder.put("codigo_sync", cabeceraCompra.getCodigo_sync());
                    jsonObjBuilder.put("codigo_formulario_sync", cabeceraCompra.getCodigo_formulario_sync());
                    jsonObjBuilder.put("observacion", cabeceraCompra.getObservacion());
                    jsonObjBuilder.put("created_at_app", cabeceraCompra.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", cabeceraCompra.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", cabeceraCompra.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", cabeceraCompra.getUpdated_by());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("formulariocompra__cabecera_compras", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "formulariocompra__detalle_compras":
                DetalleCompra detalleCompra = DetalleCompra.findById(DetalleCompra.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", detalleCompra.getId());
                    jsonObjBuilder.put("cantidad_solicitada", detalleCompra.getCantidad_solicitada());
                    jsonObjBuilder.put("cantidad_aprobada", detalleCompra.getCantidad_aprobada());
                    jsonObjBuilder.put("cantidad_recibida", detalleCompra.getCantidad_recibida());
                    jsonObjBuilder.put("cabecera_compra_id", detalleCompra.getCabecera_compra_id());
                    jsonObjBuilder.put("producto_id", detalleCompra.getProducto_id());
                    jsonObjBuilder.put("codigo_sync", detalleCompra.getCodigo_sync());
                    jsonObjBuilder.put("codigo_formulario_sync", detalleCompra.getCodigo_formulario_sync());
                    jsonObjBuilder.put("productos_sin_id", detalleCompra.getProductos_sin_id());
                    jsonObjBuilder.put("created_at_app", detalleCompra.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", detalleCompra.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", detalleCompra.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", detalleCompra.getUpdated_by());
//                    jsonObjBuilder.put("eliminado", detalleCompra.getProducto_eliminado());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("formulariocompra__detalle_compras", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "cabecera_mantenimientos":
                CabeceraMantenimiento cabeceraMantenimiento = CabeceraMantenimiento.findById(CabeceraMantenimiento.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", cabeceraMantenimiento.getId());
                    jsonObjBuilder.put("fecha", dateFormatMySQL(cabeceraMantenimiento.getFecha()));
                    jsonObjBuilder.put("solicitado_por", cabeceraMantenimiento.getSolicitado_por());
                    jsonObjBuilder.put("aprobado_por", cabeceraMantenimiento.getAprobado_por());
                    jsonObjBuilder.put("codigo_sync", cabeceraMantenimiento.getCodigo_sync());
                    jsonObjBuilder.put("codigo_formulario_sync", cabeceraMantenimiento.getCodigo_formulario_sync());
                    jsonObjBuilder.put("area", cabeceraMantenimiento.getArea());
                    jsonObjBuilder.put("created_at_app", cabeceraMantenimiento.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", cabeceraMantenimiento.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", cabeceraMantenimiento.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", cabeceraMantenimiento.getUpdated_by());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("cabecera_mantenimientos", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "detalle_mantenimientos":
                DetalleMantenimiento detalleMantenimiento = DetalleMantenimiento.findById(DetalleMantenimiento.class, id_sqlite);

                try {
                    jsonObjBuilder = new JSONObject();
                    jsonObjBuilder.put("id", detalleMantenimiento.getId());
                    jsonObjBuilder.put("tipo", detalleMantenimiento.getTipo());
                    jsonObjBuilder.put("conforme", detalleMantenimiento.getConforme());
                    jsonObjBuilder.put("cabecera_mantenimiento_id", detalleMantenimiento.getCabecera_mantenimiento_id());
                    jsonObjBuilder.put("maquinaria_id", detalleMantenimiento.getMaquinaria_id());
                    jsonObjBuilder.put("codigo_sync", detalleMantenimiento.getCodigo_sync());
                    jsonObjBuilder.put("codigo_formulario_sync", detalleMantenimiento.getCodigo_formulario_sync());
                    jsonObjBuilder.put("mantenimiento", detalleMantenimiento.getMantenimiento());
                    jsonObjBuilder.put("created_at_app", detalleMantenimiento.getCreated_at());
                    jsonObjBuilder.put("updated_at_app", detalleMantenimiento.getUpdated_at());
                    jsonObjBuilder.put("created_by_app", detalleMantenimiento.getCreated_by());
                    jsonObjBuilder.put("updated_by_app", detalleMantenimiento.getUpdated_by());
                    jsonObjBuilder.put("maquinaria_sin_id", detalleMantenimiento.getMaquinaria_sin_id());
                    jsonObjBuilder.put("observacion", detalleMantenimiento.getObservacion());
//                    jsonObjBuilder.put("eliminado", detalleMantenimiento.getMaquinaria_eliminada());
                    jsonArrayBuilder.put(jsonObjBuilder);
                    respuestaObj.put("detalle_mantenimientos", jsonArrayBuilder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

        return respuestaObj;
    }

    private static String dateFormatMySQL(String fecha_cabecera) {
        Log.d("fecha cabecera", fecha_cabecera);
        DateFormat inputFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date dateCabecera = null;
        try {
            dateCabecera = inputFormatter.parse(fecha_cabecera);
            Log.d("date cabecera", String.valueOf(dateCabecera));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("output formatter", String.valueOf(outputFormatter.format(dateCabecera)));

        return outputFormatter.format(dateCabecera);
    }

    private static JSONObject sendImageJson(int id_sqlite) {
        JSONArray jsonArrayBuilder = new JSONArray();
        JSONObject respuestaObj = new JSONObject();
        JSONObject jsonIMG;

        ItemCheck itemCheck = ItemCheck.findById(ItemCheck.class, id_sqlite);

        String imgString = "";
        if (!Objects.equals(itemCheck.getImagen(), "") && !itemCheck.getImagen().isEmpty()) {
            Uri imageUri = Uri.parse(itemCheck.getImagen());
            Bitmap imgBitmap = BitmapFactory.decodeFile(imageUri.getPath());
            //Scale Image
            //get its orginal dimensions
            int bmOriginalWidth = imgBitmap.getWidth();
            int bmOriginalHeight = imgBitmap.getHeight();
            double originalWidthToHeightRatio =  1.0 * bmOriginalWidth / bmOriginalHeight;
            double originalHeightToWidthRatio =  1.0 * bmOriginalHeight / bmOriginalWidth;
            //choose a maximum height
            int maxHeight = 1024;
            //choose a max width
            int maxWidth = 1024;
            //call the method to get the scaled bitmap
            imgBitmap = getScaledBitmap(imgBitmap, bmOriginalWidth, bmOriginalHeight,
                    originalWidthToHeightRatio, originalHeightToWidthRatio,
                    maxHeight, maxWidth);
            //Compress Image
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //compress the photo's bytes into the byte array output stream
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
            //Get bitmap as String
            imgString = getBitmapAsString(imgBitmap);
        }

        jsonIMG = new JSONObject();
        try {
            jsonIMG.put("id", itemCheck.getId());
            jsonIMG.put("codigo_sync", itemCheck.getCodigo_sync());
            jsonIMG.put("imagen", imgString);
            jsonArrayBuilder.put(jsonIMG);
            respuestaObj.put("items_check", jsonArrayBuilder);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return respuestaObj;

    }

    private static Bitmap getScaledBitmap(Bitmap bm, int bmOriginalWidth, int bmOriginalHeight, double originalWidthToHeightRatio, double originalHeightToWidthRatio, int maxHeight, int maxWidth) {
        if(bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {

            if(bmOriginalWidth > bmOriginalHeight) {
                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalWidth, originalHeightToWidthRatio);
            } else if (bmOriginalHeight > bmOriginalWidth){
                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio);
            }

        }
        return bm;
    }

    private static Bitmap scaleDeminsFromHeight(Bitmap bm, int maxHeight, int bmOriginalHeight, double originalWidthToHeightRatio) {
        int newHeight = (int) Math.max(maxHeight, bmOriginalHeight * .55);
        int newWidth = (int) (newHeight * originalWidthToHeightRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    private static Bitmap scaleDeminsFromWidth(Bitmap bm, int maxWidth, int bmOriginalWidth, double originalHeightToWidthRatio) {
        //scale the width
        int newWidth = (int) Math.max(maxWidth, bmOriginalWidth * .75);
        int newHeight = (int) (newWidth * originalHeightToWidthRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    private static String getBitmapAsString(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
