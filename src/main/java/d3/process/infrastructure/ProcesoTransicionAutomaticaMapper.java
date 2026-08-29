package d3.process.infrastructure;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.process.domain.ProcesoTransicionAutomaticaDTO;
import d3.process.domain.ProcesoTransicionAutomaticaFilterDTO;

@D3SqlConnMapper(value = "ProcesoTransicionAutomaticaMapper")
public interface ProcesoTransicionAutomaticaMapper
		extends IBasicMapper<ProcesoTransicionAutomaticaDTO, ProcesoTransicionAutomaticaFilterDTO> {

	List<ProcesoTransicionAutomaticaDTO> consultarPendientes();

	String getFieldPlantilla(String propiedad);

	Date obtenerFechaUltimaEjecucion(String transicion);

	void inactivarPropiedad(String propiedad);

	int funcionPasarTablaHistoricos(@Param("plantilla") String plantilla, @Param("fechaCorte") Date fechaCorte);

	int countExecutionInLastMonth(@Param("transition") String transition, @Param("property") String property);
}