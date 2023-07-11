package com.softure.shared.domain;

import java.util.List;

public class SharedPageResponse<T> {
	
	private List<T> content;
	private int currentPage;
	private long totalItems;
	private int totalPages;
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
