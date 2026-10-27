package d3.process.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.CacheManager;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.application.PropiedadSvc;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.document.application.CallDocumentCommons;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import d3.process.infrastructure.DocumentoPlantillaCaracteristicaMapper;
import d3.shared.application.BasicSvc;
import d3.shared.application.D3Utils;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import jakarta.annotation.PostConstruct;

@Service("documentoPlantillaCaracteristicaService")
public class DocumentoPlantillaCaracteristicaSvc
		extends BasicSvc<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO> {

	private final DocumentoPlantillaCaracteristicaMapper documentoPlantillaCaracteristicaMapper;
	private final PropiedadSvc parametroService;
	private final PropertyGetWithCacheService propertyManagerService;
	private final CallSearchProcessFromText searchProcessFromText;
	private final CacheManager cacheService;

	public DocumentoPlantillaCaracteristicaSvc(
			@Lazy DocumentoPlantillaCaracteristicaMapper documentoPlantillaCaracteristicaMapper,
			@Lazy PropiedadSvc parametroService, @Lazy PropertyGetWithCacheService propertyManagerService,
			@Lazy CallSearchProcessFromText searchProcessFromText, @Lazy CacheManager cacheService) {
		this.documentoPlantillaCaracteristicaMapper = documentoPlantillaCaracteristicaMapper;
		this.parametroService = parametroService;
		this.propertyManagerService = propertyManagerService;
		this.searchProcessFromText = searchProcessFromText;
		this.cacheService = cacheService;
	}

	@Override
	public DocumentoPlantillaCaracteristicaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. DocumentoPlantillaCaracteristica");

		DocumentoPlantillaCaracteristicaDTO _db = cacheService.getField(llave);
		if (_db != null)
			return _db;

		DocumentoPlantillaCaracteristicaFilterDTO dto = new DocumentoPlantillaCaracteristicaFilterDTO();
		dto.setLlaveTabla(llave);
		_db = documentoPlantillaCaracteristicaMapper.consultar(dto);
		if (_db != null)
			cacheService.putField(llave, _db);
		return _db;
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = documentoPlantillaCaracteristicaMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO actualizar(DocumentoPlantillaCaracteristicaDTO dto)
			throws ServerException {
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.actualizar(dto);
		organizar(dto);
		parametroService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre(), dto.getCodigo());
		PropiedadDTO differenceProperty = parametroService.getPropertyDifferenceField(dto.getLlaveTabla());
		if (differenceProperty == null) {
			PropiedadDTO filtroPlantilla = parametroService.getPropertyDifferenceTemplate(dto.getNodoSuperior());
			if (filtroPlantilla != null)
				createFieldDifference(dto, filtroPlantilla.getValor());
		} else {
			DocumentoPlantillaCaracteristicaDTO fieldDifference = consultaXId(differenceProperty.getValor());
			if (fieldDifference.getTipo().compareTo(dto.getTipo()) != 0) {
				fieldDifference.setTipo(dto.getTipo());
				update(fieldDifference);
			}
		}
		cacheService.clearFieldsMap();
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO inactivar(DocumentoPlantillaCaracteristicaDTO dto)
			throws ServerException {
		dto = super.inactivar(dto);
		organizar(dto);
		// validar que el campo no se use en ninguna propiedad
		cacheService.clearFieldsMap();
		return dto;
	}

	// Esto lo uso en APiCommon y la idea es que se mejore en las cargas masivas
	// para que solo consulte de a uno y si es null que gestione los errores por
	// fuera
	public DocumentoPlantillaCaracteristicaDTO listarCarga(DocumentoPlantillaCaracteristicaFilterDTO dto)
			throws ServerException {
		if (dto == null || dto.getLlaveTabla() == null)
			throw new ServerException("Desarrollador el DTO viene nulo");
		if (dto.getDocumentos() == null || dto.getDocumentos().isEmpty())
			throw new ServerException("Desarrollador los documentos vienen vacios");
		// if(dtoCarga.getDocumentos()==null || dtoCarga.getDocumentos().isEmpty())
		// throw new ServerException("En el campo documentos debes incluir los
		// documentos a validar, en este caso estan vacios");
		DocumentoPlantillaCaracteristicaDTO dtoCarga = cargarComplementos(consultaXId(dto.getLlaveTabla()));
		List<PedidoVentaDTO> documentAproval = new ArrayList<>();
		for (PedidoVentaDTO iDoc : dto.getDocumentos()) {
			PedidoVentaDTO addItem = new PedidoVentaDTO();
			try {
				String keyOfDocument = searchProcessFromText.getValueOptionFromText(iDoc.getNombre(), dtoCarga);
				addItem.setLlaveTabla(keyOfDocument);
			} catch (Exception e) {
				CallDocumentCommons.addMessageError(addItem, e.getMessage());
			}
			addItem.setNombre(iDoc.getNombre());
			documentAproval.add(addItem);
		}
		dtoCarga.setDocumentos(documentAproval);
		return dtoCarga;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO guardar(DocumentoPlantillaCaracteristicaDTO dto) throws ServerException {
		if (dto.getNodoSuperior() == null)
			throw new ServerException("Es necesario la plantilla a la que pertenece el campo");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCantidad = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCantidad.setNodoSuperior(dto.getNodoSuperior());
		int cantidadCampos = contarResultados(filtroCantidad);
		if (dto.getPrioridad() != null && dto.getPrioridad().compareTo(0) != 0) {
			// 2024-3 retire cantidadCampos == 0 &&
			// Porque al duplicar algunas no aparecian correctamente quinber guias
			// Esto es porque cuando se crean las plantillas automaticas no cuenta
			// correctametne la cantidad de campos ?? cuales no se si lo dane
			cantidadCampos = dto.getPrioridad();
		} else {
			cantidadCampos = cantidadCampos + 1;
		}
		if (dto.getCodigo() == null)
			dto.setCodigo(D3Utils.formatFunction(dto.getNombre()).toUpperCase());
		dto.setNombre(dto.getNombre().toUpperCase());
		dto.setPrioridad(cantidadCampos);
		dto.setCodigo(D3Utils.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.guardar(dto);
		PropiedadDTO filtroPlantilla = parametroService.getPropertyDifferenceTemplate(dto.getNodoSuperior());
		if (filtroPlantilla != null)
			createFieldDifference(dto, filtroPlantilla.getValor());
		cacheService.clearFieldsMap();
		return dto;
	}

	public void createFieldDifference(DocumentoPlantillaCaracteristicaDTO iCampo, String templateDifferenceId)
			throws ServerException {

		// Primero valido que no exista el campo
		DocumentoPlantillaCaracteristicaFilterDTO filterField = new DocumentoPlantillaCaracteristicaFilterDTO();
		filterField.setCodigo(iCampo.getCodigo());
		filterField.setNodoSuperior(templateDifferenceId);
		DocumentoPlantillaCaracteristicaDTO newCampo = consultaUnica(filterField);
		if (newCampo == null) {

			newCampo = new DocumentoPlantillaCaracteristicaDTO();
			newCampo.setCodigo(iCampo.getCodigo());
			if (iCampo.getTipo().compareTo(DocumentoPlantillaCaracteristicaDTO.INFORMATIVO) == 0) {
				newCampo.setTipo(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			} else {
				newCampo.setTipo(iCampo.getTipo());
			}
			newCampo.setImagen(iCampo.getImagen());
			newCampo.setNombre(iCampo.getNombre());
			// newCampo.setObjetivo(".");
			newCampo.setPrioridad(iCampo.getPrioridad() + 1);
			newCampo.setNodoSuperior(templateDifferenceId);
			newCampo = guardar(newCampo);

			// Esto lo quite por el tipo fecha pero no se exactamente porque lo tenia
			// parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
			// newCampo.getLlaveTabla(),
			// Propiedades.PERMISO_CAMPO_BLOQUEAR, "1", token), token);
			parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
					newCampo.getLlaveTabla(), Propiedades.PERMISO_CAMPO_OPCIONAL, "1"));

		}
		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, iCampo.getLlaveTabla(),
				Propiedades.CAMPO_DIFERENCIAS, newCampo.getLlaveTabla()));

	}

	public DocumentoPlantillaCaracteristicaDTO consultaUnicaConComplementos(String id) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campo = consultaXId(id);
		return cargarComplementos(campo);
	}

	public DocumentoPlantillaCaracteristicaDTO cargarComplementos(DocumentoPlantillaCaracteristicaDTO campo)
			throws ServerException {
		String usuario = SessionContext.getCurrentUserOrNull();
		if (campo != null)
			campo.setPropiedades(propertyManagerService.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO,
					campo.getLlaveTabla(), null, usuario));
		return campo;
	}

	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantilla(String plantilla) throws ServerException {
		if (plantilla == null)
			throw new ServerException("Para consultar los datos de una plantilla debes enviar el id de la plantilla");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCampo = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCampo.setEstado(SharedConstants.STATE_ACTIVE);
		filtroCampo.setNodoSuperior(plantilla);
		// Aumentar a 500 la cantidad de campos de un formulario, de preguntas
		filtroCampo.setPaginacionRegistroFinal(500);
		return listarConsulta(filtroCampo);
	}

	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantillaConComplementos(String plantilla,
			boolean external) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> campos = listarCamposPlantilla(plantilla);
		for (DocumentoPlantillaCaracteristicaDTO iCampo : campos) {
			iCampo = cargarComplementos(iCampo);
			if (external)
				Propiedades.clearPropertiesToOut(iCampo.getPropiedades());
		}
		return campos;
	}

	private void organizar(DocumentoPlantillaCaracteristicaDTO dto) throws ServerException {
		// Consulto todas las caracteristicas del documento
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setNodoSuperior(dto.getNodoSuperior());
		filtro.setPaginacionRegistroFinal(500);
		List<DocumentoPlantillaCaracteristicaDTO> campos = listarConsulta(filtro);
		if (campos != null && !campos.isEmpty()) {
			int cont = 1;
			for (DocumentoPlantillaCaracteristicaDTO campo : campos) {
				if (campo.getLlaveTabla().compareTo(dto.getLlaveTabla()) != 0) {
					// asumo que hay dos iguales entonces debo saltar un espacio y el que modifique
					// lo dejo quieto
					if (campo.getPrioridad().compareTo(dto.getPrioridad()) == 0)
						cont++;
					if (campo.getPrioridad() != cont) {
						campo.setPrioridad(cont);
						super.actualizar(campo);
					}
					cont++;
				} else {
					if (cont == dto.getPrioridad())
						cont++;
				}
			}
		}
		// Debo validar que las dependencias si se puedan
	}

	public void actualizarFiltros(String llaveTabla) {
		documentoPlantillaCaracteristicaMapper.actualizarFiltros(llaveTabla);
	}

	public void actualizarDescripcion(String pTemplate, String pField) {
		documentoPlantillaCaracteristicaMapper.actualizarDescripcion(pTemplate, pField);
	}

	public String crearCampoNombre(String plantilla) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("NOMBRE");
		filtro.setNodoSuperior(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre = new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("NOMBRE");
			campoNombre.setNombre("NOMBRE");
			campoNombre.setTipo(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoNombre.setPrioridad(1);
			campoNombre.setNodoSuperior(plantilla);
			campoNombre = guardar(campoNombre);
		}
		return campoNombre.getLlaveTabla();
	}

	public String createField(String template, String fieldCode, String type, Integer order) throws ServerException {
		DocumentoPlantillaCaracteristicaFilterDTO filter = new DocumentoPlantillaCaracteristicaFilterDTO();
		filter.setCodigo(fieldCode);
		filter.setNodoSuperior(template);
		DocumentoPlantillaCaracteristicaDTO field = consultaUnica(filter);
		if (field == null) {
			field = new DocumentoPlantillaCaracteristicaDTO();
			field.setCodigo(fieldCode);
			field.setNombre(fieldCode);
			field.setTipo(type);
			field.setNodoSuperior(template);
			field.setPrioridad(order);
			field = guardar(field);
		}
		return field.getLlaveTabla();
	}

	public String crearCampoIdentificacion(String plantilla) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("ID");
		filtro.setNodoSuperior(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre = new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("ID");
			campoNombre.setNombre("ID");
			campoNombre.setTipo(DocumentoPlantillaCaracteristicaDTO.NUMERO);
			campoNombre.setPrioridad(2);
			campoNombre.setNodoSuperior(plantilla);
			campoNombre = guardar(campoNombre);
		}
		return campoNombre.getLlaveTabla();
	}

	public String crearCampoTelefono(String plantilla) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("TELEFONO");
		filtro.setNodoSuperior(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoTelefono = consultaUnica(filtro);
		if (campoTelefono == null) {
			campoTelefono = new DocumentoPlantillaCaracteristicaDTO();
			campoTelefono.setCodigo("TELEFONO");
			campoTelefono.setNombre("TELEFONO");
			campoTelefono.setTipo(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoTelefono.setPrioridad(4);
			campoTelefono.setNodoSuperior(plantilla);
			campoTelefono = guardar(campoTelefono);
			// Como no se tuvo en cuenta la categoria entonces toca colocar este
			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
					campoTelefono.getLlaveTabla(), Propiedades.FORMATO, "T");
			prop.setPropiedadValor("PROP_75");
			parametroService.guardar(prop);
		}
		return campoTelefono.getLlaveTabla();
	}

	public String crearCampoCorreo(String plantilla) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("EMAIL");
		filtro.setNodoSuperior(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoCorreo = consultaUnica(filtro);
		if (campoCorreo == null) {
			campoCorreo = new DocumentoPlantillaCaracteristicaDTO();
			campoCorreo.setCodigo("EMAIL");
			campoCorreo.setNombre("EMAIL");
			campoCorreo.setTipo(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoCorreo.setPrioridad(3);
			campoCorreo.setNodoSuperior(plantilla);
			campoCorreo = guardar(campoCorreo);
			// Como no se tuvo en cuenta la categoria entonces toca colocar este
			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoCorreo.getLlaveTabla(),
					Propiedades.FORMATO, "E");
			prop.setPropiedadValor("PROP_75");
			parametroService.guardar(prop);
		}
		return campoCorreo.getLlaveTabla();
	}

	public String crearCampoMotivo(String plantilla) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("MOTIVO");
		filtro.setNodoSuperior(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre = new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("MOTIVO");
			campoNombre.setNombre("MOTIVO");
			campoNombre.setTipo(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoNombre.setPrioridad(2);
			campoNombre.setNodoSuperior(plantilla);
			;
			campoNombre = guardar(campoNombre);

			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoNombre.getLlaveTabla(),
					Propiedades.FORMATO, "E");
			prop.setPropiedadValor("PROP_01");
			parametroService.guardar(prop);
		}
		return campoNombre.getLlaveTabla();
	}

	public String crearCampoTiempoReporte(String plantilla, boolean rango) throws ServerException {
		DocumentoPlantillaCaracteristicaFilterDTO campoTiempoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
		campoTiempoFilter.setCodigo("FECHA");
		campoTiempoFilter.setNodoSuperior(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoTiempo = consultaUnica(campoTiempoFilter);
		if (campoTiempo != null)
			return campoTiempo.getLlaveTabla();

		campoTiempo = new DocumentoPlantillaCaracteristicaDTO();
		campoTiempo.setCodigo("FECHA");
		campoTiempo.setNombre((rango) ? "RANGO DE FECHAS" : "FECHA");
		campoTiempo.setTipo(DocumentoPlantillaCaracteristicaDTO.FECHA);
		campoTiempo.setPrioridad(1);
		campoTiempo.setNodoSuperior(plantilla);
		// campoTiempo.setObjetivo("Contiene las fechas del reporte");
		campoTiempo = guardar(campoTiempo);

		if (rango) {
			parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
					campoTiempo.getLlaveTabla(), Propiedades.FECHA_RANGO, "*"));
			// Esto lo quite porque al final para el usuario es mejor el rango que los
			// campos separados
		}

		return campoTiempo.getLlaveTabla();
	}

	public String crearCampoProcesos(String plantilla) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campoProceso = new DocumentoPlantillaCaracteristicaDTO();
		campoProceso.setCodigo("PROCESO");
		campoProceso.setNombre("PROCESO");
		campoProceso.setTipo(DocumentoPlantillaCaracteristicaDTO.PROCESO);
		campoProceso.setPrioridad(1);
		campoProceso.setNodoSuperior(plantilla);
		campoProceso = guardar(campoProceso);

		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				campoProceso.getLlaveTabla(), Propiedades.PROCESO_GESTIONAR_ESTADOS, "*"));

		return campoProceso.getLlaveTabla();

	}

	public String crearCampoValor(String plantilla) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campoValor = new DocumentoPlantillaCaracteristicaDTO();
		campoValor.setCodigo("VALOR");
		campoValor.setNombre("VALOR");
		campoValor.setTipo(DocumentoPlantillaCaracteristicaDTO.NUMERO);
		campoValor.setNodoSuperior(plantilla);
		// campoValor.setObjetivo("Define el valor total del documento");
		campoValor = guardar(campoValor);
		return campoValor.getLlaveTabla();
	}

	public DocumentoPlantillaCaracteristicaDTO consultaXIdProducto(String llave) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO newP = null;
		if (!llave.startsWith("***")) {
			// newP = consultaUnicaProducto(llave);
			newP = consultaXId(llave);
		} else {
			newP = new DocumentoPlantillaCaracteristicaDTO();
			newP.setCodigo(llave.replace("***", ""));
			if (llave.compareTo("***PRODUCTO") == 0) {
				newP.setTipo("Z");
			} else {
				newP.setTipo("N");
			}
		}
		return newP;
	}

	public List<DocumentoPlantillaCaracteristicaDTO> getFullToSynchronize(List<String> process) {
		return documentoPlantillaCaracteristicaMapper.getFullToSynchronize(process);
	}

	public int countFieldsVinculo(String pTemplate) throws ServerException {
		if (pTemplate == null)
			throw new ServerException("Para consultar los datos de una plantilla debes enviar el id de la plantilla");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCampo = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCampo.setEstado(SharedConstants.STATE_ACTIVE);
		filtroCampo.setNodoSuperior(pTemplate);
		filtroCampo.setTipo(DocumentoPlantillaCaracteristicaDTO.VINCULO);
		return super.contarResultados(filtroCampo);
	}

	public int countFieldsDependent(String pTemplate, String pField) {
		DocumentoPlantillaCaracteristicaFilterDTO filtroCampo = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCampo.setLlaveTabla(pField);
		filtroCampo.setNodoSuperior(pTemplate);
		return documentoPlantillaCaracteristicaMapper.countDependentsOfField(filtroCampo);
	}

}