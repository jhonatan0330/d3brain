package d3.configuration.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.ArbolConfiguracionFilterDTO;
import d3.configuration.domain.DiferenciaDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.SincronizacionSeleccionadaDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;

@Service
public class SincronizacionArbolSvc {

	private final ArbolConfiguracionSvc arbolConfiguracionSvc;
	private final ComparacionArbolSvc comparacionArbolSvc;
	private final ActualizacionEntidadSvc actualizacionEntidadSvc;
	private final ImportConfigurationFileService importService;
	private final UploadSvc uploadSvc;
	private final ObjectMapper mapper;

	public SincronizacionArbolSvc(@Lazy ArbolConfiguracionSvc arbolConfiguracionSvc,
			@Lazy ComparacionArbolSvc comparacionArbolSvc, @Lazy ActualizacionEntidadSvc actualizacionEntidadSvc,
			@Lazy ImportConfigurationFileService importService, @Lazy UploadSvc uploadSvc, ObjectMapper mapper) {
		this.arbolConfiguracionSvc = arbolConfiguracionSvc;
		this.comparacionArbolSvc = comparacionArbolSvc;
		this.actualizacionEntidadSvc = actualizacionEntidadSvc;
		this.importService = importService;
		this.uploadSvc = uploadSvc;
		this.mapper = mapper;
	}

	public TreeNodeDTO obtenerArbol(String token, ArbolConfiguracionFilterDTO filter) throws ServerException {
		return arbolConfiguracionSvc.construirArbolActual(token, filter);
	}

	public CargaArchivoDTO exportarArbol(String token, ArbolConfiguracionFilterDTO filter) throws ServerException {
		TreeNodeDTO arbol = obtenerArbol(token, filter);
		try {
			byte[] bytes = mapper.writeValueAsBytes(arbol);
			return uploadSvc.uploadFileDTO(bytes, "ArbolConfiguracion.json", token, "export", "private");
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		
	}

	public List<DiferenciaDTO> compararArbol(String token, TreeNodeDTO remoto, ArbolConfiguracionFilterDTO filter)
			throws ServerException {
		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(token, filter);
		return comparacionArbolSvc.comparar(local, remoto);
	}

	public CargaArchivoDTO sincronizar(String token, SincronizacionSeleccionadaDTO request) throws ServerException {
		if (request == null || request.getArbol() == null)
			throw new ServerException("Árbol requerido");
		TreeNodeDTO local = arbolConfiguracionSvc.construirArbolActual(token, filtroCompleto());
		try {
			actualizacionEntidadSvc.actualizarArbol(request.getArbol(), local);
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		TreeNodeDTO filtrado = arbolConfiguracionSvc.filtrarArbolPorSeleccion(request.getArbol(),
				request.getSelecciones());
		HierarchyExporterDTO hierarchy = arbolConfiguracionSvc.convertirArbolAHierarchy(filtrado);
		LogConfigurationDTO logs = importService.sincronize(token, hierarchy);

		return uploadSvc.uploadFileDTO(logs.getLogs().getBytes(), "Sincronizacion.txt", token, "import",
				"private");
	}

	private ArbolConfiguracionFilterDTO filtroCompleto() {
		ArbolConfiguracionFilterDTO filter = new ArbolConfiguracionFilterDTO();
		filter.setListarPropiedades(true);
		return filter;
	}

}