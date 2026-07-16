package com.softure.fe.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
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
import com.softure.upload.application.UploadSvc;

import xades4j.XAdES4jException;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.BasicSignatureOptions;
import xades4j.production.DataObjectReference;
import xades4j.production.SignatureAlgorithms;
import xades4j.production.SignatureAppendingStrategies;
import xades4j.production.SignedDataObjects;
import xades4j.production.SigningCertificateMode;
import xades4j.production.XadesEpesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.properties.ObjectIdentifier;
import xades4j.properties.SignaturePolicyBase;
import xades4j.properties.SignaturePolicyIdentifierProperty;
import xades4j.properties.SignaturePolicyImpliedProperty;
import xades4j.properties.SignerRoleProperty;
import xades4j.properties.SigningTimeProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SignaturePolicyInfoProvider;
import xades4j.providers.SignaturePropertiesCollector;
import xades4j.providers.SignaturePropertiesProvider;
import xades4j.utils.XadesProfileResolutionException;
import xades4j.verification.SigningCertificateReferenceNotFoundException;
import org.springframework.context.annotation.Lazy;

@Service
public class SignerService {

	private final UploadSvc uploadService;

	public SignerService(@Lazy UploadSvc uploadService) {
		this.uploadService = uploadService;
	}

	private XadesSigner signer;
	private String policyUrl = "https://facturaelectronica.dian.gov.co/politicadefirma/v2/politicadefirmav2.pdf";

	private void initialize(String certificate, String password)
			throws KeyStoreException, XadesProfileResolutionException {

		SignaturePolicyInfoProvider policyInfoProvider = new SignaturePolicyInfoProvider() {
			public SignaturePolicyBase getSignaturePolicy() {
				try {
					return new SignaturePolicyIdentifierProperty(new ObjectIdentifier(policyUrl),
							new URI(policyUrl).toURL().openStream());
				} catch (MalformedURLException ex) {
					return new SignaturePolicyImpliedProperty();
				} catch (IOException ex) {
					return new SignaturePolicyImpliedProperty();
				} catch (URISyntaxException e) {
					return new SignaturePolicyImpliedProperty();
				}
			}
		};
		File file = getFile(certificate);
		KeyingDataProvider kp = KeyStoreDataProvider.builder("pkcs12", file, new FirstCertificateSelector())
				.storePassword(new DirectPasswordProvider(password)).entryPassword(new DirectPasswordProvider(password))
				.build();

		// sobreescribe las propiedades de signer para agregar "supplier" como rol para
		// cumplir con la DIAN
		XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider)
				.withSignaturePropertiesProvider(new SignaturePropertiesProvider() {
					@Override
					public void provideProperties(SignaturePropertiesCollector arg0) {
						SigningTimeProperty sigTime = new SigningTimeProperty();
						arg0.setSignerRole(new SignerRoleProperty().withClaimedRole("supplier"));
						arg0.setSigningTime(sigTime);
					}
				})
				.withBasicSignatureOptions(new BasicSignatureOptions().includePublicKey(true).includeSubjectName(false)
						// .signKeyInfo(true)
						.includeSigningCertificate(SigningCertificateMode.SIGNING_CERTIFICATE))
				.withSignatureAlgorithms(new SignatureAlgorithms().withSignatureAlgorithm("RSA",
						"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"));
		//
		// XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider);
		signer = p.newSigner();
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

	private Node selectNode(Document doc) {
		NodeList tag = doc.getElementsByTagName("ext:ExtensionContent");
		return tag.item(tag.getLength() - 1);
	}

	private String getValueInNode(Node node, String tag) throws ServerException {
		if (node instanceof Element docElement) {
			NodeList tags = docElement.getElementsByTagName(tag);
			if (tags.getLength() == 1) {
				return tags.item(0).getTextContent();
			}
		}
		throw new ServerException(
				"En el Xml es importatnte que dentro del tag (ext:ExtensionContent) coloque un nuevo tag (" + tag
						+ ") que nos brinde la informacion del certificado");
	}

	private DataObjectDesc createDataObjectToSign() {
		return new DataObjectReference("").withTransform(new EnvelopedSignatureTransform())
				.withDataObjectFormat(new DataObjectFormatProperty("text/xml"));
	}

	private void sign(DataObjectDesc dataObjRef, Node elemToSign) throws XAdES4jException, ServerException {

		try {

			signer.sign(new SignedDataObjects(dataObjRef), elemToSign, SignatureAppendingStrategies.AsFirstChild);
		} catch (SigningCertificateReferenceNotFoundException enf) {
			throw new ServerException("Certificado no encontrado");
		} catch (XAdES4jException exa) {
			throw new ServerException(exa.getMessage());
		} catch (Exception ex) {
			throw new ServerException("Certificado vencido");
		}
	}

	private String saveDocument(Document doc) throws TransformerException {

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		Result output = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();

		transformer.transform(domSource, output);
		return writer.toString();
	}

