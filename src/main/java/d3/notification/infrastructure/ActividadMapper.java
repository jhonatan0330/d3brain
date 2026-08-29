package d3.notification.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.notification.domain.ActividadDTO;
import d3.notification.domain.ActividadFilterDTO;

@D3SqlConnMapper(value = "ActividadMapper")
public interface ActividadMapper extends IBasicMapper<ActividadDTO, ActividadFilterDTO> {

}