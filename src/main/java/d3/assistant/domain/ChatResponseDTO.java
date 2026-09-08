package d3.assistant.domain;

import java.util.List;

public class ChatResponseDTO {

	private List<ChatChoiceDTO> choices;

	public List<ChatChoiceDTO> getChoices() {
		return choices;
	}

	public void setChoices(List<ChatChoiceDTO> choices) {
		this.choices = choices;
	}
}