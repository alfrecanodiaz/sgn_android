package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class CabeceraCheck extends SugarRecord {

    private Integer id_servidor = 0;
    private String fecha;
    private String codigo_sync;
    private String verificado_por;
    private String revisado_por;
    private String codigo_formulario_sync;
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;
    private String inicio_viaje = "";
    private String fin_viaje = "";

    public CabeceraCheck() {}

    public CabeceraCheck(Integer id_servidor, String fecha, String codigo_sync, String verificado_por, String revisado_por, String codigo_formulario_sync, String created_at, String updated_at, Integer created_by, Integer updated_by, String inicio_viaje, String fin_viaje) {
        this.id_servidor = id_servidor;
        this.fecha = fecha;
        this.codigo_sync = codigo_sync;
        this.verificado_por = verificado_por;
        this.revisado_por = revisado_por;
        this.codigo_formulario_sync = codigo_formulario_sync;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.inicio_viaje = inicio_viaje;
        this.fin_viaje = fin_viaje;
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

    public String getCodigo_sync() {
        return codigo_sync;
    }

    public void setCodigo_sync(String codigo_sync) {
        this.codigo_sync = codigo_sync;
    }

    public String getVerificado_por() {
        return verificado_por;
    }

    public void setVerificado_por(String verificado_por) {
        this.verificado_por = verificado_por;
    }

    public String getRevisado_por() {
        return revisado_por;
    }

    public void setRevisado_por(String revisado_por) {
        this.revisado_por = revisado_por;
    }

    public String getCodigo_formulario_sync() {
        return codigo_formulario_sync;
    }

    public void setCodigo_formulario_sync(String codigo_formulario_sync) {
        this.codigo_formulario_sync = codigo_formulario_sync;
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

    public String getInicio_viaje() {
        return inicio_viaje;
    }

    public void setInicio_viaje(String inicio_viaje) {
        this.inicio_viaje = inicio_viaje;
    }

    public String getFin_viaje() {
        return fin_viaje;
    }

    public void setFin_viaje(String fin_viaje) {
        this.fin_viaje = fin_viaje;
    }

    @Override
    public String toString() {
        return fecha;
    }
}