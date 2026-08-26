package d3.shared.domain;

public abstract class SharedDataObjectFilter {

	private String key;
	private Integer startRow;
	private Integer endRow;
	private String filter;
	private String state;

	public String getKey() {
		return key;
	}

	public void setKey(String id) {
		this.key = id;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filterText) {
		this.filter = filterText;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
