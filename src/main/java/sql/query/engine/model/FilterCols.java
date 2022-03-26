package sql.query.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterCols {
	private String col;
	
	@JsonProperty("val_list")
	private ValList[] valList;

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public ValList[] getValList() {
		return valList;
	}

	public void setValList(ValList[] valList) {
		this.valList = valList;
	}
	
	
}
