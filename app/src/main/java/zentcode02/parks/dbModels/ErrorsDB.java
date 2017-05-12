package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

/**
 * Created by zentcode02 on 07/03/17.
 */

public class ErrorsDB extends SugarRecord {

    private String error_message;
    private int sync_id;
    private String sync_data;
    private String fecha;

    public ErrorsDB() {}

    public ErrorsDB(String error_message, int sync_id, String sync_data, String fecha) {
        this.error_message = error_message;
        this.sync_id = sync_id;
        this.sync_data = sync_data;
        this.fecha = fecha;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public int getSync_id() {
        return sync_id;
    }

    public void setSync_id(int sync_id) {
        this.sync_id = sync_id;
    }

    public String getSync_data() {
        return sync_data;
    }

    public void setSync_data(String sync_data) {
        this.sync_data = sync_data;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "status_code: " + error_message + ", datos: {" + sync_data + "}, fecha: " + fecha;
    }
}
