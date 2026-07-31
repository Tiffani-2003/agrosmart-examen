# 🧭 DECISIONES.md — Bitácora de diseño

> **Instrucciones.** Completa **una entrada por fase**, en **primera persona** y
> **refiriéndote a tu propio código**: nombres reales de tus clases, tu tabla, tus
> líneas, tu salida real de terminal.
>
> ❌ **No puntúa** una justificación genérica que podría pegarse en cualquier proyecto
> (ej.: *"usé boundedElastic porque es una buena práctica para operaciones bloqueantes"*).
> ✅ **Sí puntúa** una justificación anclada a tu código (ej.: *"en `ProductoService`
> línea 34 envolví `productoRepository.findAll()` porque Hibernate abre la conexión
> JDBC en el hilo llamante; al probarlo sin `subscribeOn` vi en el log el hilo
> `reactor-http-nio-2`, que es el event loop de Netty"*).
>
> Estas mismas preguntas se te harán en la **defensa oral**.

---

## Datos

- **Nombre: Tiffani Natahalia Torres Diaz**
- **Cédula: 1600952400**
- **NN (dos últimos dígitos): 00**
- **Categoría asignada (según el último dígito): exportadores europeos**

---

## Fase 1 — Configuración y perfiles

**1.1** ¿Qué archivo activa el perfil `prod` y qué línea exacta lo hace?

>El archivo src/main/resources/application.properties activa el perfil mediante la línea:
spring.profiles.active=prod

**1.2** Pega la línea del log de arranque donde se ve tu puerto y el perfil activo.

```2026-07-31T15:43:09.805-05:00  INFO 19252 --- [agrosmart] [           main] e.e.espe.agrosmart.AgrosmartApplication  : The following 1 profile is active: "prod"


```2026-07-31T15:43:17.648-05:00  INFO 19252 --- [agrosmart] [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 8100

**1.3** ¿Qué habría pasado si dejabas `ddl-auto=create-drop` en lugar de `update`?
Responde pensando en tus datos sembrados.

>Con create-drop, Hibernate habría borrado y recreado la tabla tbl_productos_base_00 cada vez que se reinicie la aplicación. Esto habría provocado que los 5 productos inyectados por el DataLoader (3 válidos y 2 inválidos) se pierdan constantemente en cada arranque, eliminando cualquier persistencia o modificación en caliente realizada sobre la base de datos.

**1.4** ¿Levantaste PostgreSQL con `compose.yaml` (Opción A) o con una instalación local
(Opción B)? ¿Qué ventaja tiene la que elegiste?

>Opción A (compose.yaml / Docker).

Se eligió Docker para garantizar portabilidad y aislamiento del entorno, permitiendo levantar el motor de base de datos (agrosmart-postgres) de forma limpia y estandarizada con un solo comando, sin depender de instalaciones locales del sistema operativo.

---

## Fase 2 — Persistencia con JPA/Hibernate

**2.1** ¿Cuál es el nombre exacto de tu tabla y de dónde salió ese nombre?

>El nombre exacto de la tabla es tbl_productos_base_00. Surgió de combinar la estructura base requerida (tbl_productos_base_) con los dos dígitos correspondientes, tal como se define en la anotación @Table(name = "tbl_productos_base_00") dentro de la clase ProductoEntity.java.

Breve código:
package ec.edu.espe.agrosmart.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_productos_base_00")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre_producto",
            nullable = false,
            unique = true,
            length = 120)
    private String nombreProducto;

    @Column(name = "precio_usd",
            precision = 10,
            scale = 2)
    private BigDecimal precioUsd;

    @Column(name = "stock_kg",
            nullable = false)
    private Integer stockKg;

    @Column(name = "categoria",
            length = 40)
    private String categoria;

    @Column(name = "correos_notificacion",
            length = 500)
    private String correosNotificacion;

    // Constructor vacío requerido por Hibernate
    public ProductoEntity() {
    }
```

**2.2** Pega la salida de `psql -d agrosmart_db -c "\d tbl_productos_base_NN"` y
señala dónde se ve la restricción `unique` y el `length` de 120.

**2.2** Salida de la consola con la ejecución de Hibernate y las inserciones:
``` Breve código:
```text
Hibernate:
    insert 
    into
        tbl_productos_base_00
        (categoria, correos_notificacion, nombre_producto, precio_usd, stock_kg)
    values
        (?, ?, ?, ?, ?)
```

**2.3** ¿Por qué usaste `BigDecimal` y no `double` para `precio_usd`? Relaciónalo con el
tipo que generó Hibernate en PostgreSQL.

>

**2.4** ¿Cómo hiciste idempotente tu siembra y qué pasaría en el segundo arranque si no
lo fuera? (piensa en la restricción `unique` de `nombre_producto`)

>

---

## Fase 3 — Modelo inmutable y lógica funcional

**3.1** ¿Por qué tienes **dos** clases (`ProductoEntity` y `Producto`) en lugar de una?
¿Qué te impide hacer inmutable directamente la entidad de Hibernate?

>

**3.2** Escribe el código exacto de **tus dos** copias defensivas e indica en qué línea
está cada una.

```java

```

**3.3** ¿Por qué la copia defensiva **solo en el getter** no sería suficiente? Describe
el ataque concreto que quedaría abierto sobre **tu** clase.

>

**3.4** ¿Cómo implementaste `A_MAYUSCULAS` para no mutar el `Producto` recibido?

```java

```

---

## Fase 4 — Servicio reactivo y aislamiento del bloqueo

**4.1** Pega tu método `obtenerProductosComercializables()` completo.

```java

```

**4.2** ¿Qué pasa **exactamente** si eliminas
`.subscribeOn(Schedulers.boundedElastic())` de ese método? Si lo probaste, indica qué
hilo aparecía en el log antes y después.

>

**4.3** ¿Por qué `Mono.fromCallable(...)` y no `Mono.just(ec.edu.espe.agrosmart.repository.findAll())`?
(pista: cuándo se ejecuta cada uno)

>

**4.4** En **tu** código, ¿dónde usaste `defaultIfEmpty` y dónde `switchIfEmpty`, y por
qué no son intercambiables en esos dos lugares?

>

**4.5** ¿Por qué `doOnNext` no sirve para transformar el elemento, si aparentemente
"recibe" el producto?

>

---

## Fase 5 — Módulo de IA con LangChain4j

**5.1** Pega tu interfaz `AgroSmartAIService` completa.

```java

```

**5.2** ¿Qué hace `@V("producto")` y qué pasaría si lo quitaras dejando solo el
parámetro?

>

**5.3** ¿En qué archivo y con qué líneas configuraste el modelo? ¿Por qué **no** hizo
falta declarar un `@Bean`?

>

**5.4** ¿Por qué la llamada a la IA también necesita `boundedElastic`, si no es una
consulta a base de datos?

>

**5.5** Si tu proveedor devolvió un error durante el examen, pega el mensaje real y la
respuesta que produjo tu `onErrorResume`.

```

```

---

## Fase 6 — API reactiva con WebFlux

**6.1** Pega la salida real de tus cuatro `curl`.

```

```

**6.2** ¿Cómo lograste que el id inexistente responda **404** y no 500?

>

**6.3** ¿Qué pasaría si tu controlador devolviera `List<Producto>` en lugar de
`Flux<Producto>`? ¿Seguiría compilando? ¿Seguiría siendo no bloqueante?

>

---

## Fase 7 — Pruebas unitarias

**7.1** Pega la salida real de tus pruebas (`./mvnw test` o `./gradlew test`).

```

```

**7.2** ¿Cuántos productos espera tu `expectNextCount(...)` y por qué ese número
concreto? Relaciónalo con tu semilla.

>

**7.3** ¿Por qué mockeaste `ProductoRepository` en lugar de dejar que la prueba consulte
PostgreSQL?

>

**7.4** ¿Qué demuestra `assertNotSame` que `assertEquals` **no** demuestra en tu prueba
de copia defensiva?

>

**7.5** ¿Por qué una prueba de un `Flux` que no llama a `verifyComplete()` (o a
`verify()`) no está probando nada?

>

---

## Fase 8 — Integración y cierre

**8.1** Pega tu `git log --oneline --graph --all`.

```

```

**8.2** ¿Qué fase te tomó más tiempo del previsto y por qué?

>

**8.3** Si tuvieras 30 minutos más, ¿qué mejorarías **primero** de tu entrega y por qué
esa y no otra?

>

**8.4** Declara honestamente qué herramientas consultaste durante el examen
(documentación, apuntes, asistentes de IA) y para qué. **Esta declaración no descuenta
puntaje**; su omisión o falsedad sí constituye falta de honestidad académica.

>
