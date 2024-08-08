package com.softure.upload.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DownloaderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	//@Autowired @Lazy  
	private ServidorSvc servidorService;
	
	public DownloaderServlet(ServidorSvc _servidorService) {
		this.servidorService =_servidorService;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException {
		try {
			String fileName =req.getParameter("nombre");
			if(fileName != ""){
				if (fileName.contains(".pdf")) {
					resp.setContentType("application/pdf");
				}			
				else if (fileName.contains(".xls")) {
					resp.setContentType("application/vnd.ms-excel");
				}
				else if (fileName.contains(".doc")) {
					resp.setContentType("application/msword");
				}
				else if (fileName.contains(".config")) {
					resp.setContentType("text/plain");
				}
				else if(fileName.contains(".jrxml")){
					resp.setContentType("application/jrxml");
				}
				else if (fileName.contains(".js")){
					resp.setContentType("text/javascript");
				}
				else if (fileName.contains(".htm")){
					resp.setContentType("text/html");
				}
				else if (fileName.contains(".css")){
					resp.setContentType("text/css");
				}
				else if (fileName.contains(".gif")){
					resp.setContentType("image/gif");
				}
				else if (fileName.contains(".png")){
					resp.setContentType("image/png");
				}
				else if (fileName.contains(".jpg")){
					resp.setContentType("image/jpg");
				}
				else if (fileName.contains(".txt")){
					resp.setContentType("application/msword");
				}
				ServidorDTO servidor = servidorService.obtenerServidorPrincipal(ServidorDTO.LOCAL_FTP);
				if(servidor==null) throw new ServerException("Configure el servidor LOCAL FTP");
				String serverPath = servidor.getUrl();
				if(fileName.startsWith("SW42_TEMP_")) {
					//serverPath =propiedadSistemaService.obtenerValor(Propiedades.TEMP);
					//if(serverPath== null)
					throw new IOException("Configure el parametro TEMP");
				}else {
					if(serverPath== null) throw new IOException("Configure la ruta de los reportes FILE_SERVER");
					String fileServerFolderBase = servidor.getBase();
					if(fileServerFolderBase==null) throw new ServerException("Revise la propiedad base del servidor" + servidor.getNombre());
					serverPath = serverPath + File.separator+ fileServerFolderBase;
					if(Character.isDigit(fileName.charAt(0))){
						int posSeparator = -1;
						int lastSeparator = 0;
						for(int i = 0;i <3 ;i++){
							posSeparator =fileName.indexOf("_" ,posSeparator+1);
							if(posSeparator!=-1){
								serverPath = serverPath + File.separator + fileName.substring(lastSeparator, posSeparator);
								lastSeparator = posSeparator +1;
							}else{
								break;
							}
						}
					}
				}
				
				serverPath = serverPath+ File.separator ;
				
				File directorio = new File(serverPath);
				
				if (directorio.exists()) {
					File f = new File(serverPath + fileName);
					RandomAccessFile raf = new RandomAccessFile(serverPath+ fileName, "r");
					FileInputStream fis = new FileInputStream(f);
					FileReader fr = new FileReader(f);
					resp.setDateHeader("Expires", 0);
					resp.setHeader("Content-Disposition", "inline; filename=\"" +fileName + "\"");
					byte b[] = new byte[(int) f.length()];
					raf.read(b);
					OutputStream out = resp.getOutputStream();
					out.write(b);
					out.close();
					raf.close();
					fis.close();
					fr.close();
				}
			}
		} catch (ServerException e) {
			throw new IOException(e.getMessage());
		}
	}
}