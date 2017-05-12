package zentcode02.parks.dbModels;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;

import java.util.List;

public class SeccionCheckCopia extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private String nombre_campo;
    private String observacion;

    public SeccionCheckCopia() {}

    public SeccionCheckCopia(Integer id_servidor, String nombre, String nombre_campo, String observacion) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.nombre_campo = nombre_campo;
        this.observacion = observacion;
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

    public String getNombre_campo() {
        return nombre_campo;
    }

    public void setNombre_campo(String nombre_campo) {
        this.nombre_campo = nombre_campo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public List<ItemCheckCopia> getDetalle() {
        return ItemCheckCopia.find(ItemCheckCopia.class, NamingHelper.toSQLNameDefault("titulo_check_id") + " = ? ", String.valueOf(getId_servidor()));
    }
}
