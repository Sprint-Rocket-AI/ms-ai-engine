# ai-engine

Arquitectura hexagonal (ports and adapters) para un microservicio de IA con Spring Boot.

## Estructura

- `domain`: modelos y puertos de salida.
- `application`: servicios de aplicacion (casos de uso).
- `infrastructure`: adaptadores concretos hacia proveedores externos.

## Notas

- Los adaptadores simulan llamadas a proveedores y persistencia; no usan SDKs reales.
- `OpenAIAdapterOut` esta marcado como `@Primary` para evitar ambiguedad al inyectar `LLMPortOut`.

## Probar rapido

```zsh
./mvnw -q -Dtest=RAGServiceTest test
```

Si ejecutas `./mvnw test` y tienes Spring AI Google GenAI habilitado en el classpath, puede requerir `spring.ai.google.genai.project-id`.

