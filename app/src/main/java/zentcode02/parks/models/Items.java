package zentcode02.parks.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by zentcode02 on 20/01/17.
 */

public class Items {

    private int seccion_check_id;
    private String seccion_check_nombre;
    private int item_check_id;
    private String item_check_nombre;
    private int isHeader;
    private int satisfactorio;
    private String observacion;
    //para controlar que los campos requeridos esten cargados
    private String imageLoaded;
    private String gpsLoaded;
    private String qrLoaded;
    //para controlar que los botones de carga esten habilitados
    private boolean imageEnabled;
    private boolean gpsEnabled;
    private boolean qrEnabled;
    //Para controlar la visibilidad de la fila de los botones
    public int visibility= View.GONE;
    //Para controlar los colores del boton
    private int button_color_img;
    private int button_color_gps;
    private int button_color_qr;
//    public int button_color = Color.RED;
    private int button_hide_show_color;
    private Drawable button_hide_show_drawable;

    public Items() {

    }

    public int getSeccion_check_id() {
        return seccion_check_id;
    }

    public void setSeccion_check_id(int seccion_check_id) {
        this.seccion_check_id = seccion_check_id;
    }

    public String getSeccion_check_nombre() {
        return seccion_check_nombre;
    }

    public void setSeccion_check_nombre(String seccion_check_nombre) {
        this.seccion_check_nombre = seccion_check_nombre;
    }

    public int getItem_check_id() {
        return item_check_id;
    }

    public void setItem_check_id(int item_check_id) {
        this.item_check_id = item_check_id;
    }

    public String getItem_check_nombre() {
        return item_check_nombre;
    }

    public void setItem_check_nombre(String item_check_nombre) {
        this.item_check_nombre = item_check_nombre;
    }

    public int getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(int isHeader) {
        this.isHeader = isHeader;
    }

    public int getSatisfactorio() {
        return satisfactorio;
    }

    public void setSatisfactorio(int satisfactorio) {
        this.satisfactorio = satisfactorio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getImageLoaded() {
        return imageLoaded;
    }

    public void setImageLoaded(String imageLoaded) {
        this.imageLoaded = imageLoaded;
    }

    public String getGpsLoaded() {
        return gpsLoaded;
    }

    public void setGpsLoaded(String gpsLoaded) {
        this.gpsLoaded = gpsLoaded;
    }

    public String getQrLoaded() {
        return qrLoaded;
    }

    public void setQrLoaded(String qrLoaded) {
        this.qrLoaded = qrLoaded;
    }

    public boolean isImageEnabled() {
        return imageEnabled;
    }

    public void setImageEnabled(boolean imageEnabled) {
        this.imageEnabled = imageEnabled;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(boolean gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
    }

    public boolean isQrEnabled() {
        return qrEnabled;
    }

    public void setQrEnabled(boolean qrEnabled) {
        this.qrEnabled = qrEnabled;
    }

    public int getButton_color_img() {
        return button_color_img;
    }

    public void setButton_color_img(int button_color_img) {
        this.button_color_img = button_color_img;
    }

    public int getButton_color_gps() {
        return button_color_gps;
    }

    public void setButton_color_gps(int button_color_gps) {
        this.button_color_gps = button_color_gps;
    }

    public int getButton_color_qr() {
        return button_color_qr;
    }

    public void setButton_color_qr(int button_color_qr) {
        this.button_color_qr = button_color_qr;
    }

    public int getButton_hide_show_color() {
        return button_hide_show_color;
    }

    public void setButton_hide_show_color(int button_hide_show_color) {
        this.button_hide_show_color = button_hide_show_color;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Drawable getButton_hide_show_drawable() {
        return button_hide_show_drawable;
    }

    public void setButton_hide_show_drawable(Drawable button_hide_show_drawable) {
        this.button_hide_show_drawable = button_hide_show_drawable;
    }

    public void toggleVisibility()
    {
        if(this.visibility==View.GONE) {
            this.visibility=View.VISIBLE;
        } else if(this.visibility==View.VISIBLE) {
            this.visibility=View.GONE;
        } else {
            this.visibility=View.VISIBLE;
        }
    }
}
