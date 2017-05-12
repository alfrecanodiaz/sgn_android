package zentcode02.parks.dbModels;

import com.orm.SugarRecord;

public class ListAreaCompra extends SugarRecord {

    private String area;

    public ListAreaCompra() {}

    public ListAreaCompra(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return area;
    }
}
