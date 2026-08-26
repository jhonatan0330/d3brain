package d3.fe;

import java.io.IOException;
import java.security.KeyStoreException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import d3.shared.domain.ServerException;
import d3.fe.application.SignerService;
import d3.fe.domain.FEResponse;

import xades4j.XAdES4jException;

@RestController
@RequestMapping("fe")
public class FEController {

    private final SignerService signerService;

    public FEController(@Lazy SignerService signerService) {
        this.signerService = signerService;
    }

    @PostMapping("/sign")
    public FEResponse transformXML(@RequestBody String xml) throws ServerException {
        FEResponse responseFe = new FEResponse();
        try {
            signerService.sign(xml, responseFe, false);
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

    @PostMapping("/signWithZip")
    public FEResponse transformXMLWithZip(@RequestBody String xml) throws ServerException {
        FEResponse responseFe = new FEResponse();
        try {
            signerService.sign(xml, responseFe, true);
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

    @PostMapping("/signNE")
    public FEResponse transformXMLNE(@RequestBody String xml) throws ServerException {
        FEResponse responseFe = new FEResponse();
        try {
            signerService.signNE(xml, responseFe, false);
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

    @PostMapping("/signNEWithZip")
    public FEResponse transformXMLNEWithZip(@RequestBody String xml) throws ServerException {
        FEResponse responseFe = new FEResponse();
        try {
            signerService.signNE(xml, responseFe, true);
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

    @PostMapping("/generateCU")
    public FEResponse generateCUFE(@RequestBody String xml) throws ServerException {
        return signerService.generateCodigo(xml);
    }
    
}