	public void zipFileWithoutSaveLocal(String data, FEResponse responseFe, String fileNameInZip, boolean generateZip)
			throws IOException, ServerException {

		// Hay algo similar en mailsendmessage
		responseFe.setXmlUrl(uploadService.uploadFile(data.getBytes(), "fe.xml", null, "fe_xml", "private"));
		responseFe.setXml(Base64.getEncoder().encodeToString(data.getBytes()));

		if (generateZip) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ZipOutputStream zos = new ZipOutputStream(baos)) {

				ZipEntry zipEntry = new ZipEntry(fileNameInZip);
				zos.putNextEntry(zipEntry);

				ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
				byte[] buffer = new byte[1024];
				int len;
				while ((len = bais.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				zos.closeEntry();
			}
			byte[] bytes = baos.toByteArray();
			responseFe.setZipUrl(uploadService.uploadFile(bytes, "fe.zip", null, "fe_zip", "private"));
			responseFe.setZipBase64(Base64.getEncoder().encodeToString(bytes));
		}
	}

	public void sign(String xmlIn, FEResponse responseFe, boolean generateZip) throws KeyStoreException, IOException,
			XAdES4jException, ParserConfigurationException, TransformerException, SAXException, ServerException {
		Document doc = loadDocument(xmlIn);
		// removeEmptyNodes(doc);
		doc = processCUFE(doc, responseFe);
		doc = processSoftwareSecurityCode(doc);
		// Primero tomo la informacion de los archivos adjuntos
		doc = decriptFilesBase64(doc);
		doc = processExtensionContent(doc);
		getSignatureValue(doc, responseFe);
		zipFileWithoutSaveLocal(saveDocument(doc), responseFe, getName(doc), generateZip);
	}

	public void signNE(String xmlIn, FEResponse responseFe, boolean generateZip) throws KeyStoreException, IOException,
			XAdES4jException, ParserConfigurationException, TransformerException, SAXException, ServerException {
		Document doc = loadDocument(xmlIn);
		// removeEmptyNodes(doc);
		doc = processCUNE(doc, responseFe);
		doc = processSoftwareSecurityCodeNE(doc);
		// Primero tomo la informacion de los archivos adjuntos
		doc = decriptFilesBase64(doc);
		doc = processExtensionContent(doc);
		getSignatureValue(doc, responseFe);
		zipFileWithoutSaveLocal(saveDocument(doc), responseFe, getName(doc), generateZip);
	}

	public FEResponse generateCodigo(String xmlIn) throws ServerException {
		FEResponse response = new FEResponse();
		response.setCufe(encryptThisString(xmlIn));
		response.setResult("200");
		return response;
	}

	private void getSignatureValue(Document doc, FEResponse responseFe) {
		// Para facilitar el envio a la DIAn extrayendo estos datos
		NodeList tags = doc.getElementsByTagName("ds:X509Certificate");
		if (tags.getLength() > 0)
			responseFe.setX509Certificate(tags.item(0).getTextContent().replace("\n", "").replace("\r", ""));
		tags = doc.getElementsByTagName("ds:SignatureValue");
		if (tags.getLength() > 0)
			responseFe.setSignatureValue(tags.item(0).getTextContent().replace("\n", "").replace("\r", ""));
		tags = doc.getElementsByTagName("ds:DigestValue");
		if (tags.getLength() > 0)
			responseFe.setDigestValue(tags.item(0).getTextContent().replace("\n", "").replace("\r", ""));
	}

	private String getName(Document doc) {
		String name = "fe.xml";
		NodeList tags = doc.getElementsByTagName("cbc:ID");
		if (tags.getLength() > 0)
			name = tags.item(0).getTextContent() + ".xml";
		return name;
	}

	private Document processExtensionContent(Document doc) throws KeyStoreException, IOException, XAdES4jException,
			ParserConfigurationException, TransformerException, SAXException, ServerException {
		Node elemToSign = selectNode(doc);
		String certificate = getValueInNode(elemToSign, "ext:Certificate");
		String password = getValueInNode(elemToSign, "ext:Password");
		initialize(certificate, password);
		DataObjectDesc DataObjectRef = createDataObjectToSign();
		elemToSign.setTextContent("");
		sign(DataObjectRef, elemToSign);
		return doc;
	}

	private Document processCUFE(Document doc, FEResponse responseFe) throws ServerException {
		NodeList tags = doc.getElementsByTagName("cbc:UUID");
		if (tags.getLength() == 0)
			throw new ServerException("No se identifico el tag del CUFE cbc:UUID");
		String CUFEplain = tags.item(0).getTextContent();
		if (CUFEplain == null || CUFEplain.isEmpty())
			throw new ServerException("El texto del CUFE esta vacio");
		String CUFEencrypt = encryptThisString(CUFEplain);
		tags.item(0).setTextContent(CUFEencrypt);
		responseFe.setCufe(CUFEencrypt);
		return processQR(doc, CUFEencrypt);
	}

