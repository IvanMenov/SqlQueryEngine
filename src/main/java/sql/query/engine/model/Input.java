package sql.query.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Input {
	@JsonProperty("kpi_name")
	private String kpiName;
	
	private int id;
	
	@JsonProperty("table")
	private String tableId;
	
	@JsonProperty("filter_list")
	private FilterList[] filterList;
	
	@JsonProperty("group_cols")
	private String groupColums;

	public String getGroupColums() {
		return groupColums;
	}

	public void setGroupColums(String groupColums) {
		this.groupColums = groupColums;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FilterList[] getFilterList() {
		return filterList;
	}

	public void setFilterList(FilterList[] filterList) {
		this.filterList = filterList;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	
}
