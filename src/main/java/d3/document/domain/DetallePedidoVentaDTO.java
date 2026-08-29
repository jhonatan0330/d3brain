package d3.document.domain;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicParamDTO;
import d3.tariff.domain.TarifaDTO;

@Alias("DetallePedidoVentaDTO")
public class DetallePedidoVentaDTO extends BasicParamDTO {

	private String documento;
	private String producto;
	private String productoTercero;
	private String productoCodigo;
	private String productoImagen;
	private String productoDocumento;
	private String nombre;
	private BigDecimal cantidad;
	private Integer cantidadPromocion;
	private Integer cantidadPromocionBase;
	private BigDecimal cantidadTotal;
	private BigDecimal valorMinimo;
	private BigDecimal valorTotal;
	private BigDecimal valorUnitario;
	private BigDecimal valorMaximo;
	private String plantilla;
	private BigDecimal valorSubtotal;
	private List<TarifaDTO> tarifas;
	private String transaccionRegistro;
	private String transaccionInactivo;
	private String campo;
	private String plantillaDetalle;
	private PedidoVentaDTO documentoDetalle;
	private String detalleId;

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProductoTercero(String productoTercero) {
		this.productoTercero = productoTercero;
	}

	public String getProductoTercero() {
		return productoTercero;
	}

	public void setProductoCodigo(String productoCodigo) {
		this.productoCodigo = productoCodigo;
	}

	public String getProductoCodigo() {
		return productoCodigo;
	}

	public void setProductoImagen(String productoImagen) {
		this.productoImagen = productoImagen;
	}

	public String getProductoImagen() {
		return productoImagen;
	}

	public void setProductoDocumento(String productoDocumento) {
		this.productoDocumento = productoDocumento;
	}

	public String getProductoDocumento() {
		return productoDocumento;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidadPromocion(Integer cantidadPromocion) {
		this.cantidadPromocion = cantidadPromocion;
	}

	public Integer getCantidadPromocion() {
		return cantidadPromocion;
	}

	public void setCantidadPromocionBase(Integer cantidadPromocionBase) {
		this.cantidadPromocionBase = cantidadPromocionBase;
	}

	public Integer getCantidadPromocionBase() {
		return cantidadPromocionBase;
	}

	public void setCantidadTotal(BigDecimal cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public BigDecimal getCantidadTotal() {
		return cantidadTotal;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setValorSubtotal(BigDecimal valorSubtotal) {
		this.valorSubtotal = valorSubtotal;
	}

	public BigDecimal getValorSubtotal() {
		return valorSubtotal;
	}

	public void setTarifas(List<TarifaDTO> tarifas) {
		this.tarifas = tarifas;
	}

	public List<TarifaDTO> getTarifas() {
		return tarifas;
	}

	public void setTransaccionRegistro(String transaccionRegistro) {
		this.transaccionRegistro = transaccionRegistro;
	}

	public String getTransaccionRegistro() {
		return transaccionRegistro;
	}

	public void setTransaccionInactivo(String transaccionInactivo) {
		this.transaccionInactivo = transaccionInactivo;
	}

	public String getTransaccionInactivo() {
		return transaccionInactivo;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public PedidoVentaDTO getDocumentoDetalle() {
		return documentoDetalle;
	}

	public void setDocumentoDetalle(PedidoVentaDTO documentoDetalle) {
		this.documentoDetalle = documentoDetalle;
	}

	public String getDetalleId() {
		return detalleId;
	}

	public void setDetalleId(String detalleId) {
		this.detalleId = detalleId;
	}

	public String getPlantillaDetalle() {
		return plantillaDetalle;
	}

	public void setPlantillaDetalle(String plantillaDetalle) {
		this.plantillaDetalle = plantillaDetalle;
	}
}