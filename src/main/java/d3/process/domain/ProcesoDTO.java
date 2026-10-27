package d3.process.domain;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicNodoDTO;

@Alias("ProcesoDTO")
public class ProcesoDTO extends BasicNodoDTO
{
	public static final String AGRUPADOR = "A";
	public static final String EJECUTOR = "E";

}