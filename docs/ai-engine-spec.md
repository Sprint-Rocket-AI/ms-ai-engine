# ai-engine - Arquitectura y Swagger (Spec)

Documento normativo para nuevos cambios en el servicio ai-engine.

## 1. Estilo arquitectonico

- Arquitectura hexagonal (ports and adapters).
- Separacion por capas: domain, application, infrastructure.
- Sin puertos de entrada: el controller REST llama directo a application.

## 2. Estructura de paquetes

```
cl.sprint_rocket_ai.ms_ai_engine
├── domain/
│   ├── model/
│   └── port/out/
├── application/
│   └── service/
└── infrastructure/
    ├── adapters/
    │   ├── in/rest/
    │   │   ├── <Entidad>Rest.java
    │   │   ├── <Entidad>Controller.java
    │   │   └── dtos/
    │   └── out/
    └── llm/
```

## 3. Reglas de dependencia

- domain no depende de nada fuera de si mismo.
- application depende de domain y de los DTOs del propio proyecto.
- infrastructure.in depende de application, domain y dtos.
- infrastructure.out solo depende de domain.

## 4. Naming

- Atributos en espanol (titulo, proyectoId, fechaCreacion).
- Metodos en ingles (execute, save, findById).
- Clases: <Entidad>Rest, <Entidad>Controller, <Entidad>AdapterOut, <Entidad>PortOut.

## 5. Application (casos de uso)

- Una clase = un caso de uso, @Service, final.
- Metodo publico unico: execute(...).
- Logging SLF4J: inicio y fin con placeholders y mensajes en espanol.

## 6. DTOs (infrastructure/adapters/in/rest/dtos)

- Siempre record.
- @Schema en clase y campos.
- Bean Validation en Request.
- Request.applyTo(modelo) y Response.from(modelo).

## 7. Swagger (interfaces Rest)

- <Entidad>Rest es interfaz con @Tag y @Operation.
- Sin @RequestMapping ni verbos en la interfaz.
- Controller implementa la interfaz y agrega el path base y los verbos.
- Errores documentados: 400, 404.

## 8. Anti-patrones

- Sin Lombok.
- Sin @Autowired en campos.
- Sin anotaciones Swagger en controllers.
- Sin logica en controllers.
