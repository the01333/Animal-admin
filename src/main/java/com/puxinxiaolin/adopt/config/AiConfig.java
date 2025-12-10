package com.puxinxiaolin.adopt.config;

import com.puxinxiaolin.adopt.model.AlibabaOpenAiChatModel;
import com.puxinxiaolin.adopt.service.AiToolService;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.autoconfigure.openai.OpenAiChatProperties;
import org.springframework.ai.autoconfigure.openai.OpenAiConnectionProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.observation.ChatModelObservationConvention;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class AiConfig {

    @Autowired
    private AiToolService aiToolService;

    private static final String SYSTEM_PROMPT = """
            你是宠物领养系统的客服, 只回答与本系统相关的问题, 不相关的问题直接拒绝。
            """;

    @Bean
    public ChatClient chatClient(AlibabaOpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(SYSTEM_PROMPT)
                // Function Call
                .defaultTools(aiToolService)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                ).build();

    }

    /**
     * <strong>解决兼容问题</strong> <br />
     * 自定义 OpenAiChatModel, 重写 buildGeneration() <br />
     * 由于 OpenAiChatModel 是通过 Builder 创建的, 而不是通过 Bean 创建的, <br />
     * 所以这里手动声明成 Bean
     *
     * @param commonProperties
     * @param chatProperties
     * @param restClientBuilderProvider
     * @param webClientBuilderProvider
     * @param toolCallingManager
     * @param retryTemplate
     * @param responseErrorHandler
     * @param observationRegistry
     * @param observationConvention
     * @return
     */
    @Bean
    public AlibabaOpenAiChatModel alibabaOpenAiChatModel(
            OpenAiConnectionProperties commonProperties,
            OpenAiChatProperties chatProperties,
            ObjectProvider<RestClient.Builder> restClientBuilderProvider,
            ObjectProvider<WebClient.Builder> webClientBuilderProvider,
            ToolCallingManager toolCallingManager,
            RetryTemplate retryTemplate,
            ResponseErrorHandler responseErrorHandler,
            ObjectProvider<ObservationRegistry> observationRegistry,
            ObjectProvider<ChatModelObservationConvention> observationConvention
    ) {
        String baseUrl = StringUtils.hasText(chatProperties.getBaseUrl()) ? chatProperties.getBaseUrl() : commonProperties.getBaseUrl();
        String apiKey = StringUtils.hasText(chatProperties.getApiKey()) ? chatProperties.getApiKey() : commonProperties.getApiKey();
        String projectId = StringUtils.hasText(chatProperties.getProjectId()) ? chatProperties.getProjectId() : commonProperties.getProjectId();
        String organizationId = StringUtils.hasText(chatProperties.getOrganizationId()) ? chatProperties.getOrganizationId() : commonProperties.getOrganizationId();
        Map<String, List<String>> connectionHeaders = new HashMap<>();
        if (StringUtils.hasText(projectId)) {
            connectionHeaders.put("OpenAI-Project", List.of(projectId));
        }
        if (StringUtils.hasText(organizationId)) {
            connectionHeaders.put("OpenAI-Organization", List.of(organizationId));
        }

        RestClient.Builder restClientBuilder = restClientBuilderProvider.getIfAvailable(RestClient::builder);
        WebClient.Builder webClientBuilder = webClientBuilderProvider.getIfAvailable(WebClient::builder);

        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(new SimpleApiKey(apiKey))
                .headers(CollectionUtils.toMultiValueMap(connectionHeaders))
                .completionsPath(chatProperties.getCompletionsPath())
                .embeddingsPath("/v1/embeddings")
                .restClientBuilder(restClientBuilder)
                .webClientBuilder(webClientBuilder)
                .responseErrorHandler(responseErrorHandler)
                .build();
        AlibabaOpenAiChatModel chatModel = AlibabaOpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(chatProperties.getOptions())
                .toolCallingManager(toolCallingManager)
                .retryTemplate(retryTemplate)
                .observationRegistry((ObservationRegistry) observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP))
                .build();
        Objects.requireNonNull(chatModel);
        observationConvention.ifAvailable(chatModel::setObservationConvention);

        return chatModel;
    }

}