package com.softure.upload.infrastructure;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import com.softure.java.dto.exception.ServerException;
import com.softure.upload.application.UploadSvc;

public class UploaderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	// upload settings
	private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
	private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

	@Autowired private UploadSvc uploadService;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// checks if the request actually contains upload file
		if (!ServletFileUpload.isMultipartContent(request)) {
			// if not, we stop here
			PrintWriter writer = response.getWriter();
			writer.println("Error: Form must has enctype=multipart/form-data.");
			writer.flush();
			return;
		}

		PrintWriter out = null;
		try {
			out  = response.getWriter();
			String result;
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(MEMORY_THRESHOLD);
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(MAX_FILE_SIZE);
			upload.setSizeMax(MAX_REQUEST_SIZE);
			String auth = request.getHeader("Authorization");
			try {
				List<FileItem> formItems = upload.parseRequest(request);
				if (formItems != null && formItems.size() > 0) {
					for (FileItem item : formItems) {
						if (!item.isFormField()) {
							result = uploadService.uploadFile(item.get(), item.getName(), auth, null);
							out.println("<result><operation state='true'>" + result + "</operation></result>");
						}
					}
				}
			} catch (FileUploadException e) {
				out.write("<result><operation state='false'>" + e.getMessage() + "</operation></result>");
			}
		} catch (ServerException e1) {
			out.write("<result><operation state='false'>" + e1.getMessage() + "</operation></result>");
		}finally {
			out.close();
		}

		return;
	}

}