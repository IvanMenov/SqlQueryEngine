package sql.query.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Table {
	@JsonProperty("Table")
	private String id;
	
	@JsonProperty("Dataset")
	private String dataSet;
	
	private String[] columns;

	@JsonProperty("isExist")
	private boolean isExist;
	
	private String createQuery;
	

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

	public String[] getColumns() {
		return columns;
	}

	public String getColumnsAsString(int maxColumnCount) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < maxColumnCount; i++) {
			builder.append(columns[i]);
			
			if(i < maxColumnCount -1) {
				builder.append(", ");
			}
			
		}
		return builder.toString();
	}
	
	public void setColumns(String columns) {
		this.columns = columns.split(",");
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
