package zentcode02.parks.helpers.db;

import com.orm.query.Condition;
import com.orm.query.Select;

import zentcode02.parks.dbModels.CabeceraCheck;
import zentcode02.parks.dbModels.CabeceraCheckCopia;
import zentcode02.parks.dbModels.CabeceraCompra;
import zentcode02.parks.dbModels.CabeceraMantenimiento;
import zentcode02.parks.dbModels.DetalleCompra;
import zentcode02.parks.dbModels.DetalleMantenimiento;
import zentcode02.parks.dbModels.FormularioTravesia;
import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.dbModels.ItemCheck;
import zentcode02.parks.dbModels.ItemCheckCopia;
import zentcode02.parks.dbModels.SeccionCheck;
import zentcode02.parks.dbModels.SeccionCheckCopia;
import zentcode02.parks.utils.Helper;

public class CreateDBHelper {

    public static Long createFormulario(FormularioTravesia fm_travesia, String codigo_sync, Integer user_id) {
        Formularios form = new Formularios();
        form.setNombre(fm_travesia.getNombre());
        form.setVersion(fm_travesia.getVersion());
        form.setCodigo(fm_travesia.getCodigo());
        form.setCabecera("");
        form.setDetalles("");
        form.setFormulario_travesia_id(fm_travesia.getId_servidor());
        form.setNro_checkeo(getNroCheckeo(fm_travesia.getNombre()));
        form.setCodigo_sync(codigo_sync);
        form.setEstado("Pendiente");
        form.setCreated_at(Helper.getMySqlDateTimeFormat());
        form.setUpdated_at(Helper.getMySqlDateTimeFormat());
        form.setCreated_by(user_id);
        form.setUpdated_by(user_id);
        return form.save();
    }

    private static Integer getNroCheckeo(String fm_travesia_nombre) {
        Formularios form = Select.from(Formularios.class).where(
                Condition.prop(DBConfig.F_NOMBRE).eq(fm_travesia_nombre)
        ).orderBy(DBConfig.PK_ID + " DESC").first();
        if (form != null) {
            return form.getNro_checkeo() + 1;
        } else {
            return 1;
        }
    }

    public static String setCodigoSyncFormulario(Long form_id) {
        Formularios form = Formularios.findById(Formularios.class, form_id);
        form.setCodigo_sync(form.getCodigo_sync()+"-"+String.valueOf(form.getId()));
        form.save();
        return form.getCodigo_sync();
    }

    public static Long createCabeceraCheck(CabeceraCheckCopia copia, String codigo_sync, String cod_form_sync, Integer user_id) {
        CabeceraCheck cabecera = new CabeceraCheck();
        cabecera.setFecha(Helper.getMySqlDateFormat());
        cabecera.setVerificado_por(copia.getSolicitado_por());
        cabecera.setRevisado_por(copia.getAprobado_por());
        cabecera.setCodigo_sync(codigo_sync);
        cabecera.setCodigo_formulario_sync(cod_form_sync);
        cabecera.setCreated_at(Helper.getMySqlDateTimeFormat());
        cabecera.setUpdated_at(Helper.getMySqlDateTimeFormat());
        cabecera.setCreated_by(user_id);
        cabecera.setUpdated_by(user_id);
        return cabecera.save();
    }

    public static Long createSeccionCheck(SeccionCheckCopia copia, String codigo_sync, String cod_form_sync, Integer user_id) {
        SeccionCheck seccion = new SeccionCheck();
        seccion.setNombre(copia.getNombre());
        seccion.setNombre_campo(copia.getNombre_campo());
        seccion.setObservacion(copia.getObservacion());
        seccion.setCodigo_sync(codigo_sync);
        seccion.setCodigo_formulario_sync(cod_form_sync);
        seccion.setCreated_at(Helper.getMySqlDateTimeFormat());
        seccion.setUpdated_at(Helper.getMySqlDateTimeFormat());
        seccion.setCreated_by(user_id);
        seccion.setUpdated_by(user_id);
        return seccion.save();
    }

