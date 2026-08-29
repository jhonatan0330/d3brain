package d3.process.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoFilterDTO;

@D3SqlConnMapper(value = "ProcesoMapper")
public interface ProcesoMapper extends IBasicMapper<ProcesoDTO, ProcesoFilterDTO> {

	List<ProcesoDTO> noUsoSoloQuitoErrorList();

	List<ProcesoDTO> getFullToSynchronize(@Param("process") List<String> process);
}