package com.softure.fe.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.softure.fe.domain.DirectPasswordProvider;
import com.softure.fe.domain.FEResponse;
import com.softure.fe.domain.FirstCertificateSelector;
import com.softure.fe.domain.KeyStoreDataProvider;
import com.softure.java.dto.exception.ServerException;
import com.softure.upload.application.UploadSvc;

import xades4j.XAdES4jException;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.DataObjectReference;
import xades4j.production.SignatureAppendingStrategies;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesEpesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.ObjectIdentifier;
import xades4j.properties.SignaturePolicyBase;
import xades4j.properties.SignaturePolicyIdentifierProperty;
import xades4j.properties.SignaturePolicyImpliedProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SignaturePolicyInfoProvider;
import xades4j.utils.XadesProfileResolutionException;

@Service
public class SignerService {

	@Autowired
	private UploadSvc uploadService;
	
    private XadesSigner signer;
    private String policyUrl = "https://facturaelectronica.dian.gov.co/politicadefirma/v2/politicadefirmav2.pdf";
    
    private void initialize( byte[] keyByte, String password) throws KeyStoreException, XadesProfileResolutionException {

        SignaturePolicyInfoProvider policyInfoProvider = new SignaturePolicyInfoProvider() {
            public SignaturePolicyBase getSignaturePolicy() {
                try {
                    return new SignaturePolicyIdentifierProperty( new ObjectIdentifier( policyUrl ), new URL( policyUrl ).openStream() );
                } catch (MalformedURLException ex) {
                    return new SignaturePolicyImpliedProperty();
                } catch (IOException ex) {
                    return new SignaturePolicyImpliedProperty();
                }
            }
        };
        File file = null;
		try {
			file = File.createTempFile("FE_DIAN_", ".pfx");
			//FileUtils.writeByteArrayToFile(file, keyByte);
			try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
	            fos.write(keyByte);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
        KeyingDataProvider kp = KeyStoreDataProvider
        		.builder("pkcs12", file, new FirstCertificateSelector())
        		.storePassword(new DirectPasswordProvider(password))
        		.entryPassword(new DirectPasswordProvider(password))
        		.build();
 
        XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider);
        signer = p.newSigner();
    }

    private Document loadDocument( String xmlInPath ) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlInPath)));
    }

    private Node selectNode( Document doc ){
        NodeList tag = doc.getElementsByTagName("ext:ExtensionContent");
        return tag.item(1);
    }
    
    private String getValueInNode( Node node, String tag ) throws ServerException{
    	if(node instanceof Element) {
    		 Element docElement = (Element)node;
    		 NodeList tags = docElement.getElementsByTagName(tag);
    	     if(tags.getLength()==1) {
    	    	 return tags.item(0).getTextContent();	 
    	     }
    	}
    	throw new ServerException("En el Xml es importatnte que dentro del tag (ext:ExtensionContent) coloque un nuevo tag (" + tag +") que nos brinde la informacion del certificado");
    }

    private DataObjectDesc createDataObjectToSign(){
        return new DataObjectReference("").withTransform(new EnvelopedSignatureTransform());
    }

    private void sign( DataObjectDesc dataObjRef, Node elemToSign ) throws XAdES4jException {

        signer.sign(new SignedDataObjects( dataObjRef ), elemToSign, SignatureAppendingStrategies.AsFirstChild);
    }

    private String saveDocument( Document doc ) throws TransformerException {

    	DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        Result output = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();        

        transformer.transform(domSource, output);
        return writer.toString();
    }
    
    public String zipFileWithoutSaveLocal(String data, FEResponse responseFe) throws IOException, ServerException {

        String fileNameInZip = "fe.xml";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            ZipEntry zipEntry = new ZipEntry(fileNameInZip);
            zos.putNextEntry(zipEntry);

            ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
            // one line, able to handle large size?
            //zos.write(bais.readAllBytes());

            // play safe
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bais.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            zos.closeEntry();
        }
        byte[] bytes = baos.toByteArray();
        responseFe.setZipUrl( uploadService.uploadFile(bytes, "fe.zip", null, "fe_zip"));
        return Base64.getEncoder().encodeToString(bytes);
    }

	public void sign(String xmlIn, FEResponse responseFe) throws KeyStoreException,  IOException, XAdES4jException, ParserConfigurationException, TransformerException, SAXException, ServerException {
        Document doc = loadDocument( xmlIn );
        doc = processCUFE(doc, responseFe);
        doc = processSoftwareSecurityCode(doc);
        doc = processExtensionContent(doc);
        removeEmptyNodes(doc);
        responseFe.setXml( zipFileWithoutSaveLocal(saveDocument( doc ), responseFe));
	}
	
	private Document processExtensionContent(Document doc) throws KeyStoreException,  IOException, XAdES4jException, ParserConfigurationException, TransformerException, SAXException, ServerException{
		Node elemToSign = selectNode( doc );
        String certificate = getValueInNode( elemToSign, "ext:Certificate");
        String password = getValueInNode( elemToSign, "ext:Password"); 
        initialize( Base64.getDecoder().decode(certificate), password );
        DataObjectDesc DataObjectRef =  createDataObjectToSign();
        elemToSign.setTextContent("");
        sign( DataObjectRef, elemToSign );
		return doc;
	}
	
	private Document processCUFE(Document doc, FEResponse responseFe) throws ServerException {
		NodeList tags = doc.getElementsByTagName("cbc:UUID");
		if(tags.getLength()==0) throw new ServerException("No se identifico el tag del CUFE cbc:UUID");
        String CUFEplain = tags.item(0).getTextContent();
        if(CUFEplain==null || CUFEplain.isEmpty()) throw new ServerException("El texto del CUFE esta vacio");
        String CUFEencrypt = encryptThisString(CUFEplain);
        tags.item(0).setTextContent(CUFEencrypt);
        responseFe.setCufe(CUFEencrypt);
		return processQR(doc, CUFEencrypt);
	}
	
	private Document processQR(Document doc, String cufe) throws ServerException {
		NodeList tags = doc.getElementsByTagName("sts:QRCode");
		if(tags.getLength()==0) throw new ServerException("No se identifico el tag del CUFE sts:QRCode");
        String plain = tags.item(0).getTextContent();
        if(plain==null || plain.isEmpty()) throw new ServerException("El texto del sts:QRCode esta vacio");
        tags.item(0).setTextContent(plain.replace("REPLACE_CUFE_CODE", cufe));
		return doc;
	}
	
	private Document processSoftwareSecurityCode(Document doc) throws ServerException {
		NodeList tags = doc.getElementsByTagName("sts:SoftwareSecurityCode");
		if(tags.getLength()==0) throw new ServerException("No se identifico el tag del sts:SoftwareSecurityCode");
        String plain = tags.item(0).getTextContent();
        if(plain==null || plain.isEmpty()) throw new ServerException("El texto del sts:SoftwareSecurityCode esta vacio");
        String encrypt = encryptThisString(plain);
        tags.item(0).setTextContent(encrypt);
		return doc;
	}
	
	private String encryptThisString(String input) throws ServerException{
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
 
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
  
            String hashtext = no.toString(16);
  
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
  
            return hashtext;
        }      catch (NoSuchAlgorithmException e) {
            throw new ServerException(e.getMessage());
        }
    }
	
	private void removeEmptyNodes(Node node) {
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i < nodeList.getLength(); i++){
			Node childNode = nodeList.item(i);
			if(childNode.getTextContent().equals("")){
				childNode.getParentNode().removeChild(childNode);
				i--;
			}
			removeEmptyNodes(childNode);
		}
	}

}



