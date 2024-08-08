package com.shared.application;


import com.shared.domain.ServerException;
import com.shared.domain.SharedToken;

import jakarta.servlet.http.HttpServletRequest;

public interface SharedAuthenticateService {

	SharedToken validate(String token, HttpServletRequest request) throws ServerException;

	String getUser(String token, HttpServletRequest request) throws ServerException;

}
