package zentcode02.parks.helpers.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zentcode02.parks.dbModels.CabeceraCheck;
import zentcode02.parks.dbModels.CabeceraCheckCopia;
import zentcode02.parks.dbModels.CabeceraCompra;
import zentcode02.parks.dbModels.CabeceraMantenimiento;
import zentcode02.parks.dbModels.CategoriaMaquinaria;
import zentcode02.parks.dbModels.DetalleCompra;
import zentcode02.parks.dbModels.DetalleMantenimiento;
import zentcode02.parks.dbModels.FormularioCategoria;
import zentcode02.parks.dbModels.FormularioCompraCategoria;
import zentcode02.parks.dbModels.FormularioCompraProducto;
import zentcode02.parks.dbModels.FormularioModelo;
import zentcode02.parks.dbModels.FormularioTravesia;
import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.dbModels.ItemCheck;
import zentcode02.parks.dbModels.ItemCheckCopia;
import zentcode02.parks.dbModels.ListAreaCompra;
import zentcode02.parks.dbModels.ListAreaMantenimiento;
import zentcode02.parks.dbModels.Maquinaria;
import zentcode02.parks.dbModels.SeccionCheck;
import zentcode02.parks.dbModels.SeccionCheckCopia;
import zentcode02.parks.dbModels.Travesia;

public class SyncDBHelper {

    public static Boolean isValidArray(JSONArray arr) throws JSONException {
        return !arr.isNull(0) && arr.length() > 0;
    }

    public static Long saveTravesia(JSONObject obj) throws JSONException {
        Travesia travesia = new Travesia();
        travesia.setId_servidor(obj.getInt("id"));
        travesia.setResponsable2(obj.getString("responsable2"));
        travesia.setFecha_inicio(obj.getString("fecha_inicio"));
        travesia.setFecha_fin(obj.getString("fecha_fin"));
        travesia.setResponsable1(obj.getString("destino"));
        travesia.setObservacion(obj.getString("observacion"));
        travesia.setResumen(obj.getString("resumen"));
        travesia.setNro_viaje(obj.getInt("nro_viaje"));
        travesia.setSync(obj.getInt("sync"));
        travesia.setUnidad_id(obj.getInt("unidad_id"));
        travesia.setEstado_id(obj.getInt("estado_id"));
        travesia.setNro_app_sync(obj.getInt("nro_app_sync"));
        return travesia.save();
    }

    public static Long saveFormularioTravesia(JSONObject obj) throws JSONException {
        FormularioTravesia fTravesia = new FormularioTravesia();
        fTravesia.setId_servidor(obj.getInt("id"));
        fTravesia.setNombre(obj.getString("nombre"));
        fTravesia.setVersion(obj.getInt("version"));
        fTravesia.setCodigo(obj.getString("codigo"));
        fTravesia.setCabecera(obj.getString("cabecera"));
        fTravesia.setDetalles(obj.getString("detalles"));
        fTravesia.setFormulario_modelo_id(obj.getInt("formulario_modelo_id"));
        fTravesia.setTravesia_id(obj.getInt("travesia_id"));
        fTravesia.setCabecera_copia(obj.getString("cabecera_copia"));
        fTravesia.setDetalles_copia(obj.getString("detalles_copia"));
        return fTravesia.save();
    }

    public static Long saveFormulario(JSONObject obj) throws JSONException {
        Formularios form = new Formularios();
        form.setId_servidor(obj.getInt("id"));
        form.setNombre(obj.getString("nombre"));
        form.setVersion(obj.getInt("version"));
        form.setCodigo(obj.getString("codigo"));
        form.setCabecera(obj.getString("cabecera"));
        form.setDetalles(obj.getString("detalles"));
        form.setFormulario_travesia_id(obj.getInt("formulario_travesia_id"));
        form.setNro_checkeo(obj.getInt("nro_checkeo"));
        form.setCodigo_sync(obj.getString("codigo_sync"));
        form.setEstado(obj.getString("estado"));
        form.setCreated_at(obj.optString("created_at_app"));
        form.setUpdated_at(obj.optString("updated_at_app"));
        form.setCreated_by(obj.optInt("created_by_app"));
        form.setUpdated_by(obj.optInt("updated_by_app"));
        return form.save();
    }

