package ec.edu.espe.agrosmart.service;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.mapper.ProductoMapper;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
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

    private static final Producto PRODUCTO_GENERICO = new Producto(
            0L,
            "GENERICO",
            BigDecimal.ZERO,
            0,
            "N/A",
            "admin@agrosmart.com"
    );

    public ProductoService(ProductoRepository repository, AgroSmartAIService aiService) {
        this.repository = repository;
        this.aiService = aiService;
    }

    public Flux<Producto> obtenerProductosComercializables() {
        return Mono.fromCallable(repository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .map(ProductoMapper::toDomain)
                .filter(p -> p.precioUsd() != null && p.precioUsd().compareTo(BigDecimal.ZERO) > 0)
                .doOnNext(p -> System.out.println("Producto procesado con éxito"))
                .defaultIfEmpty(PRODUCTO_GENERICO);
    }

    public Mono<Producto> buscarPorId(Long id) {
        return Mono.fromCallable(() -> repository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty)
                .map(ProductoMapper::toDomain)
                .switchIfEmpty(Mono.error(new ProductoNoEncontradoException()));
    }

    public Mono<String> generarPublicidad(String producto, String audiencia) {
        return Mono.fromCallable(() -> aiService.generarPublicidad(producto, audiencia))
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(e -> Mono.just(
                        "Publicidad no disponible en este momento (" + e.getClass().getSimpleName() + ")"));
    }
}