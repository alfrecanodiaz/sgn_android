package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

import java.util.List;

public class ItemCheck extends SugarRecord {

    private Integer id_servidor = 0;
    private String nombre;
    private Integer imagen_habilitar;
    private Integer imagen_requerido;
    private Integer gps_habilitar;
    private Integer gps_requerido;
    private Integer qr_habilitar;
    private Integer qr_requerido;
    private Integer qr_status = 0;
    private String imagen = "";
    private String gps = "";
    private String qr;
    private Integer seccion_check_id;
    private String fecha_app;
    private String codigo_sync;
    private Integer critico;
    private Integer satisfactorio = 2;
    private String observacion = "";
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;

    public ItemCheck() {}

    public ItemCheck(Integer id_servidor, String nombre, Integer imagen_habilitar, Integer imagen_requerido, Integer gps_habilitar, Integer gps_requerido, Integer qr_habilitar, Integer qr_requerido, Integer qr_status, String imagen, String gps, String qr, Integer seccion_check_id, String fecha_app, String codigo_sync, Integer critico, Integer satisfactorio, String observacion, String created_at, String updated_at, Integer created_by, Integer updated_by) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.imagen_habilitar = imagen_habilitar;
        this.imagen_requerido = imagen_requerido;
        this.gps_habilitar = gps_habilitar;
        this.gps_requerido = gps_requerido;
        this.qr_habilitar = qr_habilitar;
        this.qr_requerido = qr_requerido;
        this.qr_status = qr_status;
        this.imagen = imagen;
        this.gps = gps;
        this.qr = qr;
        this.seccion_check_id = seccion_check_id;
        this.fecha_app = fecha_app;
        this.codigo_sync = codigo_sync;
        this.critico = critico;
        this.satisfactorio = satisfactorio;
        this.observacion = observacion;
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

    public Integer getImagen_habilitar() {
        return imagen_habilitar;
    }

    public void setImagen_habilitar(Integer imagen_habilitar) {
        this.imagen_habilitar = imagen_habilitar;
    }

    public Integer getImagen_requerido() {
        return imagen_requerido;
    }

    public void setImagen_requerido(Integer imagen_requerido) {
        this.imagen_requerido = imagen_requerido;
    }

    public Integer getGps_habilitar() {
        return gps_habilitar;
    }

    public void setGps_habilitar(Integer gps_habilitar) {
        this.gps_habilitar = gps_habilitar;
    }

    public Integer getGps_requerido() {
        return gps_requerido;
    }

    public void setGps_requerido(Integer gps_requerido) {
        this.gps_requerido = gps_requerido;
    }

    public Integer getQr_habilitar() {
        return qr_habilitar;
    }

    public void setQr_habilitar(Integer qr_habilitar) {
        this.qr_habilitar = qr_habilitar;
    }

    public Integer getQr_requerido() {
        return qr_requerido;
    }

    public void setQr_requerido(Integer qr_requerido) {
        this.qr_requerido = qr_requerido;
    }

    public Integer getQr_status() {
        return qr_status;
    }

    public void setQr_status(Integer qr_status) {
        this.qr_status = qr_status;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public Integer getSeccion_check_id() {
        return seccion_check_id;
    }

    public void setSeccion_check_id(Integer seccion_check_id) {
        this.seccion_check_id = seccion_check_id;
    }

    public String getFecha_app() {
        return fecha_app;
    }

    public void setFecha_app(String fecha_app) {
        this.fecha_app = fecha_app;
    }

    public String getCodigo_sync() {
        return codigo_sync;
    }

    public void setCodigo_sync(String codigo_sync) {
        this.codigo_sync = codigo_sync;
    }

    public Integer getCritico() {
        return critico;
    }

    public void setCritico(Integer critico) {
        this.critico = critico;
    }

    public Integer getSatisfactorio() {
        return satisfactorio;
    }

    public void setSatisfactorio(Integer satisfactorio) {
        this.satisfactorio = satisfactorio;
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

    @Override
    public String toString() {
        return this.nombre;
    }

    public List<SeccionCheck> getSeccion() {
        return SeccionCheck.find(SeccionCheck.class, "id = ?", String.valueOf(getSeccion_check_id()));
    }
}