    public static Long saveFormularioModelo(JSONObject obj) throws JSONException {
        FormularioModelo fModelo = new FormularioModelo();
        fModelo.setId_servidor(obj.getInt("id"));
        fModelo.setNombre(obj.getString("nombre"));
        fModelo.setVersion(obj.getInt("version"));
        fModelo.setCodigo(obj.getString("codigo"));
        fModelo.setDescripcion(obj.getString("descripcion"));
        fModelo.setCabecera(obj.getString("cabecera"));
        fModelo.setDetalles(obj.getString("detalles"));
        fModelo.setCategoria_id(obj.getInt("categoria_id"));
        fModelo.setPlantilla(obj.getString("plantilla"));
        return fModelo.save();
    }

    public static Long saveFormularioCategoria(JSONObject obj) throws JSONException {
        FormularioCategoria fCategoria = new FormularioCategoria();
        fCategoria.setId_servidor(obj.getInt("id"));
        fCategoria.setNombre(obj.getString("nombre"));
        fCategoria.setDescripcion(obj.getString("descripcion"));
        fCategoria.setModulo_id(obj.getInt("modulo_id"));
        fCategoria.setColor("#" + obj.getString("color"));
        return fCategoria.save();
    }

    public static Long saveCabeceraCompra(JSONObject obj) throws JSONException {
        CabeceraCompra cabecera = new CabeceraCompra();
        cabecera.setId_servidor(obj.getInt("id"));
        cabecera.setEmpresa_del_grupo(obj.getString("empresa_del_grupo"));
        cabecera.setPrioridad(obj.getString("prioridad"));
        cabecera.setSolicitado_por(obj.getString("solicitado_por"));
        cabecera.setSolicitado_fecha(obj.getString("solicitado_fecha"));
        cabecera.setEnviado_por(obj.getString("enviado_por"));
        cabecera.setEnviado_fecha(obj.getString("enviado_fecha"));
        cabecera.setAprobado_por(obj.getString("aprobado_por"));
        cabecera.setAprobado_fecha(obj.getString("aprobado_fecha"));
        cabecera.setRecibido_por(obj.getString("recibido_por"));
        cabecera.setRecibido_fecha(obj.getString("recibido_fecha"));
        cabecera.setNro_oc(obj.getString("nro_oc"));
        cabecera.setCodigo_sync(obj.getString("codigo_sync"));
        cabecera.setCodigo_formulario_sync(obj.getString("codigo_formulario_sync"));
        cabecera.setObservacion(obj.getString("observacion"));
        cabecera.setArea(obj.getString("area"));
        cabecera.setCreated_at(obj.getString("created_at_app"));
        cabecera.setUpdated_at(obj.getString("updated_at_app"));
        cabecera.setCreated_by(obj.getInt("created_by_app"));
        cabecera.setUpdated_by(obj.getInt("updated_by_app"));
        return cabecera.save();
    }

    public static Long saveDetalleCompra(JSONObject obj) throws JSONException {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setId_servidor(obj.getInt("id"));
        detalle.setCantidad_solicitada(obj.optInt("cantidad_solicitada"));
        detalle.setCantidad_aprobada(obj.optInt("cantidad_aprobada"));
        detalle.setCantidad_recibida(obj.optInt("cantidad_recibida"));
        detalle.setProductos_sin_id(obj.optString("productos_sin_id"));
        detalle.setCabecera_compra_id(obj.getInt("cabecera_compra_id"));
        detalle.setProducto_id(obj.optInt("producto_id"));
        detalle.setCodigo_sync(obj.getString("codigo_sync"));
        detalle.setCodigo_formulario_sync(obj.getString("codigo_formulario_sync"));
        detalle.setCreated_at(obj.getString("created_at_app"));
        detalle.setUpdated_at(obj.optString("updated_at_app"));
        detalle.setCreated_by(obj.getInt("created_by_app"));
        detalle.setUpdated_by(obj.optInt("updated_by_app"));
        detalle.setEliminado(obj.optInt("eliminado"));
        return detalle.save();
    }

    public static Long saveFormularioCompraProducto(JSONObject obj) throws JSONException {
        FormularioCompraProducto fCompra = new FormularioCompraProducto();
        fCompra.setId_servidor(obj.getInt("id"));
        fCompra.setNombre(obj.getString("nombre"));
        fCompra.setUnidad_de_medida(obj.getString("unidad_de_medida"));
        fCompra.setObservacion(obj.getString("observacion"));
        fCompra.setCategoria_id(obj.getInt("categoria_id"));
        return fCompra.save();
    }

