package d3.mail.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajeFilterDTO;

@D3SqlConnMapper(value = "MensajeMapper")
public interface MensajeMapper extends IBasicMapper<MensajeDTO, MensajeFilterDTO> {

	List<MensajeDTO> mensajesUsuario(MensajeFilterDTO dto);

	List<MensajeDTO> mensajesDisponibles();

	List<MensajeDTO> correosMensaje(@Param("llavePropiedad") String estado, @Param("documento") String documento,
			@Param("modificador") String modificador, @Param("token") String token);

	// List<MensajeDTO> mensajesTransaccion(@Param("transaccion") String
	// transaccion);
}