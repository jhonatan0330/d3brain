package d3.users.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.authentication.application.UsuarioSesionSvc;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.users.domain.ServidorDTO;
import d3.users.domain.ServidorFilterDTO;
import d3.users.infrastructure.ServidorMapper;
import jakarta.annotation.PostConstruct;

@Service("servidorService")
public class ServidorSvc extends BasicSvc<ServidorDTO, ServidorFilterDTO> {

	private final ServidorMapper servidorMapper;
	private final Environment env;

	public ServidorSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy ServidorMapper servidorMapper,
			Environment env) {
		super(usuarioSesionService);
		this.servidorMapper = servidorMapper;
		this.env = env;
	}

	private ServidorDTO localServer;

	private ServidorDTO ftpServer;

	@Override
	public ServidorDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Servidor");
		ServidorFilterDTO dto = new ServidorFilterDTO();
		dto.setLlaveTabla(llave);
		return servidorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = servidorMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ServidorDTO actualizar(ServidorDTO dto, String token) throws ServerException {
		validateDTO(dto);
		localServer = null;
		ftpServer = null;
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ServidorDTO inactivar(ServidorDTO dto, String token) throws ServerException {
		localServer = null;
		ftpServer = null;
		return super.inactivar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ServidorDTO guardar(ServidorDTO dto, String token) throws ServerException {
		validateDTO(dto);
		localServer = null;
		ftpServer = null;
		return super.guardar(dto, token);
	}

	public ServidorDTO obtenerServidorPrincipal(String tipo) throws ServerException {
		ServidorFilterDTO filtroFilter = new ServidorFilterDTO();
		filtroFilter.setEstado(SharedConstants.STATE_ACTIVE);
		filtroFilter.setTipo(tipo);
		List<ServidorDTO> servidores = listarConsulta(filtroFilter);
		if (servidores == null || servidores.isEmpty())
			return null;
		return servidores.get(0);
	}

	private void validateDTO(ServidorDTO dto) throws ServerException {
		if (dto.getPuerto() != null) {
			try {
				Integer.parseInt(dto.getPuerto());
			} catch (Exception e) {
				throw new ServerException("REvisa el valor del puerto que debe ser numerico y sin espacios");
			}
		}
	}

	public String getFromMail(ServidorDTO server) {
		if (server.getBase() != null && !server.getBase().isEmpty()) {
			return server.getBase();
		}
		return server.getUsuario();
	}

	public ServidorDTO getLocalServer() {
		return localServer;
	}

	public ServidorDTO getFTPServer() {
		return ftpServer;
	}

	public void setLocalServer(ServidorDTO _server) {
		this.localServer = _server;
	}

	public void setFTPServer(ServidorDTO _server) {
		this.ftpServer = _server;
	}

	public ServidorDTO resolveServer() throws ServerException {
		if (getLocalServer() == null && getFTPServer() == null) {
			setLocalServer(obtenerServidorPrincipal(ServidorDTO.LOCAL_FTP));
		}

		if (getLocalServer() == null && getFTPServer() == null)
			setFTPServer(obtenerServidorPrincipal(ServidorDTO.FTP));

		ServidorDTO server = getLocalServer() != null ? getLocalServer() : getFTPServer();

		if (server != null)
			return server;

		return resolveLocalServer();
	}

	public ServidorDTO resolveLocalServer() throws ServerException {
		if (localServer != null)
			return localServer;
		String _configPathToLocalFiles = env.getProperty("app.pathFiles");
		if (_configPathToLocalFiles == null)
			throw new ServerException(
					"No se ha configurado el servidor para carga de archivos, ni tampoco ruta en el archivo de configuracion");

		ServidorDTO server = new ServidorDTO();
		server.setTipo(ServidorDTO.LOCAL_FTP);
		server.setBase(_configPathToLocalFiles);
		setLocalServer(server);
		return server;
	}
}