    public static Long saveFormularioCompraCategoria(JSONObject obj) throws JSONException {
        FormularioCompraCategoria fCategoria = new FormularioCompraCategoria();
        fCategoria.setId_servidor(obj.getInt("id"));
        fCategoria.setDescripcion(obj.getString("descripcion"));
        return fCategoria.save();
    }

    public static Long saveCabeceraCheckCopia(JSONObject obj) throws JSONException {
        CabeceraCheckCopia cabecera = new CabeceraCheckCopia();
        cabecera.setId_servidor(obj.getInt("id"));
        cabecera.setFecha(obj.getString("fecha"));
        cabecera.setSolicitado_por(obj.optString("solicitado_por"));
        cabecera.setAprobado_por(obj.optString("aprobado_por"));
        return cabecera.save();
    }

    public static Long saveSeccionCheckCopia(JSONObject obj) throws JSONException {
        SeccionCheckCopia seccion = new SeccionCheckCopia();
        seccion.setId_servidor(obj.getInt("id"));
        seccion.setNombre(obj.getString("nombre"));
        seccion.setNombre_campo(obj.getString("nombre_campo"));
        seccion.setObservacion(obj.optString("observacion"));
        return seccion.save();
    }

    public static Long saveItemCheckCopia(JSONObject obj) throws JSONException {
        ItemCheckCopia item = new ItemCheckCopia();
        item.setId_servidor(obj.getInt("id"));
        item.setNombre(obj.getString("nombre"));
        item.setImagen_habilitar(obj.optInt("imagen_habilitar"));
        item.setImagen_requerido(obj.optInt("imagen_requerido"));
        item.setGps_habilitar(obj.optInt("gps_habilitar"));
        item.setGps_requerido(obj.optInt("gps_requerido"));
        item.setQr_requerido(obj.optInt("qr_requerido"));
        item.setQr_habilitar(obj.optInt("qr_habilitar"));
        item.setCritico(obj.optInt("critico"));
        item.setQr_generado(obj.optString("qr_generado"));
        item.setSeccion_check_id(obj.getInt("seccion_check_id"));
        return item.save();
    }

    public static Long saveCabeceraCheck(JSONObject obj) throws JSONException {
        CabeceraCheck cabecera = new CabeceraCheck();
        cabecera.setId_servidor(obj.getInt("id"));
        cabecera.setFecha(obj.getString("fecha"));
        cabecera.setCodigo_sync(obj.getString("codigo_sync"));
        cabecera.setCodigo_formulario_sync(obj.getString("codigo_formulario_sync"));
        cabecera.setCreated_at(obj.getString("created_at_app"));
        cabecera.setUpdated_at(obj.optString("updated_at_app"));
        cabecera.setCreated_by(obj.getInt("created_by_app"));
        cabecera.setUpdated_by(obj.optInt("updated_by_app"));
        cabecera.setVerificado_por(obj.getString("verificado_por"));
        cabecera.setRevisado_por(obj.getString("revisado_por"));
        cabecera.setInicio_viaje(obj.getString("inicio_viaje"));
        cabecera.setFin_viaje(obj.getString("fin_viaje"));
        return cabecera.save();
    }

    public static Long saveSeccionCheck(JSONObject obj) throws JSONException {
        SeccionCheck seccion = new SeccionCheck();
        seccion.setId_servidor(obj.getInt("id"));
        seccion.setNombre(obj.getString("nombre"));
        seccion.setNombre_campo(obj.getString("nombre_campo"));
        seccion.setObservacion(obj.optString("observacion"));
        seccion.setCodigo_sync(obj.getString("codigo_sync"));
        seccion.setCodigo_formulario_sync(obj.getString("codigo_formulario_sync"));
        seccion.setCreated_at(obj.getString("created_at_app"));
        seccion.setUpdated_at(obj.optString("updated_at_app"));
        seccion.setCreated_by(obj.getInt("created_by_app"));
        seccion.setUpdated_by(obj.optInt("updated_by_app"));
        return seccion.save();
    }

