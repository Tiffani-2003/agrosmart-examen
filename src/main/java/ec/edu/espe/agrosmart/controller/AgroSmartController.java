package ec.edu.espe.agrosmart.controller;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
import ec.edu.espe.agrosmart.service.ProductoService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AgroSmartController {

    private final ProductoService service;

    public AgroSmartController(ProductoService service) {
        this.service = service;
    }

    // =====================================
    // GET PRODUCTOS
    // =====================================

    @GetMapping("/productos")
    public Flux<Producto> obtenerProductos() {
        // Reemplaza "obtenerProductosComercializables" por el nombre exacto de tu método en ProductoService si es diferente
        return service.obtenerProductosComercializables();
    }

    // =====================================
    // GET PRODUCTO POR ID
    // =====================================

    @GetMapping("/productos/{id}")
    public Mono<Producto> obtenerProductoPorId(
            @PathVariable Long id) {
        return service.buscarPorId(id)
                .switchIfEmpty(
                        Mono.error(
                                new ProductoNoEncontradoException()
                        )
                );
    }

    // =====================================
    // PUBLICIDAD IA
    // =====================================

    @GetMapping(
            value = "/agrosmart/publicidad",
            produces = "text/plain"
    )
    public Mono<String> publicidad(
            @RequestParam String producto,
            @RequestParam String audiencia
    ) {
        String texto =
                "Producto: " + producto +
                        "\nAudiencia: " + audiencia +
                        "\nMensaje generado: Producto ecuatoriano de alta calidad ideal para exportación.";

        return Mono.just(texto);
    }
}