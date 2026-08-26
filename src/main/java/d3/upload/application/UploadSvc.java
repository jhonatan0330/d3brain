package d3.upload.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.logisticpymes.application.ServidorSvc;
import d3.logisticpymes.domain.ServidorDTO;
import d3.upload.domain.CargaArchivoDTO;
import org.springframework.context.annotation.Lazy;

@Service("uploadService")
public class UploadSvc {

	private final CargaArchivoSvc cargaService;
	private final ServidorSvc servidorService;

	public UploadSvc(@Lazy CargaArchivoSvc cargaService, @Lazy ServidorSvc servidorService) {
		this.cargaService = cargaService;
		this.servidorService = servidorService;
	}

	public String uploadFile(byte[] bytes, String name, String token, String typeFile, String pVisibility)
			throws ServerException {
		if (typeFile == null)
			typeFile = "files";
		CargaArchivoDTO registro = new CargaArchivoDTO();
		registro.setFechaInicio(new Date());
		registro.setSize(bytes.length);
		if (token != null)
			registro.setUsuario(cargaService.getUserFlex(token));

		ServidorDTO _server = servidorService.resolveServer();
		registro.setServidor(_server.getLlaveTabla());

		try {
			registro.setUrl(uploadWithServer(_server, bytes, name, typeFile, pVisibility));
		} catch (Exception e) {
			registro.setError(e.getMessage());
			throw new ServerException(e.getMessage(), e);
		} finally {
			cargaService.guardar(registro, null);
		}
		return registro.getUrl();
	}

	private String uploadWithServer(ServidorDTO pServer, byte[] pBytes, String pName, String pType, String pVisibility)
			throws IOException, ServerException {
		if (pServer == null || pServer.getTipo() == null)
			return null;
		if (pServer.getTipo().equals(ServidorDTO.FTP)) {
			return uploadToFTP(pServer, pBytes, pName, pType);
		} else {
			return uploadToLocal(pServer, pBytes, pName, pType, pVisibility);
		}
	}

