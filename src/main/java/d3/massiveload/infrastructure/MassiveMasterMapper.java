package d3.massiveload.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.infrastructure.SharedCRUDMapperMybatis;
import d3.massiveload.domain.MassiveMasterDTO;
import d3.massiveload.domain.MassiveMasterFilter;

@D3SqlConnMapper(value = "MassiveMasterMapper")
public interface MassiveMasterMapper extends SharedCRUDMapperMybatis<MassiveMasterDTO, MassiveMasterFilter> {

}
