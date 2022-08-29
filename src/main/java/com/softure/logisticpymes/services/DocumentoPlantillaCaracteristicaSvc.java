package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.ProductoCaracteristicaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
import com.softure.java.services.SoftureUtil;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.logisticpymes.persistence.DocumentoPlantillaCaracteristicaMapper;

@Service("documentoPlantillaCaracteristicaService")
public class DocumentoPlantillaCaracteristicaSvc extends BasicSvc<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO> {
	
	@Autowired
	private DocumentoPlantillaCaracteristicaMapper documentoPlantillaCaracteristicaMapper;
	
	// BEGIN region servicesDocumentoPlantillaCaracteristica
	@Autowired private PropiedadSvc parametroService;
	@Autowired private PedidoVentaSvc documentoService;
	@Autowired private ProductoCaracteristicaSvc campoProductoService;
	// END region servicesDocumentoPlantillaCaracteristica

	@Override
	public DocumentoPlantillaCaracteristicaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DocumentoPlantillaCaracteristica");
		DocumentoPlantillaCaracteristicaFilterDTO dto = new DocumentoPlantillaCaracteristicaFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoPlantillaCaracteristicaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = documentoPlantillaCaracteristicaMapper;
	}
	
	@Override
	public DocumentoPlantillaCaracteristicaDTO activar(DocumentoPlantillaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantillaCaracteristica_activar
		return super.activar(dto, token);
		// END DocumentoPlantillaCaracteristica_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO actualizar( DocumentoPlantillaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantillaCaracteristica_actualizar
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.actualizar(dto, token);
		organizar(dto, token);
		PropiedadDTO filtro = new PropiedadDTO();
		filtro.setValor(dto.getLlaveTabla());
		filtro.setTexto(dto.getNombre());
		parametroService.actualizarValorPropiedad(filtro);
		return dto;
		// END DocumentoPlantillaCaracteristica_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO inactivar(DocumentoPlantillaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantillaCaracteristica_inactivar
		dto = super.inactivar(dto, token);
		organizar(dto, token);
		return dto;
		// END DocumentoPlantillaCaracteristica_inactivar
	}
	
	@Override
	public DocumentoPlantillaCaracteristicaDTO consultaUnica(DocumentoPlantillaCaracteristicaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DocumentoPlantillaCaracteristicaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DocumentoPlantillaCaracteristicaDTO> listarConsulta(DocumentoPlantillaCaracteristicaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public DocumentoPlantillaCaracteristicaDTO listarCarga(DocumentoPlantillaCaracteristicaFilterDTO dto)throws ServerException{
		// BEGIN region listarCarga
		if(dto == null || dto.getLlaveTabla()==null) throw new ServerException("Desarrollador el DTO viene nulo");
		if(dto.getDocumentos()==null || dto.getDocumentos().isEmpty()) throw new ServerException("Desarrollador los documentos vienen vacios");
		DocumentoPlantillaCaracteristicaDTO dtoCarga = cargarComplementos( consultaXId(dto.getLlaveTabla()), dto.getSecurityToken());
		String plantilla = Propiedades.obtenerValor(dtoCarga, Propiedades.PLANTILLA_AUXILIAR);
		if(plantilla==null) throw new ServerException("Por el momento no se tiene desarrollado los campos sin plantilla");
		dtoCarga.setDocumentos(dto.getDocumentos());
		if(dtoCarga.getDocumentos()==null || dtoCarga.getDocumentos().isEmpty()) throw new ServerException("En el campo documentos debes incluir los documentos a validar, en este caso estan vacios");
		for (PedidoVentaDTO iDoc : dtoCarga.getDocumentos()) {
			PedidoVentaFilterDTO filtro = new PedidoVentaFilterDTO();
			filtro.setNombre(SoftureUtil.cleanStartEndSpaces(iDoc.getNombre()));
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			filtro.setPlantilla(plantilla);
			PedidoVentaDTO document = documentoService.consultaUnica(filtro);
			if(document == null) throw new ServerException("No se encuentra activo el documento con codigo : " + iDoc.getNombre());
			iDoc.setLlaveTabla(document.getLlaveTabla());
		}
		return dtoCarga;
		// END region listarCarga
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaCaracteristicaDTO guardar(DocumentoPlantillaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantillaCaracteristica_guardar
		if(dto.getPlantilla()==null) throw new ServerException("Es necesario la plantilla a la que pertenece el campo");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCantidad = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCantidad.setPlantilla(dto.getPlantilla());
		int cantidadCampos = contarResultados(filtroCantidad);
		if(cantidadCampos==0 && dto.getOrden().compareTo(0)!=0) {
			cantidadCampos = dto.getOrden();//Esto es porque cuando se crean las plantillas automaticas no cuenta correctametne la cantidad de campos
		}else {
			cantidadCampos = cantidadCampos+1;			
		}
		if(dto.getCodigo()==null) dto.setCodigo(SoftureUtil.formatFunction(dto.getNombre()).toUpperCase());
		dto.setOrden(cantidadCampos);
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.guardar(dto, token);
		return dto;
		// END DocumentoPlantillaCaracteristica_guardar
	}

// BEGIN region aditionalMethods

	private DocumentoPlantillaCaracteristicaDTO consultaUnicaProducto(String id) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campo = consultaXId(id);
		if(campo ==null) {//Seguramente viende de un producto
			ProductoCaracteristicaDTO campoProducto = campoProductoService.consultaXId(id);
			if(campoProducto==null) throw new ServerException("El id del campo no se encuentra en la BD.");
			campo = new DocumentoPlantillaCaracteristicaDTO();
			campo.setCodigo(campoProducto.getCodigo());
			campo.setEstado(campoProducto.getEstado());
			campo.setFormato(campoProducto.getFormato());
			campo.setImagen(campoProducto.getImagen());
			campo.setNombre(campoProducto.getNombre());
			campo.setOrden(campoProducto.getOrden());
			campo.setObjetivo(campoProducto.getObjetivo());
			campo.setPlantilla(campoProducto.getBase());
			campo.setPlantillaNombre(campoProducto.getBaseNombre());
			campo.setLlaveTabla(id);
		}
		return campo;
	}
	
	public DocumentoPlantillaCaracteristicaDTO consultaUnicaConComplementos(String id, String token) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campo = consultaXId(id);
		if(campo ==null) {//Seguramente viende de un producto
			campo = consultaUnicaProducto(id);
		}
		return cargarComplementos(campo, token);
	}
	
	public DocumentoPlantillaCaracteristicaDTO cargarComplementos(DocumentoPlantillaCaracteristicaDTO campo, String token)throws ServerException {
		String usuario = null;
		if(token !=null) usuario = getUserFlex(token);
		if(campo!=null)	campo.setPropiedades(parametroService.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO, campo.getLlaveTabla(), null, usuario));
		return campo;
	}
	
	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantilla(String plantilla, String token) throws ServerException {
		if(plantilla ==null) throw new ServerException("Para consultar los datos de una plantilla debes enviar el id de la plantilla");
		DocumentoPlantillaCaracteristicaFilterDTO filtroCampo = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtroCampo.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroCampo.setSecurityToken(token);
		filtroCampo.setPlantilla(plantilla);
		return listarConsulta(filtroCampo);
	}
	
	public List<DocumentoPlantillaCaracteristicaDTO> listarCamposPlantillaConComplementos(String plantilla, String token) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> campos = listarCamposPlantilla(plantilla, token);
		for (DocumentoPlantillaCaracteristicaDTO  iCampo: campos){
			iCampo = cargarComplementos(iCampo, token);
		}
		return campos;
	}
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	private void organizar(DocumentoPlantillaCaracteristicaDTO dto, String token) throws ServerException{
		//Consulto todas las caracteristicas del documento
		DocumentoPlantillaCaracteristicaFilterDTO filtro = new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setPlantilla(dto.getPlantilla());
		List<DocumentoPlantillaCaracteristicaDTO> campos = listarConsulta(filtro);
		if(campos!=null && !campos.isEmpty()){
			int cont = 1;
			for(DocumentoPlantillaCaracteristicaDTO campo : campos){
				if(campo.getLlaveTabla().compareTo(dto.getLlaveTabla())!=0){
					//asumo que hay dos iguales entonces debo saltar un espacio y el que modifique lo dejo quieto
					if(campo.getOrden().compareTo(dto.getOrden())==0) cont++;
					if(campo.getOrden()!= cont){
						campo.setOrden(cont);
						super.actualizar(campo, token);
					}
					cont++;
				}else{
					if(cont == dto.getOrden()) cont++;
				}
			}
		}
		//Debo validar que las dependencias si se puedan
	}
	
	public void actualizarFiltros(String llaveTabla) throws ServerException {
		documentoPlantillaCaracteristicaMapper.actualizarFiltros(llaveTabla);
	}
	
	public String crearCampoNombre(String plantilla, String token) throws ServerException {
		//Primero filtro si existe el campo nombre, eso evita un error al copiar plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro =new DocumentoPlantillaCaracteristicaFilterDTO();
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
			campoNombre.setObjetivo("Almacenar el nombre");
			campoNombre = guardar(campoNombre, token);
		}
		return campoNombre.getLlaveTabla();
	}
	
	public String crearCampoIdentificacion(String plantilla, String token) throws ServerException {
		//Primero filtro si existe el campo nombre, eso evita un error al copiar plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro =new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("ID");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoNombre = consultaUnica(filtro);
		if (campoNombre == null) {
			campoNombre =new DocumentoPlantillaCaracteristicaDTO();
			campoNombre.setCodigo("ID");
			campoNombre.setNombre("ID");
			campoNombre.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
			campoNombre.setOrden(2);
			campoNombre.setPlantilla(plantilla);
			campoNombre.setObjetivo("Almacenar el id");
			campoNombre = guardar(campoNombre, token);
		}
		return campoNombre.getLlaveTabla();
	}
	
	public String crearCampoTelefono(String plantilla, String token) throws ServerException {
		//Primero filtro si existe el campo nombre, eso evita un error al copiar plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro =new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("TELEFONO");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoTelefono = consultaUnica(filtro);
		if (campoTelefono == null) {
			campoTelefono =new DocumentoPlantillaCaracteristicaDTO();
			campoTelefono.setCodigo("TELEFONO");
			campoTelefono.setNombre("TELEFONO");
			campoTelefono.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoTelefono.setOrden(4);
			campoTelefono.setPlantilla(plantilla);
			campoTelefono.setObjetivo(".");
			campoTelefono = guardar(campoTelefono, token);
			// Como no se tuvo en cuenta la categoria entonces toca colocar este 
			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoTelefono.getLlaveTabla(), 
					Propiedades.FORMATO, "T", token);
			prop.setPropiedadValor("PROP_75");
			parametroService.guardar(prop, token);
		}
		return campoTelefono.getLlaveTabla();
	}
	
	public String crearCampoCorreo(String plantilla, String token) throws ServerException {
		//Primero filtro si existe el campo nombre, eso evita un error al copiar plantilla
		DocumentoPlantillaCaracteristicaFilterDTO filtro =new DocumentoPlantillaCaracteristicaFilterDTO();
		filtro.setCodigo("EMAIL");
		filtro.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoCorreo = consultaUnica(filtro);
		if (campoCorreo == null) {
			campoCorreo =new DocumentoPlantillaCaracteristicaDTO();
			campoCorreo.setCodigo("EMAIL");
			campoCorreo.setNombre("EMAIL");
			campoCorreo.setFormato(DocumentoPlantillaCaracteristicaDTO.TEXTO);
			campoCorreo.setOrden(3);
			campoCorreo.setPlantilla(plantilla);
			campoCorreo.setObjetivo(".");
			campoCorreo = guardar(campoCorreo, token);
			// Como no se tuvo en cuenta la categoria entonces toca colocar este 
			PropiedadDTO prop = Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoCorreo.getLlaveTabla(), 
					Propiedades.FORMATO, "E", token);
			prop.setPropiedadValor("PROP_75");
			parametroService.guardar(prop, token);
		}
		return campoCorreo.getLlaveTabla();
	}
	
	public String crearCampoTiempoReporte(String plantilla, String token, boolean rango) throws ServerException {
		DocumentoPlantillaCaracteristicaFilterDTO campoTiempoFilter =new DocumentoPlantillaCaracteristicaFilterDTO();
		campoTiempoFilter.setCodigo("FECHA");
		campoTiempoFilter.setPlantilla(plantilla);
		DocumentoPlantillaCaracteristicaDTO campoTiempo = consultaUnica(campoTiempoFilter);
		if(campoTiempo!=null) return campoTiempo.getLlaveTabla();
		
		campoTiempo =new DocumentoPlantillaCaracteristicaDTO();
		campoTiempo.setCodigo("FECHA");
		campoTiempo.setNombre("FECHA");
		campoTiempo.setFormato(DocumentoPlantillaCaracteristicaDTO.FECHA);
		campoTiempo.setOrden(1);
		campoTiempo.setPlantilla(plantilla);
		campoTiempo.setObjetivo("Contiene las fechas del reporte");
		campoTiempo = guardar(campoTiempo, token);
		
		if(rango) {
			parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoTiempo.getLlaveTabla(), 
					Propiedades.FECHA_RANGO, "*", token), token);
		}
		
		return campoTiempo.getLlaveTabla();
	}
	
	public String crearCampoProcesos(String plantilla, String token) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campoProceso =new DocumentoPlantillaCaracteristicaDTO();
		campoProceso.setCodigo("PROCESO");
		campoProceso.setNombre("PROCESO");
		campoProceso.setFormato(DocumentoPlantillaCaracteristicaDTO.PROCESO);
		campoProceso.setOrden(1);
		campoProceso.setPlantilla(plantilla);
		campoProceso.setObjetivo("Contiene el proceso que se va a gestionar");
		campoProceso = guardar(campoProceso, token);
		
		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoProceso.getLlaveTabla(), 
				Propiedades.PROCESO_GESTIONAR_ESTADOS, "*", token), token);

		return campoProceso.getLlaveTabla();

	}
	
	public String crearCampoValor(String plantilla, String token) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO campoValor =new DocumentoPlantillaCaracteristicaDTO();
		campoValor.setCodigo("VALOR");
		campoValor.setNombre("VALOR");
		campoValor.setFormato(DocumentoPlantillaCaracteristicaDTO.NUMERO);
		campoValor.setPlantilla(plantilla);
		campoValor.setObjetivo("Define el valor total del documento");
		campoValor = guardar(campoValor, token);
		
		parametroService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, campoValor.getLlaveTabla(), 
				Propiedades.NUMERO_MONEDA, Propiedades.TRUE, token), token);
		
		return campoValor.getLlaveTabla();
	}
	
	public DocumentoPlantillaCaracteristicaDTO consultaXIdProducto(String llave) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO newP = null;
		if(!llave.startsWith("***")) {
			newP = consultaUnicaProducto(llave);
		}else {
			newP = new DocumentoPlantillaCaracteristicaDTO();
			newP.setCodigo(llave.replace("***", ""));
			if(llave.compareTo("***PRODUCTO")==0) {
				newP.setFormato("Z");
			}else {
				newP.setFormato("N");	
			}
		}
		return newP;
	}
// END region aditionalMethods

}