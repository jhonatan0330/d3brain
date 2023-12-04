package com.shared.application;

import javax.servlet.http.HttpServletRequest;

import com.shared.domain.ServerException;
import com.shared.domain.SharedToken;

public interface SharedAuthenticateService {

	SharedToken validate(String token, HttpServletRequest request) throws ServerException;

	String getUser(String token, HttpServletRequest request) throws ServerException;

}
