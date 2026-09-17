package d3.shared.domain;

import java.util.Date;

public class SharedToken {

	private String token;
	private String user;
	private String userId;
	private String userName;
	private Date fechaCierre;
	private Boolean privada = null;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Boolean getPrivada() {
		return privada;
	}

	public void setPrivada(Boolean privada) {
		this.privada = privada;
	}

}
