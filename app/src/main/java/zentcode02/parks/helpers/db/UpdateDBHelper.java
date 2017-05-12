package zentcode02.parks.helpers.db;

import zentcode02.parks.dbModels.Formularios;
import zentcode02.parks.utils.DBHelper;
import zentcode02.parks.utils.Helper;

public class UpdateDBHelper {

    public static void updateEstadoFormulario(Long form_id, String estado, Integer user_id) {
        Formularios form = Formularios.findById(Formularios.class, form_id);
        form.setEstado(estado);
        form.setUpdated_at(Helper.getMySqlDateTimeFormat());
        form.setUpdated_by(user_id);
        form.save();
        DBHelper.saveSync("formularios", form.getId(), "update", false, 0);
    }
}
