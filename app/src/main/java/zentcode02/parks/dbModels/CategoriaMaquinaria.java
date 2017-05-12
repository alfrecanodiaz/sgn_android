package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class CategoriaMaquinaria extends SugarRecord {

    private Integer id_servidor;
    private String descripcion;

    public CategoriaMaquinaria() {}

    public CategoriaMaquinaria(Integer id_servidor, String descripcion) {
        this.id_servidor = id_servidor;
        this.descripcion = descripcion;
    }

    public Integer getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(Integer id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
