package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class FormularioCategoria extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private String descripcion;
    private Integer modulo_id;
    private String color;

    public FormularioCategoria() {}

    public FormularioCategoria(Integer id_servidor, String nombre, String descripcion, Integer modulo_id, String color) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modulo_id = modulo_id;
        this.color = color;
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

    public Integer getModulo_id() {
        return modulo_id;
    }

    public void setModulo_id(Integer modulo_id) {
        this.modulo_id = modulo_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}