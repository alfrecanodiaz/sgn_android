package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class Sincronizar extends SugarRecord {

    private String tabla_a_sincronizar;
    private Long id_sqlite;
    private String accion;
    private Boolean enviado;
    private Integer tipo;

    public Sincronizar() {}

    public Sincronizar(String tabla_a_sincronizar, Long id_sqlite, String accion, Boolean enviado, Integer tipo) {
        this.tabla_a_sincronizar = tabla_a_sincronizar;
        this.id_sqlite = id_sqlite;
        this.accion = accion;
        this.enviado = enviado;
        this.tipo = tipo;
    }

    public String getTabla_a_sincronizar() {
        return tabla_a_sincronizar;
    }

    public void setTabla_a_sincronizar(String tabla_a_sincronizar) {
        this.tabla_a_sincronizar = tabla_a_sincronizar;
    }

    public Long getId_sqlite() {
        return id_sqlite;
    }

    public void setId_sqlite(Long id_sqlite) {
        this.id_sqlite = id_sqlite;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
