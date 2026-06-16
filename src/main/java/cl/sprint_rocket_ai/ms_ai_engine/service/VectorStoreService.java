package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.AIIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class VectorStoreService {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreService.class);
    private final VectorStore store;
    private final JdbcTemplate jdbcTemplate;

    public VectorStoreService(VectorStore store,
                              JdbcTemplate jdbcTemplate
    ) {
        this.store = store;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(AIIndexRequest request) {
        log.info("Indexando Documento Mongo con Vector Store");
        Map<String,Object> metadata = request.metadata();
        metadata.put("document_id", request.id());
        metadata.put("tags",request.tags());
        metadata.put("tipo",request.tipo());
        Document document = Document.builder()
                            .id(request.id())
                            .text(request.contenido())
                            .metadata(metadata)
                            .build();

        TokenTextSplitter splitter = this.getSplitter();

        List<Document> chunks = splitter.apply(List.of(document));

        List<Document> normalized = chunks.stream()
                .map(c -> Document.builder()
                        .id(request.id())
                        .text(c.getText())
                        .metadata(c.getMetadata())
                        .build())
                .toList();
        store.add(normalized);
        log.info("Documento guardado en Vector Store");
    }

    public void update(String id, AIIndexRequest request) {
        log.info("Actualizando embeddings para document_id={} en Vector Store", id);
        this.deleteByDocumentId(id);
        this.save(request);
        log.info("Embeddings actualizados para document_id={} en Vector Store", id);
    }

    public void deleteByDocumentId(String documentId) {
        log.info("Eliminando embeddings para document_id={} en Vector Store (tabla ai_embeddings)", documentId);
        try {
            String sql = "DELETE FROM ai_embeddings WHERE id = ? OR metadata->>'document_id' = ?";
            int rows = jdbcTemplate.update(sql, documentId, documentId);
            log.info("Filas eliminadas en ai_embeddings: {}", rows);
        } catch (Exception e) {
            log.error("Error al eliminar embeddings para document_id={}: {}", documentId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Document> search(String query) {
        log.info("Realizando busqueda en Vector Store");
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(3)
                .build();

        List<Document> documents = store.similaritySearch(searchRequest);
        log.info("Búsqueda finalizada en Vector Store, cantidad de documentos: {}",documents.size());
        return documents;

    }

    public void savePdf(MultipartFile file) {
        log.info("Indexando Documento PDF con Vector Store");
        Resource resource = null;
        try {
            resource = new InputStreamResource(file.getInputStream());
            PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
            List<Document> documents = reader.get();
            TokenTextSplitter splitter = this.getSplitter();
            List<Document> chunks = splitter.apply(documents);
            String filename = getDefaultFilename(file);
            chunks.forEach(doc -> doc.getMetadata().put("source", filename));
            store.add(chunks);
        } catch (IOException e) {
            log.error("Error al indexar PDF: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("Documento PDF guardado en Vector Store");
    }


    private TokenTextSplitter getSplitter(){
        return TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(5)
                .withMaxNumChunks(10000)
                .withKeepSeparator(true)
                .build();
    }

    private String getDefaultFilename(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && !originalFilename.isEmpty()
                ? originalFilename
                : "file.pdf";
    }


}
