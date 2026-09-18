package d3.inventory;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.inventory.application.ProductoInventarioSvc;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.shared.domain.ServerException;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	private final ProductoInventarioSvc inventoryService;

	public InventoryController(@Lazy ProductoInventarioSvc inventoryService) {
		this.inventoryService = inventoryService;
	}


	@GetMapping(value = "/getInventory/{id}")
	public List<ProductoInventarioDTO> getInventory(@PathVariable("id") String pId) throws ServerException {
		return inventoryService.getByProducto(pId);
	}

	
}
