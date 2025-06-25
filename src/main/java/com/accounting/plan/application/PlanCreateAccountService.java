package com.accounting.plan.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.domain.VoucherRequest;
import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;

@Service("PlanCreateAccountTemplateAccountingService")
public class PlanCreateAccountService {

	@Autowired
	@Lazy
	private AccountService accountService;
	@Autowired
	@Lazy
	private PlanGetAccountService planGetAccountService;
	@Autowired
	@Lazy
	private CatalogService catalogService;
	@Autowired
	@Lazy
	private PedidoVentaSvc documentService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO call(AccountDTO account) throws ServerException {
		if (account.getCatalogDocument() != null) {
			CatalogFilterDTO filter = new CatalogFilterDTO();
			filter.setDocument(account.getCatalogDocument());
			CatalogDTO _catalog = catalogService.getOne(filter);
			if(_catalog==null) throw new ServerException("Vamos a crear una cuenta pero no llego un documento que se relacione a un catalogo :(");
			account.setCatalog(_catalog.getKey());
		}
		if (account.getParentDocument() != null) {
			AccountFilterDTO filterParent = new AccountFilterDTO();
			filterParent.setDocument(account.getParentDocument());
			account.setParent(accountService.getOne(filterParent).getKey());
		}
		if (account.getCatalog() == null)
			throw new ServerException("Es importante asignar la cuenta a un catalogo");
		if (account.getParent() != null && account.getParent().isEmpty())
			account.setParent(null);
		if (account.getCode() != null && account.getCode().isEmpty())
			account.setCode(null);
		if(account.getType()==null)account.setType(AccountConst.TYPE_OPERATIONAL);
		if(account.getOperation()==null) account.setOperation(AccountConst.OPERATION_ADD);
		
		AccountFilterDTO filter = new AccountFilterDTO();
		String prefixWBS = "";
		if (account.getParent() == null) {
			filter.setLevel(1);
			account.setLevel(1);
		} else {
			filter.setParent(account.getParent());
			AccountDTO parentAccount = accountService.getById(account.getParent());
			if (parentAccount == null)
				throw new ServerException("En la cuenta " + account.getName() + " el nodo principal "
						+ account.getParent() + " no se encuentra en la BD por su identificador");
			if (parentAccount.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("La cuenta" + parentAccount.getName() + " no se encuentra activa");
			prefixWBS = parentAccount.getWbs() + ".";
			account.setLevel(parentAccount.getLevel() + 1);
			if (parentAccount.getType().compareTo(AccountConst.TYPE_OPERATIONAL) == 0 && account.getType().compareTo(AccountConst.TYPE_AUXILIAR)!= 0) {
				parentAccount.setType(AccountConst.TYPE_GROUP);
				accountService.update(parentAccount);
			}
		}
		filter.setState(SharedConstants.STATE_ACTIVE);
		int countAccount = accountService.count(filter);
		account.setWbs(prefixWBS + "%1$4s".formatted((countAccount + 1)));
		
		if (account.getCode() == null)
			account.setCode(account.getWbs());
		accountService.save(account);
		return accountService.getById(account.getKey());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO callUpdate(AccountDTO account) throws ServerException {
		AccountDTO bd = accountService.getById(account.getKey());
		if (bd == null)
			throw new ServerException("No se identifica la cuenta");
		if (bd.getCatalog().compareTo(account.getCatalog()) != 0)
			throw new ServerException("No se puede modificar el catalogo");
		if (account.getParent() != null && account.getParent().isEmpty())
			account.setParent(null);
		if (account.getParent() != null && account.getParent().compareTo(account.getKey()) == 0)
			throw new ServerException("La cuenta parent no puede ser la misma");
		if (account.getCode() != null && account.getCode().isEmpty())
			account.setCode(null);
		account.setState(bd.getState());
		if(account.getOperation()==null) account.setOperation(AccountConst.OPERATION_ADD);
		accountService.update(account);
		return accountService.getById(account.getKey());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO callDelete(String accountId) throws ServerException {
		AccountDTO result = accountService.delete(accountId);
		if(result.getParent()== null ) return result;
		if(result.getType().compareTo(AccountConst.TYPE_OPERATIONAL) != 0) return result;
		AccountFilterDTO filter = new AccountFilterDTO();
		filter.setParent(result.getParent());
		filter.setState(SharedConstants.STATE_ACTIVE);
		if(accountService.count(filter)==0) {
			AccountDTO parent = accountService.getById(result.getParent());
			parent.setType(AccountConst.TYPE_OPERATIONAL);
			accountService.update(parent);
		}
		return result;
	}

	public AccountDTO createAuxiliarAccount(VoucherRequest _item, String accountParentId,
			String documentId) throws ServerException {
		AccountDTO accountReference = new AccountDTO();
		accountReference.setDocument(documentId);
		accountReference.setCatalog(_item.getCatalog());
		AccountDTO _parentAccount = accountService.getById(accountParentId);
		if (_parentAccount == null)
			throw new ServerException("No se identifica la cuenta");
		
		PedidoVentaDTO _document = null;
		if(documentId.compareTo(AccountConst.AUXILIAR_EMPTY)==0) {
			_document = new PedidoVentaDTO();
			_document.setNombre((_parentAccount==null)?"_0":(_parentAccount.getCode()+"_0"));
			_document.setDescripcion("Cuenta auxiliar sin documento");
		}else {
			_document = documentService.consultaXId(documentId);
		}
		if(_document == null)
			throw new ServerException("No se encuentra el documento con id " + documentId);
		accountReference.setCode(_document.getNombre());
		accountReference.setName((_document.getDescripcion()==null)? _document.getNombre() : _document.getDescripcion());
		accountReference.setParent(accountParentId);
		accountReference.setType(AccountConst.TYPE_AUXILIAR);
		accountReference = call(accountReference);
		if(_parentAccount.getParent()!=null) {
			AccountDTO accountAuxParent = planGetAccountService.findAccountByDocumentId(_item.getCatalog(), documentId, _parentAccount.getParent());
			if (accountAuxParent == null) 
				createAuxiliarAccount(_item, _parentAccount.getParent(), documentId);
		}
		return accountReference;
	}

}