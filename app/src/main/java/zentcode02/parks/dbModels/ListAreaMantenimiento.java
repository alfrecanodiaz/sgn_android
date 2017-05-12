package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class ListAreaMantenimiento extends SugarRecord {

    private String area;

    public ListAreaMantenimiento() {}

    public ListAreaMantenimiento(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
