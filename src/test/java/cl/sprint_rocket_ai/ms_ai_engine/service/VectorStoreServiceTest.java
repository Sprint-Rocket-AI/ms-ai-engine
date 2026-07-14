package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIIndexRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VectorStoreServiceTest {

    private static final String ID = "doc-01";
    private static final String TYPE = "ticket";
    private static final String CONTENT = "texto corto";
    private static final String QUERY = "buscar documento";
    private static final String TAG = "tag-1";

    @Mock
    private VectorStore store;

    @InjectMocks
    private VectorStoreService vectorStoreService;

    @Test
    @DisplayName("Debe indexar el documento con sus metadatos en Vector Store")
    void shouldWhenGuardaDocumentoIndexaMetadatos() {
        // Given
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("otro", "dato");
        AIIndexRequest request = new AIIndexRequest(ID, TYPE, CONTENT, List.of(TAG), metadata);

        // When
        vectorStoreService.save(request);

        // Then
        ArgumentCaptor<List<Document>> docsCaptor = ArgumentCaptor.forClass(List.class);
        verify(store).add(docsCaptor.capture());
        verifyNoMoreInteractions(store);

        List<Document> docs = docsCaptor.getValue();
        assertEquals(1, docs.size());
        assertEquals(CONTENT, docs.get(0).getText());
        assertNotNull(docs.get(0).getId());
        assertEquals(ID, docs.get(0).getMetadata().get("document_id"));
        assertEquals(List.of(TAG), docs.get(0).getMetadata().get("tags"));
        assertEquals(TYPE, docs.get(0).getMetadata().get("tipo"));
        assertEquals("dato", docs.get(0).getMetadata().get("otro"));
    }

    @Test
    @DisplayName("Debe delegar la búsqueda al Vector Store y devolver los documentos")
    void shouldWhenBuscaDocumentosRetornaResultados() {
        // Given
        List<Document> expected = new ArrayList<>();
        expected.add(Document.builder().text("resultado 1").build());
        when(store.similaritySearch(any(SearchRequest.class))).thenReturn(expected);

        // When
        List<Document> response = vectorStoreService.search(QUERY);

        // Then
        ArgumentCaptor<SearchRequest> requestCaptor = ArgumentCaptor.forClass(SearchRequest.class);
        verify(store).similaritySearch(requestCaptor.capture());
        verifyNoMoreInteractions(store);

        assertEquals(expected, response);
        assertEquals(QUERY, requestCaptor.getValue().getQuery());
        assertEquals(3, requestCaptor.getValue().getTopK());
        assertFalse(requestCaptor.getValue().hasFilterExpression());
    }

    @Test
    @DisplayName("Debe eliminar los embeddings existentes y volver a indexar el documento actualizado")
    void shouldWhenActualizaDocumentoEliminaYReindexaConNuevoContenido() {
        // Given
        Map<String, Object> metadata = new HashMap<>();
        AIIndexRequest request = new AIIndexRequest(ID, TYPE, CONTENT, List.of(TAG), metadata);

        // When
        vectorStoreService.update(ID, request);

        // Then
        var inOrder = inOrder(store);
        inOrder.verify(store).delete(ID);
        inOrder.verify(store).add(anyList());
        verifyNoMoreInteractions(store);
    }

    @Test
    @DisplayName("Debe eliminar los embeddings por documentId sin lanzar excepción")
    void shouldWhenEliminaEmbeddingsPorDocumentIdLlamaStoreDelete() {
        // Given / When
        vectorStoreService.deleteByDocumentId(ID);

        // Then
        verify(store).delete(ID);
        verifyNoMoreInteractions(store);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el store falla al eliminar embeddings")
    void shouldWhenStoreDeleteFallaLanzaRuntimeException() {
        // Given
        doThrow(new RuntimeException("error store")).when(store).delete(ID);

        // When / Then
        assertThrows(RuntimeException.class, () -> vectorStoreService.deleteByDocumentId(ID));
        verify(store).delete(ID);
        verifyNoMoreInteractions(store);
    }

    @Test
    @DisplayName("Debe indexar un PDF correctamente usando el nombre original del archivo")
    void shouldWhenGuardaPdfConNombreOriginalIndexaEnVectorStore() throws IOException {
        // Given — PDF mínimo válido (1 página vacía)
        byte[] minimalPdf = buildMinimalPdf();
        MultipartFile file = new MockMultipartFile("file", "documento.pdf", "application/pdf", minimalPdf);

        // When
        vectorStoreService.savePdf(file);

        // Then
        verify(store).add(anyList());
        verifyNoMoreInteractions(store);
    }

    @Test
    @DisplayName("Debe usar 'file.pdf' como nombre cuando el archivo no tiene nombre original")
    void shouldWhenGuardaPdfSinNombreUsaNombrePorDefecto() throws IOException {
        // Given — MultipartFile sin originalFilename
        byte[] minimalPdf = buildMinimalPdf();
        MultipartFile file = new MockMultipartFile("file", "", "application/pdf", minimalPdf);

        // When
        vectorStoreService.savePdf(file);

        // Then
        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(store).add(captor.capture());
        captor.getValue().forEach(doc ->
                assertEquals("file.pdf", doc.getMetadata().get("source"))
        );
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el MultipartFile lanza IOException al leer el stream")
    void shouldWhenMultipartFileIOExceptionLanzaRuntimeException() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("stream error"));

        // When / Then
        assertThrows(RuntimeException.class, () -> vectorStoreService.savePdf(file));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /**
     * Construye un PDF mínimo válido en memoria (header + 1 página vacía + xref).
     * Suficiente para que PagePdfDocumentReader no falle al parsear.
     */
    private byte[] buildMinimalPdf() {
        String pdf =
                "%PDF-1.4\n" +
                "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n" +
                "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n" +
                "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] >>\nendobj\n" +
                "xref\n0 4\n0000000000 65535 f \n0000000009 00000 n \n0000000058 00000 n \n0000000115 00000 n \n" +
                "trailer\n<< /Size 4 /Root 1 0 R >>\nstartxref\n190\n%%EOF";
        return pdf.getBytes();
    }
}
