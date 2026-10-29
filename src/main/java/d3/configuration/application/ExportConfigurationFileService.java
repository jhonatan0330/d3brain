package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.ExportListRequest;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.ProcesoSvc;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoFilterDTO;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;

@Service
public class ExportConfigurationFileService {

	private final UploadSvc uploadService;
	private final ProcesoSvc procesoService;
	private final NodoHijoMapper nodoHijoMapper;

	public ExportConfigurationFileService(@Lazy UploadSvc uploadService, @Lazy ProcesoSvc procesoService,
			@Lazy NodoHijoMapper nodoHijoMapper) {
		this.uploadService = uploadService;
		this.procesoService = procesoService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public CargaArchivoDTO call() throws ServerException {
		return uploadFile(construirHierarchy());
	}

	public HierarchyExporterDTO construirHierarchy() throws ServerException {
		SessionContext.getCurrentUser();
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		hierarchy.setPropertyTypes(nodoHijoMapper.listarTiposPropiedadFull());
		hierarchy.setProperties(nodoHijoMapper.listarPropiedadesFull());
		hierarchy.setRelations(nodoHijoMapper.listarRelacionesFull());

		hierarchy.setOrganization(nodoHijoMapper.obtenerOrganizacionPrincipal());
		hierarchy.setApis(nodoHijoMapper.listarApisFull(null));
		hierarchy.setProcess(nodoHijoMapper.listarProcesosFull(null));
		hierarchy.setStates(nodoHijoMapper.listarEstadosFull(null));
		hierarchy.setTransitions(nodoHijoMapper.listarTransicionesFull(null));
		hierarchy.setTemplates(nodoHijoMapper.listarPlantillasFull(null));
		hierarchy.setReports(nodoHijoMapper.listarReportesFull(null));
		hierarchy.setFields(nodoHijoMapper.listarCamposFull(null));

		return hierarchy;
	}

	public CargaArchivoDTO call(ExportListRequest modules) throws ServerException {

		if (modules == null || modules.getModulesCode() == null || modules.getModulesCode().isEmpty())
			throw new ServerException("No hay modulos");
		List<String> processToInclude = new ArrayList<>();

		SessionContext.getCurrentUser();

		for (String iModule : modules.getModulesCode()) {
			ProcesoFilterDTO filterProcess = new ProcesoFilterDTO();
			filterProcess.setEstado(SharedConstants.STATE_ACTIVE);
			filterProcess.setCodigo(iModule);
			List<ProcesoDTO> processModule = procesoService.listarConsulta(filterProcess);
			if (processModule != null && !processModule.isEmpty())
				processToInclude.addAll(getProcessFromMacro(processModule.get(0).getLlaveTabla()));
		}
		if (processToInclude.isEmpty())
			throw new ServerException("No se identifica un modulo con el codigos elegidos ");
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();

		hierarchy.setPropertyTypes(nodoHijoMapper.listarTiposPropiedadFull());
		hierarchy.setProperties(nodoHijoMapper.listarPropiedadesFull());
		hierarchy.setRelations(nodoHijoMapper.listarRelacionesFull());

		hierarchy.setApis(nodoHijoMapper.listarApisFull(processToInclude));
		hierarchy.setProcess(nodoHijoMapper.listarProcesosFull(processToInclude));
		hierarchy.setStates(nodoHijoMapper.listarEstadosFull(processToInclude));
		hierarchy.setTransitions(nodoHijoMapper.listarTransicionesFull(processToInclude));
		hierarchy.setTemplates(nodoHijoMapper.listarPlantillasFull(processToInclude));
		hierarchy.setReports(nodoHijoMapper.listarReportesFull(processToInclude));
		hierarchy.setFields(nodoHijoMapper.listarCamposFull(processToInclude));

		return uploadFile(hierarchy);
	}


	private CargaArchivoDTO uploadFile(HierarchyExporterDTO hierarchy) throws ServerException {
		return uploadService.uploadFileDTO(convert(hierarchy), "Entrada.txt", "export", "private");
	}

	private byte[] convert(HierarchyExporterDTO hierarchy) throws ServerException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsBytes(hierarchy);
		} catch (JsonProcessingException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private List<String> getProcessFromMacro(String parentProcess) throws ServerException {
		List<String> processToInclude = new ArrayList<>();
		ProcesoFilterDTO filterProcess = new ProcesoFilterDTO();
		filterProcess.setEstado(SharedConstants.STATE_ACTIVE);
		filterProcess.setNodoSuperior(parentProcess);
		List<ProcesoDTO> processModule = procesoService.listarConsulta(filterProcess);
		if (processModule != null && !processModule.isEmpty()) {
			for (ProcesoDTO procesoDTO : processModule) {
				processToInclude.addAll(getProcessFromMacro(procesoDTO.getLlaveTabla()));
				processToInclude.add(parentProcess);
			}
		}
		processToInclude.add(parentProcess);
		return processToInclude;
	}

}
