package com.softure.configuration_file.domain;

import java.util.List;

public class ExportListRequest {
	private List<String> modulesCode;

	public List<String> getModulesCode() {
		return modulesCode;
	}

	public void setModulesCode(List<String> modulesCode) {
		this.modulesCode = modulesCode;
	}

}
