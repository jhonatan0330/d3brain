
package com.softure.fe.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.xml.security.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.shared.domain.ServerException;

@Service
public class DianSoapSecurityHeader {

	private static final Logger logger = LoggerFactory.getLogger(DianSoapSecurityHeader.class);

	private static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";
	private static final String DS_NS = "http://www.w3.org/2000/09/xmldsig#";

	private static final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
	private static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
	private static final String C14N_EXCL = "http://www.w3.org/2001/10/xml-exc-c14n#";

	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			.withZone(ZoneOffset.UTC);

	
	public String signHeaderTest(String xmlIn) throws ServerException {

		// Desactivar warnings de XML Security
		try {
			Field f = XMLUtils.class.getDeclaredField("ignoreLineBreaks");
			f.setAccessible(true);
			f.set(null, Boolean.TRUE);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			System.out.println("No se pudo desactivar ignoreLineBreaks: {}");
		}

		// Construir y firmar el envelope SOAP
		Document signedDoc;
		try {
			signedDoc = buildSignedSoapEnvelope(xmlIn, obtenerValorCertificateBase(xmlIn),
					obtenerValorCertificatePasword(xmlIn));
			// Guardar el resultado
			try {
				DOMSource domSource = new DOMSource(signedDoc);
				StringWriter writer = new StringWriter();
				Result output = new StreamResult(writer);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(domSource, output);
				xmlIn = writer.toString();

			} catch (TransformerException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlIn;
	}

	private static String obtenerValorCertificateBase(String texto) {
		Pattern pattern = Pattern.compile("REPLACE_CERTIFICATE_BASE\\((.*?)\\)");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String obtenerValorCertificatePasword(String texto) {
		Pattern pattern = Pattern.compile("REPLACE_CERTIFICATE_PASSWORD\\((.*?)\\)");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	
	private Document buildSignedSoapEnvelope(String bodyXml, String certificateUrl, String certificatePassword)
			throws Exception {

		File certFile = downloadCertificate(certificateUrl);
		KeyStore keyStore = loadKeyStore(certFile, certificatePassword);
		String alias = keyStore.aliases().nextElement();
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, certificatePassword.toCharArray());
		X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
		String timestampId = "TS-" + generateId();
		String bstId = "X509-" + generateId();
		String sigId = "SIG-" + generateId();
		String keyInfoId = "KI-" + generateId();
		String strId = "STR-" + generateId();
		String toId = "id-" + generateId();
		Document doc = createSoapEnvelope(bodyXml, timestampId, bstId, toId);
		return signDocument(doc, privateKey, certificate, bstId, toId, sigId, keyInfoId, strId);

	}

	private Document createSoapEnvelope(String fullXml, String timestampId, String bstId, String toId)
			throws Exception {
		Instant created = Instant.now();
		Instant expiration = created.plusSeconds(300);
		String now = ISO_FORMATTER.format(created);
		String expires = ISO_FORMATTER.format(expiration);
		String soapXml = "<soap:Header xmlns:wsa=\"" + WSA_NS + "\">" + "<wsse:Security xmlns:wsse=\"" + WSSE_NS
				+ "\" xmlns:wsu=\"" + WSU_NS + "\">" + "<wsu:Timestamp wsu:Id=\"" + timestampId + "\">"
				+ "<wsu:Created>" + now + "</wsu:Created>" + "<wsu:Expires>" + expires + "</wsu:Expires>"
				+ "</wsu:Timestamp>" + "<wsse:BinarySecurityToken"
				+ " EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\""
				+ " ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\""
				+ " wsu:Id=\"" + bstId + "\">PLACEHOLDER_CERTIFICATE</wsse:BinarySecurityToken>" + "</wsse:Security>"
				+ "<wsa:Action>http://wcf.dian.colombia/IWcfDianCustomerServices/GetAcquirer</wsa:Action>"
				+ "<wsa:To wsu:Id=\"" + toId + "\" xmlns:wsu=\"" + WSU_NS
				+ "\">https://vpfe.dian.gov.co/WcfDianCustomerServices.svc</wsa:To>" + "</soap:Header>";
		fullXml = fullXml.replaceFirst("<soap:Header.*?</soap:Header>", soapXml);
		Document doc = loadDocument(fullXml);
		markAllWsuidAsId(doc);
		return doc;
	}

	private Document loadDocument(String xmlInPath) throws IOException, SAXException, ParserConfigurationException {
		try {
			Field f = XMLUtils.class.getDeclaredField("ignoreLineBreaks");
			f.setAccessible(true);
			f.set(null, Boolean.TRUE);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		// en las FE varios no mbre tiene & ej:J&G, estos nombres mostraban error
		xmlInPath = xmlInPath.replaceAll("\\&", "\\<\\!\\[CDATA\\[\\&\\]\\]\\>");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlInPath)));
	}

	private void setCertificateInToken(Document doc, X509Certificate certificate) {
		NodeList bstNodes = doc.getElementsByTagNameNS(WSSE_NS, "BinarySecurityToken");
		if (bstNodes.getLength() > 0) {
			Element bstElement = (Element) bstNodes.item(0);
			try {
				byte[] certDer = certificate.getEncoded();
				String certBase64 = java.util.Base64.getEncoder().encodeToString(certDer);
				bstElement.setTextContent(certBase64);
			} catch (Exception e) {
				logger.error("Error al codificar certificado", e);
			}
		}
	}

	private void markAllWsuidAsId(Document doc) {
		NodeList allElements = doc.getElementsByTagName("*");
		for (int i = 0; i < allElements.getLength(); i++) {
			Element el = (Element) allElements.item(i);
			String idValue = el.getAttributeNS(WSU_NS, "Id");
			if (idValue != null && !idValue.isEmpty()) {
				el.setIdAttributeNS(WSU_NS, "Id", true);
			}
		}
	}

	private Document signDocument(Document doc, PrivateKey privateKey, X509Certificate certificate, String bstId,
			String toId, String sigId, String keyInfoId, String strId) throws Exception {
		setCertificateInToken(doc, certificate);
		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM");
		ExcC14NParameterSpec signedInfoC14nParams = new ExcC14NParameterSpec(List.of("wsa", "soap", "wcf"));
		CanonicalizationMethod c14nMethod = sigFactory.newCanonicalizationMethod(C14N_EXCL, signedInfoC14nParams);
		SignatureMethod sigMethod = sigFactory.newSignatureMethod(RSA_SHA256, null);
		ExcC14NParameterSpec referenceC14nParams = new ExcC14NParameterSpec(List.of("soap", "wcf"));
		Transform c14nTransform = sigFactory.newTransform(C14N_EXCL, referenceC14nParams);
		List<Transform> transforms = List.of(c14nTransform);
		Reference refTo = sigFactory.newReference("#" + toId, sigFactory.newDigestMethod(SHA256, null), transforms,
				null, null);
		SignedInfo signedInfo = sigFactory.newSignedInfo(c14nMethod, sigMethod, List.of(refTo));
		KeyInfo keyInfo = createDianKeyInfo(sigFactory, doc, bstId, strId, keyInfoId);
		XMLSignature xmlSignature = sigFactory.newXMLSignature(signedInfo, keyInfo, null, sigId, null);
		NodeList securityNodes = doc.getElementsByTagNameNS(WSSE_NS, "Security");
		if (securityNodes.getLength() == 0) {
			throw new IllegalStateException("No se encontró wsse:Security");
		}
		Element securityElement = (Element) securityNodes.item(0);
		Element toElement = findElementByWsuId(doc, toId);
		if (toElement == null) {
			throw new IllegalStateException("No se encontró wsa:To con wsu:Id=" + toId);
		}
		toElement.setIdAttributeNS(WSU_NS, "Id", true);
		DOMSignContext signContext = new DOMSignContext(privateKey, securityElement);
		signContext.putNamespacePrefix(DS_NS, "ds");
		signContext.putNamespacePrefix(WSU_NS, "wsu");
		signContext.putNamespacePrefix(WSSE_NS, "wsse");
		signContext.putNamespacePrefix(WSA_NS, "wsa");
		xmlSignature.sign(signContext);
		return doc;
	}

	
	private KeyInfo createDianKeyInfo(XMLSignatureFactory sigFactory, Document doc, String bstId, String strId,
			String keyInfoId) {
		KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
		Element securityTokenReference = doc.createElementNS(WSSE_NS, "wsse:SecurityTokenReference");
		securityTokenReference.setAttributeNS(WSU_NS, "wsu:Id", strId);
		// Registrar el wsu:Id como ID real
		securityTokenReference.setIdAttributeNS(WSU_NS, "Id", true);
		Element reference = doc.createElementNS(WSSE_NS, "wsse:Reference");
		reference.setAttribute("URI", "#" + bstId);
		reference.setAttribute("ValueType",
				"http://docs.oasis-open.org/wss/2004/01/" + "oasis-200401-wss-x509-token-profile-1.0#X509v3");
		securityTokenReference.appendChild(reference);
		DOMStructure domStructure = new DOMStructure(securityTokenReference);
		// IMPORTANTE:
		// crear KeyInfo con su Id desde el principio
		return keyInfoFactory.newKeyInfo(List.of(domStructure), keyInfoId);
	}

	private Element findElementByWsuId(Document doc, String id) {
		NodeList allElements = doc.getElementsByTagName("*");
		for (int i = 0; i < allElements.getLength(); i++) {
			Element element = (Element) allElements.item(i);
			String value = element.getAttributeNS(WSU_NS, "Id");
			if (id.equals(value)) {
				return element;
			}
		}
		return null;
	}

	private File downloadCertificate(String certificateUrl) throws IOException, URISyntaxException {
		File tempFile = File.createTempFile("dian_cert_", ".p12");
		tempFile.deleteOnExit();

		if (certificateUrl.startsWith("http")) {
			FileUtils.copyURLToFile(new URI(certificateUrl).toURL(), tempFile);
		} else {
			byte[] certBytes = java.util.Base64.getDecoder().decode(certificateUrl);
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(certBytes);
			}
		}

		return tempFile;
	}

	private KeyStore loadKeyStore(File file, String password) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		try (InputStream is = java.nio.file.Files.newInputStream(file.toPath())) {
			keyStore.load(is, password.toCharArray());
		}
		return keyStore;
	}

	private String generateId() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
	}

}
