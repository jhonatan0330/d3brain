package d3.shared.application;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedToken;

import jakarta.servlet.http.HttpServletRequest;

public interface SharedAuthenticateService {

	SharedToken validate(String token, HttpServletRequest request) throws ServerException;

	String getUser(String token, HttpServletRequest request) throws ServerException;

}
