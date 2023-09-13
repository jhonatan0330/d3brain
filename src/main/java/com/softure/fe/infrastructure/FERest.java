package com.softure.fe.infrastructure;

import java.io.IOException;
import java.security.KeyStoreException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.softure.fe.application.SignerService;
import com.softure.fe.domain.FEResponse;
import com.softure.java.dto.exception.ServerException;

import xades4j.XAdES4jException;

@RestController
@RequestMapping("fe")
public class FERest {

	@Autowired
	SignerService signerService;

	@PostMapping("/sign")
	public FEResponse transformXML(@RequestBody String xml) throws ServerException {
		FEResponse responseFe = new FEResponse();
		try {
			responseFe.setXml(signerService.sign(xml));
			responseFe.setResult("200");
		} catch (KeyStoreException e) {
			responseFe.setError(e.getMessage());
			responseFe.setResult("400");
		} catch (IOException e) {
			responseFe.setError(e.getMessage());
			responseFe.setResult("400");
		} catch (XAdES4jException e) {
			responseFe.setError(e.getMessage() + " :" + e.getCause().getCause().toString());
			responseFe.setResult("400");
		} catch (ParserConfigurationException e) {
			responseFe.setError(e.getMessage());
			responseFe.setResult("400");
		} catch (TransformerException e) {
			responseFe.setError(e.getMessage());
			responseFe.setResult("400");
		} catch (SAXException e) {
			responseFe.setError(e.getMessage());
			responseFe.setResult("400");
		}
		return responseFe;
	}
}
