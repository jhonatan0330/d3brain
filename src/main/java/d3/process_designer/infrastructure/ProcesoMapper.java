package d3.process_designer.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.process_designer.domain.ProcesoDTO;
import d3.process_designer.domain.ProcesoFilterDTO;

@D3SqlConnMapper(value = "ProcesoMapper")
public interface ProcesoMapper extends IBasicMapper<ProcesoDTO, ProcesoFilterDTO> {

	List<ProcesoDTO> noUsoSoloQuitoErrorList();

	List<ProcesoDTO> getFullToSynchronize(@Param("process") List<String> process);
}