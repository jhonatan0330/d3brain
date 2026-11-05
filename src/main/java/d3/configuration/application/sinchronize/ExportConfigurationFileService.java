package d3.configuration.application.sinchronize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.ExportListRequest;
import d3.configuration.domain.NodoArbolDTO;
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
	private final ArbolEnsambladorSvc arbolEnsambladorService;

	public ExportConfigurationFileService(@Lazy UploadSvc uploadService, @Lazy ProcesoSvc procesoService,
			@Lazy ArbolEnsambladorSvc arbolEnsambladorService) {
		this.uploadService = uploadService;
		this.procesoService = procesoService;
		this.arbolEnsambladorService = arbolEnsambladorService;
	}

	public CargaArchivoDTO call() throws ServerException {
		SessionContext.getCurrentUser();
		return uploadFile(arbolEnsambladorService.cargarArboles());
	}

	public List<NodoArbolDTO> construirArboles() throws ServerException {
		SessionContext.getCurrentUser();
		return arbolEnsambladorService.cargarArboles();
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

		return uploadFile(arbolEnsambladorService.cargarArboles(processToInclude));
	}

	private CargaArchivoDTO uploadFile(List<NodoArbolDTO> arboles) throws ServerException {
		return uploadService.uploadFileDTO(convert(arboles), "Entrada.txt", "export", "private");
	}

	private byte[] convert(List<NodoArbolDTO> arboles) throws ServerException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsBytes(arboles);
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
