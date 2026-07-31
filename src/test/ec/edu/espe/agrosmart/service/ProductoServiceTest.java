package ec.edu.espe.agrosmart.service;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ProductoServiceTest {

    @Test
    void obtenerProductosComercializables_conValidosEInvalidos_debeEmitirSoloValidos() {
        ProductoRepository repo = Mockito.mock(ProductoRepository.class);

        List<Producto> productos = List.of(
                new Producto(1L, "Valido 1", new BigDecimal("10.0"), 10, "Cat", Collections.singletonList("a@mail.com")),
                new Producto(2L, "Invalido Precio", BigDecimal.ZERO, 10, "Cat", Collections.singletonList("b@mail.com")),
                new Producto(3L, "Valido 2", new BigDecimal("20.0"), 10, "Cat", Collections.singletonList("c@mail.com"))
        );
        Mockito.when(repo.findAll()).thenReturn(List.of()); // O mockear según devuelva tu repositorio

        ProductoService service = new ProductoService(repo, null);

        StepVerifier.create(service.obtenerProductosComercializables())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void buscarPorId_conIdInexistente_lanzaExcepcion() {
        ProductoRepository repo = Mockito.mock(ProductoRepository.class);
        Mockito.when(repo.findById(99L)).thenReturn(Optional.empty());

        ProductoService service = new ProductoService(repo, null);

        StepVerifier.create(service.buscarPorId(99L))
                .expectError(ProductoNoEncontradoException.class)
                .verify();
    }
}