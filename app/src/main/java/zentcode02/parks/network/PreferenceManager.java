package zentcode02.parks.network;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private Context mContext;

    public PreferenceManager() {}

    public PreferenceManager(Context context) {
        this.mContext = context;
    }

    public void saveApiToken(String api_token) {
        SharedPreferences sp = mContext.getSharedPreferences("api_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("api_token", api_token);
        editor.apply();
    }

    public String getApiToken() {
        SharedPreferences sp = mContext.getSharedPreferences("api_token", Context.MODE_PRIVATE);
        return sp.getString("api_token", "");
    }

    public void clearApiToken() {
        SharedPreferences sp = mContext.getSharedPreferences("api_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUserId(int user_id) {
        SharedPreferences sp = mContext.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("user_id", user_id);
        editor.apply();
    }

    public int getUserId() {
        SharedPreferences sp = mContext.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        return sp.getInt("user_id", 0);
    }

    public void clearUserId() {
        SharedPreferences sp = mContext.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUserName(String user_name) {
        SharedPreferences sp = mContext.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_name", user_name);
        editor.apply();
    }

    public String getUserName() {
        SharedPreferences sp = mContext.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        return sp.getString("user_name", "");
    }

    public void clearUserName() {
        SharedPreferences sp = mContext.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveNombreModulo(String nombre_modulo) {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_modulo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nombre_modulo", nombre_modulo);
        editor.apply();
    }

    public String getNombreModulo() {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_modulo", Context.MODE_PRIVATE);
        return sp.getString("nombre_modulo", "");
    }

    public void clearNombreModulo() {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_modulo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveNombreUnidad(String nombre_unidad) {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_unidad", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nombre_unidad", nombre_unidad);
        editor.apply();
    }

    public String getNombreUnidad() {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_unidad", Context.MODE_PRIVATE);
        return sp.getString("nombre_unidad", "");
    }

    public void clearNombreUnidad() {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_unidad", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveTravesiaId(int travesia_id) {
        SharedPreferences sp = mContext.getSharedPreferences("travesia_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("travesia_id",travesia_id);
        editor.apply();
    }

    public int getTravesiaId() {
        SharedPreferences sp = mContext.getSharedPreferences("travesia_id", Context.MODE_PRIVATE);
        return sp.getInt("travesia_id", 0);
    }

    public void clearTravesiaId() {
        SharedPreferences sp = mContext.getSharedPreferences("travesia_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveDestinoTravesia(String destino) {
        SharedPreferences sp = mContext.getSharedPreferences("destino", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("destino", destino);
        editor.apply();
    }

    public String getDestinoTravesia() {
        SharedPreferences sp = mContext.getSharedPreferences("destino", Context.MODE_PRIVATE);
        return sp.getString("destino", "");
    }

    public void clearDestinoTravesia() {
        SharedPreferences sp = mContext.getSharedPreferences("destino", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveNroAppSync(int nro_app_sync) {
        SharedPreferences sp = mContext.getSharedPreferences("nro_app_sync", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("nro_app_sync",nro_app_sync);
        editor.apply();
    }

    public int getNroAppSync() {
        SharedPreferences sp = mContext.getSharedPreferences("nro_app_sync", Context.MODE_PRIVATE);
        return sp.getInt("nro_app_sync", 0);
    }

    public void clearNroAppSync() {
        SharedPreferences sp = mContext.getSharedPreferences("nro_app_sync", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveNombreCategoria(String nombre_categoria) {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_categoria", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nombre_categoria",nombre_categoria);
        editor.apply();
    }

    public String getNombreCategoria() {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_categoria", Context.MODE_PRIVATE);
        return sp.getString("nombre_categoria", "");
    }

    public void clearNombreCategoria() {
        SharedPreferences sp = mContext.getSharedPreferences("nombre_categoria", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
