package com.softure.configuration_file.domain;

import java.util.List;

import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.report.domain.ReporteBaseDTO;

public class HierarchyExporterDTO {

	private OrganizacionDTO organization;
	private List<ProcesoDTO> process;
	private List<ProcesoEstadoDTO> states;
	private List<PropiedadDTO> properties;
	private List<RelacionInternaDTO> relations;
	private List<DocumentoPlantillaDTO> templates;
	private List<DocumentoPlantillaCaracteristicaDTO> fields;
	private List<RolAccesoDTO> roles;
	private List<ReporteBaseDTO> reports;
	private List<PropiedadValorDefinidoDTO> propertyTypes;

	public OrganizacionDTO getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizacionDTO organization) {
		this.organization = organization;
	}

	public List<ProcesoDTO> getProcess() {
		return process;
	}

	public void setProcess(List<ProcesoDTO> process) {
		this.process = process;
	}

	public List<PropiedadDTO> getProperties() {
		return properties;
	}

	public void setProperties(List<PropiedadDTO> properties) {
		this.properties = properties;
	}

	public List<RelacionInternaDTO> getRelations() {
		return relations;
	}

	public void setRelations(List<RelacionInternaDTO> relations) {
		this.relations = relations;
	}

	public List<DocumentoPlantillaDTO> getTemplates() {
		return templates;
	}

	public void setTemplates(List<DocumentoPlantillaDTO> templates) {
		this.templates = templates;
	}

	public List<ProcesoEstadoDTO> getStates() {
		return states;
	}

	public void setStates(List<ProcesoEstadoDTO> states) {
		this.states = states;
	}

	public List<RolAccesoDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RolAccesoDTO> roles) {
		this.roles = roles;
	}

	public List<ReporteBaseDTO> getReports() {
		return reports;
	}

	public void setReports(List<ReporteBaseDTO> reports) {
		this.reports = reports;
	}

	public List<DocumentoPlantillaCaracteristicaDTO> getFields() {
		return fields;
	}

	public void setFields(List<DocumentoPlantillaCaracteristicaDTO> fields) {
		this.fields = fields;
	}

	public List<PropiedadValorDefinidoDTO> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List<PropiedadValorDefinidoDTO> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}
	
}
