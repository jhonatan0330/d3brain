package com.softure.document_transaction.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_transaction.domain.TransaccionLogDTO;
import com.softure.document_transaction.domain.TransaccionLogFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "TransaccionLogMapper")
public interface TransaccionLogMapper extends IBasicMapper<TransaccionLogDTO, TransaccionLogFilterDTO> {

}