package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.AIIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class VectorStoreService {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreService.class);
    private final VectorStore store;

    public VectorStoreService(VectorStore store) {
        this.store = store;
    }

    public void save(AIIndexRequest request) {
        log.info("Indexando Documento Mongo con Vector Store");
        Document document = Document.builder()
                            .id(request.id())
                            .text(request.contenido())
                            .metadata(request.metadata())
                            .build();

        TokenTextSplitter splitter = this.getSplitter();

        List<Document> chunks = splitter.apply(List.of(document));

        store.add(chunks);
        log.info("Documento guardado en Vector Store");
    }

    public List<Document> search(String query) {
        log.info("Realizando busqueda en Vector Store");
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(3)
                .build();

        List<Document> documents = store.similaritySearch(searchRequest);
        log.info("Búsqueda finalizada");
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
