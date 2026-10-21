package d3.multitenancy.application;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import d3.multitenancy.domain.TenantCodigoNormalizer;
import d3.multitenancy.domain.TenantCrearDTO;
import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantMetadataProvider;
import d3.multitenancy.domain.TenantProvisionFase;
import d3.multitenancy.domain.TenantPublicDTO;
import d3.multitenancy.domain.TenantRegistry;
import d3.multitenancy.domain.TenantUsuarioDTO;
import d3.multitenancy.domain.TenantUsuarioFilterDTO;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.multitenancy.infrastructure.TenantUsuarioMapper;
import d3.shared.application.D3Utils;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.webservice.application.MailSendMessageToAdminService;

@Service("tenantProvisioningSvc")
public class TenantProvisioningSvc {

	private static final Pattern URL_PADRE = Pattern.compile("^jdbc:postgresql://([^/:]+)(?::(\\d+))?/([^?]+).*$");
	private static final String BASE_PLANTILLA = "safir_template";

	private final TenantMapper tenantMapper;
	private final TenantUsuarioMapper tenantUsuarioMapper;
	private final TenantMetadataProvider metadataProvider;
	private final TenantRegistry tenantRegistry;
	private final TenantDataSourceFactory dataSourceFactory;
	private final TenantScriptExecutor scriptExecutor;
	private final MailSendMessageToAdminService mailAdminService;
	private final Environment env;

	public TenantProvisioningSvc(@Lazy TenantMapper tenantMapper, @Lazy TenantUsuarioMapper tenantUsuarioMapper,
			TenantMetadataProvider metadataProvider, TenantRegistry tenantRegistry,
			TenantDataSourceFactory dataSourceFactory, TenantScriptExecutor scriptExecutor,
			@Lazy MailSendMessageToAdminService mailAdminService, Environment env) {
		this.tenantMapper = tenantMapper;
		this.tenantUsuarioMapper = tenantUsuarioMapper;
		this.metadataProvider = metadataProvider;
		this.tenantRegistry = tenantRegistry;
		this.dataSourceFactory = dataSourceFactory;
		this.scriptExecutor = scriptExecutor;
		this.mailAdminService = mailAdminService;
		this.env = env;
	}

