package com.softure.process_form.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.process_form.domain.ConsecutivoDTO;
import com.softure.process_form.domain.ConsecutivoFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.infrastructure.ConsecutivoMapper;

@Service("consecutivoService")
public class ConsecutivoSvc extends BasicSvc<ConsecutivoDTO, ConsecutivoFilterDTO> {
	
	@Autowired
	private ConsecutivoMapper consecutivoMapper;
	
	// BEGIN region servicesConsecutivo
	@Autowired private DocumentoPlantillaSvc plantillaService;
	// END region servicesConsecutivo

	@Override
	public ConsecutivoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Consecutivo");
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
		// BEGIN Consecutivo_activar
		return super.activar(dto, token);
		// END Consecutivo_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ConsecutivoDTO actualizar( ConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN Consecutivo_actualizar
		return super.actualizar(dto, token);
		// END Consecutivo_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ConsecutivoDTO inactivar(ConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN Consecutivo_inactivar
		return super.inactivar(dto, token);
		// END Consecutivo_inactivar
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
	public List<ConsecutivoDTO> listarConsulta(ConsecutivoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ConsecutivoDTO asignarConsecutivo(ConsecutivoDTO dto, String token)throws ServerException{
		// BEGIN region asignarConsecutivo
		if(dto.getLlaveTabla()==null) throw new ServerException("Para asignar el consecutivo se debe enviar la clave del consecutivo");
		ConsecutivoDTO consecutivoBD = consultaXId(dto.getLlaveTabla());
		if(consecutivoBD.getEstado().compareTo(SharedConstants.STATE_ACTIVE)!=0) throw new ServerException("Este consecutivo no se encuentra activo." + consecutivoBD.getNombre());
		if(consecutivoBD.getManual()){
			if(dto.getNumeroActual().compareTo(BigDecimal.ZERO)==0)throw new ServerException("El numero no puede ser cero");
			consecutivoBD.setNumeroActual(dto.getNumeroActual());
		}else{
			//Aumento en 1 el valor del actual
			consecutivoBD.setNumeroActual(consecutivoBD.getNumeroActual().add(BigDecimal.ONE));
			consecutivoBD = update(consecutivoBD);
		}
		if(consecutivoBD.getNumeroActual().compareTo(consecutivoBD.getNumeroInicial())<0)throw new ServerException("El numero no puede ser menor a " +SoftureUtil.formatNumber(consecutivoBD.getNumeroInicial()) + "\n" + consecutivoBD.getNombre());
		if(consecutivoBD.getNumeroFinal().compareTo(BigDecimal.ZERO)!=0){
			if(consecutivoBD.getNumeroActual().compareTo(consecutivoBD.getNumeroFinal())>0)throw new ServerException("El numero no puede ser mayor a " +SoftureUtil.formatNumber(consecutivoBD.getNumeroFinal()) + "\n" + consecutivoBD.getNombre());
		}
		//Armo el numero acual
		String cons = "";
		if(consecutivoBD.getPrefijo()!=null) cons = cons + consecutivoBD.getPrefijo();
		if(consecutivoBD.getPadding()==null) {
			cons = cons + consecutivoBD.getNumeroActual().toBigInteger().toString();	
		}else {
			if(!consecutivoBD.getPadding().contains("%"))
				throw new ServerException("El padding del consecutivo no es correcto sigue este ejemplo : %07d (rellena con ceros en 7 espacios)");
			cons = cons + String.format(consecutivoBD.getPadding().toLowerCase(), consecutivoBD.getNumeroActual().toBigInteger());
		}
		if(consecutivoBD.getSufijo()!=null) cons = cons + consecutivoBD.getSufijo();
		consecutivoBD.setConsecutivoActual(cons);
		return consecutivoBD;
		// END region asignarConsecutivo
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ConsecutivoDTO guardar(ConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN Consecutivo_guardar
		return super.guardar(dto, token);
		// END Consecutivo_guardar
	}

// BEGIN region aditionalMethods
	public void crear(DocumentoPlantillaDTO plantilla, String token) throws ServerException {
		ConsecutivoDTO nuevo = new ConsecutivoDTO();
		nuevo.setNombre(plantilla.getNombre());
		nuevo.setPrefijo(plantilla.getCodigo() + "-");
		nuevo.setNumeroInicial(new BigDecimal(100));
		nuevo.setNumeroActual(new BigDecimal(100));
		//if(cantidad!=null)nuevo.setNumeroFinal(cantidad.add(augend));
		nuevo = guardar(nuevo, token);
		plantilla.setConsecutivo(nuevo.getLlaveTabla());
		plantillaService.update(plantilla);
	}
	
	public ConsecutivoDTO crear2Opcion(String consecutivo, String campo, String opcion, String token) throws ServerException {
		ConsecutivoDTO actual = consultaXId(consecutivo);
		if(actual==null) throw new ServerException("Revisa el id del consecutivo");
		if(actual.getEstado().compareTo(SharedConstants.STATE_ACTIVE)!=0) throw new ServerException("Consecutivo inactivo " + actual.getNombre());
		
		
		ConsecutivoDTO nuevo = new ConsecutivoDTO();
		nuevo.setNombre(actual.getNombre()); 
		String consecutivoDocumento = consecutivoMapper.obtenerPrefijo(opcion);
		if (consecutivoDocumento==null) {
			consecutivoDocumento = "";
		}
		if(actual.getPrefijo()!=null)consecutivoDocumento = consecutivoDocumento + actual.getPrefijo();
		if(!consecutivoDocumento.isEmpty()) {
			consecutivoDocumento = consecutivoDocumento.replace("-", "");
			nuevo.setPrefijo(consecutivoDocumento+"-");
			nuevo.setNombre(nuevo.getNombre() + "-" + consecutivoDocumento);
		}
		if(actual.getNumeroFinal().compareTo(BigDecimal.ZERO)==0) {
			nuevo.setNumeroInicial(new BigDecimal(100));
			nuevo.setNumeroActual(new BigDecimal(100));
		}else {
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
		if(manuales==null || manuales.isEmpty()) throw new ServerException("No se tiene configurados consecutivos manuales activos para las personas");
		return manuales.get(0);
	}
			
// END region aditionalMethods

}