package d3.configuration.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.domain.RelacionInternaFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "RelacionInternaMapper")
public interface RelacionInternaMapper extends IBasicMapper<RelacionInternaDTO, RelacionInternaFilterDTO> {

	List<RelacionInternaDTO> getRelationsFullToSynchronize();

	String getTemplateOfField(String pFieldId);

	void updatePropertyRelations(String pProperty);
}