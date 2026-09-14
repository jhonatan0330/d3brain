package d3.usage.infrastructure;

import d3.D3SqlConnMapper;
import d3.usage.domain.SaldoConsumoDTO;
import d3.usage.domain.SaldoConsumoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "SaldoConsumoMapper")
public interface SaldoConsumoMapper extends IBasicMapper<SaldoConsumoDTO, SaldoConsumoFilterDTO> {

	SaldoConsumoDTO obtenerSaldoActivo(SaldoConsumoFilterDTO dto);

}