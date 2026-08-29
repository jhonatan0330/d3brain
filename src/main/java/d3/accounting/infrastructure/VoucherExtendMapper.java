package d3.accounting.infrastructure;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document.domain.PedidoVentaDTO;

@D3SqlConnMapper(value = "VoucherExtendAccountingMapper")
public interface VoucherExtendMapper {

	List<PedidoVentaDTO> itemsToRecreateVoucher(@Param("pTemplateId") String pTemplateId,
			@Param("pStartDate") Date pStartDate, @Param("pEndDate") Date pEndDate);

	List<PedidoVentaDTO> itemsToDeleteVoucher(@Param("pTemplateId") String pTemplateId,
			@Param("pStartDate") Date pStartDate, @Param("pEndDate") Date pEndDate);

}