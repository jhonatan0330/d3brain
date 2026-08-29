package d3.upload.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.upload.domain.CargaArchivoDTO;
import d3.upload.domain.CargaArchivoFilterDTO;

@D3SqlConnMapper(value = "CargaArchivoMapper")
public interface CargaArchivoMapper extends IBasicMapper<CargaArchivoDTO, CargaArchivoFilterDTO> {

}