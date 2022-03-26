package sql.query.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterList {
	@JsonProperty("select_group")
	private String selectGroup;
	
	
	private String isMinus;
	
	@JsonProperty("filter_cols")
	private FilterCols[] filterCols;

	public String getSelectGroup() {
		return selectGroup;
	}

	public void setSelectGroup(String selectGroup) {
		this.selectGroup = selectGroup;
	}

	public String isMinus() {
		return isMinus;
	}

	@JsonProperty("is_minus")
	public void setMinus(String isMinus) {
		if(Boolean.parseBoolean(isMinus)) {
			this.isMinus = " * -1";
		}else {
			this.isMinus = "";
		}
		
	}

	public FilterCols[] getFilterCols() {
		return filterCols;
	}

	public void setFilterCols(FilterCols[] filterCols) {
		this.filterCols = filterCols;
	}

}
