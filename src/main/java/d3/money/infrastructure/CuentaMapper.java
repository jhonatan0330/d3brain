package d3.money.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.money.domain.CuentaDTO;
import d3.money.domain.CuentaFilterDTO;

@D3SqlConnMapper(value = "CuentaMapper")
public interface CuentaMapper extends IBasicMapper<CuentaDTO, CuentaFilterDTO> {
	Long sobregiro(String documento);

	boolean turnomultiple(String documento);
}