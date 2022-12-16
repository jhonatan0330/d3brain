package com.softure.api.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentVO;
import com.softure.api.domain.FilterDocumentVO;
import com.softure.java.dto.exception.ServerException;

@Service
public class ApiGetService {

	public List<DocumentVO> call(String token, FilterDocumentVO filter) throws ServerException {
		return null;
	}
}
