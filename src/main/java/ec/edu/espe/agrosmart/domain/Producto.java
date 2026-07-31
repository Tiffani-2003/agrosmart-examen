package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;

public record Producto(
        Long idProducto,
        String nombreProducto,
        BigDecimal precioUsd,
        Integer stockKg,
        String categoria,
        String correosNotificacion
) {
}