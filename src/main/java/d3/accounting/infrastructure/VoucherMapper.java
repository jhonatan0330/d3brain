package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.VoucherDTO;
import d3.accounting.domain.VoucherFilterDTO;

@D3SqlConnMapper(value = "VoucherAccountingMapper")
public interface VoucherMapper {

	VoucherDTO insert(VoucherDTO dto);

	VoucherDTO update(VoucherDTO dto);

	int count(VoucherFilterDTO filter);

	VoucherDTO getOne(VoucherFilterDTO filter);

	List<VoucherDTO> getMany(VoucherFilterDTO filter);

}