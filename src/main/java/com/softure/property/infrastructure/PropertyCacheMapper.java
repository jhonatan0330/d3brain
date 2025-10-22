package com.softure.property.infrastructure;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;

@SoftureSqlConnMapper(value = "PropertyCacheMapper")
public interface PropertyCacheMapper{
	
	
	List<PropiedadDTO> consultarPermisosUsuario( @Param("usuario")String usuario);
	List<PropiedadDTO> consultarRol(@Param("dto")PropiedadFilterDTO dto, @Param("usuario")String usuario, @Param("fecha") Date fecha, @Param("privada") Boolean privada);
	List<PropiedadDTO> consultarPermisosFullPlantilla(PropiedadDTO dto);
	List<PropiedadDTO> listarPlantillasSimplificar(@Param("plantillas") List<DocumentoPlantillaDTO> plantillas, @Param("usuario")String usuario, @Param("fecha") Date fecha);
	List<PropiedadDTO> obtenerEspecialFullPermisosSimplificandoBD(@Param("plantillas") List<DocumentoPlantillaDTO> plantillas);

	List<String> getUserRole(@Param("pUser") String pUser);
}