	public TenantPublicDTO crearTenant(TenantCrearDTO dto) throws ServerException {
		String padre = TenantContext.getCurrentTenant();
		if (padre == null || padre.isBlank()) {
			padre = "default";
		}
		final String tenantPadre = padre;
		String solicitante = SessionContext.getCurrentUserOrNull();
		if (solicitante == null || solicitante.isBlank()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.ASIGNAR_USUARIO,
					"Token ausente o invalido: se requiere un usuario autenticado para crear el tenant");
		}
		String codigoCrudo = dto != null && dto.getCodigo() != null ? dto.getCodigo().trim() : "?";
		try {
			if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
				throw TenantProvisionFase.fallar(TenantProvisionFase.VALIDAR_CODIGO,
						"El nombre del tenant es obligatorio");
			}
			validarAdministrador(dto);
			String codigo = TenantCodigoNormalizer.normalizar(dto.getCodigo());
			String nombre = dto.getNombre().trim();
			System.out.println("Creando tenant: nombre=" + nombre + " codigo=" + codigo + " padre=" + padre);
			TenantDTO datosPadre = metadataProvider.resolve(tenantPadre).orElseThrow(() -> TenantProvisionFase
					.fallar(TenantProvisionFase.VERIFICAR_DUPLICADOS, "El tenant padre no existe: " + tenantPadre));
			ServidorPadre servidor = extraerServidor(datosPadre);
			String baseDatos = "produccion_" + codigo;
			String rol = "d3_" + codigo;
			String clave = codigo + "123";
			String url = "jdbc:postgresql://" + servidor.host + ":" + servidor.puerto + "/" + baseDatos;
			String key = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
			verificarDuplicados(padre, codigo, url);
			clonarBaseDatos(servidor, datosPadre, baseDatos, rol, clave);
			aprovisionarDesdePlantilla(url, rol, clave);
			crearAdministrador(url, rol, clave, dto, nombre, codigo);
			registrarCatalogo(padre, key, nombre, codigo, url, rol, clave, dto.getImagen());
			asignarUsuario(key, codigo, solicitante);
			registrarEnCaliente(key, nombre, codigo, url, rol, clave, dto.getImagen());
			notificarAcceso(padre, dto, nombre, codigo);
			System.out.println("Tenant creado: key=" + key + " url=" + url);
			TenantPublicDTO respuesta = new TenantPublicDTO();
			respuesta.setKey(key);
			respuesta.setName(nombre);
			respuesta.setImagen(dto.getImagen());
			return respuesta;
		} catch (ServerException e) {
			avisarAdministrador(padre, nombreSeguro(dto), codigoCrudo, e);
			throw e;
		} catch (Exception e) {
			ServerException envuelto = TenantProvisionFase.fallar(TenantProvisionFase.FALLO_GENERAL,
					"Fallo inesperado creando el tenant", e);
			avisarAdministrador(padre, nombreSeguro(dto), codigoCrudo, envuelto);
			throw envuelto;
		}
	}

	private void verificarDuplicados(String padre, String codigo, String url) throws ServerException {
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(padre);
			TenantFilterDTO porCodigo = new TenantFilterDTO();
			porCodigo.setCodigo(codigo);
			if (tenantMapper.getOne(porCodigo) != null) {
				throw TenantProvisionFase.fallar(TenantProvisionFase.VERIFICAR_DUPLICADOS,
						"Ya existe un tenant con el codigo " + codigo + " en " + padre);
			}
			TenantFilterDTO porUrl = new TenantFilterDTO();
			porUrl.setDatasourceUrl(url);
			if (tenantMapper.getOne(porUrl) != null) {
				throw TenantProvisionFase.fallar(TenantProvisionFase.VERIFICAR_DUPLICADOS,
						"La base de datos " + url + " ya esta registrada en otro tenant");
			}
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.VERIFICAR_DUPLICADOS,
					"No se pudo verificar duplicados en el tenant padre " + padre, e);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	private void crearRol(Connection conn, String rol, String clave) throws ServerException {
		try {
			try (PreparedStatement existe = conn.prepareStatement("select 1 from pg_roles where rolname = ?")) {
				existe.setString(1, rol);
				try (ResultSet rs = existe.executeQuery()) {
					if (rs.next()) {
						throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ROL,
								"El rol " + rol + " ya existe en el motor");
					}
				}
			}
			try (Statement stmt = conn.createStatement()) {
				stmt.execute("CREATE ROLE " + entrecomillar(rol) + " WITH LOGIN PASSWORD " + literal(clave));
			}
			System.out.println("Rol creado: " + rol);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ROL, "No se pudo crear el rol " + rol, e);
		}
	}

	private void otorgarPermisos(Connection conn, String baseDatos, String rol) throws ServerException {
		try (Statement stmt = conn.createStatement()) {
			stmt.execute("GRANT ALL PRIVILEGES ON DATABASE " + entrecomillar(baseDatos) + " TO " + entrecomillar(rol));
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.OTORGAR_PERMISOS,
					"No se pudo otorgar permisos sobre " + baseDatos + " al rol " + rol, e);
		}
	}

	private void clonarBaseDatos(ServidorPadre servidor, TenantDTO datosPadre, String baseDatos, String rol, String clave)
			throws ServerException {
		cargarDriver(datosPadre);
		String adminUrl = "jdbc:postgresql://" + servidor.host + ":" + servidor.puerto + "/postgres";
		String plantillaUrl = "jdbc:postgresql://" + servidor.host + ":" + servidor.puerto + "/" + BASE_PLANTILLA;
		Properties adminProps = propiedadesConexion(datosPadre.getDatasourceUsername(),
				datosPadre.getDatasourcePassword());
		try (Connection admin = DriverManager.getConnection(adminUrl, adminProps)) {
			admin.setAutoCommit(true);
			validarPlantilla(admin, BASE_PLANTILLA);
			List<String> duenos = leerDueniosPlantilla(plantillaUrl, adminProps);
			validarPlantillaSinConexiones(admin, BASE_PLANTILLA);
			crearRol(admin, rol, clave);
			crearBaseDatosConPlantilla(admin, baseDatos, BASE_PLANTILLA, rol);
			otorgarPermisos(admin, baseDatos, rol);
			reasignarPropiedadesPlantilla(servidor, baseDatos, adminProps, duenos, rol);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_BASE_DATOS,
					"No se pudo conectar al motor del tenant padre "
							+ (datosPadre.getDatasourceUrl() != null ? datosPadre.getDatasourceUrl() : ""),
					e);
		}
	}

	private void validarPlantilla(Connection admin, String plantilla) throws ServerException {
		try (PreparedStatement ps = admin.prepareStatement("select 1 from pg_database where datname = ?")) {
			ps.setString(1, plantilla);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
							"No existe la base de datos plantilla " + plantilla + " en el motor del padre");
				}
			}
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
					"No se pudo verificar la plantilla " + plantilla, e);
		}
	}

	private void validarPlantillaSinConexiones(Connection admin, String plantilla) throws ServerException {
		try (PreparedStatement ps = admin.prepareStatement(
				"select count(*) from pg_stat_activity where datname = ? and pid <> pg_backend_pid()")) {
			ps.setString(1, plantilla);
			try (ResultSet rs = ps.executeQuery()) {
				int conexiones = 0;
				if (rs.next()) {
					conexiones = rs.getInt(1);
				}
				if (conexiones > 0) {
					throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
							"La plantilla " + plantilla + " tiene " + conexiones
									+ " conexion(es) activa(s); libere la plantilla e intente de nuevo");
				}
			}
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
					"No se pudo verificar el uso de la plantilla " + plantilla, e);
		}
	}

	private List<String> leerDueniosPlantilla(String plantillaUrl, Properties adminProps) throws ServerException {
		List<String> duenos = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(plantillaUrl, adminProps)) {
			String sello = null;
			try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select description from pg_description "
							+ "join pg_class on pg_description.objoid = pg_class.oid "
							+ "join pg_namespace on pg_class.relnamespace = pg_namespace.oid "
							+ "where relname = 'usuario_usrp';")) {
				while (rs.next()) {
					sello = rs.getString("description");
				}
			}
			if (sello == null || sello.isBlank()) {
				throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
						"La plantilla " + BASE_PLANTILLA
								+ " no tiene sello de version (COMMENT ON TABLE usuario_usrp); aplique los deltas sobre la plantilla");
			}
			try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select distinct pg_get_userbyid(nspowner) from pg_namespace where nspname = 'public' "
									+ "union "
									+ "select distinct pg_get_userbyid(c.relowner) from pg_class c "
									+ "inner join pg_namespace n on n.oid = c.relnamespace where n.nspname = 'public' "
									+ "union "
									+ "select distinct pg_get_userbyid(p.proowner) from pg_proc p "
									+ "inner join pg_namespace n on n.oid = p.pronamespace where n.nspname = 'public'")) {
				while (rs.next()) {
					String dueno = rs.getString(1);
					if (dueno != null && !dueno.isBlank() && !duenos.contains(dueno)) {
						duenos.add(dueno);
					}
				}
			}
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
					"No se pudo leer la plantilla " + BASE_PLANTILLA, e);
		}
		if (duenos.isEmpty()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.PLANTILLA,
					"La plantilla " + BASE_PLANTILLA + " no tiene objetos en el esquema public");
		}
		return duenos;
	}

	private void crearBaseDatosConPlantilla(Connection conn, String baseDatos, String plantilla, String rol)
			throws ServerException {
		try {
			try (PreparedStatement existe = conn.prepareStatement("select 1 from pg_database where datname = ?")) {
				existe.setString(1, baseDatos);
				try (ResultSet rs = existe.executeQuery()) {
					if (rs.next()) {
						throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_BASE_DATOS,
								"La base de datos " + baseDatos + " ya existe en el motor");
					}
				}
			}
			try (Statement stmt = conn.createStatement()) {
				stmt.execute("CREATE DATABASE " + entrecomillar(baseDatos) + " WITH TEMPLATE " + entrecomillar(plantilla)
						+ " OWNER " + entrecomillar(rol));
			}
			System.out.println("Base de datos creada desde plantilla " + plantilla + ": " + baseDatos);
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_BASE_DATOS,
					"No se pudo crear la base de datos " + baseDatos + " desde la plantilla " + plantilla, e);
		}
	}

	private void reasignarPropiedadesPlantilla(ServidorPadre servidor, String baseDatos, Properties adminProps,
			List<String> duenos, String rol) throws ServerException {
		String nuevaUrl = "jdbc:postgresql://" + servidor.host + ":" + servidor.puerto + "/" + baseDatos;
		try (Connection conn = DriverManager.getConnection(nuevaUrl, adminProps)) {
			conn.setAutoCommit(true);
			for (String dueno : duenos) {
				if (rol.equals(dueno)) {
					continue;
				}
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("REASSIGN OWNED BY " + entrecomillar(dueno) + " TO " + entrecomillar(rol));
				}
				System.out.println("Propiedades reasignadas de " + dueno + " a " + rol);
			}
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.REASIGNAR_PROPIEDADES,
					"No se pudieron reasignar las propiedades de la plantilla al rol " + rol, e);
		}
	}

	private void aprovisionarDesdePlantilla(String url, String rol, String clave) throws ServerException {
		TenantDTO temporal = new TenantDTO();
		temporal.setDatasourceUrl(url);
		temporal.setDatasourceUsername(rol);
		temporal.setDatasourcePassword(clave);
		DataSource nuevaDs = dataSourceFactory.createPooledDataSource(temporal);
		try {
			System.out.println("Aplicando deltas pendientes sobre BD clonada -> " + new Date());
			scriptExecutor.ejecutarPendientesHastaHoyExigente(nuevaDs);
		} finally {
			if (nuevaDs instanceof PooledDataSource pooled) {
				pooled.forceCloseAll();
			}
		}
	}

	private void validarAdministrador(TenantCrearDTO dto) throws ServerException {
		if (dto.getAdminNombre() == null || dto.getAdminNombre().isBlank()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"El nombre del administrador es obligatorio");
		}
		if (dto.getAdminIdentificacion() == null || dto.getAdminIdentificacion().isBlank()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"La identificacion del administrador es obligatoria");
		}
		String identificacion = dto.getAdminIdentificacion().trim();
		if (identificacion.length() > 32) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"La identificacion del administrador no puede superar 32 caracteres");
		}
		if (dto.getAdminCorreo() == null || dto.getAdminCorreo().isBlank()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"El correo del administrador es obligatorio para enviarle las instrucciones de acceso");
		}
		if (!dto.getAdminCorreo().trim().contains("@")) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"El correo del administrador no es valido: " + dto.getAdminCorreo().trim());
		}
	}

	private void crearAdministrador(String url, String rol, String clave, TenantCrearDTO dto, String nombreTenant,
			String codigo) throws ServerException {
		String identificacion = dto.getAdminIdentificacion().trim();
		String nombreAdmin = dto.getAdminNombre().trim();
		try (Connection conn = DriverManager.getConnection(url, propiedadesConexion(rol, clave))) {
			conn.setAutoCommit(false);
			try {
				if (dto.getImagen() != null && !dto.getImagen().isBlank()) {
					actualizarUna(conn,
							"update usuario_usrp set cusr_identificacion = ?, cusr_nombre = ?, cusr_imagen = ?, cusr_correo = ?, cusr_telefono = ? where cusr_llave = 'SYSTEM'",
							"SYSTEM de usuario_usrp", identificacion, nombreAdmin, dto.getImagen().trim(),
							dto.getAdminCorreo().trim(), dto.getAdminTelefono());
				} else {
					actualizarUna(conn,
							"update usuario_usrp set cusr_identificacion = ?, cusr_nombre = ?, cusr_correo = ?, cusr_telefono = ? where cusr_llave = 'SYSTEM'",
							"SYSTEM de usuario_usrp", identificacion, nombreAdmin, dto.getAdminCorreo().trim(),
							dto.getAdminTelefono());
				}
				Calendar vigencia = Calendar.getInstance();
				vigencia.add(Calendar.MONTH, 2);
				actualizarUna(conn,
						"update usuarioautenticacion_uaup set cuau_sesion = ?, cuau_clave = ?, duau_fechamaxima = ? where cuau_llave = 'SYSTEM'",
						"SYSTEM de usuarioautenticacion_uaup", identificacion, identificacion,
						new Timestamp(vigencia.getTimeInMillis()));
				actualizarUna(conn, "update pedidoventa_pdvp set cpdv_nombre = ? where cpdv_llave = 'SYSTEM'",
						"SYSTEM de pedidoventa_pdvp", identificacion);
				actualizarUna(conn,
						"update pedidoventacaracteristica_pvcp set cpvc_valortext = ? where cpvc_llave = 'SYSTEM-ID'",
						"SYSTEM-ID de pedidoventacaracteristica_pvcp", identificacion);
				actualizarUna(conn,
						"update pedidoventacaracteristica_pvcp set cpvc_valortext = ? where cpvc_llave = 'SYSTEM-NM'",
						"SYSTEM-NM de pedidoventacaracteristica_pvcp", nombreAdmin);
				int organizacion;
				if (dto.getImagen() != null && !dto.getImagen().isBlank()) {
					try (PreparedStatement ps = conn.prepareStatement(
							"update organizacion_orgp set corg_nombre = ?, corg_codigo = ?, corg_imagen = ?, corg_usuariosystem = 'SYSTEM' where corg_llave = 'ORG1'")) {
						ps.setString(1, nombreTenant);
						ps.setString(2, codigo.toUpperCase());
						ps.setString(3, dto.getImagen().trim());
						organizacion = ps.executeUpdate();
					}
				} else {
					try (PreparedStatement ps = conn.prepareStatement(
							"update organizacion_orgp set corg_nombre = ?, corg_codigo = ?, corg_usuariosystem = 'SYSTEM' where corg_llave = 'ORG1'")) {
						ps.setString(1, nombreTenant);
						ps.setString(2, codigo.toUpperCase());
						organizacion = ps.executeUpdate();
					}
				}
				if (organizacion == 0) {
					throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
							"No se encontro la organizacion ORG1 en la base de datos nueva");
				}
				conn.commit();
				System.out.println("Administrador actualizado: " + identificacion);
			} catch (ServerException e) {
				conn.rollback();
				throw e;
			} catch (Exception e) {
				conn.rollback();
				throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
						"No se pudo personalizar el administrador " + identificacion, e);
			}
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"No se pudo conectar a la base de datos nueva con el rol " + rol, e);
		}
	}

	private void actualizarUna(Connection conn, String sql, String fila, Object... valores) throws ServerException {
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < valores.length; i++) {
				ps.setObject(i + 1, valores[i]);
			}
			if (ps.executeUpdate() == 0) {
				throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
						"No se encontro la fila " + fila + " en la base de datos nueva");
			}
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_ADMINISTRADOR,
					"No se pudo actualizar la fila " + fila, e);
		}
	}

	private void asignarUsuario(String key, String codigo, String usuario) throws ServerException {
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			TenantUsuarioFilterDTO existente = new TenantUsuarioFilterDTO();
			existente.setUsuario(usuario);
			existente.setTenant(key);
			if (tenantUsuarioMapper.getOne(existente) != null) {
				System.out.println("El usuario ya tiene asignado el tenant: key=" + key);
				return;
			}
			TenantUsuarioDTO dto = new TenantUsuarioDTO();
			dto.setKey(D3Utils.generarLlave());
			dto.setUsuario(usuario);
			dto.setTenant(key);
			dto.setState(SharedConstants.STATE_ACTIVE);
			tenantUsuarioMapper.insert(dto);
			System.out.println("Tenant asignado al usuario en default: key=" + key);
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.ASIGNAR_USUARIO,
					"No se pudo asignar el tenant al usuario en el catalogo default"
							+ ". Limpieza manual: DROP DATABASE " + entrecomillar("produccion_" + codigo)
							+ "; DROP ROLE " + entrecomillar("d3_" + codigo)
							+ "; delete from tenant_ten where cten_codigo = '" + codigo
							+ "'; delete from tenantusuario_tnu where ctnu_tenant = '" + key + "';",
					e);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	private void notificarAcceso(String padre, TenantCrearDTO dto, String nombreTenant, String codigo) {
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(padre);
			String identificacion = dto.getAdminIdentificacion().trim();
			String base = obtenerBaseRequest();
			String ruta = construirRutaTenant(padre, codigo);
			String acceso = base != null ? base + "/" + ruta
					: "la aplicacion (selecciona el tenant " + codigo + " en el selector)";
			String titulo = "Acceso a " + nombreTenant;
			String texto = "Hola " + dto.getAdminNombre().trim() + ",\n\nSe creo el tenant " + nombreTenant + " ("
					+ codigo + ") y quedaste como administrador.\n\nPara ingresar:\n- URL: " + acceso + "\n- Tenant: "
					+ codigo + "\n- Usuario (sesion): " + identificacion + "\n- Clave inicial: " + identificacion
					+ "\n\nPor seguridad cambia tu clave en el primer ingreso.";
			mailAdminService.call(titulo, texto, dto.getAdminCorreo().trim());
			System.out.println("Instrucciones de acceso enviadas a " + dto.getAdminCorreo().trim());
		} catch (Exception e) {
			System.out.println("[" + TenantProvisionFase.NOTIFICAR_ACCESO + "] No se pudo enviar las "
					+ "instrucciones de acceso a " + (dto.getAdminCorreo() != null ? dto.getAdminCorreo().trim() : "?")
					+ ": " + e.getMessage());
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	private String obtenerBaseRequest() {
		try {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs == null) {
				return null;
			}
			String url = attrs.getRequest().getRequestURL().toString();
			String sufijo = "/multi-tenancy";
			if (url.endsWith(sufijo)) {
				url = url.substring(0, url.length() - sufijo.length());
			}
			url = url.replaceAll("/+$", "");
			return url.isEmpty() ? null : url;
		} catch (Exception e) {
			return null;
		}
	}

	private String construirRutaTenant(String padre, String codigo) {
		try {
			StringBuilder ruta = new StringBuilder();
			if (padre != null && !"default".equals(padre)) {
				String anterior = TenantContext.getCurrentTenant();
				try {
					String contexto = "default";
					for (String parte : padre.split("/")) {
						String segmento = parte.trim();
						if (segmento.isEmpty()) {
							continue;
						}
						TenantContext.setCurrentTenant(contexto);
						TenantDTO fila = buscarPorKeyOCodigo(segmento);
						String codigoNivel = fila != null && fila.getCodigo() != null && !fila.getCodigo().isBlank()
								? fila.getCodigo().trim()
								: segmento;
						if (ruta.length() > 0) {
							ruta.append("/");
						}
						ruta.append(codigoNivel);
						contexto = "default".equals(contexto) ? segmento : contexto + "/" + segmento;
					}
				} finally {
					TenantContext.setCurrentTenant(anterior);
				}
			}
			if (ruta.length() > 0) {
				ruta.append("/");
			}
			ruta.append(codigo);
			return ruta.toString();
		} catch (Exception e) {
			return codigo;
		}
	}

	private TenantDTO buscarPorKeyOCodigo(String segmento) {
		try {
			TenantFilterDTO porKey = new TenantFilterDTO();
			porKey.setKey(segmento);
			TenantDTO fila = tenantMapper.getOne(porKey);
			if (fila != null) {
				return fila;
			}
			TenantFilterDTO porCodigo = new TenantFilterDTO();
			porCodigo.setCodigo(segmento);
			return tenantMapper.getOne(porCodigo);
		} catch (Exception e) {
			return null;
		}
	}

	private void registrarCatalogo(String padre, String key, String nombre, String codigo, String url, String rol,
			String clave, String imagen) throws ServerException {
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(padre);
			TenantDTO fila = new TenantDTO();
			fila.setKey(key);
			fila.setName(nombre);
			fila.setCodigo(codigo);
			fila.setDatasourceUrl(url);
			fila.setDatasourceUsername(rol);
			fila.setDatasourcePassword(clave);
			fila.setState(SharedConstants.STATE_ACTIVE);
			fila.setImagen(imagen);
			tenantMapper.insert(fila);
			System.out.println("Tenant registrado en catalogo de " + padre + ": key=" + key);
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.REGISTRAR_CATALOGO,
					"No se pudo registrar el tenant en el catalogo de " + padre + ". Limpieza manual: DROP DATABASE "
							+ entrecomillar("produccion_" + codigo) + "; DROP ROLE " + entrecomillar("d3_" + codigo)
							+ ";",
					e);
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	private void registrarEnCaliente(String key, String nombre, String codigo, String url, String rol, String clave,
			String imagen) {
		TenantDTO dto = new TenantDTO();
		dto.setKey(key);
		dto.setName(nombre);
		dto.setCodigo(codigo);
		dto.setDatasourceUrl(url);
		dto.setDatasourceUsername(rol);
		dto.setDatasourcePassword(clave);
		dto.setState(SharedConstants.STATE_ACTIVE);
		dto.setImagen(imagen);
		if (metadataProvider instanceof DatabaseTenantMetadataProvider proveedor) {
			proveedor.register(dto);
		}
		if (tenantRegistry instanceof DatabaseTenantRegistry registro) {
			registro.register(key);
		}
	}

	private void avisarAdministrador(String padre, String nombre, String codigo, ServerException error) {
		String anterior = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant(padre);
			String titulo = "Fallo creacion tenant " + nombre + " (" + codigo + ")";
			String texto = "Padre: " + padre + "\nFase: " + error.getOrigen() + "\nCausa: " + error.getTextMessage()
					+ "\nFecha: " + new Date() + "\nLimpieza manual: DROP DATABASE produccion_" + codigo
					+ "; DROP ROLE d3_" + codigo + "; delete from tenant_ten where cten_codigo = '" + codigo + "';";
			mailAdminService.call(titulo, texto);
		} catch (Exception e) {
			System.out.println("No se pudo avisar al administrador: " + e.getMessage());
		} finally {
			TenantContext.setCurrentTenant(anterior);
		}
	}

	private Properties propiedadesConexion(String usuario, String clave) {
		Properties props = new Properties();
		props.setProperty("user", usuario);
		props.setProperty("password", clave);
		props.setProperty("connectTimeout", "10");
		props.setProperty("socketTimeout", "60");
		return props;
	}

	private ServidorPadre extraerServidor(TenantDTO datosPadre) throws ServerException {
		if (datosPadre.getDatasourceUrl() == null) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.VERIFICAR_DUPLICADOS,
					"El tenant padre no tiene URL de conexion configurada");
		}
		Matcher matcher = URL_PADRE.matcher(datosPadre.getDatasourceUrl().trim());
		if (!matcher.matches()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.VERIFICAR_DUPLICADOS,
					"La URL del tenant padre no tiene formato postgres esperado: " + datosPadre.getDatasourceUrl());
		}
		ServidorPadre servidor = new ServidorPadre();
		servidor.host = matcher.group(1);
		servidor.puerto = matcher.group(2) != null ? matcher.group(2) : "5432";
		return servidor;
	}

	private void cargarDriver(TenantDTO datosPadre) throws ServerException {
		String driver = datosPadre.getDriver();
		if (driver == null || driver.isBlank()) {
			driver = env.getProperty("db.driver");
		}
		try {
			Class.forName(driver);
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.CREAR_BASE_DATOS,
					"No se pudo cargar el driver " + driver, e);
		}
	}

	private String entrecomillar(String identificador) {
		return "\"" + identificador.replace("\"", "\"\"") + "\"";
	}

	private String literal(String valor) {
		return "'" + valor.replace("'", "''") + "'";
	}

	private String nombreSeguro(TenantCrearDTO dto) {
		if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
			return "?";
		}
		return dto.getNombre().trim();
	}

	private static class ServidorPadre {
		String host;
		String puerto;
	}
}
