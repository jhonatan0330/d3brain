package com.softure.fe.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStoreException;
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

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.softure.fe.domain.DirectPasswordProvider;
import com.softure.fe.domain.FirstCertificateSelector;
import com.softure.fe.domain.KeyStoreDataProvider;
import com.softure.java.dto.exception.ServerException;

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
    
    public String zipFileWithoutSaveLocal(String data) throws IOException {

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
        return Base64.getEncoder().encodeToString(bytes);
    }

	public String sign(String xmlIn) throws KeyStoreException,  IOException, XAdES4jException, ParserConfigurationException, TransformerException, SAXException, ServerException {

        Document doc = loadDocument( xmlIn );
        Node elemToSign = selectNode( doc );
        String certificate = getValueInNode( elemToSign, "ext:Certificate");
        String password = getValueInNode( elemToSign, "ext:Password"); 
        initialize( Base64.getDecoder().decode(certificate), password );
        DataObjectDesc DataObjectRef =  createDataObjectToSign();
        sign( DataObjectRef, elemToSign );
        return zipFileWithoutSaveLocal(saveDocument( doc ));
	}
}