	private String uploadToLocal(ServidorDTO pServer, byte[] pBytes, String pName, String pType, String pVisibility)
			throws IOException, ServerException {

		String baseDir = Optional.ofNullable(pServer.getBase())
				.orElseThrow(() -> new ServerException("No se configuró la carpeta base para almacenamiento local"));

		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

		Path uploadDir = Paths.get(baseDir, pVisibility, pType, year, month, day);
		Files.createDirectories(uploadDir);

		String extension = getExtension(pName);
		String uniqueName = UUID.randomUUID().toString().replace("-", "") + extension;
		Path filePath = uploadDir.resolve(uniqueName);

		Files.write(filePath, pBytes, StandardOpenOption.CREATE_NEW);

		return filePath.toString().replace(baseDir, "");
	}
	/*
	 * private String uploadLocal(ServidorDTO pServer, byte[] pBytes, String pName,
	 * String pType) throws IOException, ServerException { try { String uploadPath =
	 * pServer.getUrl(); String folderBase = pServer.getBase(); if (folderBase ==
	 * null) throw new ServerException(
	 * "No se a configurado la CARPETA BASE base del servidor FTP, en donde se puede consultar los documentos"
	 * );
	 * 
	 * uploadPath = uploadPath + File.separator + folderBase; if (!new
	 * File(uploadPath).exists()) new File(uploadPath).mkdir(); uploadPath =
	 * uploadPath + File.separator + pType; if (!new File(uploadPath).exists()) new
	 * File(uploadPath).mkdir(); uploadPath = uploadPath + File.separator +
	 * String.valueOf(Calendar.getInstance().get(Calendar.YEAR)); if (!new
	 * File(uploadPath).exists()) new File(uploadPath).mkdir(); uploadPath =
	 * uploadPath + File.separator +
	 * String.valueOf(Calendar.getInstance().get(Calendar.MONTH)); if (!new
	 * File(uploadPath).exists()) new File(uploadPath).mkdir(); uploadPath =
	 * uploadPath + File.separator +
	 * String.valueOf(Calendar.getInstance().get(Calendar.DATE)); if (!new
	 * File(uploadPath).exists()) new File(uploadPath).mkdir();
	 * 
	 * String fileName = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) +
	 * "_" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH)) + "_" +
	 * String.valueOf(Calendar.getInstance().get(Calendar.DATE)) + "_" +
	 * UUID.randomUUID().toString().replaceAll("-", "") + getExtension(pName);
	 * String filePath = uploadPath + File.separator + fileName; File storeFile =
	 * new File(filePath); // saves the file on disk try { BufferedOutputStream
	 * stream = new BufferedOutputStream(new FileOutputStream(storeFile));
	 * stream.write(pBytes); stream.close(); } catch (Exception e) { throw new
	 * ServerException(e.getMessage()); } return fileName;
	 * 
	 * } catch (Exception e) { throw new ServerException(e.getMessage()); } }
	 * 
	 * private String uploadFTP(ServidorDTO pServer, byte[] pBytes, String pName,
	 * String pTypeFile) throws ServerException {
	 * 
	 * FTPClient ftpClient = new FTPClient(); String server = pServer.getUrl(); int
	 * port = 21; String user = pServer.getUsuario(); String pass =
	 * pServer.getClave(); String urlBase = pServer.getUrlConexion(); String
	 * folderBase = pServer.getBase(); if (server == null || user == null || pass ==
	 * null) throw new ServerException(
	 * "Parametros FTP incompletos, o coloque la propiedad FILE_SERVER, para almacenar los recursos en su equipo"
	 * ); if (urlBase == null) throw new ServerException(
	 * "No se a configurado la URL base del servidor FTP, en donde se puede consultar los documentos"
	 * ); if (folderBase == null) throw new ServerException(
	 * "No se a configurado la CARPETA BASE base del servidor FTP, en donde se puede consultar los documentos"
	 * ); String dirToCreate = folderBase; String extension = getExtension(pName);
	 * String fileName = UUID.randomUUID().toString().replaceAll("-", "") +
	 * extension; String urlFinal = null; try { ftpClient.connect(server, port); int
	 * replyCode = ftpClient.getReplyCode(); if
	 * (!FTPReply.isPositiveCompletion(replyCode)) { throw new
	 * ServerException("Operation failed. Server reply code: " +
	 * showServerReply(ftpClient)); } boolean success = ftpClient.login(user, pass);
	 * if (!success) { throw new ServerException("Could not login to the server" +
	 * showServerReply(ftpClient)); } // Creates a directory
	 * createInFolder(ftpClient, dirToCreate);
	 * 
	 * if (pTypeFile != null) { dirToCreate = dirToCreate + "/" + pTypeFile;
	 * createInFolder(ftpClient, pTypeFile); }
	 * 
	 * SimpleDateFormat sm = new SimpleDateFormat("yyyy"); String yearFolder =
	 * sm.format(new Date()); dirToCreate = dirToCreate + "/" + yearFolder;
	 * createInFolder(ftpClient, yearFolder);
	 * 
	 * sm.applyPattern("MM"); String monthFolder = sm.format(new Date());
	 * dirToCreate = dirToCreate + "/" + monthFolder; createInFolder(ftpClient,
	 * monthFolder);
	 * 
	 * sm.applyPattern("dd"); String dayFolder = sm.format(new Date()); dirToCreate
	 * = dirToCreate + "/" + dayFolder; createInFolder(ftpClient, dayFolder);
	 * 
	 * ftpClient.enterLocalPassiveMode();
	 * ftpClient.setFileType(FTP.BINARY_FILE_TYPE); success =
	 * ftpClient.storeFile(fileName, new ByteArrayInputStream(pBytes)); if
	 * (!success) throw new ServerException(showServerReply(ftpClient)); urlFinal =
	 * new URI(urlBase + "/" + dirToCreate + "/" + fileName).normalize().toString();
	 * } catch (IOException ex) { throw new ServerException(ex.getMessage() +
	 * "\nDetalle" + ex.getLocalizedMessage() + " DIR: " + dirToCreate); } catch
	 * (URISyntaxException e) { throw new ServerException(e.getMessage() +
	 * " . ERROR Creando la URL"); } finally { try { if (ftpClient.isConnected()) {
	 * ftpClient.logout(); ftpClient.disconnect(); } } catch (IOException ex) {
	 * throw new ServerException(ex.getMessage()); } } return urlFinal; }
	 * 
	 * private void createInFolder(FTPClient ftpClient, String folder) throws
	 * IOException, ServerException { try {
	 * ftpClient.changeWorkingDirectory(folder); } catch (Exception e) {
	 * ftpClient.makeDirectory(folder); ftpClient.changeWorkingDirectory(folder); }
	 * int returnCode = ftpClient.getReplyCode(); if (returnCode == 550) { boolean
	 * success = ftpClient.makeDirectory(folder); if (!success) { throw new
	 * ServerException( "Failed to create directory " + folder +
	 * ". See server's reply." + showServerReply(ftpClient)); } else {
	 * ftpClient.changeWorkingDirectory(folder); } } }
	 */

