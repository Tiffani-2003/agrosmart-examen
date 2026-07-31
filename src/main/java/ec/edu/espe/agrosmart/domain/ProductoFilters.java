package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;

public record ProductoFilters(
        String nombre,
        String categoria,
        BigDecimal precioMin,
        BigDecimal precioMax
) {
}