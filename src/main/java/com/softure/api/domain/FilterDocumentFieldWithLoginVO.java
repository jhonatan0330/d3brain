package com.softure.api.domain;

import lombok.Data;

@Data
public class FilterDocumentFieldWithLoginVO {
	private LoginVO login;
	private FilterFieldVO field;
}
