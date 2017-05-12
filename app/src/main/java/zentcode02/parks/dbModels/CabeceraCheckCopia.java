package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class CabeceraCheckCopia extends SugarRecord {

    private Integer id_servidor;
    private String fecha;
    private String solicitado_por;
    private String aprobado_por;

    public CabeceraCheckCopia() {}

    public CabeceraCheckCopia(Integer id_servidor, String fecha, String solicitado_por, String aprobado_por) {
        this.id_servidor = id_servidor;
        this.fecha = fecha;
        this.solicitado_por = solicitado_por;
        this.aprobado_por = aprobado_por;
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

    @Override
    public String toString() {
        return this.fecha;
    }
}