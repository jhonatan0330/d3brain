package d3.assistant.domain;

import java.util.List;

public class ChatRequestDTO {

	private String model;
	private List<ChatMessageDTO> messages;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<ChatMessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessageDTO> messages) {
		this.messages = messages;
	}
}