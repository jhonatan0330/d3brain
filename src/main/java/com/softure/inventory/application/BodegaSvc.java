package com.softure.inventory.application;

import java.util.List;

import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.domain.BodegaDTO;
import com.softure.inventory.domain.BodegaFilterDTO;
import com.softure.inventory.infrastructure.BodegaMapper;
// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("bodegaService")
public class BodegaSvc extends BasicSvc<BodegaDTO, BodegaFilterDTO> {
	
	@Autowired
	private BodegaMapper bodegaMapper;
	
	// BEGIN region servicesBodega
	// END region servicesBodega

	@Override
	public BodegaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Bodega");
		BodegaFilterDTO dto = new BodegaFilterDTO();
		dto.setLlaveTabla(llave);
		return bodegaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = bodegaMapper;
	}
	
	@Override
	public BodegaDTO activar(BodegaDTO dto, String token) throws ServerException {
		// BEGIN Bodega_activar
		return super.activar(dto, token);
		// END Bodega_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public BodegaDTO actualizar( BodegaDTO dto, String token) throws ServerException {
		// BEGIN Bodega_actualizar
		return super.actualizar(dto, token);
		// END Bodega_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public BodegaDTO inactivar(BodegaDTO dto, String token) throws ServerException {
		// BEGIN Bodega_inactivar
		return super.inactivar(dto, token);
		// END Bodega_inactivar
	}
	
	@Override
	public BodegaDTO consultaUnica(BodegaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(BodegaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<BodegaDTO> listarConsulta(BodegaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public BodegaDTO guardar(BodegaDTO dto, String token) throws ServerException {
		// BEGIN Bodega_guardar
		return super.guardar(dto, token);
		// END Bodega_guardar
	}

// BEGIN region aditionalMethods
	public BodegaDTO crearDesdeDocumento(PedidoVentaDTO documento) throws ServerException{
		BodegaFilterDTO newBodegaFilter = new BodegaFilterDTO();
		newBodegaFilter.setDocumento(documento.getLlaveTabla());
		BodegaDTO newBodega = consultaUnica(newBodegaFilter);
		if (newBodega==null) {
			newBodega = new BodegaDTO();
			newBodega.setDocumento(documento.getLlaveTabla());
			newBodega = save(newBodega);
		}else {
			if(documento.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) {
				if(newBodega.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)!=0) {
					newBodega.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
					//newBodega.setSecurityToken(documento.getSecurityToken());
					newBodega = update(newBodega);
				}
			}else {
				if(newBodega.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) {
					newBodega.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
					//newBodega.setSecurityToken(documento.getSecurityToken());
					newBodega = update(newBodega);
				}
			}
		}
		return newBodega;
	}
	
// END region aditionalMethods

}