package d3.money.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.shared.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("MovimientoDTO")
public class MovimientoDTO extends BasicDTO
{
	public static final String SALIDA_GASTO = "G";
	public static final String ENTRADA_INGRESO = "I";

	private String tipo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEvento;
	private String cuenta;
	private String cuentaNombre;
	private BigDecimal monto;
	private String turno;
	private BigDecimal montoAplicado;
	private BigDecimal saldoInicial;
	private BigDecimal saldoFinal;
	private String anterior;
	private String siguiente;
	private String relacionado;
	private String documento;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuentaNombre(String cuentaNombre) {
		this.cuentaNombre = cuentaNombre;
	}

	public String getCuentaNombre() {
		return cuentaNombre;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getTurno() {
		return turno;
	}

	public void setMontoAplicado(BigDecimal montoAplicado) {
		this.montoAplicado = montoAplicado;
	}

	public BigDecimal getMontoAplicado() {
		return montoAplicado;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public BigDecimal getSaldoFinal() {
		return saldoFinal;
	}

	public void setAnterior(String anterior) {
		this.anterior = anterior;
	}

	public String getAnterior() {
		return anterior;
	}

	public void setSiguiente(String siguiente) {
		this.siguiente = siguiente;
	}

	public String getSiguiente() {
		return siguiente;
	}

	public void setRelacionado(String relacionado) {
		this.relacionado = relacionado;
	}

	public String getRelacionado() {
		return relacionado;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

}