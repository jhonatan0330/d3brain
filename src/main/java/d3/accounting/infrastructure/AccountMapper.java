package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.AccountDTO;
import d3.accounting.domain.AccountFilterDTO;

@D3SqlConnMapper(value = "AccountAccountingMapper")
public interface AccountMapper {

	AccountDTO insert(AccountDTO dto);

	AccountDTO update(AccountDTO dto);

	int count(AccountFilterDTO filter);

	AccountDTO getOne(AccountFilterDTO filter);

	List<AccountDTO> getMany(AccountFilterDTO filter);

}