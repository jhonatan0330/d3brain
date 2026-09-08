package d3.assistant.infrastructure;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.assistant.application.AssistantChatSvc;
import d3.assistant.domain.ChatRequestDTO;
import d3.assistant.domain.ChatResponseDTO;
import d3.authentication.application.UsuarioSesionSvc;
import d3.shared.domain.ServerException;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

	private final AssistantChatSvc assistantChatService;
	private final UsuarioSesionSvc usuarioSesionService;

	public AssistantController(@Lazy AssistantChatSvc assistantChatService,
			@Lazy UsuarioSesionSvc usuarioSesionService) {
		this.assistantChatService = assistantChatService;
		this.usuarioSesionService = usuarioSesionService;
	}

	@PostMapping("/chat")
	public ChatResponseDTO chat(@RequestBody ChatRequestDTO request,
			@RequestHeader("Authorization") String token) throws ServerException {
		usuarioSesionService.getUserFlex(token);
		return assistantChatService.chat(request);
	}
}