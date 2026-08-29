package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.AccountRecordAuxiliarDTO;
import d3.accounting.domain.AccountRecordAuxiliarFilterDTO;

@D3SqlConnMapper(value = "AccountRecordAuxiliarAccountingMapper")
public interface AccountRecordAuxiliarMapper {

	AccountRecordAuxiliarDTO insert(AccountRecordAuxiliarDTO dto);

	AccountRecordAuxiliarDTO update(AccountRecordAuxiliarDTO dto);

	int count(AccountRecordAuxiliarFilterDTO filter);

	AccountRecordAuxiliarDTO getOne(AccountRecordAuxiliarFilterDTO filter);

	List<AccountRecordAuxiliarDTO> getMany(AccountRecordAuxiliarFilterDTO filter);

}