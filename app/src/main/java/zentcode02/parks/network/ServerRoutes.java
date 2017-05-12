package zentcode02.parks.network;

public class ServerRoutes {

    public static String getBaseUrl() {
        return Config.SERVER_URL;
    }

    public static String getLoginUrl() {
        return Config.SERVER_URL + "login";
    }

    public static String getModulosUrl(String token) {
        return Config.SERVER_URL + "modulos" + "?api_token=" + token;
    }

    public static String getUnidadesUrl(Integer modulo_id, String token) {
        return Config.SERVER_URL + "unidades/" + modulo_id + "?api_token=" + token;
    }

    public static String getTravesiasUrl(Integer unidad_id, String token) {
        return Config.SERVER_URL + "travesias/" + unidad_id + "?api_token=" + token;
    }

    public static String getSyncUrl(Integer travesia_id, String token) {
        return Config.SERVER_URL + "sincronizar/" + travesia_id + "?api_token=" + token;
    }
}
