package d3.report.domain;


import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import d3.shared.domain.BasicNodoDTO;

@Alias("ReporteBaseDTO")
@JsonInclude(Include.NON_NULL)
public class ReporteBaseDTO extends BasicNodoDTO {

}