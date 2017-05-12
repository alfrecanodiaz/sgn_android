package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class CabeceraMantenimiento extends SugarRecord {

    private Integer id_servidor = 0;
    private String fecha;
    private String solicitado_por = "";
    private String aprobado_por = "";
    private String codigo_sync;
    private String codigo_formulario_sync;
    private String area = "";
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;

    public CabeceraMantenimiento() {}

    public CabeceraMantenimiento(Integer id_servidor, String fecha, String solicitado_por, String aprobado_por, String codigo_sync, String codigo_formulario_sync, String area, String created_at, String updated_at, Integer created_by, Integer updated_by) {
        this.id_servidor = id_servidor;
        this.fecha = fecha;
        this.solicitado_por = solicitado_por;
        this.aprobado_por = aprobado_por;
        this.codigo_sync = codigo_sync;
        this.codigo_formulario_sync = codigo_formulario_sync;
        this.area = area;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public Integer getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(Integer id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSolicitado_por() {
        return solicitado_por;
    }

    public void setSolicitado_por(String solicitado_por) {
        this.solicitado_por = solicitado_por;
    }

    public String getAprobado_por() {
        return aprobado_por;
    }

    public void setAprobado_por(String aprobado_por) {
        this.aprobado_por = aprobado_por;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
}
