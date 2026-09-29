package d3.multitenancy.domain;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonProperty;

@Alias("PwaManifestIconDTO")
public class PwaManifestIconDTO {

	private String src;

	private String sizes;

	private String type;

	private String purpose;

	public PwaManifestIconDTO() {
	}

	public PwaManifestIconDTO(String src, String sizes, String type, String purpose) {
		this.src = src;
		this.sizes = sizes;
		this.type = type;
		this.purpose = purpose;
	}

	@JsonProperty("src")
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@JsonProperty("sizes")
	public String getSizes() {
		return sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("purpose")
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

}
