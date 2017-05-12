package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class FormularioCompraProducto extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private String unidad_de_medida;
    private String observacion;
    private Integer categoria_id;

    public FormularioCompraProducto() {}

    public FormularioCompraProducto(Integer id_servidor, String nombre, String unidad_de_medida, String observacion, Integer categoria_id) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.unidad_de_medida = unidad_de_medida;
        this.observacion = observacion;
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

    public String getUnidad_de_medida() {
        return unidad_de_medida;
    }

    public void setUnidad_de_medida(String unidad_de_medida) {
        this.unidad_de_medida = unidad_de_medida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(Integer categoria_id) {
        this.categoria_id = categoria_id;
    }

    @Override
    public String toString() {
        return nombre ;
    }
}