package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormularioTravesia extends SugarRecord {

    private Integer id_servidor;
    private String nombre;
    private Integer version;
    private String codigo;
    private String cabecera;
    private String detalles;
    private Integer formulario_modelo_id;
    private Integer travesia_id;
    private String cabecera_copia;
    private String detalles_copia;

    /**
     * Relations
     */
    Travesia travesia;

    public FormularioTravesia() {}

    public FormularioTravesia(Integer id_servidor, String nombre, Integer version, String codigo, String cabecera, String detalles, Integer formulario_modelo_id, Integer travesia_id, String cabecera_copia, String detalles_copia) {
        this.id_servidor = id_servidor;
        this.nombre = nombre;
        this.version = version;
        this.codigo = codigo;
        this.cabecera = cabecera;
        this.detalles = detalles;
        this.formulario_modelo_id = formulario_modelo_id;
        this.travesia_id = travesia_id;
        this.cabecera_copia = cabecera_copia;
        this.detalles_copia = detalles_copia;
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

    public Integer getFormulario_modelo_id() {
        return formulario_modelo_id;
    }

    public void setFormulario_modelo_id(Integer formulario_modelo_id) {
        this.formulario_modelo_id = formulario_modelo_id;
    }

    public Integer getTravesia_id() {
        return travesia_id;
    }

    public void setTravesia_id(Integer travesia_id) {
        this.travesia_id = travesia_id;
    }

    public String getCabecera_copia() {
        return cabecera_copia;
    }

    public void setCabecera_copia(String cabecera_copia) {
        this.cabecera_copia = cabecera_copia;
    }

    public String getDetalles_copia() {
        return detalles_copia;
    }

    public void setDetalles_copia(String detalles_copia) {
        this.detalles_copia = detalles_copia;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public Integer getCabecera_id() {
        Integer id = null;
        JSONObject obj_cabecera = null;
        try {
            obj_cabecera = new JSONObject(getCabecera_copia());
            JSONArray arr = obj_cabecera.getJSONArray("cabecera");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.has("id")) {
                    id = obj.getInt("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<String> getSecciones() {
        List<String> secciones = new ArrayList<>();
        JSONObject obj_detalle = null;
        try {
            obj_detalle = new JSONObject(getDetalles_copia());
            JSONArray arr = obj_detalle.getJSONArray("detalles");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.has("id")) {
                    secciones.add(String.valueOf(obj.getInt("id")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return secciones;
    }
}