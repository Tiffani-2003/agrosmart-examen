package ec.edu.espe.agrosmart.config;

import ec.edu.espe.agrosmart.entity.ProductoEntity;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatos(ProductoRepository repository) {

        return args -> {

            // Evita insertar datos duplicados
            if (repository.count() == 0) {

                repository.save(new ProductoEntity(
                        null,
                        "Cacao Fino de Aroma",
                        new BigDecimal("120.50"),
                        500,
                        "Cacao",
                        "ventas@agrosmart.ec"));

                repository.save(new ProductoEntity(
                        null,
                        "Cacao Orgánico Premium",
                        new BigDecimal("95.80"),
                        350,
                        "Cacao",
                        "exportaciones@agrosmart.ec"));

                repository.save(new ProductoEntity(
                        null,
                        "Cacao Nacional",
                        new BigDecimal("150.00"),
                        600,
                        "Cacao",
                        "clientes@agrosmart.ec"));

                // Producto inválido (precio = 0)

                repository.save(new ProductoEntity(
                        null,
                        "Cacao Económico",
                        new BigDecimal("0.00"),
                        400,
                        "Cacao",
                        "ventas@agrosmart.ec"));

                // Producto inválido (sin correos)

                repository.save(new ProductoEntity(
                        null,
                        "Cacao Experimental",
                        new BigDecimal("85.00"),
                        250,
                        "Cacao",
                        ""));

                System.out.println("====================================");
                System.out.println("Productos sembrados correctamente.");
                System.out.println("Total: 5 productos");
                System.out.println("3 válidos");
                System.out.println("2 inválidos");
                System.out.println("====================================");
            }

        };

    }

}