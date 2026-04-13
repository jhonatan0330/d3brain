package com.softure.fe.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.xml.security.utils.XMLUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.shared.domain.ServerException;
import com.softure.fe.domain.DirectPasswordProvider;
import com.softure.fe.domain.FEResponse;
import com.softure.fe.domain.FirstCertificateSelector;
import com.softure.fe.domain.KeyStoreDataProvider;

import xades4j.XAdES4jException;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.algorithms.GenericAlgorithm;
import xades4j.production.BasicSignatureOptions;
import xades4j.production.DataObjectReference;
import xades4j.production.SignatureAppendingStrategies;
import xades4j.production.SignedDataObjects;
import xades4j.production.SigningCertificateMode;
import xades4j.production.XadesBesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.verification.SigningCertificateReferenceNotFoundException;

@Service
public class DianWsSecuritySigner {

    private XadesSigner signer;
    
    

    private void initialize() throws Exception {

    	String certificate = "https://fs10.softwareparati.com/box/2025/12/24/e96a6b5035a94a4ba8a1391aa8fb43d8.p12";
    	String password = "JeUKQsEBVAMIouzl";
		File file = getFile(certificate);
		KeyingDataProvider kp = KeyStoreDataProvider.builder("pkcs12", file, new FirstCertificateSelector())
				.storePassword(new DirectPasswordProvider(password)).entryPassword(new DirectPasswordProvider(password))
				.build();

        XadesSigningProfile profile =
                new XadesBesSigningProfile(kp)
                .withBasicSignatureOptions(
                        new BasicSignatureOptions()
                        .includePublicKey(false)
                        .includeSubjectName(false)
                        .signKeyInfo(true)
                        .omitSigningCertificateProperty(true)
                        //.includeSigningCertificate(SigningCertificateMode.SIGNING_CERTIFICATE)
                );

        signer = profile.newSigner();
    }

    public Document signHeader(String xmlIn, FEResponse responseFe, boolean generateZip) throws Exception {

       String xmlFile = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wcf=\"http://wcf.dian.colombia\">"
       		+ "<soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">"
       		+ "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"
       		+ "<wsu:Timestamp wsu:Id=\"TS-19F6D69D1B0868FB47176660956255812\">"
       		+ "<wsu:Created>2025-12-24T20:52:42.558Z</wsu:Created>"
       		+ "<wsu:Expires>2025-12-25T13:32:42.558Z</wsu:Expires>"
       		+ "</wsu:Timestamp>"
       		+ "<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"X509-19F6D69D1B0868FB4717666095625157\">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</wsse:BinarySecurityToken>"
       		+ "</wsse:Security>"
       		+ "<wsa:Action>http://wcf.dian.colombia/IWcfDianCustomerServices/SendBillSync</wsa:Action>"
       		+ "<wsa:To wsu:Id=\"id-19F6D69D1B0868FB47176660956252710\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">https://vpfe-hab.dian.gov.co/WcfDianCustomerServices.svc</wsa:To>"
       		+ "</soap:Header>"
       		+ "<soap:Body>"
       		+ "<wcf:SendBillSync>"
       		+ "<wcf:contentFile>{{FE_CONTENT_FILE}}</wcf:contentFile>"
       		+ "</wcf:SendBillSync>"
       		+ "</soap:Body>"
       		+ "</soap:Envelope>";
        Document doc = loadDocument(xmlFile);
        
     // Marcar todos los wsu:Id como atributos Id
        markWsuIdAttributes(doc);

        // Obtener el nodo wsse:Security
        XPath xp = XPathFactory.newInstance().newXPath();
        xp.setNamespaceContext(new WsNamespaceContext());

        Element securityEl = (Element) xp.evaluate(
                "/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Security']",
                doc, XPathConstants.NODE
        );

        if (securityEl == null) {
            throw new RuntimeException("No se encontró wsse:Security");
        }

        // Definir DataObject para firmar wsa:To
        String toId = xp.evaluate(
                "/*[local-name()='Envelope']/*[local-name()='Header']//*[local-name()='To']/@wsu:Id",
                doc
        );
        
        Element toElement = (Element) doc.getElementsByTagNameNS(
                "http://www.w3.org/2005/08/addressing",
                "To"
        ).item(0);
        
        toElement.setIdAttributeNS(
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
                "Id",
                true
        );

        initialize();
     
       // DataObjectReference toObject = new DataObjectReference("#" + toId);

        // Transformación CANONICAL EXCLUSIVO
        //toObject.withTransform(
        	//	new GenericAlgorithm("http://www.w3.org/2001/10/xml-exc-c14n#")
        //);


        //SignedDataObjects signedObjs = new SignedDataObjects(toObject);

        DataObjectDesc DataObjectRef = new DataObjectReference("#" + toId)
				.withTransform(new GenericAlgorithm("http://www.w3.org/2001/10/xml-exc-c14n#"));
        
        System.out.println(
        	    toElement
        	);
        
        String _d = saveDocument(doc);
        // Firmar e insertar ds:Signature dentro de Security
        sign(DataObjectRef, securityEl);
        zipFileWithoutSaveLocal(saveDocument(doc), responseFe);
        return doc;
        

        
    }
    
    private void sign(DataObjectDesc dataObjRef, Node elemToSign) throws XAdES4jException, ServerException {

		try {
			
			signer.sign(new SignedDataObjects(dataObjRef), elemToSign, SignatureAppendingStrategies.AsLastChild);
		}catch (SigningCertificateReferenceNotFoundException enf) {
            throw new ServerException("Certificado no encontrado");
		} catch (XAdES4jException exa) {
			throw new ServerException(exa.getMessage());
		}
		catch(Exception ex) {
			throw new ServerException("Certificado vencido");
		}
	}

    private void markWsuIdAttributes(Document doc) {
        NodeList all = doc.getElementsByTagNameNS(
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
                "Id"
        );

        for (int i = 0; i < all.getLength(); i++) {
            Element el = (Element) all.item(i);
            el.setIdAttributeNS(
                    "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
                    "Id", true
            );
        }
    }
    
    
    //////////////////
    ///Copiado de Signer
    ///
    private Document loadDocument(String xmlInPath) throws IOException, SAXException, ParserConfigurationException {
		try {
			Field f = XMLUtils.class.getDeclaredField("ignoreLineBreaks");
			f.setAccessible(true);
			f.set(null, Boolean.TRUE);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// en las FE varios no mbre tiene & ej:J&G, estos nombres mostraban error
		xmlInPath = xmlInPath.replaceAll("\\&", "\\<\\!\\[CDATA\\[\\&\\]\\]\\>");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlInPath)));
	}
    
    private File getFile(String certificate) {

		try {
			File file = File.createTempFile("FE_DIAN_", ".pfx");
			if (certificate.startsWith("http")) {
				FileUtils.copyURLToFile(new URI(certificate).toURL(), file);
			} else {
				byte[] keyByte = Base64.getDecoder().decode(certificate);
				// FileUtils.writeByteArrayToFile(file, keyByte);
				try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
					fos.write(keyByte);
				}
			}
			return file;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    private String saveDocument(Document doc) throws TransformerException {

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		Result output = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();

		transformer.transform(domSource, output);
		return writer.toString();
	}
    
    public void zipFileWithoutSaveLocal(String data, FEResponse responseFe)
			throws IOException, ServerException {

		// Hay algo similar en mailsendmessage
		responseFe.setXmlUrl(data);

	}
}
