package zentcode02.parks.dbModels;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import zentcode02.parks.helpers.db.DBConfig;

public class Travesia extends SugarRecord {

    private Integer id_servidor;
    private String responsable1;
    private String responsable2;
    private String fecha_inicio;
    private String fecha_fin;
    private String destino;
    private String observacion;
    private String resumen;
    private Integer nro_viaje;
    private Integer sync;
    private Integer unidad_id;
    private Integer estado_id;
    private Integer nro_app_sync;

    public Travesia() {}

    public Travesia(Integer id_servidor, String responsable1, String responsable2, String fecha_inicio, String fecha_fin, String destino, String observacion, String resumen, Integer nro_viaje, Integer sync, Integer unidad_id, Integer estado_id, Integer nro_app_sync) {
        this.id_servidor = id_servidor;
        this.responsable1 = responsable1;
        this.responsable2 = responsable2;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.destino = destino;
        this.observacion = observacion;
        this.resumen = resumen;
        this.nro_viaje = nro_viaje;
        this.sync = sync;
        this.unidad_id = unidad_id;
        this.estado_id = estado_id;
        this.nro_app_sync = nro_app_sync;
    }

    public Integer getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(Integer id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getResponsable1() {
        return responsable1;
    }

    public void setResponsable1(String responsable1) {
        this.responsable1 = responsable1;
    }

    public String getResponsable2() {
        return responsable2;
    }

    public void setResponsable2(String responsable2) {
        this.responsable2 = responsable2;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Integer getNro_viaje() {
        return nro_viaje;
    }

    public void setNro_viaje(Integer nro_viaje) {
        this.nro_viaje = nro_viaje;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public Integer getUnidad_id() {
        return unidad_id;
    }

    public void setUnidad_id(Integer unidad_id) {
        this.unidad_id = unidad_id;
    }

    public Integer getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(Integer estado_id) {
        this.estado_id = estado_id;
    }

    public Integer getNro_app_sync() {
        return nro_app_sync;
    }

    public void setNro_app_sync(Integer nro_app_sync) {
        this.nro_app_sync = nro_app_sync;
    }

    public List<FormularioTravesia> getFormulariosTravesia() {
        return Select.from(FormularioTravesia.class).where(
                Condition.prop(DBConfig.TRAVESIA_ID).eq(getId_servidor())
        ).list();
    }
}