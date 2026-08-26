package d3.inventory.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.inventory.domain.ProductoDTO;
import d3.inventory.domain.ProductoFilterDTO;
import d3.inventory.infrastructure.ProductoMapper;
import d3.java.services.D3Utils;
import d3.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("productoService")
public class ProductoSvc extends BasicSvc<ProductoDTO, ProductoFilterDTO> {

	private final ProductoMapper productoMapper;

	public ProductoSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy ProductoMapper productoMapper) {
		super(usuarioSesionService);
		this.productoMapper = productoMapper;
	}

	@Override
	public ProductoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Producto");
		ProductoFilterDTO dto = new ProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return productoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = productoMapper;
	}

	@Override
	public ProductoDTO activar(ProductoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoDTO actualizar(ProductoDTO dto, String token) throws ServerException {
		throw new ServerException("La modificacion de productos se debe realizar por los fomularios");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoDTO inactivar(ProductoDTO dto, String token) throws ServerException {
		throw new ServerException("La inactivacion de productos se debe realizar por los fomularios");
	}

	@Override
	public ProductoDTO consultaUnica(ProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(ProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<ProductoDTO> listarConsulta(ProductoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProductoDTO guardar(ProductoDTO dto, String token) throws ServerException {
		throw new ServerException("La creacion de productos se debe realizar por los fomularios");
	}


	public List<ProductoDTO> listarProductoPlantillaResponsable(ProductoFilterDTO dto) throws ServerException {
		if (dto.getUsuarioRol() == null)
			throw new ServerException("Se necesita el usuariorol en la plantillaproducto");
		if (dto != null && dto.getFiltroParametro() != null && dto.getFiltroParametro().compareTo("*") == 0)
			dto.setFiltroParametro(null);
		return productoMapper.listarProductoPlantillaResponsable(dto);
	}

	public List<ProductoDTO> listarProductoFuncion(String funcion, String documento, String filtro, String token,
			List<PedidoVentaCaracteristicaDTO> parametros) throws ServerException {
		return productoMapper.listarProductoFuncion(funcion, documento, filtro, token, parametros);
	}

	public List<ProductoDTO> listarProductoCampo(String campo, String filtro) throws ServerException {
		if (filtro != null)
			filtro = D3Utils.formatFunction(filtro).toUpperCase();
		return productoMapper.listarProductoCampo(campo, filtro);
	}

	public List<ProductoDTO> listarProductoSimplificar(List<ProductoDTO> productos) throws ServerException {
		if (productos == null || productos.isEmpty())
			return new ArrayList<ProductoDTO>();
		return productoMapper.listarProductoSimplificado(productos);
	}

	public ProductoDTO getProduct2Document(String document) throws ServerException {
		ProductoFilterDTO p = new ProductoFilterDTO();
		p.setDocumento(document);
		p.setEstado(SharedConstants.STATE_ACTIVE);
		ProductoDTO pr = consultaUnica(p);
		if (pr != null && pr.getProductoBase() != null) {
			ProductoDTO pb = consultaXId(pr.getProductoBase());
			pr.setBaseNombre(pb.getNombre());
		}
		return pr;
	}

	public List<ProductoDTO> getProducts2Filter(String filter) throws ServerException {
		ProductoFilterDTO p = new ProductoFilterDTO();
		p.setFiltroParametro(filter);
		p.setEstado(SharedConstants.STATE_ACTIVE);
		return listarConsulta(p);
	}

	public ProductoDTO filtrarPorCodigo(String codigo) throws ServerException {
		return productoMapper.filtrarPorCodigo(codigo);
	}

}