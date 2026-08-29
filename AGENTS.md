# AGENTS.md — d3brain

## Documentación SDD

Este proyecto se está desarrollando mediante **SDD (Spec-Driven Development)**. La
documentación de especificaciones (requisitos, diseño, casos de uso, tareas) vive en
la carpeta **`sdd/`** (raíz del monorepo, al mismo nivel que `d3brain/` y `d3_front/`).

- Estructura: `sdd/specs/domains/<dominio>/` (`requirements.md`, `design.md`,
  `use-cases-back.md` [contratos/endpoints], `use-cases-front.md` [pasos de UI, si aplica],
  `tasks.md`) y `sdd/docs/`.
- Al implementar o modificar funcionalidades, mantener la documentación SDD
  sincronizada con el código (nombres de clases, endpoints y DTOs reales).

## Descripción
Aplicación Spring Boot 3.5 (Java 17, Gradle) para la gestión de documentos/expedientes (software D3). Backend REST monolítico con multi-tenancy por catálogo JDBC externo (`TenantContext` + `CacheManager` por tenant) y persistencia con MyBatis 3.0.5 sobre PostgreSQL/SQL Server.

## Comandos
- Compilar: `gradlew build` (o `./gradlew.bat` en Windows)
- Compilar sin tests: `gradlew build -x test`
- Clase principal: `d3.Sw42WebApplication`
- No hay linter/typecheck adicional; el compilador de Java es la verificación.
- Config: `src/main/resources/application.properties` (BD, tenant, JWT, Google).

## Convenciones del código
- Código en **español** (mensajes de error, métodos, comentarios). No agregar comentarios salvo que se pidan.
- Sin Spring Security. Autenticación propia por token (ver sección Seguridad).
- Layering por paquete: `domain` (DTO con `@Alias` de MyBatis), `infrastructure` (Mapper/Controller), `application` (Svc).
- Servicios extienden `d3.shared.application.BasicSvc<T, TFilter>`; sobreescriben `consultaXId`, e inyectan el mapper y `@PostConstruct initIt()`.
- Inyección con `@Lazy` en constructores (evitar ciclos). Anotaciones `@Service("nombre")`/`@RestController`.
- Mappers: interfaz anotada `@D3SqlConnMapper(value = "X")` + XML en `src/main/resources/com/...` con el mismo namespace. Columnas BD `cxxx_...` mapeadas a camelCase (`llaveTabla`, `fechaCierre`, etc.).
- Transacciones: `@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)`.
- Errores de dominio: `d3.shared.domain.ServerException` (se serializa al cliente). Estado: `SharedConstants.STATE_ACTIVE` (`A`) / `STATE_INACTIVE` (`I`).
- La sesión/token viaja por header `Authorization` o campo `securityToken` de los DTO.

## Seguridad (arquitectura actual y objetivos)

### Modelo actual
- Login: `UsuarioAutenticacionSvc.autenticar()` valida credenciales y crea una fila en `usuariosesion_ussp` (tabla de sesiones) con llave generada (`D3Utils.generarLlave()`, UUID hex de 32). Ese id era el bearer token opaco.
- Validación por request: `UsuarioSesionSvc.checkToken/getUserFlex` (revisa caché por tenant → BD → estado y `fechaCierre`) y `SharedAuthenticateService.validate/getUser` (implementado por `UsuarioAutenticacionSvc`, usado en `TaskRest`, `VoucherRest`, `AccountApiRest`).

### Objetivo: JWT (en curso)
- **Decisión**: JWT firmado + tabla de sesiones como fuente de verdad para revocación. JWT lleva `jti` = `cuss_llave` de la sesión.
- Librería: **JJWT 0.12.x** (`io.jsonwebtoken:jjwt-api/impl/jackson`).
- `JwtService` (`d3.authentication.application`): genera/parsea/valida JWT HS256.
  - Claims: `sub`/`user` (llave de usuario), `userId`, `userName`, `org`, `tenant`, `jti` (id de sesión), `iat`, `exp`.
  - Config: `jwt.secret` (mínimo 32 bytes), `jwt.expiration` (fallback en ms cuando la sesión no tiene `fechaCierre`), `jwt.issuer`.
- Generación: en `autenticar()` (y flujo Google) después de `usuarioSesionService.guardar(sesion)` → `autenticacion.setToken(jwtService.generate(...))`.
- Validación: `UsuarioSesionSvc` resuelve `jti` si el token es JWT (3 segmentos separados por `.`), si no trata el token como llave opaca (compatibilidad con `getTokenPublic`, `generateAdministratorToken`, flujos internos). Luego consulta la sesión por `jti` en caché/BD.
- Reglas a respetar en validación: estado `A`, `fechaCierre` futura, exp del JWT, `closeAllSession` al cambiar clave (cerrar todas salvo la actual, resolviendo `jti`).

### Objetivo: Google Sign-In (en curso)
- **Decisión**: manual, sin Spring Security. Verificación del `id_token` de Google vía `GoogleIdTokenVerifier` (`com.google.api.client:google-api-client`).
- Config: `google.client-id` (OAuth Client ID de Google).
- Flujo: cliente envía `id_token` → `GoogleAuthenticationService` verifica (`aud` = client-id, `iss`, `exp`) → se busca el usuario por `cusr_correo` (`usuario_usrp`) → validar estado activo → crear sesión + JWT con el mismo flujo que el login normal.
- Endpoint: `POST /main/googleAuthenticate` (`GoogleAuthenticationDTO { idToken, urlServer }`).
- Requisito de negocio: el correo de Google debe existir en `usuario_usrp.cusr_correo`; si no, se rechaza el acceso (no auto-registro por ahora).

### Buenas prácticas obligatorias
- Nunca loguear tokens, secretos o claves. `jwt.secret` y `google.client-id` van en `application.properties` (o variables de entorno) y **no** deben committearse con valores reales.
- Firmar JWT con clave ≥ 32 bytes. No incluir datos sensibles como claims.
- No guardar la clave del usuario en texto plano en flujos nuevos.

