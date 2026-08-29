package d3.property.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.property.domain.RelacionInternaDTO;
import d3.property.domain.RelacionInternaFilterDTO;

@D3SqlConnMapper(value = "RelacionInternaMapper")
public interface RelacionInternaMapper extends IBasicMapper<RelacionInternaDTO, RelacionInternaFilterDTO> {

	List<RelacionInternaDTO> getRelationsFullToSynchronize();

	String getTemplateOfField(String pFieldId);

	void updatePropertyRelations(String pProperty);
}