package com.softure.process_form.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;

@SoftureSqlConnMapper(value = "DocumentoPlantillaCaracteristicaMapper")
public interface DocumentoPlantillaCaracteristicaMapper
		extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO> {

	void actualizarFiltros(String llaveTabla);

	void actualizarDescripcion(@Param("pTemplate") String pTemplate, @Param("pField") String pField);

	List<DocumentoPlantillaCaracteristicaDTO> getFullToSynchronize(@Param("process") List<String> process);

	int countDependentsOfField(DocumentoPlantillaCaracteristicaFilterDTO pFilter);

}