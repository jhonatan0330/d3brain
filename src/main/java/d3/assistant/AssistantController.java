package d3.assistant;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.assistant.application.AssistantChatSvc;
import d3.assistant.domain.ChatRequestDTO;
import d3.assistant.domain.ChatResponseDTO;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;

@RestController
@RequestMapping("/assistant")
public class AssistantController {

	private final AssistantChatSvc assistantChatService;

	public AssistantController(@Lazy AssistantChatSvc assistantChatService) {
		this.assistantChatService = assistantChatService;
	}

	@PostMapping("/chat")
	public ChatResponseDTO chat(@RequestBody ChatRequestDTO request) throws ServerException {
		SessionContext.getCurrentUser();
		return assistantChatService.chat(request);
	}
}