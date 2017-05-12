package zentcode02.parks.models;

/**
 * Created by zentcode02 on 28/12/16.
 */

public class Unidades {

    private int id;
    private String nombre;

    public Unidades() {
        //Constructor
    }

    public Unidades(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    @Override
    public String toString() {

        return this.nombre;
    }

    public int idUnidad() {

        return this.id;
    }

    public String nombreUnidad() {

        return this.nombre;
    }

}
