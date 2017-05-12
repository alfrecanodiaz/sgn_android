package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class CabeceraCompra extends SugarRecord {

    private Integer id_servidor = 0;
    private String empresa_del_grupo = "";
    private String prioridad = "";
    private String solicitado_por = "";
    private String solicitado_fecha = "";
    private String enviado_por = "";
    private String enviado_fecha = "";
    private String aprobado_por = "";
    private String aprobado_fecha = "";
    private String recibido_por = "";
    private String recibido_fecha = "";
    private String nro_oc = "";
    private String codigo_sync;
    private String codigo_formulario_sync;
    private String observacion = "";
    private String area = "";
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;

    public CabeceraCompra() {}

    public CabeceraCompra(Integer id_servidor, String empresa_del_grupo, String prioridad, String solicitado_por, String solicitado_fecha, String enviado_por, String enviado_fecha, String aprobado_por, String aprobado_fecha, String recibido_por, String recibido_fecha, String nro_oc, String codigo_sync, String codigo_formulario_sync, String observacion, String area, String created_at, String updated_at, Integer created_by, Integer updated_by) {
        this.id_servidor = id_servidor;
        this.empresa_del_grupo = empresa_del_grupo;
        this.prioridad = prioridad;
        this.solicitado_por = solicitado_por;
        this.solicitado_fecha = solicitado_fecha;
        this.enviado_por = enviado_por;
        this.enviado_fecha = enviado_fecha;
        this.aprobado_por = aprobado_por;
        this.aprobado_fecha = aprobado_fecha;
        this.recibido_por = recibido_por;
        this.recibido_fecha = recibido_fecha;
        this.nro_oc = nro_oc;
        this.codigo_sync = codigo_sync;
        this.codigo_formulario_sync = codigo_formulario_sync;
        this.observacion = observacion;
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

    public String getEmpresa_del_grupo() {
        return empresa_del_grupo;
    }

    public void setEmpresa_del_grupo(String empresa_del_grupo) {
        this.empresa_del_grupo = empresa_del_grupo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getSolicitado_por() {
        return solicitado_por;
    }

    public void setSolicitado_por(String solicitado_por) {
        this.solicitado_por = solicitado_por;
    }

    public String getSolicitado_fecha() {
        return solicitado_fecha;
    }

    public void setSolicitado_fecha(String solicitado_fecha) {
        this.solicitado_fecha = solicitado_fecha;
    }

    public String getEnviado_por() {
        return enviado_por;
    }

    public void setEnviado_por(String enviado_por) {
        this.enviado_por = enviado_por;
    }

    public String getEnviado_fecha() {
        return enviado_fecha;
    }

    public void setEnviado_fecha(String enviado_fecha) {
        this.enviado_fecha = enviado_fecha;
    }

    public String getAprobado_por() {
        return aprobado_por;
    }

    public void setAprobado_por(String aprobado_por) {
        this.aprobado_por = aprobado_por;
    }

    public String getAprobado_fecha() {
        return aprobado_fecha;
    }

    public void setAprobado_fecha(String aprobado_fecha) {
        this.aprobado_fecha = aprobado_fecha;
    }

    public String getRecibido_por() {
        return recibido_por;
    }

    public void setRecibido_por(String recibido_por) {
        this.recibido_por = recibido_por;
    }

    public String getRecibido_fecha() {
        return recibido_fecha;
    }

    public void setRecibido_fecha(String recibido_fecha) {
        this.recibido_fecha = recibido_fecha;
    }

    public String getNro_oc() {
        return nro_oc;
    }

    public void setNro_oc(String nro_oc) {
        this.nro_oc = nro_oc;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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