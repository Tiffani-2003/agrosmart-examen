package ec.edu.espe.agrosmart.service;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.mapper.ProductoMapper;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final AgroSmartAIService aiService;

    public ProductoService(ProductoRepository repository, AgroSmartAIService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    // Obtener todos los productos
    public Flux<Producto> listarProductos() {
        return Flux.fromIterable(repository.findAll())
                .map(ProductoMapper::toDomain);
    }

    // Buscar producto por ID
    public Mono<Producto> buscarPorId(Long id) {
        return Mono.justOrEmpty(repository.findById(id))
                .map(ProductoMapper::toDomain);
    }

    // Guardar producto con validaciones
    public Mono<Producto> guardarProducto(Producto producto) {
        return validarProducto(producto)
                .map(ProductoMapper::toEntity)
                .map(repository::save)
                .map(ProductoMapper::toDomain);
    }

    // Generar publicidad usando LangChain4j de forma reactiva (Fase 5.3)
    public Mono<String> generarPublicidad(String producto, String audiencia) {
        return Mono.fromCallable(() -> aiService.generarPublicidad(producto, audiencia))
                .subscribeOn(Schedulers.boundedElastic())   // la llamada HTTP bloquea
                .timeout(Duration.ofSeconds(30))
                // onErrorResume: un fallo del proveedor externo no puede tumbar el endpoint
                .onErrorResume(e -> Mono.just(
                        "Publicidad no disponible en este momento (" + e.getClass().getSimpleName() + ")"));
    }

    // Validaciones del examen
    private Mono<Producto> validarProducto(Producto producto) {
        if (producto.precioUsd() == null ||
                producto.precioUsd().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(
                    new RuntimeException("El precio debe ser mayor a cero")
            );
        }

        if (producto.correosNotificacion() == null ||
                producto.correosNotificacion().isBlank()) {
            return Mono.error(
                    new RuntimeException("Debe existir correo de notificación")
            );
        }

        return Mono.just(producto);
    }
}