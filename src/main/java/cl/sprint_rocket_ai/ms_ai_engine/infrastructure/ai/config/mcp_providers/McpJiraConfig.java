package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.config.mcp_providers;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider; // Provider oficial de Spring AI
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class McpJiraConfig {

    @Value("${mcp.client.jira.url}")
    private String jiraUrl;

    @Value("${mcp.client.jira.email}")
    private String jiraEmail;

    @Value("${mcp.client.jira.api-token}")
    private String jiraApiToken;

    @Bean
    public McpSyncClient mcpJiraSyncClient() {

        ServerParameters params = ServerParameters.builder("npx")
                .args(List.of("-y", "@modelcontextprotocol/server-jira"))
                .env(Map.of(
                        "JIRA_URL", jiraUrl,
                        "JIRA_EMAIL", jiraEmail,
                        "JIRA_API_TOKEN", jiraApiToken
                ))
                .build();

        McpJsonMapper mapper = new JacksonMcpJsonMapper();

        StdioClientTransport transport =
                new StdioClientTransport(params,mapper);

        McpSyncClient client = McpClient.sync(transport).build();

        client.initialize();

        return client;
    }

    @Bean
    public ToolCallbackProvider jiraToolCallbackProvider(McpSyncClient mcpJiraSyncClient) {
        // Usamos el builder oficial de Spring AI para crear el callback provider síncrono
        return SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpJiraSyncClient)
                .build();
    }
}