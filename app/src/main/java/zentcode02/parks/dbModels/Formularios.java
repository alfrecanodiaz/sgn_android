package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class Formularios extends SugarRecord {

    private Integer id_servidor = 0;
    private String nombre;
    private Integer version;
    private String codigo;
    private String cabecera;
    private String detalles;
    private Integer formulario_travesia_id;
    private Integer nro_checkeo;
    private String codigo_sync;
    private String estado;
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;

    public Formularios() {}

    public Formularios(Integer id_servidor, String nombre, Integer version, String codigo, String cabecera, String detalles, Integer formulario_travesia_id, Integer nro_checkeo, String codigo_sync, String estado, String created_at, String updated_at, Integer created_by, Integer updated_by) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.version = version;
        this.codigo = codigo;
        this.cabecera = cabecera;
        this.detalles = detalles;
        this.formulario_travesia_id = formulario_travesia_id;
        this.nro_checkeo = nro_checkeo;
        this.codigo_sync = codigo_sync;
        this.estado = estado;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Integer getFormulario_travesia_id() {
        return formulario_travesia_id;
    }

    public void setFormulario_travesia_id(Integer formulario_travesia_id) {
        this.formulario_travesia_id = formulario_travesia_id;
    }

    public Integer getNro_checkeo() {
        return nro_checkeo;
    }

    public void setNro_checkeo(Integer nro_checkeo) {
        this.nro_checkeo = nro_checkeo;
    }

    public String getCodigo_sync() {
        return codigo_sync;
    }

    public void setCodigo_sync(String codigo_sync) {
        this.codigo_sync = codigo_sync;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    @Override
    public String toString() {
        return this.estado;
    }
}
