package d3.massiveload.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.infrastructure.SharedCRUDMapperMybatis;
import d3.massiveload.domain.MassiveItemDTO;
import d3.massiveload.domain.MassiveItemFilter;

@D3SqlConnMapper(value = "MassiveItemMapper")
public interface MassiveItemMapper extends SharedCRUDMapperMybatis<MassiveItemDTO, MassiveItemFilter> {

}
