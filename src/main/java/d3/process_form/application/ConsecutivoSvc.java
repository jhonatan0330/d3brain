package d3.process_form.application;

import java.util.List;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.java.services.D3Utils;
import d3.logisticpymes.application.BasicSvc;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.process_form.domain.ConsecutivoDTO;
import d3.process_form.domain.ConsecutivoFilterDTO;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.process_form.infrastructure.ConsecutivoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("consecutivoService")
public class ConsecutivoSvc extends BasicSvc<ConsecutivoDTO, ConsecutivoFilterDTO> {

	private final ConsecutivoMapper consecutivoMapper;

	public ConsecutivoSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy ConsecutivoMapper consecutivoMapper,
			@Lazy DocumentoPlantillaSvc plantillaService) {
		super(usuarioSesionService);
		this.consecutivoMapper = consecutivoMapper;
		this.plantillaService = plantillaService;
	}

	private final DocumentoPlantillaSvc plantillaService;

	@Override
	public ConsecutivoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Consecutivo");
		ConsecutivoFilterDTO dto = new ConsecutivoFilterDTO();
		dto.setLlaveTabla(llave);
		return consecutivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = consecutivoMapper;
	}

	@Override
	public ConsecutivoDTO activar(ConsecutivoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ConsecutivoDTO actualizar(ConsecutivoDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ConsecutivoDTO inactivar(ConsecutivoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public ConsecutivoDTO consultaUnica(ConsecutivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(ConsecutivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<ConsecutivoDTO> listarConsulta(ConsecutivoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ConsecutivoDTO asignarConsecutivo(ConsecutivoDTO dto, String token) throws ServerException {
		if (dto.getLlaveTabla() == null)
			throw new ServerException("Para asignar el consecutivo se debe enviar la clave del consecutivo");
		ConsecutivoDTO consecutivoBD = consultaXId(dto.getLlaveTabla());
		if (consecutivoBD.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("Este consecutivo no se encuentra activo." + consecutivoBD.getNombre());
		if (consecutivoBD.getManual()) {
			if (dto.getNumeroActual().compareTo(BigDecimal.ZERO) == 0)
				throw new ServerException("El numero no puede ser cero");
			consecutivoBD.setNumeroActual(dto.getNumeroActual());
		} else {
			// Aumento en 1 el valor del actual
			consecutivoBD.setNumeroActual(consecutivoBD.getNumeroActual().add(BigDecimal.ONE));
			consecutivoBD = update(consecutivoBD);
		}
		if (consecutivoBD.getNumeroActual().compareTo(consecutivoBD.getNumeroInicial()) < 0)
			throw new ServerException(
					"El numero no puede ser menor a " + D3Utils.formatNumber(consecutivoBD.getNumeroInicial())
							+ "\nPor favor revisa el consecutivo :" + consecutivoBD.getNombre());
		if (consecutivoBD.getNumeroFinal().compareTo(BigDecimal.ZERO) != 0) {
			if (consecutivoBD.getNumeroActual().compareTo(consecutivoBD.getNumeroFinal()) > 0)
				throw new ServerException(
						"El numero no puede ser mayor a " + D3Utils.formatNumber(consecutivoBD.getNumeroFinal())
								+ "\nPor favor revisa el consecutivo " + consecutivoBD.getNombre());
		}
		// Armo el numero acual
		String cons = "";
		if (consecutivoBD.getPrefijo() != null)
			cons = cons + consecutivoBD.getPrefijo();
		if (consecutivoBD.getPadding() == null) {
			cons = cons + consecutivoBD.getNumeroActual().toBigInteger().toString();
		} else {
			if (!consecutivoBD.getPadding().contains("%"))
				throw new ServerException("El padding del consecutivo " + consecutivoBD.getNombre()
						+ " no es correcto sigue este ejemplo : %07d (rellena con ceros en 7 espacios)");
			cons = cons + consecutivoBD.getPadding().toLowerCase()
					.formatted(consecutivoBD.getNumeroActual().toBigInteger());
		}
		if (consecutivoBD.getSufijo() != null)
			cons = cons + consecutivoBD.getSufijo();
		consecutivoBD.setConsecutivoActual(cons);
		return consecutivoBD;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ConsecutivoDTO guardar(ConsecutivoDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}

	public void crear(DocumentoPlantillaDTO plantilla, String token) throws ServerException {
		// A veces el numero del consecutivo se repetia en ese caso toca evitar para las
		// automaticas que se cree error
		String prefix = plantilla.getCodigo();
		ConsecutivoFilterDTO filter = new ConsecutivoFilterDTO();
		filter.setPrefijo(prefix);
		List<ConsecutivoDTO> result = listarConsulta(filter);
		if (result != null && !result.isEmpty())
			prefix = "D" + prefix;
		ConsecutivoDTO nuevo = new ConsecutivoDTO();
		nuevo.setNombre(plantilla.getNombre());
		nuevo.setPrefijo(prefix);
		nuevo.setNumeroInicial(new BigDecimal(100));
		nuevo.setNumeroActual(new BigDecimal(100));
		// if(cantidad!=null)nuevo.setNumeroFinal(cantidad.add(augend));
		nuevo = guardar(nuevo, token);
		plantilla.setConsecutivo(nuevo.getLlaveTabla());
		plantillaService.update(plantilla);
	}

	public ConsecutivoDTO crear2Opcion(String consecutivo, String campo, String opcion, String token)
			throws ServerException {
		ConsecutivoDTO actual = consultaXId(consecutivo);
		if (actual == null)
			throw new ServerException("Revisa el id del consecutivo");
		if (actual.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("Consecutivo inactivo " + actual.getNombre());

		ConsecutivoDTO nuevo = new ConsecutivoDTO();
		nuevo.setNombre(actual.getNombre());
		String consecutivoDocumento = consecutivoMapper.obtenerPrefijo(opcion);
		if (consecutivoDocumento == null) {
			consecutivoDocumento = "";
		}
		if (actual.getPrefijo() != null)
			consecutivoDocumento = consecutivoDocumento + actual.getPrefijo();
		if (!consecutivoDocumento.isEmpty()) {
			consecutivoDocumento = consecutivoDocumento.replace("-", "");
			nuevo.setPrefijo(consecutivoDocumento);
			nuevo.setNombre(nuevo.getNombre() + consecutivoDocumento);
		}
		if (actual.getNumeroFinal().compareTo(BigDecimal.ZERO) == 0) {
			nuevo.setNumeroInicial(new BigDecimal(100));
			nuevo.setNumeroActual(new BigDecimal(100));
		} else {
			nuevo.setNumeroInicial(actual.getNumeroFinal().add(BigDecimal.ONE));
			nuevo.setNumeroActual(actual.getNumeroFinal());
			nuevo.setNumeroFinal(nuevo.getNumeroInicial().add(actual.getNumeroFinal()));
		}
		return guardar(nuevo, token);
	}

	public ConsecutivoDTO consultarConsecutivoManual() throws ServerException {
		ConsecutivoFilterDTO filtro = new ConsecutivoFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setManualFilter(true);
		List<ConsecutivoDTO> manuales = listarConsulta(filtro);
		if (manuales == null || manuales.isEmpty())
			throw new ServerException("No se tiene configurados consecutivos manuales activos para las personas");
		return manuales.get(0);
	}


}