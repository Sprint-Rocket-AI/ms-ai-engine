package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.ActividadAgent;
import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.JiraAgent;
import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.RecordatorioAgent;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.McpController;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp-tools")
public class McpRestController implements McpController {

    private final ActividadAgent actividadesAgent;
    private final JiraAgent jiraAgent;
    private final RecordatorioAgent recordatorioAgent;

    public McpRestController(
            ActividadAgent actividadesAgent,
            JiraAgent jiraAgent,
            RecordatorioAgent recordatorioAgent) {

        this.actividadesAgent = actividadesAgent;
        this.jiraAgent = jiraAgent;
        this.recordatorioAgent = recordatorioAgent;
    }

    @Override
    public ResponseEntity<String> actividadTool(@Valid @RequestBody MCPQueryRequest request) {
        return ResponseEntity.ok(actividadesAgent.actividadChat(request));
    }

    @Override
    public ResponseEntity<String> jiraTool(@Valid @RequestBody MCPQueryRequest request) {
        return ResponseEntity.ok(jiraAgent.jiraChat(request));
    }

    @Override
    public ResponseEntity<String> recordatorioTool(@Valid @RequestBody MCPQueryRequest request) {
        return ResponseEntity.ok(recordatorioAgent.recordatorioChat(request));
    }
}
