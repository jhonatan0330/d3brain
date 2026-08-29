package d3.mail.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicDTO;

@Alias("MensajePlantillaCorreoDTO")
public class MensajePlantillaCorreoDTO extends BasicDTO
{

	private String nombre;
	private String titulo;
	private String texto;
	private String servidor;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getServidor() {
		return servidor;
	}

}