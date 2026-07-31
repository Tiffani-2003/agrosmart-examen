package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public record Producto(
        Long idProducto,
        String nombreProducto,
        BigDecimal precioUsd,
        Integer stockKg,
        String categoria,
        List<String> correosNotificacion
) {
    // Constructor compacto para aplicar copia defensiva en la entrada
    public Producto {
        correosNotificacion = List.copyOf(correosNotificacion);
    }

    // Sobrescribimos el getter para asegurar copia defensiva de salida inmodificable
    @Override
    public List<String> correosNotificacion() {
        return Collections.unmodifiableList(correosNotificacion);
    }
}