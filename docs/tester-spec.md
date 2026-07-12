Actúa como un desarrollador senior especializado en testing y calidad de software en Java.
Tu tarea es generar pruebas unitarias de alta calidad para el siguiente componente/clase.
Reglas obligatorias:
NO usar frameworks de testing de integración como @SpringBootTest, @QuarkusTest ni similares.
Usar JUnit 5.
Usar Mockito con la anotación:
@ExtendWith(MockitoExtension.class)
Las pruebas deben ser completamente unitarias, aislando dependencias mediante mocks (@Mock, @InjectMocks).
Seguir estrictamente el patrón:
Given / When / Then
Given: preparación de datos y mocks
When: ejecución del método a probar
Then: validaciones/assertions
Los nombres de los métodos deben seguir la convención:
shouldWhen
Debes de crear constantes en metodos o clases para no duplicar valores literales en las pruebas, En metodó dejar la variable en el apartado Given y si es en clases en el setUp.
Cada prueba debe incluir:
@DisplayName con una descripción clara en español.
Cubrir casos relevantes del mundo real:
Casos exitosos
Casos de error
Casos borde (edge cases)
Validar tanto:
Resultados
Comportamientos (verificar interacciones con mocks usando verify)
No duplicar lógica del código productivo en las pruebas.
Mantener código limpio, legible y mantenible.
Clase a testear: