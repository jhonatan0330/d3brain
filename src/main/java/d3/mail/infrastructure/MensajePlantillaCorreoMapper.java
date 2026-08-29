package d3.mail.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.mail.domain.MensajePlantillaCorreoDTO;
import d3.mail.domain.MensajePlantillaCorreoFilterDTO;

@D3SqlConnMapper(value = "MensajePlantillaCorreoMapper")
public interface MensajePlantillaCorreoMapper
		extends IBasicMapper<MensajePlantillaCorreoDTO, MensajePlantillaCorreoFilterDTO> {

	List<MensajePlantillaCorreoDTO> getFullToSynchronize(@Param("process") List<String> process);

}