package com.accounting.voucher.infrastructure;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.PedidoVentaDTO;

@SoftureSqlConnMapper(value = "VoucherExtendAccountingMapper")
public interface VoucherExtendMapper {

	List<PedidoVentaDTO> itemsToRecreateVoucher(@Param("pTemplateId") String pTemplateId, @Param("pStartDate") Date pStartDate, @Param("pEndDate") Date pEndDate);
	
	List<PedidoVentaDTO> itemsToDeleteVoucher(@Param("pTemplateId") String pTemplateId, @Param("pStartDate") Date pStartDate, @Param("pEndDate") Date pEndDate);

}