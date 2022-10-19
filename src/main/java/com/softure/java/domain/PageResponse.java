package com.softure.java.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {
	
	private List<T> content;
	private int currentPage;
	private long totalItems;
	private int totalPages;
	
}
