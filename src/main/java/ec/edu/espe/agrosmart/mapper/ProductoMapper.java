package ec.edu.espe.agrosmart.mapper;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.entity.ProductoEntity;

public class ProductoMapper {

    public static Producto toDomain(ProductoEntity entity) {
        return new Producto(
                entity.getIdProducto(),
                entity.getNombreProducto(),
                entity.getPrecioUsd(),
                entity.getStockKg(),
                entity.getCategoria(),
                entity.getCorreosNotificacion()
        );
    }

    public static ProductoEntity toEntity(Producto producto) {
        return new ProductoEntity(
                producto.idProducto(),
                producto.nombreProducto(),
                producto.precioUsd(),
                producto.stockKg(),
                producto.categoria(),
                producto.correosNotificacion()
        );
    }
}