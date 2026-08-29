package d3.process.infrastructure;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.domain.ProcesoTransicionFilterDTO;

@D3SqlConnMapper(value = "ProcesoTransicionMapper")
public interface ProcesoTransicionMapper extends IBasicMapper<ProcesoTransicionDTO, ProcesoTransicionFilterDTO> {

	List<ProcesoTransicionDTO> listarTransicionesRol(ProcesoTransicionFilterDTO dto);

	List<ProcesoTransicionDTO> listarTransaccionInicial(ProcesoTransicionFilterDTO dto);

	String decision(@Param("sqlFuncionDecision") String sqlFuncionDecision,
			@Param("llaveTablaDocumento") String llaveTablaDocumento,
			@Param("llaveTablaModificador") String llaveTablaModificador, @Param("ramdom") String ramdom);

	BigDecimal valorEntransicionParaRevertir(@Param("documento") String documento,
			@Param("expediente") String expediente);

	List<ProcesoTransicionDTO> getFullToSynchronize(@Param("process") List<String> process);

	int funcionRegresarTablaHistoricos(@Param("documentId") String documentId);

	void clearStateOfDocumentsProcess(@Param("templateId") String templateId);
}