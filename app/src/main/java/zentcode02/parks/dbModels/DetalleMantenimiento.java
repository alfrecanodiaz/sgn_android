package zentcode02.parks.dbModels;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

public class DetalleMantenimiento extends SugarRecord {

    private Integer id_servidor = 0;
    private String tipo = "";
    private String conforme = "";
    private Integer cabecera_mantenimiento_id;
    private Integer maquinaria_id;
    private String codigo_sync;
    private String codigo_formulario_sync;
    private String mantenimiento = "Ninguno";
    private String observacion = "";
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;
    private String maquinaria_sin_id = "";
    private Integer eliminado = 0;

    public DetalleMantenimiento() {}

    public DetalleMantenimiento(Integer id_servidor, String tipo, String conforme, Integer cabecera_mantenimiento_id, Integer maquinaria_id, String codigo_sync, String codigo_formulario_sync, String mantenimiento, String observacion, String created_at, String updated_at, Integer created_by, Integer updated_by, String maquinaria_sin_id, Integer eliminado) {
        this.id_servidor = id_servidor;
        this.tipo = tipo;
        this.conforme = conforme;
        this.cabecera_mantenimiento_id = cabecera_mantenimiento_id;
        this.maquinaria_id = maquinaria_id;
        this.codigo_sync = codigo_sync;
        this.codigo_formulario_sync = codigo_formulario_sync;
        this.mantenimiento = mantenimiento;
        this.observacion = observacion;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.maquinaria_sin_id = maquinaria_sin_id;
        this.eliminado = eliminado;
    }

    public Integer getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(Integer id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getConforme() {
        return conforme;
    }

    public void setConforme(String conforme) {
        this.conforme = conforme;
    }

    public Integer getCabecera_mantenimiento_id() {
        return cabecera_mantenimiento_id;
    }

    public void setCabecera_mantenimiento_id(Integer cabecera_mantenimiento_id) {
        this.cabecera_mantenimiento_id = cabecera_mantenimiento_id;
    }

    public Integer getMaquinaria_id() {
        return maquinaria_id;
    }

    public void setMaquinaria_id(Integer maquinaria_id) {
        this.maquinaria_id = maquinaria_id;
    }

    public String getCodigo_sync() {
        return codigo_sync;
    }

    public void setCodigo_sync(String codigo_sync) {
        this.codigo_sync = codigo_sync;
    }

    public String getCodigo_formulario_sync() {
        return codigo_formulario_sync;
    }

    public void setCodigo_formulario_sync(String codigo_formulario_sync) {
        this.codigo_formulario_sync = codigo_formulario_sync;
    }

    public String getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(String mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public Integer getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Integer updated_by) {
        this.updated_by = updated_by;
    }

    public String getMaquinaria_sin_id() {
        return maquinaria_sin_id;
    }

    public void setMaquinaria_sin_id(String maquinaria_sin_id) {
        this.maquinaria_sin_id = maquinaria_sin_id;
    }

    public Integer getEliminado() {
        return eliminado;
    }

    public void setEliminado(Integer eliminado) {
        this.eliminado = eliminado;
    }

    public String getMaquinaria() {
        Maquinaria maquinaria = Select.from(Maquinaria.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("maquinarias_id")).eq(getMaquinaria_id())
                ).first();
        return maquinaria.getNombre();
    }
}
