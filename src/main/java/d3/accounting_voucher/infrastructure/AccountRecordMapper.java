package d3.accounting_voucher.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting_voucher.domain.AccountRecordDTO;
import d3.accounting_voucher.domain.AccountRecordFilterDTO;

@D3SqlConnMapper(value = "AccountRecordAccountingMapper")
public interface AccountRecordMapper {

	AccountRecordDTO insert(AccountRecordDTO dto);

	AccountRecordDTO update(AccountRecordDTO dto);

	int count(AccountRecordFilterDTO filter);

	AccountRecordDTO getOne(AccountRecordFilterDTO filter);

	List<AccountRecordDTO> getMany(AccountRecordFilterDTO filter);

}