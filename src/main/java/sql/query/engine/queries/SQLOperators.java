package sql.query.engine.queries;

public enum SQLOperators {
	OR(" or "),
	AND(" and "),
	WITH("With "),
	UNION_ALL("union all");
	
	private String operatorValue;
	
	SQLOperators(String operator){
		this.operatorValue = operator;
	}
	public String getOperatorValue() {
		return operatorValue;
	}
}
