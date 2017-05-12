package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class ItemCheckCopia extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private Integer imagen_habilitar;
    private Integer imagen_requerido;
    private Integer gps_habilitar;
    private Integer gps_requerido;
    private Integer qr_requerido;
    private Integer qr_habilitar;
    private Integer critico;
    private String qr_generado;
    private Integer seccion_check_id;

    public ItemCheckCopia() {}

    public ItemCheckCopia(Integer id_servidor, String nombre, Integer imagen_habilitar, Integer imagen_requerido, Integer gps_habilitar, Integer gps_requerido, Integer qr_requerido, Integer qr_habilitar, Integer critico, String qr_generado, Integer seccion_check_id) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.imagen_habilitar = imagen_habilitar;
        this.imagen_requerido = imagen_requerido;
        this.gps_habilitar = gps_habilitar;
        this.gps_requerido = gps_requerido;
        this.qr_requerido = qr_requerido;
        this.qr_habilitar = qr_habilitar;
        this.critico = critico;
        this.qr_generado = qr_generado;
        this.seccion_check_id = seccion_check_id;
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

    public Integer getQr_requerido() {
        return qr_requerido;
    }

    public void setQr_requerido(Integer qr_requerido) {
        this.qr_requerido = qr_requerido;
    }

    public Integer getQr_habilitar() {
        return qr_habilitar;
    }

    public void setQr_habilitar(Integer qr_habilitar) {
        this.qr_habilitar = qr_habilitar;
    }

    public Integer getCritico() {
        return critico;
    }

    public void setCritico(Integer critico) {
        this.critico = critico;
    }

    public String getQr_generado() {
        return qr_generado;
    }

    public void setQr_generado(String qr_generado) {
        this.qr_generado = qr_generado;
    }

    public Integer getSeccion_check_id() {
        return seccion_check_id;
    }

    public void setSeccion_check_id(Integer seccion_check_id) {
        this.seccion_check_id = seccion_check_id;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}