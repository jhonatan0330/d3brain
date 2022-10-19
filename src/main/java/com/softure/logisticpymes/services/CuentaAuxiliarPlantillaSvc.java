package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
// END region interImport
import com.softure.logisticpymes.infrastructure.mybatis.mapper.CuentaAuxiliarPlantillaMapper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.CuentaAuxiliarPlantillaDTO;
import com.softure.logisticpymes.domain.dto.CuentaContableDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.filter.CuentaAuxiliarPlantillaFilterDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaFilterDTO;

@Service("cuentaAuxiliarPlantillaService")
public class CuentaAuxiliarPlantillaSvc extends BasicSvc<CuentaAuxiliarPlantillaDTO, CuentaAuxiliarPlantillaFilterDTO> {
	
	@Autowired
	private CuentaAuxiliarPlantillaMapper cuentaAuxiliarPlantillaMapper;
	
	// BEGIN region servicesCuentaAuxiliarPlantilla
	@Autowired private PedidoVentaSvc documentoService;
	@Autowired private CuentaContableSvc cuentaService;
	// END region servicesCuentaAuxiliarPlantilla

	@Override
	public CuentaAuxiliarPlantillaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CuentaAuxiliarPlantilla");
		CuentaAuxiliarPlantillaFilterDTO dto = new CuentaAuxiliarPlantillaFilterDTO();
		dto.setLlaveTabla(llave);
		return cuentaAuxiliarPlantillaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cuentaAuxiliarPlantillaMapper;
	}
	
	@Override
	public CuentaAuxiliarPlantillaDTO activar(CuentaAuxiliarPlantillaDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarPlantilla_activar
		return super.activar(dto, token);
		// END CuentaAuxiliarPlantilla_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaAuxiliarPlantillaDTO actualizar( CuentaAuxiliarPlantillaDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarPlantilla_actualizar
		throw new ServerException("No se puede actualizar");
		// END CuentaAuxiliarPlantilla_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaAuxiliarPlantillaDTO inactivar(CuentaAuxiliarPlantillaDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarPlantilla_inactivar
		return super.inactivar(dto, token);
		// END CuentaAuxiliarPlantilla_inactivar
	}
	
	@Override
	public CuentaAuxiliarPlantillaDTO consultaUnica(CuentaAuxiliarPlantillaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CuentaAuxiliarPlantillaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CuentaAuxiliarPlantillaDTO> listarConsulta(CuentaAuxiliarPlantillaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaAuxiliarPlantillaDTO guardar(CuentaAuxiliarPlantillaDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarPlantilla_guardar
		dto =  super.guardar(dto, token);
		generarCuentaAuxiliar(dto, token);
		return dto;
		// END CuentaAuxiliarPlantilla_guardar
	}

// BEGIN region aditionalMethods
	private void generarCuentaAuxiliar(CuentaAuxiliarPlantillaDTO dto, String token)throws ServerException {
		PedidoVentaFilterDTO filtroAuxiliares = new PedidoVentaFilterDTO();
		filtroAuxiliares.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroAuxiliares.setPlantilla(dto.getPlantilla());
		List<PedidoVentaDTO> auxiliares = documentoService.listarConsulta(filtroAuxiliares);
		if(auxiliares==null || auxiliares.isEmpty()) return;
		CuentaContableDTO cuentaPrincipal = cuentaService.consultaXId(dto.getCuentaPrincipal());
		for (PedidoVentaDTO pedidoVentaDTO : auxiliares) {
			cuentaService.guardar(crearAuxiliar(cuentaPrincipal, pedidoVentaDTO), token );
		}
	}
	
	public void crearNueva(PedidoVentaDTO documento, String token) throws ServerException {
		CuentaAuxiliarPlantillaFilterDTO filtro = new CuentaAuxiliarPlantillaFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setPlantilla(documento.getPlantilla());
		List<CuentaAuxiliarPlantillaDTO> auxiliares = listarConsulta(filtro);
		if(auxiliares==null || auxiliares.isEmpty()) return;
		for (CuentaAuxiliarPlantillaDTO cuentaAuxiliarPlantillaDTO : auxiliares) {
			CuentaContableDTO cuentaPrincipal = cuentaService.consultaXId(cuentaAuxiliarPlantillaDTO.getCuentaPrincipal());
			cuentaService.guardar(crearAuxiliar(cuentaPrincipal, documento), token);
		}
	}
	
	private CuentaContableDTO crearAuxiliar(CuentaContableDTO cuentaPrincipal, PedidoVentaDTO documento) {
		CuentaContableDTO cuentaAuxiliar = new CuentaContableDTO();
		cuentaAuxiliar.setCatalogo(cuentaPrincipal.getCatalogo());
		cuentaAuxiliar.setCodigo(cuentaPrincipal.getCodigo() + documento.getConsecutivo().intValue());
		cuentaAuxiliar.setCuentaPadre(cuentaPrincipal.getLlaveTabla());
		cuentaAuxiliar.setNombre(documento.getDescripcion());
		return cuentaAuxiliar;
	}
// END region aditionalMethods

}