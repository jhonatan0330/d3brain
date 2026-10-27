package d3.webservice.domain;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import d3.shared.domain.BasicNodoDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@Alias("WebServiceDTO")
public class WebServiceDTO extends BasicNodoDTO {

}