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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
    private static final String VALUE = "valor-1";

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
}
