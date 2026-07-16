package com.softure.property.infrastructure;

import java.util.List;

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
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/property")
public class PropertyController {

	private final PropiedadSvc propertyService;
	private final PropiedadValorDefinidoSvc propertyTypeService;

	public PropertyController(@Lazy PropiedadSvc propertyService, @Lazy PropiedadValorDefinidoSvc propertyTypeService) {
		this.propertyService = propertyService;
		this.propertyTypeService = propertyTypeService;
	}

	@GetMapping(value = "/{type}/{field}")
	public List<PropiedadDTO> getFullProperties(@RequestHeader("Authorization") String token,
			@PathVariable(name = "type") String pType, @PathVariable(name = "field") String pField)
			throws ServerException {
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setTipo(pType);
		filter.setCampo(pField);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertyService.listarConsulta(filter);
	}

	@GetMapping(value = "/type/{type}/{filterName}")
	public List<PropiedadValorDefinidoDTO> getTypeProperty(@RequestHeader("Authorization") String token,
			@PathVariable(name = "type") String pType, @PathVariable(name = "filterName") String pFilterName)
			throws ServerException {
		PropiedadValorDefinidoFilterDTO filter = new PropiedadValorDefinidoFilterDTO();
		filter.setOrigen(pType);
		filter.setFiltroParametro(pFilterName);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertyTypeService.listarConsulta(filter);
	}

	@GetMapping(value = "/{key}")
	public PropiedadDTO getProperty(@RequestHeader("Authorization") String token,
			@PathVariable(name = "key") String pKey) throws ServerException {
		return propertyService.consultaXId(pKey);
	}

	@PostMapping(value = "/")
	public PropiedadDTO createProperty(@RequestHeader("Authorization") String token, @RequestBody PropiedadDTO property)
			throws ServerException {
		return propertyService.guardar(property, token);
	}
}
