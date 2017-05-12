package zentcode02.parks.helpers.db;

import com.orm.util.NamingHelper;

public class DBConfig {

    /**
     * Statements
     */
    public static final String SELECT_ALL = " SELECT * FROM ";
    public static final String SELECT = " SELECT ";
    public static final String FROM = " FROM ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String ON = " ON ";
    public static final String WHERE = " WHERE ";
    public static final String AND = " AND ";

    /**
     * Operators
     */
    public static final String EQUAL = " = ";
    public static final String NOT_EQUAL = " != ";

    /**
     * Tables
     */
    public static final String CABECERA_CHECK = NamingHelper.toSQLNameDefault("CabeceraCheck");
    public static final String CABECERA_CHECK_COPIA = NamingHelper.toSQLNameDefault("CabeceraCheckCopia");
    public static final String CABECERA_COMPRA = NamingHelper.toSQLNameDefault("CabeceraCompra");
    public static final String CABECERA_MANTENIMIENTO = NamingHelper.toSQLNameDefault("CabeceraMantenimiento");
    public static final String CATEGORIA_MAQUINARIA = NamingHelper.toSQLNameDefault("CategoriaMaquinaria");
    public static final String DETALLE_COMPRA = NamingHelper.toSQLNameDefault("DetalleCompra");
    public static final String DETALLE_MANTENIMIENTO = NamingHelper.toSQLNameDefault("DetalleMantenimiento");
    public static final String FORMULARIO_CATEGORIA = NamingHelper.toSQLNameDefault("FormularioCategoria");
    public static final String FORMULARIO_COMPRA_CATEGORIA = NamingHelper.toSQLNameDefault("FormularioCompraCategoria");
    public static final String FORMULARIO_COMPRA_PRODUCTO = NamingHelper.toSQLNameDefault("FormularioCompraProducto");
    public static final String FORMULARIO_MODELO = NamingHelper.toSQLNameDefault("FormularioModelo");
    public static final String FORMULARIOS = NamingHelper.toSQLNameDefault("Formularios");
    public static final String FORMULARIO_TRAVESIA = NamingHelper.toSQLNameDefault("FormularioTravesia");
    public static final String ITEM_CHECK = NamingHelper.toSQLNameDefault("ItemCheck");
    public static final String ITEM_CHECK_COPIA = NamingHelper.toSQLNameDefault("ItemCheckCopia");
    public static final String MAQUINARIA = NamingHelper.toSQLNameDefault("Maquinaria");
    public static final String SECCION_CHECK = NamingHelper.toSQLNameDefault("SeccionCheck");
    public static final String SECCION_CHECK_COPIA = NamingHelper.toSQLNameDefault("SeccionCheckCopia");
    public static final String SINCRONIZAR = NamingHelper.toSQLNameDefault("Sincronizar");
    public static final String TRAVESIA = NamingHelper.toSQLNameDefault("Travesia");

    /**
     * Common Fields
     */
    public static final String PK_ID = NamingHelper.toSQLNameDefault("id");
    public static final String FK_ID = NamingHelper.toSQLNameDefault("id_servidor");
    public static final String TRAVESIA_ID = NamingHelper.toSQLNameDefault("travesia_id");
    public static final String F_TRAVESIA_ID = NamingHelper.toSQLNameDefault("formulario_travesia_id");
    public static final String F_MODELO_ID = NamingHelper.toSQLNameDefault("formulario_modelo_id");
    public static final String F_NOMBRE = NamingHelper.toSQLNameDefault("nombre");
    public static final String F_ESTADO = NamingHelper.toSQLNameDefault("estado");

}
