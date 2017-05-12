package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FormularioModelo extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private Integer version;
    private String codigo;
    private String descripcion;
    private String cabecera;
    private String detalles;
    private Integer categoria_id;
    private String plantilla;

    public FormularioModelo() {}

    public FormularioModelo(Integer id_servidor, String nombre, Integer version, String codigo, String descripcion, String cabecera, String detalles, Integer categoria_id, String plantilla) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.version = version;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cabecera = cabecera;
        this.detalles = detalles;
        this.categoria_id = categoria_id;
        this.plantilla = plantilla;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Integer getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(Integer categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(String plantilla) {
        this.plantilla = plantilla;
    }

    @Override
    public String toString() {
        return String.valueOf(categoria_id);
    }

    public String getCabeceraTipo() {
        JSONObject obj;
        String tipo = null;
        try {
            JSONObject jObject = new JSONObject(getCabecera());
            JSONArray jArray = jObject.getJSONArray("cabecera");
            for (int i = 0; i < jArray.length(); i ++) {
                obj = jArray.getJSONObject(i);
                if (obj.has("nombre")) {
                    tipo = obj.getString("nombre");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tipo;
    }
}