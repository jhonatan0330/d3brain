package d3.process_form.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.process_form.domain.DocumentoPlantillaFilterDTO;

@D3SqlConnMapper(value = "DocumentoPlantillaMapper")
public interface DocumentoPlantillaMapper extends IBasicMapper<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO> {

	List<DocumentoPlantillaDTO> listarMenu(DocumentoPlantillaFilterDTO dto);

	List<DocumentoPlantillaDTO> getProcessBoardsToMenu(DocumentoPlantillaFilterDTO dto);

	List<DocumentoPlantillaDTO> getFullToSynchronize(@Param("process") List<String> process);

	List<DocumentoPlantillaDTO> getTemplateofCategoriesReplace();

}