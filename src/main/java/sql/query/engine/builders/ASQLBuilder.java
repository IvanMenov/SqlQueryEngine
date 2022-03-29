package sql.query.engine.builders;

import java.util.Map;

public abstract class ASQLBuilder{
			
	public static final String WRAP_IN_COMMENT = "/*{0}*/";

	public static final String NEWLINE = "\n";
	
	public static final String COMMA = ",";
	
	public static final String ASTERISK = "*";
	
	public static final String AS = " as ";
	
	public static final char SEMICOLON = ';';
	
	public static final char OPEN_BRACKET = '(';
	
	public static final char CLOSE_BRACKET = ')';
	
	public static final char WHITESPACE = ' ';
	private StringBuilder builder = new StringBuilder();

	public StringBuilder getBuilder() {
		return builder;
	}
	
	public abstract Map<String, String> buildMap();
	
	public abstract String buildQuery();
}
