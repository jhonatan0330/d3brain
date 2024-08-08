package com.softure.property.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.PropiedadValorDefinidoSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/property")
public class PropertyController {
	
	@Autowired @Lazy  private PropiedadSvc propertyService;
	@Autowired @Lazy  private PropiedadValorDefinidoSvc propertyTypeService;
	
	@GetMapping(value="/{type}/{field}")
	public List<PropiedadDTO> getFullProperties(@RequestHeader("Authorization") String token, @PathVariable String type, @PathVariable String field) throws ServerException {
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setTipo(type);
		filter.setCampo(field);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertyService.listarConsulta(filter);
	}
	
	@GetMapping(value="/type/{type}/{filterName}")
	public List<PropiedadValorDefinidoDTO> getTypeProperty(@RequestHeader("Authorization") String token, @PathVariable String type, @PathVariable String filterName) throws ServerException {
		PropiedadValorDefinidoFilterDTO filter = new PropiedadValorDefinidoFilterDTO();
		filter.setOrigen(type);
		filter.setFiltroParametro(filterName);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertyTypeService.listarConsulta(filter);
	}
	
	@GetMapping(value="/{key}")
	public PropiedadDTO getProperty(@RequestHeader("Authorization") String token, @PathVariable String key) throws ServerException {
		return propertyService.consultaXId(key);
	}
	
	@PostMapping(value="/")
	public PropiedadDTO createProperty(@RequestHeader("Authorization") String token, @RequestBody PropiedadDTO property) throws ServerException {
		return propertyService.guardar(property, token);
	}
}
