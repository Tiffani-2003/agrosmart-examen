package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoTest {

    @org.junit.jupiter.api.Test
    void getCorreosNotificacion_alMutarLaListaOriginal_noDebeAfectarAlProducto() {
        List<String> correos = new ArrayList<>();
        correos.add("ventas@agrosmart.ec");

        Producto producto = new Producto(
                1L,
                "Cacao fino",
                new BigDecimal("120.50"),
                100,
                "Cacao",
                new ArrayList<>(correos)
        );

        correos.add("intruso@mail.com");

        assertEquals(1, producto.correosNotificacion().size());
        assertNotSame(correos, producto.correosNotificacion());
    }

    @org.junit.jupiter.api.Test
    void getCorreosNotificacion_alIntentarModificarLaListaDelGet_lanzaExcepcion() {
        List<String> correos = new ArrayList<>();
        correos.add("ventas@agrosmart.ec");

        Producto producto = new Producto(
                1L,
                "Cacao fino",
                new BigDecimal("120.50"),
                100,
                "Cacao",
                new ArrayList<>(correos)
        );

        assertThrows(UnsupportedOperationException.class, () -> {
            producto.correosNotificacion().add("nuevo@mail.com");
        });
    }
}