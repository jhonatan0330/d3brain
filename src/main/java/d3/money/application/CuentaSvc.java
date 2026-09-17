package d3.money.application;

import java.math.BigDecimal;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.document.domain.PedidoVentaDTO;
import d3.money.domain.CuentaDTO;
import d3.money.domain.CuentaFilterDTO;
import d3.money.infrastructure.CuentaMapper;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import jakarta.annotation.PostConstruct;

@Service("cuentaService")
public class CuentaSvc extends BasicSvc<CuentaDTO, CuentaFilterDTO> {

	private final CuentaMapper cuentaMapper;

	public CuentaSvc(@Lazy CuentaMapper cuentaMapper) {
		this.cuentaMapper = cuentaMapper;
	}

	@Override
	public CuentaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Cuenta");
		CuentaFilterDTO dto = new CuentaFilterDTO();
		dto.setLlaveTabla(llave);
		return cuentaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = cuentaMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CuentaDTO actualizar(CuentaDTO dto) throws ServerException {
		CuentaDTO cuenta = consultaXId(dto.getLlaveTabla());
		if (cuenta.getFechaConciliacion() != null) {
			if (dto.getFechaConciliacion() == null)
				dto.setFechaConciliacion(cuenta.getFechaConciliacion());
			if (dto.getFechaConciliacion().before(cuenta.getFechaConciliacion()))
				throw new ServerException("La nueva fecha de conciliacion no puede ser menor a "
						+ cuenta.getFechaConciliacion().toString());
		}
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CuentaDTO inactivar(CuentaDTO dto) throws ServerException {
		return super.inactivar(dto);
	}

	public CuentaDTO crearCuenta(PedidoVentaDTO dto) throws ServerException {
		CuentaFilterDTO filter = new CuentaFilterDTO();
		filter.setDocumento(dto.getLlaveTabla());
		CuentaDTO cuentaNueva = consultaUnica(filter);
		if (cuentaNueva != null)
			return cuentaNueva;

		cuentaNueva = new CuentaDTO();
		cuentaNueva.setCodigo(dto.getNombre());
		cuentaNueva.setNombre(dto.getDescripcion());
		cuentaNueva.setDocumento(dto.getLlaveTabla());
		// if(propValue!=null) cuentaNueva.setValidarTurno(propValue.compareTo("0")!=0);
		return guardar(cuentaNueva);
	}

	public void inactivarDocumento(PedidoVentaDTO dto) throws ServerException {
		CuentaFilterDTO cuentaFilter = new CuentaFilterDTO();
		cuentaFilter.setDocumento(dto.getLlaveTabla());
		cuentaFilter.setEstado(SharedConstants.STATE_ACTIVE);
		CuentaDTO cuenta = consultaUnica(cuentaFilter);
		if (cuenta != null) {
			inactivar(cuenta);
		}
	}

	public BigDecimal sobregiro(String cuentaId) {
		return new BigDecimal(cuentaMapper.sobregiro(cuentaId));
	}

	public boolean turnomultiple(String cuentaId) {
		return cuentaMapper.turnomultiple(cuentaId);
	}

}