    public static Long saveItemCheck(JSONObject obj) throws JSONException {
        ItemCheck item = new ItemCheck();
        item.setId_servidor(obj.getInt("id"));
        item.setNombre(obj.getString("nombre"));
        item.setImagen_habilitar(obj.getInt("imagen_habilitar"));
        item.setImagen_requerido(obj.getInt("imagen_requerido"));
        item.setGps_habilitar(obj.getInt("gps_habilitar"));
        item.setGps_requerido(obj.getInt("gps_requerido"));
        item.setQr_requerido(obj.getInt("qr_requerido"));
        item.setQr_habilitar(obj.getInt("qr_habilitar"));
        item.setImagen(obj.optString("imagen"));
        item.setGps(obj.optString("gps"));
        item.setQr(obj.optString("qr"));
        item.setFecha_app(obj.getString("fecha_app"));
        item.setCodigo_sync(obj.getString("codigo_sync"));
        item.setCritico(obj.getInt("critico"));
        item.setSatisfactorio(obj.getInt("satisfactorio"));
        item.setObservacion(obj.getString("observacion"));
        item.setQr_status(obj.optInt("qr_status"));
        item.setSeccion_check_id(obj.getInt("seccion_check_id"));
        item.setCreated_at(obj.getString("created_at_app"));
        item.setUpdated_at(obj.getString("updated_at_app"));
        item.setCreated_by(obj.getInt("created_by_app"));
        item.setUpdated_by(obj.getInt("updated_by_app"));
        return item.save();
    }

    public static Long saveCategoriaMaquinaria(JSONObject obj) throws JSONException {
        CategoriaMaquinaria categoria = new CategoriaMaquinaria();
        categoria.setId_servidor(obj.getInt("id"));
        categoria.setDescripcion(obj.getString("descripcion"));
        return categoria.save();
    }

    public static Long saveMaquinaria(JSONObject obj) throws JSONException {
        Maquinaria maquinaria = new Maquinaria();
        maquinaria.setId_servidor(obj.getInt("id"));
        maquinaria.setDescripcion(obj.optString("descripcion"));
        maquinaria.setCategoria_id(obj.getInt("categoria_id"));
        return maquinaria.save();
    }

    public static Long saveCabeceraMantenimiento(JSONObject obj) throws JSONException {
        CabeceraMantenimiento cabecera = new CabeceraMantenimiento();
        cabecera.setId_servidor(obj.getInt("id"));
        cabecera.setFecha(obj.getString("fecha"));
        cabecera.setSolicitado_por(obj.getString("solicitado_por"));
        cabecera.setAprobado_por(obj.getString("aprobado_por"));
        cabecera.setCodigo_sync(obj.getString("codigo_sync"));
        cabecera.setCodigo_formulario_sync(obj.getString("codigo_formulario_sync"));
        cabecera.setArea(obj.getString("area"));
        cabecera.setCreated_at(obj.getString("created_at_app"));
        cabecera.setUpdated_at(obj.optString("updated_at_app"));
        cabecera.setCreated_by(obj.getInt("created_by_app"));
        cabecera.setUpdated_by(obj.optInt("updated_by_app"));
        return cabecera.save();
    }

    public static Long saveDetalleMantenimiento(JSONObject obj) throws JSONException {
        DetalleMantenimiento detalle = new DetalleMantenimiento();
        detalle.setId_servidor(obj.getInt("id"));
        detalle.setTipo(obj.getString("tipo"));
        detalle.setConforme(obj.getString("conforme"));
        detalle.setCabecera_mantenimiento_id(obj.getInt("cabecera_mantenimiento_id"));
        detalle.setMaquinaria_id(obj.optInt("maquinaria_id"));
        detalle.setCodigo_sync(obj.getString("codigo_sync"));
        detalle.setCodigo_formulario_sync(obj.getString("codigo_formulario_sync"));
        detalle.setMantenimiento(obj.getString("mantenimiento"));
        detalle.setObservacion(obj.getString("observacion"));
        detalle.setCreated_at(obj.getString("created_at_app"));
        detalle.setUpdated_at(obj.optString("updated_at_app"));
        detalle.setCreated_by(obj.getInt("created_by_app"));
        detalle.setUpdated_by(obj.optInt("updated_by_app"));
        detalle.setMaquinaria_sin_id(obj.optString("maquinaria_sin_id"));
        detalle.setEliminado(obj.optInt("eliminado"));
        return detalle.save();
    }

    public static Long saveListAreaCompra(JSONObject obj, String index) throws JSONException {
        ListAreaCompra listArea = new ListAreaCompra();
        listArea.setArea(obj.getString(index));
        return listArea.save();
    }

    public static Long saveListAreaMantenimiento(JSONObject obj, String index) throws JSONException {
        ListAreaMantenimiento listArea = new ListAreaMantenimiento();
        listArea.setArea(obj.getString(index));
        return listArea.save();
    }
}
