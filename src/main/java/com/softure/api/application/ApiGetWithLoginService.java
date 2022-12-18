package com.softure.api.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentVO;
import com.softure.api.domain.FilterWithLoginVO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@Service
public class ApiGetWithLoginService {

	@Autowired ApiLoginService loginService;
	@Autowired ApiGetService getService;
	
	public List<DocumentVO> call(FilterWithLoginVO item) throws ServerException {
		IdResponse token = loginService.call(item.getLogin());
		return getService.call(token.getId(), item.getDocument());
	}

}
