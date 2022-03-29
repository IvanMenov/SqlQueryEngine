package sql.query.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Table {
	@JsonProperty("Table")
	private String id;
	
	@JsonProperty("Dataset")
	private String dataSet;
	
	@JsonProperty("columns_group_list")
	private String[] columnsGroupList;
	
	@JsonProperty("columns_agg_list")
	private String[] columnsAggList;
	
	@JsonProperty("columns_agg_func")
	private String[] columnsAggFunction;

	@JsonProperty("isExist")
	private boolean isExist;
	
	private String createQuery;
	
	
	public String[] getColumnsGroupList() {
		return columnsGroupList;
	}
	public String getColumnsListAsString(String[] name) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < name.length; i++) {
			builder.append(name[i]);
			
			if(i < name.length -1) {
				builder.append(", ");
			}
			
		}
		return builder.toString();
	}
	public void setColumnsGroupList(String columnsGroupList) {
		this.columnsGroupList = columnsGroupList.split(",");
	}

	public String[] getColumnsAggList() {
		return columnsAggList;
	}

	public void setColumnsAggList(String columnsAggList) {
		this.columnsAggList = columnsAggList.split(",");
	}

	public String[] getColumnsAggFunction() {
		return columnsAggFunction;
	}

	public void setColumnsAggFunction(String columnsAggFunction) {
		this.columnsAggFunction = columnsAggFunction.split(",");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public void setColumns(String columns) {
		this.columnsGroupList = columns.split(",");
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public String getCreateQuery() {
		return createQuery;
	}

	public void setCreateQuery(String createQuery) {
		this.createQuery = createQuery;
	}
}
