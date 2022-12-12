package com.softure.api.application;

import com.softure.api.domain.ApiVO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

public interface IApiSendService {

	IdResponse call(String token, ApiVO item) throws ServerException ;
}
