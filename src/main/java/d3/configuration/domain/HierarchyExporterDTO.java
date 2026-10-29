package d3.configuration.domain;

import java.util.ArrayList;
import java.util.List;

import d3.authentication.domain.OrganizacionDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.report.domain.ReporteBaseDTO;
import d3.webservice.domain.WebServiceDTO;

public class HierarchyExporterDTO {

	private OrganizacionDTO organization;
	private List<ProcesoDTO> process;
	private List<ProcesoEstadoDTO> states;
	private List<ProcesoTransicionDTO> transitions;
	private List<DocumentoPlantillaDTO> templates;
	private List<DocumentoPlantillaCaracteristicaDTO> fields;
	private List<ReporteBaseDTO> reports;
	private List<WebServiceDTO> apis;

	private List<PropiedadValorDefinidoDTO> propertyTypes;
	private List<PropiedadDTO> properties;
	private List<RelacionInternaDTO> relations;
	
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

	public List<WebServiceDTO> getApis() {
		return apis;
	}

	public void setApis(List<WebServiceDTO> apis) {
		this.apis = apis;
	}

	public List<ProcesoTransicionDTO> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<ProcesoTransicionDTO> transitions) {
		this.transitions = transitions;
	}

	public void normalizar() {
		if (process == null)
			process = new ArrayList<>();
		if (states == null)
			states = new ArrayList<>();
		if (transitions == null)
			transitions = new ArrayList<>();
		if (templates == null)
			templates = new ArrayList<>();
		if (fields == null)
			fields = new ArrayList<>();
		if (reports == null)
			reports = new ArrayList<>();
		if (apis == null)
			apis = new ArrayList<>();
		if (properties == null)
			properties = new ArrayList<>();
		if (relations == null)
			relations = new ArrayList<>();
		if (propertyTypes == null)
			propertyTypes = new ArrayList<>();
	}

}
