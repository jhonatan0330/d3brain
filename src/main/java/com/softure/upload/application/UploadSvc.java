package com.softure.upload.application;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.java.services.CompressionUtils;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.upload.domain.CargaArchivoDTO;

@Service("uploadService")
public class UploadSvc {

	@Autowired
	private CargaArchivoSvc cargaService;
	@Autowired
	private ServidorSvc servidorService;

	public String uploadFile(byte[] bytes, String name, String token, String typeFile) throws ServerException {
		CargaArchivoDTO registro = new CargaArchivoDTO();
		registro.setFechaInicio(new Date());
		registro.setSize(bytes.length);
		if (token != null)
			registro.setUsuario(cargaService.getUserFlex(token));
		ServidorDTO uploadPath = servidorService.obtenerServidorPrincipal(ServidorDTO.LOCAL_FTP);
		if (uploadPath != null) {
			try {
				registro.setUrl(uploadLocal(bytes, uploadPath, name));
				cargaService.guardar(registro, null);
				return registro.getUrl();
			} catch (IOException e) {
				registro.setError(e.getMessage());
				cargaService.guardar(registro, null);
				throw new ServerException(e.getMessage());
			}
		} else {
			try {
				registro.setUrl(uploadFTP(bytes, name, registro, typeFile));
				cargaService.guardar(registro, null);
				return registro.getUrl();
			} catch (Exception e) {
				registro.setError(e.getMessage());
				cargaService.guardar(registro, null);
				throw new ServerException(e.getMessage());
			}
		}
	}

	public byte[] transformBase64ToPDF(String b64) {
		return Base64.getDecoder().decode(b64);
	}

	private String uploadFTP(byte[] bytes, String name, CargaArchivoDTO registro, String typeFile)
			throws ServerException {
		ServidorDTO servidor = servidorService.obtenerServidorPrincipal(ServidorDTO.FTP);
		if (servidor == null)
			throw new ServerException("Configure el servidor FTP");

		registro.setServidor(servidor.getLlaveTabla());

		FTPClient ftpClient = new FTPClient();
		String server = servidor.getUrl();
		int port = 21;
		String user = servidor.getUsuario();
		String pass = servidor.getClave();
		String urlBase = servidor.getUrlConexion();
		String folderBase = servidor.getBase();
		if (server == null || user == null || pass == null)
			throw new ServerException(
					"Parametros FTP incompletos, o coloque la propiedad FILE_SERVER, para almacenar los recursos en su equipo");
		if (urlBase == null)
			throw new ServerException(
					"No se a configurado la URL base del servidor FTP, en donde se puede consultar los documentos");
		if (folderBase == null)
			throw new ServerException(
					"No se a configurado la CARPETA BASE base del servidor FTP, en donde se puede consultar los documentos");
		String dirToCreate = folderBase;
		String extension = getExtension(name);
		String fileName = UUID.randomUUID().toString().replaceAll("-", "") + extension;
		String urlFinal = null;
		try {
			ftpClient.connect(server, port);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new ServerException("Operation failed. Server reply code: " + showServerReply(ftpClient));
			}
			boolean success = ftpClient.login(user, pass);
			if (!success) {
				throw new ServerException("Could not login to the server" + showServerReply(ftpClient));
			}
			// Creates a directory
			createInFolder(ftpClient, dirToCreate);

			if (typeFile != null) {
				dirToCreate = dirToCreate + "/" + typeFile;
				createInFolder(ftpClient, typeFile);
			}

			SimpleDateFormat sm = new SimpleDateFormat("yyyy");
			String yearFolder = sm.format(new Date());
			dirToCreate = dirToCreate + "/" + yearFolder;
			createInFolder(ftpClient, yearFolder);

			sm.applyPattern("MM");
			String monthFolder = sm.format(new Date());
			dirToCreate = dirToCreate + "/" + monthFolder;
			createInFolder(ftpClient, monthFolder);

			sm.applyPattern("dd");
			String dayFolder = sm.format(new Date());
			dirToCreate = dirToCreate + "/" + dayFolder;
			createInFolder(ftpClient, dayFolder);

			if (extension.toLowerCase().compareTo(".jpg") == 0 || extension.toLowerCase().compareTo(".jpeg") == 0)
				bytes = CompressionUtils.compress(bytes);

			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			success = ftpClient.storeFile(fileName, new ByteArrayInputStream(bytes));
			if (!success)
				throw new ServerException(showServerReply(ftpClient));
			urlFinal = new URI(urlBase + "/" + dirToCreate + "/" + fileName).normalize().toString();
		} catch (IOException ex) {
			throw new ServerException(ex.getMessage() + "\nDetalle" + ex.getLocalizedMessage() + " DIR: " + dirToCreate);
		} catch (URISyntaxException e) {
			throw new ServerException(e.getMessage() + " . ERROR Creando la URL");
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				throw new ServerException(ex.getMessage());
			}
		}
		return urlFinal;
	}

	private void createInFolder(FTPClient ftpClient, String folder) throws IOException, ServerException {
		try {
			ftpClient.changeWorkingDirectory(folder);
		} catch (Exception e) {
			ftpClient.makeDirectory(folder);
			ftpClient.changeWorkingDirectory(folder);
		}
		int returnCode = ftpClient.getReplyCode();
		if (returnCode == 550) {
			boolean success = ftpClient.makeDirectory(folder);
			if (!success) {
				throw new ServerException(
						"Failed to create directory " + folder + ". See server's reply." + showServerReply(ftpClient));
			} else {
				ftpClient.changeWorkingDirectory(folder);
			}
		}
	}

	private String uploadLocal(byte[] bytes, ServidorDTO servidor, String name) throws IOException, ServerException {
		try {
			String uploadPath = servidor.getUrl();
			String folderBase = servidor.getBase();
			if (folderBase == null)
				throw new ServerException(
						"No se a configurado la CARPETA BASE base del servidor FTP, en donde se puede consultar los documentos");

			uploadPath = uploadPath + File.separator + folderBase;
			if (!new File(uploadPath).exists())
				new File(uploadPath).mkdir();
			uploadPath = uploadPath + File.separator + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			if (!new File(uploadPath).exists())
				new File(uploadPath).mkdir();
			uploadPath = uploadPath + File.separator + String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
			if (!new File(uploadPath).exists())
				new File(uploadPath).mkdir();
			uploadPath = uploadPath + File.separator + String.valueOf(Calendar.getInstance().get(Calendar.DATE));
			if (!new File(uploadPath).exists())
				new File(uploadPath).mkdir();

			String fileName = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + "_"
					+ String.valueOf(Calendar.getInstance().get(Calendar.MONTH)) + "_"
					+ String.valueOf(Calendar.getInstance().get(Calendar.DATE)) + "_"
					+ UUID.randomUUID().toString().replaceAll("-", "") + getExtension(name);
			String filePath = uploadPath + File.separator + fileName;
			File storeFile = new File(filePath);
			// saves the file on disk
			try {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(storeFile));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				throw new ServerException(e.getMessage());
			}
			return fileName;

		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
	}

	private String getExtension(String nombre) {
		if (nombre != null) {
			int lastPoint = nombre.lastIndexOf(".");
			if (lastPoint > 0) {
				return nombre.substring(lastPoint);
			}
		}
		return "";
	}

	private static String showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		String result = "SERVER: ";
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				result = result + aReply;
			}
		}
		return result;
	}

}
