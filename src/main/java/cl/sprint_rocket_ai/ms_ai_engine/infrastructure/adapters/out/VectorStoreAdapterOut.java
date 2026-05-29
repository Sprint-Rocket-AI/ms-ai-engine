package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class VectorStoreAdapterOut implements VectorStorePortOut {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreAdapterOut.class);
    private final VectorStore store;

    public VectorStoreAdapterOut(VectorStore store) {
        this.store = store;
    }

    @Override
    public void save(AIIndexRequest request) {
        log.info("Indexando documento con Vector Store");
        Document document = Document.builder()
                            .id(request.id())
                            .text(request.contenido())
                            .metadata(request.metadata())
                            .build();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                        .           withChunkSize(800)
                                    .withMinChunkSizeChars(350)
                                    .withMinChunkLengthToEmbed(5)
                                    .withMaxNumChunks(10000)
                                    .withKeepSeparator(true)
                                    .build();

        List<Document> chunks = splitter.apply(List.of(document));

        store.add(chunks);
        log.info("Documento guardado en Vector Store");
    }

    @Override
    public List<VectorDocument> search(String query) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(3)
                .build();

        List<Document> documents = store.similaritySearch(searchRequest);

        return documents.stream()
                .map(this::toVectorDocument)
                .toList();

    }


    private VectorDocument toVectorDocument(Document doc) {
        return new VectorDocument(
                doc.getId(),
                doc.getText(),
                doc.getMetadata()
        );
    }


}
