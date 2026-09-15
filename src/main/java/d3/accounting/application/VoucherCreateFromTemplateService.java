package d3.accounting.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting.domain.AccountRecordDTO;
import d3.accounting.domain.Voucher;
import d3.accounting.domain.VoucherDTO;
import d3.accounting.domain.VoucherLine;
import d3.document.domain.PedidoVentaDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;

@Service
public class VoucherCreateFromTemplateService {

	private final VoucherCreateService createVoucherService;
	private final PlanGetAccountService getAccountService;

	public VoucherCreateFromTemplateService(@Lazy VoucherCreateService createVoucherService,@Lazy PlanGetAccountService getAccountService) {
		this.createVoucherService = createVoucherService;
		this.getAccountService = getAccountService;
	}

	public SharedIdResponse call(String _token, PedidoVentaDTO _item) throws ServerException {
		
		/*VoucherRequest vr = new VoucherRequest();
		vr.setDocument(_item.getLlaveTabla());
		vr.setFactDate(_item.getFecha());
		if(_item.getDinero()!=null) {
			vr.setValue(_item.getDinero().getValorTotal());			
		}*/
		
		VoucherDTO header = new VoucherDTO();
		header.setFactDate(_item.getFecha());
		header.setDocument(_item.getLlaveTabla());
		header.setCode(_item.getNombre());
		
		List<VoucherLine> lines = new ArrayList<>();
		
		AccountRecordDTO line = new AccountRecordDTO();
		
		line.setAccount(getAccountService.findAccountByTemplateId(_item.getPlantilla()).getKey());
		if(_item.getDinero()!=null) {
			line.setPositive(_item.getDinero().getValorTotal());
		}else {
			line.setPositive(BigDecimal.ONE);
		}
		
		//line.setNote(accountRecordDTO.getNote());
		//line.setType(accountRecordDTO.getType());
		//line.setMainDocument(accountRecordDTO.getMainDocument());
		//line.setAccountLink(accountRecordDTO.getAccountLink());
		
		VoucherLine voucherLine = new VoucherLine();
		voucherLine.setLine(line);
		
		lines.add(voucherLine);
		
		Voucher voucher = new Voucher();
		voucher.setHeader(header);
		voucher.setRecords(lines);
		
		return createVoucherService.call(voucher, null);//tokenService.validate(token, null));
	}

	
}
