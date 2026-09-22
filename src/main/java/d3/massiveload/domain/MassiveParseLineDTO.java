package d3.massiveload.domain;

import java.util.List;

import d3.document.domain.DocumentMessage;
import d3.document.domain.PedidoVentaDTO;

public class MassiveParseLineDTO {

	private int orderNumber;
	private String updateId;
	private String status;
	private List<DocumentMessage> messages;
	private PedidoVentaDTO document;

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DocumentMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<DocumentMessage> messages) {
		this.messages = messages;
	}

	public PedidoVentaDTO getDocument() {
		return document;
	}

	public void setDocument(PedidoVentaDTO document) {
		this.document = document;
	}
}