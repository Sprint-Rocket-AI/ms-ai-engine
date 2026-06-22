package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIIndexRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "RAG", description = "Endpoints de indexacion y RAG")
public interface RAGController {

    @Operation(summary = "Indexar documentos", description = "Indexa documentos en el vector store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Indexacion aceptada"),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<Void> index(@Valid @RequestBody AIIndexRequest request);

    @Operation(summary = "RAG", description = "Consulta al modelo usando contexto recuperado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta generada",
                    content = @Content(schema = @Schema(implementation = AIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<AIResponse> rag(@Valid @RequestBody AIRequest request);

    @Operation(summary = "Indexar PDF", description = "Indexa un archivo PDF en el vector store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Indexacion aceptada"),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<Void> indexPdf(@RequestPart("file") MultipartFile file);

    @Operation(summary = "Eliminar index por id de documento", description = "Elimina todos los embeddings asociados al id del documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Eliminacion OK"),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<Void> deleteIndex(@NotBlank String id);

    @Operation(summary = "Actualizar index por id de documento", description = "Elimina los embeddings asociados al id y re-indexa con el request recibido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Actualizacion aceptada"),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<Void> updateIndex(@NotBlank String id, @Valid @RequestBody AIIndexRequest request);
}

