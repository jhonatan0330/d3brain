package com.softure.process_form.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.CacheManager;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.services.SoftureUtil;
// END region interImport
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.infrastructure.DocumentoPlantillaCaracteristicaMapper;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import jakarta.annotation.PostConstruct;

@Service("documentoPlantillaCaracteristicaService")
public class DocumentoPlantillaCaracteristicaSvc
		extends BasicSvc<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO> {

	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaMapper documentoPlantillaCaracteristicaMapper;
	@Autowired
	@Lazy
	private PropiedadSvc parametroService;
	@Autowired
	@Lazy
	private PropertyGetWithCacheService propertyManagerService;
	@Autowired
	@Lazy
	private CallSearchProcessFromText searchProcessFromText;
	@Autowired
	@Lazy
	private CacheManager cacheService;
	
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
		cacheService.putField(llave, _db);
		return _db;
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = documentoPlantillaCaracteristicaMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO actualizar(DocumentoPlantillaCaracteristicaDTO dto, String token)
			throws ServerException {
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.actualizar(dto, token);
		organizar(dto, token);
		parametroService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		PropiedadDTO differenceProperty = parametroService.getPropertyDifferenceField(dto.getLlaveTabla());
		if (differenceProperty == null) {
			PropiedadDTO filtroPlantilla = parametroService.getPropertyDifferenceTemplate(dto.getPlantilla());
			if (filtroPlantilla != null)
				createFieldDifference(dto, filtroPlantilla.getValor(), token);
		} else {
			DocumentoPlantillaCaracteristicaDTO fieldDifference = consultaXId(differenceProperty.getValor());
			if (fieldDifference.getFormato().compareTo(dto.getFormato()) != 0) {
				fieldDifference.setFormato(dto.getFormato());
				update(fieldDifference);
			}
		}
		cacheService.clearFieldsMap();
		return dto;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO inactivar(DocumentoPlantillaCaracteristicaDTO dto, String token)
			throws ServerException {
		dto = super.inactivar(dto, token);
		organizar(dto, token);
		// validar que el campo no se use en ninguna propiedad
		cacheService.clearFieldsMap();
		return dto;
	}

	// Esto lo uso en APiCommon y la idea es que se mejore en las cargas masivas
	// para que solo consulte de a uno y si es null que gestione los errores por
	// fuera
	public DocumentoPlantillaCaracteristicaDTO listarCarga(DocumentoPlantillaCaracteristicaFilterDTO dto)
			throws ServerException {
		// BEGIN region listarCarga
		if (dto == null || dto.getLlaveTabla() == null)
			throw new ServerException("Desarrollador el DTO viene nulo");
		if (dto.getDocumentos() == null || dto.getDocumentos().isEmpty())
			throw new ServerException("Desarrollador los documentos vienen vacios");
		// if(dtoCarga.getDocumentos()==null || dtoCarga.getDocumentos().isEmpty())
		// throw new ServerException("En el campo documentos debes incluir los
		// documentos a validar, en este caso estan vacios");
		DocumentoPlantillaCaracteristicaDTO dtoCarga = cargarComplementos(consultaXId(dto.getLlaveTabla()),
				dto.getSecurityToken());
		List<PedidoVentaDTO> documentAproval = new ArrayList<>();
		for (PedidoVentaDTO iDoc : dto.getDocumentos()) {
			PedidoVentaDTO addItem = new PedidoVentaDTO();
			try {
				String keyOfDocument = searchProcessFromText.getValueOptionFromText(dto.getSecurityToken(),
						iDoc.getNombre(), dtoCarga);
				addItem.setLlaveTabla(keyOfDocument);
			} catch (Exception e) {
				CallDocumentCommons.addMessageError(addItem, e.getMessage());
			}
			addItem.setNombre(iDoc.getNombre());
			documentAproval.add(addItem);
		}
		dtoCarga.setDocumentos(documentAproval);
		return dtoCarga;
		// END region listarCarga
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO guardar(DocumentoPlantillaCaracteristicaDTO dto, String token)
			throws ServerException {
		if (dto.getPlantilla() == null)
			throw new ServerException("Es necesario la plantilla a la que pertenece el campo");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCantidad = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCantidad.setPlantilla(dto.getPlantilla());
		int cantidadCampos = contarResultados(filtroCantidad);
		if (dto.getOrden() != null && dto.getOrden().compareTo(0) != 0) {
			// 2024-3 retire cantidadCampos == 0 &&
			// Porque al duplicar algunas no aparecian correctamente quinber guias
			// Esto es porque cuando se crean las plantillas automaticas no cuenta
			// correctametne la cantidad de campos ?? cuales no se si lo dane
			cantidadCampos = dto.getOrden();
		} else {
			cantidadCampos = cantidadCampos + 1;
		}
		if (dto.getCodigo() == null)
			dto.setCodigo(SoftureUtil.formatFunction(dto.getNombre()).toUpperCase());
		dto.setNombre(dto.getNombre().toUpperCase());
		dto.setOrden(cantidadCampos);
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.guardar(dto, token);
		PropiedadDTO filtroPlantilla = parametroService.getPropertyDifferenceTemplate(dto.getPlantilla());
		if (filtroPlantilla != null)
			createFieldDifference(dto, filtroPlantilla.getValor(), token);
		cacheService.clearFieldsMap();
		return dto;
	}

	public void createFieldDifference(DocumentoPlantillaCaracteristicaDTO iCampo, String templateDifferenceId,
			String token) throws ServerException {

		// Primero valido que no exista el campo
		DocumentoPlantillaCaracteristicaFilterDTO filterField = new DocumentoPlantillaCaracteristicaFilterDTO();
		filterField.setCodigo(iCampo.getCodigo());
		filterField.setPlantilla(templateDifferenceId);
		DocumentoPlantillaCaracteristicaDTO newCampo = consultaUnica(filterField);
		if (newCampo == null) {

			newCampo = new DocumentoPlantillaCaracteristicaDTO();
			newCampo.setCodigo(iCampo.getCodigo());
			if (iCampo.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.INFORMATIVO) == 0) {
				newCampo.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			} else {
				newCampo.setFormato(iCampo.getFormato());
			}
			newCampo.setImagen(iCampo.getImagen());
			newCampo.setNombre(iCampo.getNombre());
			// newCampo.setObjetivo(".");
			newCampo.setOrden(iCampo.getOrden() + 1);
			newCampo.setPlantilla(templateDifferenceId);
			newCampo = guardar(newCampo, token);

			// Esto lo quite por el tipo fecha pero no se exactamente porque lo tenia
			// parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
			// newCampo.getLlaveTabla(),
			// Propiedades.PERMISO_CAMPO_BLOQUEAR, "1", token), token);
			parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
					newCampo.getLlaveTabla(), Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		}
		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, iCampo.getLlaveTabla(),
				Propiedades.CAMPO_DIFERENCIAS, newCampo.getLlaveTabla(), token), token);

	}

	public DocumentoPlantillaCaracteristicaDTO consultaUnicaConComplementos(String id, String token)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campo = consultaXId(id);
		// if (campo == null) {// Seguramente viende de un producto
		// campo = consultaUnicaProducto(id);
		// }
		return cargarComplementos(campo, token);
	}

	public DocumentoPlantillaCaracteristicaDTO cargarComplementos(DocumentoPlantillaCaracteristicaDTO campo,
			String token) throws ServerException {
		String usuario = null;
		if (token != null)
			usuario = getUserFlex(token);
		if (campo != null)
			campo.setPropiedades(propertyManagerService.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO, campo.getLlaveTabla(),
					null, usuario));
		return campo;
	}

	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantilla(String plantilla, String token)
			throws ServerException {
		if (plantilla == null)
			throw new ServerException("Para consultar los datos de una plantilla debes enviar el id de la plantilla");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCampo = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCampo.setEstado(SharedConstants.STATE_ACTIVE);
		filtroCampo.setSecurityToken(token);
		filtroCampo.setPlantilla(plantilla);
		// Aumentar a 500 la cantidad de campos de un formulario, de preguntas
		filtroCampo.setPaginacionRegistroFinal(500);
		return listarConsulta(filtroCampo);
	}

	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantillaConComplementos(String plantilla,
			String token, boolean external) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> campos = listarCamposPlantilla(plantilla, token);
		for (DocumentoPlantillaCaracteristicaDTO iCampo : campos) {
			iCampo = cargarComplementos(iCampo, token);
			if (external)
				Propiedades.clearPropertiesToOut(iCampo.getPropiedades());
		}
		return campos;
	}

	private void organizar(DocumentoPlantillaCaracteristicaDTO dto, String token) throws ServerException {
		// Consulto todas las caracteristicas del documento
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setPlantilla(dto.getPlantilla());
		filtro.setPaginacionRegistroFinal(500);
		List<DocumentoPlantillaCaracteristicaDTO> campos = listarConsulta(filtro);
		if (campos != null && !campos.isEmpty()) {
			int cont = 1;
			for (DocumentoPlantillaCaracteristicaDTO campo : campos) {
				if (campo.getLlaveTabla().compareTo(dto.getLlaveTabla()) != 0) {
					// asumo que hay dos iguales entonces debo saltar un espacio y el que modifique
					// lo dejo quieto
					if (campo.getOrden().compareTo(dto.getOrden()) == 0)
						cont++;
					if (campo.getOrden() != cont) {
						campo.setOrden(cont);
						super.actualizar(campo, token);
					}
					cont++;
				} else {
					if (cont == dto.getOrden())
						cont++;
				}
			}
		}
		// Debo validar que las dependencias si se puedan
	}

	public void actualizarFiltros(String llaveTabla) throws ServerException {
		documentoPlantillaCaracteristicaMapper.actualizarFiltros(llaveTabla);
	}

	public void actualizarDescripcion(String pTemplate, String pField) throws ServerException {
		documentoPlantillaCaracteristicaMapper.actualizarDescripcion(pTemplate, pField);
	}

	public String crearCampoNombre(String plantilla, String token) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("NOMBRE");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre = new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("NOMBRE");
			campoNombre.setNombre("NOMBRE");
			campoNombre.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoNombre.setOrden(1);
			campoNombre.setPlantilla(plantilla);
			campoNombre = guardar(campoNombre, token);
		}
		return campoNombre.getLlaveTabla();
	}

	public String createField(String template, String fieldCode, String type, Integer order, String token)
			throws ServerException {
		DocumentoPlantillaCaracteristicaFilterDTO filter = new DocumentoPlantillaCaracteristicaFilterDTO();
		filter.setCodigo(fieldCode);
		filter.setPlantilla(template);
		DocumentoPlantillaCaracteristicaDTO field = consultaUnica(filter);
		if (field == null) {
			field = new DocumentoPlantillaCaracteristicaDTO();
			field.setCodigo(fieldCode);
			field.setNombre(fieldCode);
			field.setFormato(type);
			field.setPlantilla(template);
			field.setOrden(order);
			field = guardar(field, token);
		}
		return field.getLlaveTabla();
	}

	public String crearCampoIdentificacion(String plantilla, String token) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("ID");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre = new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("ID");
			campoNombre.setNombre("ID");
			campoNombre.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
			campoNombre.setOrden(2);
			campoNombre.setPlantilla(plantilla);
			campoNombre = guardar(campoNombre, token);
		}
		return campoNombre.getLlaveTabla();
	}

	public String crearCampoTelefono(String plantilla, String token) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("TELEFONO");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoTelefono = consultaUnica(filtro);
		if (campoTelefono == null) {
			campoTelefono = new DocumentoPlantillaCaracteristicaDTO();
			campoTelefono.setCodigo("TELEFONO");
			campoTelefono.setNombre("TELEFONO");
			campoTelefono.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoTelefono.setOrden(4);
			campoTelefono.setPlantilla(plantilla);
			campoTelefono = guardar(campoTelefono, token);
			// Como no se tuvo en cuenta la categoria entonces toca colocar este
			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
					campoTelefono.getLlaveTabla(), Propiedades.FORMATO, "T", token);
			prop.setPropiedadValor("PROP_75");
			parametroService.guardar(prop, token);
		}
		return campoTelefono.getLlaveTabla();
	}

	public String crearCampoCorreo(String plantilla, String token) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("EMAIL");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoCorreo = consultaUnica(filtro);
		if (campoCorreo == null) {
			campoCorreo = new DocumentoPlantillaCaracteristicaDTO();
			campoCorreo.setCodigo("EMAIL");
			campoCorreo.setNombre("EMAIL");
			campoCorreo.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoCorreo.setOrden(3);
			campoCorreo.setPlantilla(plantilla);
			campoCorreo = guardar(campoCorreo, token);
			// Como no se tuvo en cuenta la categoria entonces toca colocar este
			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoCorreo.getLlaveTabla(),
					Propiedades.FORMATO, "E", token);
			prop.setPropiedadValor("PROP_75");
			parametroService.guardar(prop, token);
		}
		return campoCorreo.getLlaveTabla();
	}

	public String crearCampoMotivo(String plantilla, String token) throws ServerException {
		// Primero filtro si existe el campo nombre, eso evita un error al copiar
		// plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("MOTIVO");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre = new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("MOTIVO");
			campoNombre.setNombre("MOTIVO");
			campoNombre.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoNombre.setOrden(2);
			campoNombre.setPlantilla(plantilla);
			;
			campoNombre = guardar(campoNombre, token);

			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoNombre.getLlaveTabla(),
					Propiedades.FORMATO, "E", token);
			prop.setPropiedadValor("PROP_01");
			parametroService.guardar(prop, token);
		}
		return campoNombre.getLlaveTabla();
	}

	public String crearCampoTiempoReporte(String plantilla, String token, boolean rango) throws ServerException {
		DocumentoPlantillaCaracteristicaFilterDTO campoTiempoFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
		campoTiempoFilter.setCodigo("FECHA");
		campoTiempoFilter.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoTiempo = consultaUnica(campoTiempoFilter);
		if (campoTiempo != null)
			return campoTiempo.getLlaveTabla();

		campoTiempo = new DocumentoPlantillaCaracteristicaDTO();
		campoTiempo.setCodigo("FECHA");
		campoTiempo.setNombre((rango) ? "RANGO DE FECHAS" : "FECHA");
		campoTiempo.setFormato(DocumentoPlantillaCaracteristicaDTO.FECHA);
		campoTiempo.setOrden(1);
		campoTiempo.setPlantilla(plantilla);
		// campoTiempo.setObjetivo("Contiene las fechas del reporte");
		campoTiempo = guardar(campoTiempo, token);

		if (rango) {
			parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
					campoTiempo.getLlaveTabla(), Propiedades.FECHA_RANGO, "*", token), token);
			// Esto lo quite porque al final para el usuario es mejor el rango que los
			// campos separados
		}

		return campoTiempo.getLlaveTabla();
	}

	public String crearCampoProcesos(String plantilla, String token) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campoProceso = new DocumentoPlantillaCaracteristicaDTO();
		campoProceso.setCodigo("PROCESO");
		campoProceso.setNombre("PROCESO");
		campoProceso.setFormato(DocumentoPlantillaCaracteristicaDTO.PROCESO);
		campoProceso.setOrden(1);
		campoProceso.setPlantilla(plantilla);
		campoProceso = guardar(campoProceso, token);

		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO,
				campoProceso.getLlaveTabla(), Propiedades.PROCESO_GESTIONAR_ESTADOS, "*", token), token);

		return campoProceso.getLlaveTabla();

	}

	public String crearCampoValor(String plantilla, String token) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campoValor = new DocumentoPlantillaCaracteristicaDTO();
		campoValor.setCodigo("VALOR");
		campoValor.setNombre("VALOR");
		campoValor.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
		campoValor.setPlantilla(plantilla);
		// campoValor.setObjetivo("Define el valor total del documento");
		campoValor = guardar(campoValor, token);
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
				newP.setFormato("Z");
			} else {
				newP.setFormato("N");
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
		filtroCampo.setPlantilla(pTemplate);
		filtroCampo.setFormato(DocumentoPlantillaCaracteristicaDTO.VINCULO);
		return super.contarResultados(filtroCampo);
	}
	
	public int countFieldsDependent(String pTemplate, String pField) throws ServerException {
		DocumentoPlantillaCaracteristicaFilterDTO filtroCampo = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCampo.setLlaveTabla(pField);
		filtroCampo.setPlantilla(pTemplate);
		return documentoPlantillaCaracteristicaMapper.countDependentsOfField(filtroCampo);
	}

}