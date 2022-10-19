package com.softure.massiveload.application;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

// Start of user code imports
//End of user code

public interface ICargaMasivaItemSincronizeService {

	IdResponse call(String token, String itemId) throws ServerException ;

}