	private Document processCUNE(Document doc, FEResponse responseFe) throws ServerException {
		NodeList tags = doc.getElementsByTagName("InformacionGeneral");
		if (tags.getLength() == 0)
			throw new ServerException("No se identifico el tag del CUNE InformacionGeneral");
		String CUFEplain = tags.item(0).getAttributes().getNamedItem("CUNE").getNodeValue();
		if (CUFEplain == null || CUFEplain.isEmpty())
			throw new ServerException("El texto del CUFE esta vacio");
		String CUFEencrypt = encryptThisString(CUFEplain);
		for (int i = 0; i < tags.getLength(); i++) {
			tags.item(i).getAttributes().getNamedItem("CUNE").setTextContent(CUFEencrypt);
		}
		responseFe.setCufe(CUFEencrypt);
		return processQRNE(doc, CUFEencrypt);
	}

	private Document processQRNE(Document doc, String cufe) throws ServerException {
		NodeList tags = doc.getElementsByTagName("CodigoQR");
		if (tags.getLength() == 0)
			return doc;// throw new ServerException("No se identifico el tag del CUFE sts:QRCode");
		String plain = tags.item(0).getTextContent();
		if (plain == null || plain.isEmpty())
			return doc; // throw new ServerException("El texto del sts:QRCode esta vacio");
		tags.item(0).setTextContent(plain.replace("REPLACE_CUFE_CODE", cufe));
		return doc;
	}

	private Document processQR(Document doc, String cufe) throws ServerException {
		NodeList tags = doc.getElementsByTagName("sts:QRCode");
		if (tags.getLength() == 0)
			return doc;// throw new ServerException("No se identifico el tag del CUFE sts:QRCode");
		String plain = tags.item(0).getTextContent();
		if (plain == null || plain.isEmpty())
			return doc; // throw new ServerException("El texto del sts:QRCode esta vacio");
		tags.item(0).setTextContent(plain.replace("REPLACE_CUFE_CODE", cufe));
		return doc;
	}

	private Document processSoftwareSecurityCode(Document doc) throws ServerException {
		NodeList tags = doc.getElementsByTagName("sts:SoftwareSecurityCode");
		if (tags.getLength() == 0)
			return doc; // throw new ServerException("No se identifico el tag del
						// sts:SoftwareSecurityCode");
		String plain = tags.item(0).getTextContent();
		if (plain == null || plain.isEmpty())
			return doc; // throw new ServerException("El texto del sts:SoftwareSecurityCode esta
						// vacio");
		String encrypt = encryptThisString(plain);
		tags.item(0).setTextContent(encrypt);
		return doc;
	}

	private Document processSoftwareSecurityCodeNE(Document doc) throws ServerException {
		NodeList tags = doc.getElementsByTagName("ProveedorXML");
		if (tags.getLength() == 0)
			return doc;
		String plain = tags.item(0).getAttributes().getNamedItem("SoftwareSC").getNodeValue();
		if (plain == null || plain.isEmpty())
			return doc;
		String encrypt = encryptThisString(plain);
		tags.item(0).getAttributes().getNamedItem("SoftwareSC").setTextContent(encrypt);
		return doc;
	}

	// Esto lo uso para el attachment
	private Document decriptFilesBase64(Document doc) throws ServerException {
		NodeList tags = doc.getElementsByTagName("cbc:Description");
		if (tags.getLength() == 0)
			return doc;

		for (int i = 0; i < tags.getLength(); i++) {
			String plain = tags.item(i).getTextContent();
			if (plain != null && !plain.isEmpty()) {
				if (plain.startsWith("http"))
					plain = getHtmlContent(plain);
				if (plain.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")) {
					plain = new String(Base64.getDecoder().decode(plain.getBytes()));
					Node cdata = doc.createCDATASection(plain);
					tags.item(i).setTextContent("");
					tags.item(i).appendChild(cdata);
				} else {
					// Node cdata = doc.createCDATASection(plain);
					// tags.item(i).setTextContent("");
					// tags.item(i).appendChild(plain);
				}
			}
		}
		return doc;
	}

	private String encryptThisString(String input) throws ServerException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-384");

			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);

			String hashtext = no.toString(16);

			while (hashtext.length() < 96) {
				hashtext = "0" + hashtext;
			}

			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new ServerException(e.getMessage());
		}
	}

	/*
	 * private void removeEmptyNodes(Node node) { NodeList nodeList =
	 * node.getChildNodes(); for (int i = 0; i < nodeList.getLength(); i++) { Node
	 * childNode = nodeList.item(i); if (childNode.getTextContent().equals("") &&
	 * childNode.getAttributes().getLength()==0) {
	 * childNode.getParentNode().removeChild(childNode); i--; }
	 * removeEmptyNodes(childNode); } }
	 */

	private String getHtmlContent(String _url) {
		URL url;

		try {
			url = new URI(_url).toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			return _url;
		}

		/*
		 * try (Scanner sc = new Scanner(url.openStream(), StandardCharsets.UTF_8)) {
		 * return sc.useDelimiter("\\A").next(); }
		 */
		try (Scanner sc = new Scanner(url.openStream())) {
			StringBuilder sb = new StringBuilder();

			while (sc.hasNext()) {
				sb.append(sc.next());
			}

			return sb.toString();

		} catch (IOException e) {
			return e.getLocalizedMessage();
		}
	}

}
