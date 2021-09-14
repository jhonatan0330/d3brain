package com.softure.logisticpymes.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ActividadDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.ProductoDTO;
import com.softure.logisticpymes.dto.ProductoInventarioDTO;
import com.softure.logisticpymes.dto.TarifaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.services.ActividadSvc;
import com.softure.logisticpymes.services.DeduccionProductoSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.ProductoInventarioSvc;
import com.softure.logisticpymes.services.ProductoSvc;
import com.softure.logisticpymes.services.TarifaSvc;
import com.softure.logisticpymes.services.UploadSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/document")
public class DocumentController {

	@Autowired private PedidoVentaSvc pedidoVentaService;
	@Autowired private UploadSvc uploadService;
	@Autowired private ActividadSvc actividadService;
	@Autowired private ProductoSvc productService;
	@Autowired private TarifaSvc tarifaService;
	@Autowired private ProductoInventarioSvc inventoryService;
	@Autowired private DeduccionProductoSvc deduccionService;
	
	@RequestMapping(value="/getDocument", method=RequestMethod.POST)
	public PedidoVentaDTO consultarDocumento(@RequestBody PedidoVentaFilterDTO filter, String token) throws ServerException  {
		return pedidoVentaService.consultaCompleta(filter);
	}
	
	@RequestMapping(value="/getDocuments", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarDocumentos(@RequestBody PedidoVentaFilterDTO filter, @RequestHeader("Authorization") String token) throws ServerException {
		filter.setSecurityToken(token);
		return pedidoVentaService.listarAvanzado(filter);
	}

	@RequestMapping(value="/saveDocument", method=RequestMethod.POST)
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO document, @RequestBody String token)  throws ServerException  {
		PedidoVentaDTO result = new PedidoVentaDTO();
		if(document.getLlaveTabla()==null){
			document = pedidoVentaService.guardar(document, token);
		}else{
			document = pedidoVentaService.actualizar(document, token);
		}
		result.setNombre(document.getNombre());
		result.setPlantilla(document.getPlantilla());
		result.setLlaveTabla(document.getLlaveTabla());
		return result;
	}
	
	@RequestMapping(value="/getDashboard", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarDashboard(@RequestBody String token) throws ServerException {
		PedidoVentaFilterDTO pd = new PedidoVentaFilterDTO();
		pd.setSecurityToken(token);
		return pedidoVentaService.listarUsuario(pd);
	}
	
	@RequestMapping(value="/getUserActivities", method=RequestMethod.GET)
	public List<ActividadDTO> listUserActivities(@RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.listUserActivities(token);
	}
	
	@RequestMapping(value="/readActivity", method=RequestMethod.POST)
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla(), token);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file,  @RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
			return uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), token);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@RequestMapping(value="/reasignar", method=RequestMethod.POST)
	public ActividadDTO reasignar(@RequestBody ActividadDTO asignacion, String token)  throws ServerException  {
		return actividadService.guardar(asignacion, token);	
	}
	
	@GetMapping(value="/getInventory/{id}")
	public List<ProductoInventarioDTO> getInventory(@PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return inventoryService.getByProducto(id);
	}
	
	@GetMapping(value="/getProduct/{id}")
	public ProductoDTO getProduct(@PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return productService.getProduct2Document(id);
	}
	
	@PostMapping(value="/updateProduct")
	public ProductoDTO updateProduct(@RequestBody ProductoDTO product, @RequestHeader("Authorization") String token) throws ServerException {
		return productService.actualizar(product, token);
	}
	
	@GetMapping(value="/getProducts/{filter}")
	public List<ProductoDTO> getProducts(@PathVariable String filter, @RequestHeader("Authorization") String token)  throws ServerException {
		return productService.getProducts2Filter(filter);
	}
	
	@GetMapping(value="/getTarifas/{productId}")
	public List<TarifaDTO> getTarifas2Product(@PathVariable String productId, @RequestHeader("Authorization") String token)  throws ServerException {
		return tarifaService.getTarifas2Product(productId);
	}
	
	@PostMapping(value="/recalculateInventory")
	public void recalculateInventory(@RequestBody String documentId, @RequestHeader("Authorization") String token) throws ServerException {
		deduccionService.recalcularInventarioDocumento(documentId, token);
	}
}
