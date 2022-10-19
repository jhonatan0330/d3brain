package com.softure.massiveload.application;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

// Start of user code imports
//End of user code

public interface ICargaMasivaSincronizeService {

	IdResponse call(String token, String fileUrl, String template) throws ServerException ;

}