    public static Long createItemCheck(ItemCheckCopia copia, String codigo_sync, Long seccion_id, Integer user_id) {
        ItemCheck item = new ItemCheck();
        item.setNombre(copia.getNombre());
        item.setImagen_habilitar(copia.getImagen_habilitar());
        item.setImagen_requerido(copia.getImagen_requerido());
        item.setGps_habilitar(copia.getGps_habilitar());
        item.setGps_requerido(copia.getGps_requerido());
        item.setQr_habilitar(copia.getQr_habilitar());
        item.setQr_requerido(copia.getQr_requerido());
        item.setQr(copia.getQr_generado());
        item.setSeccion_check_id(seccion_id.intValue());
        item.setFecha_app(Helper.getMySqlDateFormat());
        item.setCodigo_sync(codigo_sync);
        item.setCritico(copia.getCritico());
        item.setCreated_at(Helper.getMySqlDateTimeFormat());
        item.setUpdated_at(Helper.getMySqlDateTimeFormat());
        item.setCreated_by(user_id);
        item.setUpdated_by(user_id);
        return item.save();
    }

    public static Long createCabeceraCompra(String codigo_sync, String cod_form_sync, Integer user_id) {
        CabeceraCompra cabecera = new CabeceraCompra();
        cabecera.setId_servidor(0);
        cabecera.setCodigo_sync(codigo_sync);
        cabecera.setCodigo_formulario_sync(cod_form_sync);
        cabecera.setCreated_at(Helper.getMySqlDateTimeFormat());
        cabecera.setUpdated_at(Helper.getMySqlDateTimeFormat());
        cabecera.setCreated_by(user_id);
        cabecera.setUpdated_by(user_id);
        return cabecera.save();
    }

    public static Long createDetalleCompra(Integer producto_id, Long cabecera_id, String codigo_sync, String cod_form_sync, Integer user_id) {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setCabecera_compra_id(cabecera_id.intValue());
        detalle.setProducto_id(producto_id);
        detalle.setCodigo_sync(codigo_sync);
        detalle.setCodigo_formulario_sync(cod_form_sync);
        detalle.setCreated_at(Helper.getMySqlDateTimeFormat());
        detalle.setUpdated_at(Helper.getMySqlDateTimeFormat());
        detalle.setCreated_by(user_id);
        detalle.setUpdated_by(user_id);
        return detalle.save();
    }

    public static Long createCabeceraMantenimiento(String codigo_sync, String cod_form_sync, Integer user_id) {
        CabeceraMantenimiento cabecera = new CabeceraMantenimiento();
        cabecera.setFecha(Helper.getMySqlDateFormat());
        cabecera.setCodigo_sync(codigo_sync);
        cabecera.setCodigo_formulario_sync(cod_form_sync);
        cabecera.setCreated_at(Helper.getMySqlDateTimeFormat());
        cabecera.setUpdated_at(Helper.getMySqlDateTimeFormat());
        cabecera.setCreated_by(user_id);
        cabecera.setUpdated_by(user_id);
        return cabecera.save();
    }

    public static Long createDetalleMantenimiento(Integer maquinaria_id, Long cabecera_id, String codigo_sync, String cod_form_sync, Integer user_id) {
        DetalleMantenimiento detalle = new DetalleMantenimiento();
        detalle.setCabecera_mantenimiento_id(cabecera_id.intValue());
        detalle.setMaquinaria_id(maquinaria_id);
        detalle.setCodigo_sync(codigo_sync);
        detalle.setCodigo_formulario_sync(cod_form_sync);
        detalle.setCreated_at(Helper.getMySqlDateTimeFormat());
        detalle.setUpdated_at(Helper.getMySqlDateTimeFormat());
        detalle.setCreated_by(user_id);
        detalle.setUpdated_by(user_id);
        return detalle.save();
    }
}