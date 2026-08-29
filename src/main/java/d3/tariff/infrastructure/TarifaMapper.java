package d3.tariff.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.shared.domain.IBasicMapper;
import d3.tariff.domain.TarifaDTO;
import d3.tariff.domain.TarifaFilterDTO;

@D3SqlConnMapper(value = "TarifaMapper")
public interface TarifaMapper extends IBasicMapper<TarifaDTO, TarifaFilterDTO> {

	List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto);

	List<TarifaDTO> obtenerTarifaFuncion(@Param("propiedad") String propiedad, @Param("producto") String producto,
			@Param("productoBase") String productoBase,
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
}