	private String uploadToFTP(ServidorDTO server, byte[] bytes, String name, String type) throws ServerException {

		FTPClient ftpClient = new FTPClient();
		String host = server.getUrl();
		int port = 21;
		String user = server.getUsuario();
		String pass = server.getClave();
		String urlBase = server.getUrlConexion();
		String folderBase = server.getBase();

		if (host == null || user == null || pass == null)
			throw new ServerException("Parámetros FTP incompletos");

		if (urlBase == null)
			throw new ServerException("No se configuró la URL base del servidor FTP");

		if (folderBase == null)
			throw new ServerException("No se configuró la carpeta base del servidor FTP");

		String extension = getExtension(name);
		String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

		String finalUrl;
		try {
			ftpClient.connect(host, port);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply))
				throw new ServerException("Respuesta del servidor no positiva: " + showServerReply(ftpClient));

			if (!ftpClient.login(user, pass))
				throw new ServerException("No se pudo iniciar sesión en el servidor FTP");

			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// Crear estructura de carpetas
			String remoteDir = buildFTPDirectory(ftpClient, folderBase, type);

			// Subir archivo
			boolean stored = ftpClient.storeFile(fileName, new ByteArrayInputStream(bytes));
			if (!stored)
				throw new ServerException("No se pudo subir el archivo. " + showServerReply(ftpClient));

			// Construir URL final
			finalUrl = new URI(urlBase + "/" + remoteDir + "/" + fileName).normalize().toString();

		} catch (IOException | URISyntaxException e) {
			throw new ServerException("Error en FTP: " + e.getMessage(), e);
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ignore) {
			}
		}

		return finalUrl;
	}

	// --------------------------------------------------------
	// CREAR DIRECTORIOS EN FTP
	// --------------------------------------------------------
	private String buildFTPDirectory(FTPClient ftpClient, String base, String type)
			throws IOException, ServerException {

		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));

		List<String> dirs = Arrays.asList(base, type, year, month, day);
		StringBuilder currentPath = new StringBuilder();

		for (String dir : dirs) {
			if (dir == null || dir.isEmpty())
				continue;
			currentPath.append(dir).append("/");
			if (!ftpClient.changeWorkingDirectory(dir)) {
				ftpClient.makeDirectory(dir);
				ftpClient.changeWorkingDirectory(dir);
			}
		}

		return currentPath.toString().replaceAll("/$", "");
	}

	private String getExtension(String name) {
		if (name != null && name.contains(".")) {
			return name.substring(name.lastIndexOf("."));
		}
		return "";
	}

	private static String showServerReply(FTPClient ftpClient) {
		StringBuilder sb = new StringBuilder("SERVER: ");
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null)
			for (String reply : replies)
				sb.append(reply).append(" ");
		return sb.toString();
	}

	public byte[] transformBase64ToPDF(String b64) {
		return Base64.getDecoder().decode(b64);
	}
}
