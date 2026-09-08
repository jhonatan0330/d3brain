package d3.assistant.application;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import d3.assistant.domain.ChatRequestDTO;
import d3.assistant.domain.ChatResponseDTO;
import d3.shared.domain.ServerException;
import reactor.core.publisher.Mono;

@Service("assistantChatSvc")
public class AssistantChatSvc {

	private static Logger log = LoggerFactory.getLogger(AssistantChatSvc.class);

	private final WebClient webClient;

	@Value("${assistant.api.url:}")
	private String apiUrl;

	@Value("${assistant.api.key:}")
	private String apiKey;

	@Value("${assistant.api.model:}")
	private String model;

	public AssistantChatSvc(@Lazy WebClient webClient) {
		this.webClient = webClient;
	}

	public ChatResponseDTO chat(ChatRequestDTO request) throws ServerException {
		if (apiKey == null || apiKey.isBlank())
			throw new ServerException("El proveedor de chat no esta configurado (assistant.api.key)");
		if (apiUrl == null || apiUrl.isBlank())
			throw new ServerException("El proveedor de chat no esta configurado (assistant.api.url)");
		if (request == null || request.getMessages() == null || request.getMessages().isEmpty())
			throw new ServerException("No se enviaron mensajes al chat");

		Map<String, Object> body = new HashMap<>();
		String modelToUse = (model == null || model.isBlank()) ? request.getModel() : model;
		body.put("model", modelToUse);
		body.put("messages", request.getMessages());

		try {
			Mono<ChatResponseDTO> responseMono = webClient.post().uri(apiUrl)
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey).bodyValue(body).retrieve()
					.bodyToMono(ChatResponseDTO.class);
			ChatResponseDTO response = responseMono.block();
			if (response == null || response.getChoices() == null || response.getChoices().isEmpty())
				throw new ServerException("El proveedor de chat respondio sin contenido");
			log.info("Chat del assistant procesado con el modelo {}", modelToUse);
			return response;
		} catch (WebClientRequestException e) {
			throw new ServerException("Error de conexion con el proveedor de chat", e);
		} catch (WebClientResponseException e) {
			throw new ServerException("Error del proveedor de chat: " + e.getResponseBodyAsString(), e);
		}
	}
}