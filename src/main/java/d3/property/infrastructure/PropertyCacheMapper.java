package d3.property.infrastructure;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadFilterDTO;

@D3SqlConnMapper(value = "PropertyCacheMapper")
public interface PropertyCacheMapper {

	List<PropiedadDTO> consultarPermisosUsuario(@Param("usuario") String usuario);

	List<PropiedadDTO> consultarRol(@Param("dto") PropiedadFilterDTO dto, @Param("usuario") String usuario,
			@Param("fecha") Date fecha, @Param("privada") Boolean privada);

	List<PropiedadDTO> consultarPermisosFullPlantilla(PropiedadDTO dto);

	List<PropiedadDTO> listarPlantillasSimplificar(@Param("plantillas") List<DocumentoPlantillaDTO> plantillas,
			@Param("usuario") String usuario, @Param("fecha") Date fecha);

	List<PropiedadDTO> obtenerEspecialFullPermisosSimplificandoBD(
			@Param("plantillas") List<DocumentoPlantillaDTO> plantillas, @Param("profile") String pProfile,
			@Param("user") String pUser);

	List<String> getUserRole(@Param("pUser") String pUser);
}