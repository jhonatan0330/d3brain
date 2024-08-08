package com.softure.document_transaction.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.document_transaction.domain.DocumentoTransaccionDTO;
import com.softure.document_transaction.domain.DocumentoTransaccionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "DocumentoTransaccionMapper")
public interface DocumentoTransaccionMapper extends IBasicMapper<DocumentoTransaccionDTO, DocumentoTransaccionFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}