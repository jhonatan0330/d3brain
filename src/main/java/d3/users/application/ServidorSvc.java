package d3.users.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.CacheManager;
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
	private final CacheManager cacheManager;

	public ServidorSvc(@Lazy ServidorMapper servidorMapper,
			Environment env, @Lazy CacheManager cacheManager) {
		this.servidorMapper = servidorMapper;
		this.env = env;
		this.cacheManager = cacheManager;
	}

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
	public ServidorDTO actualizar(ServidorDTO dto) throws ServerException {
		validateDTO(dto);
		cacheManager.clearLocalServer();
		cacheManager.clearFtpServer();
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ServidorDTO inactivar(ServidorDTO dto) throws ServerException {
		cacheManager.clearLocalServer();
		cacheManager.clearFtpServer();
		return super.inactivar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ServidorDTO guardar(ServidorDTO dto) throws ServerException {
		validateDTO(dto);
		cacheManager.clearLocalServer();
		cacheManager.clearFtpServer();
		return super.guardar(dto);
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

	public ServidorDTO getLocalServer() {
		return cacheManager.getLocalServer();
	}

	public ServidorDTO getFTPServer() {
		return cacheManager.getFtpServer();
	}

	public void setLocalServer(ServidorDTO _server) {
		cacheManager.setLocalServer(_server);
	}

	public void setFTPServer(ServidorDTO _server) {
		cacheManager.setFtpServer(_server);
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
		ServidorDTO localServer = getLocalServer();
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