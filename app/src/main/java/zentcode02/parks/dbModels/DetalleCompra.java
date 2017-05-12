package zentcode02.parks.dbModels;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

public class DetalleCompra extends SugarRecord {

    private Integer id_servidor = 0;
    private Integer cantidad_solicitada = 0;
    private Integer cantidad_aprobada = 0;
    private Integer cantidad_recibida = 0;
    private String productos_sin_id = "";
    private Integer cabecera_compra_id;
    private Integer producto_id;
    private String codigo_sync;
    private String codigo_formulario_sync;
    private String created_at;
    private String updated_at;
    private Integer created_by;
    private Integer updated_by;
    private Integer eliminado = 0;

    public DetalleCompra() {}

    public DetalleCompra(Integer id_servidor, Integer cantidad_solicitada, Integer cantidad_aprobada, Integer cantidad_recibida, String productos_sin_id, Integer cabecera_compra_id, Integer producto_id, String codigo_sync, String codigo_formulario_sync, String created_at, String updated_at, Integer created_by, Integer updated_by, Integer eliminado) {
        this.id_servidor = id_servidor;
        this.cantidad_solicitada = cantidad_solicitada;
        this.cantidad_aprobada = cantidad_aprobada;
        this.cantidad_recibida = cantidad_recibida;
        this.productos_sin_id = productos_sin_id;
        this.cabecera_compra_id = cabecera_compra_id;
        this.producto_id = producto_id;
        this.codigo_sync = codigo_sync;
        this.codigo_formulario_sync = codigo_formulario_sync;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.eliminado = eliminado;
    }

    public Integer getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(Integer id_servidor) {
        this.id_servidor = id_servidor;
    }

    public Integer getCantidad_solicitada() {
        return cantidad_solicitada;
    }

    public void setCantidad_solicitada(Integer cantidad_solicitada) {
        this.cantidad_solicitada = cantidad_solicitada;
    }

    public Integer getCantidad_aprobada() {
        return cantidad_aprobada;
    }

    public void setCantidad_aprobada(Integer cantidad_aprobada) {
        this.cantidad_aprobada = cantidad_aprobada;
    }

    public Integer getCantidad_recibida() {
        return cantidad_recibida;
    }

    public void setCantidad_recibida(Integer cantidad_recibida) {
        this.cantidad_recibida = cantidad_recibida;
    }

    public String getProductos_sin_id() {
        return productos_sin_id;
    }

    public void setProductos_sin_id(String productos_sin_id) {
        this.productos_sin_id = productos_sin_id;
    }

    public Integer getCabecera_compra_id() {
        return cabecera_compra_id;
    }

    public void setCabecera_compra_id(Integer cabecera_compra_id) {
        this.cabecera_compra_id = cabecera_compra_id;
    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public String getCodigo_sync() {
        return codigo_sync;
    }

    public void setCodigo_sync(String codigo_sync) {
        this.codigo_sync = codigo_sync;
    }

    public String getCodigo_formulario_sync() {
        return codigo_formulario_sync;
    }

    public void setCodigo_formulario_sync(String codigo_formulario_sync) {
        this.codigo_formulario_sync = codigo_formulario_sync;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public Integer getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Integer updated_by) {
        this.updated_by = updated_by;
    }

    public Integer getEliminado() {
        return eliminado;
    }

    public void setEliminado(Integer eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return codigo_sync;
    }

    public String getProducto() {
        FormularioCompraProducto producto = Select.from(FormularioCompraProducto.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("producto_compra_id")).eq(getProducto_id())
                ).first();
        return producto.getNombre();
    }

    public String getUnidadMedida() {
        FormularioCompraProducto producto = Select.from(FormularioCompraProducto.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("producto_compra_id")).eq(getProducto_id())
                ).first();
        return producto.getUnidad_de_medida();
    }

    public int getCategoriaId() {
        FormularioCompraProducto producto = Select.from(FormularioCompraProducto.class)
                .where(
                        Condition.prop(NamingHelper.toSQLNameDefault("producto_compra_id")).eq(getProducto_id())
                ).first();
        return producto.getCategoria_id();
    }
}
