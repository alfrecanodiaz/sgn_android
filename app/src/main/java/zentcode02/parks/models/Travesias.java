package zentcode02.parks.models;

/**
 * Created by zentcode02 on 03/01/17.
 */

public class Travesias {

    private int id;
    private String destino;
    private String responsable1;
    private String responsable2;
    private String fecha_inicio;

    public Travesias() {
        //Constructor
    }

    public Travesias(int id, String destino, String responsable1, String responsable2, String fecha_inicio) {
        this.id = id;
        this.destino = destino;
        this.responsable1 = responsable1;
        this.responsable2 = responsable2;
        this.fecha_inicio = fecha_inicio;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getDestino() {

        return destino;
    }

    public void setDestino(String destino) {

        this.destino = destino;
    }

    public String getResponsable1() {
        return responsable1;
    }

    public void setResponsable1(String responsable1) {
        this.responsable1 = responsable1;
    }

    public String getResponsable2() {
        return responsable2;
    }

    public void setResponsable2(String responsable2) {
        this.responsable2 = responsable2;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    @Override
    public String toString() {

        return this.destino;
    }

    public int idTravesia() {

        return this.id;
    }

    public String nombreTravesia() {

        return this.destino;
    }

}
