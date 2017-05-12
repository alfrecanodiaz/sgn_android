package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class Maquinaria extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private String descripcion;
    private Integer categoria_id;

    public Maquinaria() {}

    public Maquinaria(Integer id_servidor, String nombre, String descripcion, Integer categoria_id) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria_id = categoria_id;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(Integer categoria_id) {
        this.categoria_id = categoria_id;
    }
}
