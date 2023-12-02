package com.accounting.plan.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.plan.domain.AccountDTO;
import com.shared.domain.ServerException;

@Service("PlanUploadAccountAccountingService")
public class PlanUploadAccountService {

	@Autowired
	private PlanCreateAccountService createAccountService;

	private static String TYPE = "text/csv";

	public void call(String catalogId, String token, MultipartFile file) throws ServerException {
		if (catalogId == null)
			throw new ServerException("No se reconoce el Id del catalogo");
		if (!TYPE.equals(file.getContentType()))
			throw new ServerException("Solo se aceptan archivos con extension csv");
		try {
			List<AccountDTO> accounts = csvToAccounts(file.getInputStream());
			for (AccountDTO accountDTO : accounts) {
				accountDTO.setCatalog(catalogId);
				accountDTO = createAccountService.call(accountDTO, token);
				if(accountDTO.getCode()!=null) {
					for (AccountDTO iAccount : accounts) {
						if(iAccount.getParent()!=null && iAccount.getParent().compareTo(accountDTO.getCode())==0) {
							iAccount.setParent(accountDTO.getKey());
						}
					}
				}
			}
		} catch (IOException e) {
			throw new ServerException("fail to store csv data: " + e.getMessage());
		}
	}


	private static List<AccountDTO> csvToAccounts(InputStream is) throws IOException {
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setIgnoreEmptyLines(true).setSkipHeaderRecord(true).build();
		List<AccountDTO> accounts = new ArrayList<AccountDTO>();
		Iterable<CSVRecord> csvRecords = csvFormat.parse(fileReader);
		for (CSVRecord csvRecord : csvRecords) {
			AccountDTO account = new AccountDTO();
			account.setCode(csvRecord.get(0));
			account.setName(csvRecord.get(1));
			account.setParent(csvRecord.get(2));
			accounts.add(account);
		}
		return accounts;
	}
}
